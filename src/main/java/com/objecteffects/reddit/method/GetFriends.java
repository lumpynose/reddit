package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.data.FriendAbout;

import jakarta.inject.Inject;

/**
 */
public class GetFriends implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditGetMethod getMethod;

    @Inject
    private UnFriend unFriend;

    private final int defaultCount = 0;
    private final boolean defaultGetKarma = false;

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    public GetFriends() {
    }

    /**
     * @param _getMethod the getMethod to set
     */
    public void setGetMethod(final RedditGetMethod _getMethod) {
        this.getMethod = _getMethod;
    }

    /**
     * @param _unFriend the unFriend to set
     */
    public void setUnFriend(final UnFriend _unFriend) {
        this.unFriend = _unFriend;
    }

    /**
     * Gets all friends, no karma.
     *
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends() throws IOException, InterruptedException {
        return getFriends(this.defaultCount, this.defaultGetKarma);
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
        return getFriends(this.defaultCount, getKarma);
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
        final HttpResponse<String> response = this.getMethod
                .getMethod("prefs/friends", Collections.emptyMap());

        if (response == null) {
            throw new IllegalStateException("null friends respones");
        }

        this.log.debug("friends method response status: {}",
                Integer.valueOf(response.statusCode()));

        final String path = "$[0]['data']['children']";

        final TypeRef<List<Friend>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext = JsonPath
                .using(this.conf).parse(response.body());

        final List<Friend> friends = jsonContext.read(path, typeRef);

        this.log.debug("friends length: {}",
                Integer.valueOf(friends.size()));

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

        for (final Friend f : sublist) {
            Thread.sleep(600);

            final String aboutUri =
                    String.format("/api/v1/me/friends/%s", f.getName());

            final HttpResponse<String> response = this.getMethod
                    .getMethod(aboutUri, Collections.emptyMap());

            if (response == null) {
                this.log.debug("null response: {}", f.getName());

                // this.unFriend.unFriend(f.getName());

                f.setKarma(0);

                continue;
            }

            final String body = response.body();

            this.log.debug("about response: {}", body);

            final String path = "$['data']";

            final DocumentContext jsonContext = JsonPath
                    .using(this.conf).parse(body);

            final FriendAbout fabout = jsonContext.read(path,
                    FriendAbout.class);

            this.log.debug("fabout: {}", fabout);

            f.setKarma(fabout.getTotalKarma());
            f.setIsBlocked(fabout.getIsBlocked());

            if (fabout.getIsSuspended() == null) {
                f.setIsSuspended(Boolean.FALSE);
            }
            else {
                f.setIsSuspended(fabout.getIsSuspended());
            }

            if (f.getIsSuspended()) {
                this.log.debug("unfriending: {}", f.getName());

                this.unFriend.unFriend(f.getName());
            }
        }

        return sublist;
    }
}
