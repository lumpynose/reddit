package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.data.FriendAbout;
import com.objecteffects.reddit.data.Post;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class GetFriends implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditGet getMethod;

    @Inject
    private UnFriend unFriend;

    @Inject
    private GetPosts getPosts;

    private final int defaultLimit = 0;
    private final boolean defaultGetKarma = false;

    private final Configuration conf = Utils.jsonConf();

    /**
     */
    public GetFriends() {
    }

    /**
     * @param _getMethod the getMethod to set
     */
    public void setGetMethod(final RedditGet _getMethod) {
        this.getMethod = _getMethod;
    }

    /**
     * @param _unFriend the unFriend to set
     */
    public void setUnFriend(final UnFriend _unFriend) {
        this.unFriend = _unFriend;
    }

    /**
     * @param _getPosts
     */
    public void setGetPosts(final GetPosts _getPosts) {
        this.getPosts = _getPosts;
    }

    /**
     * Gets all friends, no karma.
     *
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends() throws IOException, InterruptedException {
        return getFriends(this.defaultLimit, this.defaultGetKarma);
    }

    /**
     * Gets all friends, with karma.
     *
     * @param getKarma
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends(final boolean getKarma)
            throws IOException, InterruptedException {
        return getFriends(this.defaultLimit, getKarma);
    }

    /**
     * Gets some friends, with karma.
     *
     * @param count
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends(final int count)
            throws IOException, InterruptedException {
        return getFriends(count, true);
    }

    /**
     * Gets all or some friends, with or without karma.
     *
     * @param count
     * @param getKarma
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends(final int count,
            final boolean getKarma)
            throws IOException, InterruptedException {
        // .getMethod("prefs/friends", Collections.emptyMap());
        // .getMethod("api/v1/me/friends", Collections.emptyMap());
        final HttpResponse<String> response = this.getMethod
                .getMethod("prefs/friends", Collections.emptyMap());

        if (response == null) {
            throw new IllegalStateException("null friends respones");
        }

        this.log.debug("friends method response status: {}",
                response.statusCode());

        final String path = "$[0]['data']['children']";

        final TypeRef<List<Friend>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext = JsonPath
                .using(this.conf).parse(response.body());

        final List<Friend> friends = jsonContext.read(path, typeRef);

        this.log.debug("friends length: {}",
                friends.size());

        if (getKarma) {
            return decodeAbout(friends, count);
        }

        return friends;
    }

    @SuppressWarnings("boxing")
    private List<Friend> decodeAbout(final List<Friend> friends,
            final int count)
            throws IOException, InterruptedException {
        List<Friend> sublist = friends;

        /*
         * getFriends above always returns all friends so we trim it here if
         * necessary.
         */
        if (count > 0 && count < friends.size()) {
            sublist = friends.subList(0, count);
        }

        for (final Friend friend : sublist) {
            Thread.sleep(600);

            // none of these work when used with successive users
            // and I can't figure out why.
//            String.format("/api/v1/me/friends/%s", f.getName());
//            String.format("/user/%s/overview", f.getName());
            final String aboutUri =
                    String.format("/user/%s/about", friend.getName());

            final HttpResponse<String> response = this.getMethod
                    .getMethod(aboutUri, Collections.emptyMap());

            if (response == null) {
                this.log.debug("null response: {}", friend.getName());

                // this.unFriend.unFriend(f.getName());

                friend.setKarma(0);

                continue;
            }

            final String body = response.body();

//            this.log.debug("about response: {}", body);

            final String path = "$['data']";

            final DocumentContext jsonContext = JsonPath
                    .using(this.conf).parse(body);

            final FriendAbout fabout = jsonContext.read(path,
                    FriendAbout.class);

//            this.log.debug("fabout: {}", fabout);

            friend.setKarma(fabout.getTotalKarma());
            friend.setIsBlocked(fabout.getIsBlocked());

            if (fabout.getIsSuspended() == null) {
                friend.setIsSuspended(Boolean.FALSE);
            }
            else {
                friend.setIsSuspended(fabout.getIsSuspended());
            }

            if (friend.getIsSuspended()) {
                this.log.debug("unfriending: {}", friend.getName());

                this.unFriend.unFriend(friend.getName());
            }

            getLatest(friend);
        }

        return sublist;
    }

    private void getLatest(final Friend friend)
            throws InterruptedException, IOException {
        final List<Post> posts = this.getPosts
                .getPosts(friend.getName(), 500, null);

        if (posts.isEmpty()) {
            friend.setLatest("none");
            friend.setPercentage((float) 0);

            return;
        }

        final Post post0 = posts.get(0);

//        this.log.debug("post: {}", post0);

        final Date date = new Date(post0.getCreated() * 1000L);

        final String df = DateFormat.getDateInstance().format(date);
        this.log.debug("post created: {}", df);

        friend.setLatest(df);

        int upCount = 0;

        for (final Post post : posts) {
            if (post.getLikes())
                upCount++;
        }

        // ratio = ups / (ups + downs)
        double ratio;

        if (posts.size() == 0) {
            ratio = 0;
        }
        else {
            ratio = (double) upCount / posts.size();
        }

        friend.setPercentage((float) (ratio * 100));

        this.log.debug("ratio: {}", friend.getPercentage());
    }
}
