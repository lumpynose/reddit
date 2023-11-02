package com.objecteffects.reddit.main;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.objecteffects.reddit.http.RedditGetMethod;
import com.objecteffects.reddit.http.RedditOAuth;
import com.objecteffects.reddit.http.data.FriendAbout;
import com.objecteffects.reddit.http.data.Friends;
import com.objecteffects.reddit.http.data.Friends.Friend;

/**
 *
 */
public class GetFriends {
    private final Logger log =
            LoggerFactory.getLogger(GetFriends.class);
    private final Gson gson = new Gson();
    private final int defaultCount = 0;
    private final boolean defaultGetKarma = false;
    private final RedditOAuth redditOAuth = new RedditOAuth();

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
    @SuppressWarnings("boxing")
    public List<Friend> getFriends(final int count, final boolean getKarma)
            throws IOException, InterruptedException {
        final RedditGetMethod client = new RedditGetMethod();

        final HttpResponse<String> methodResponse = client
                .getMethod("prefs/friends", Collections.emptyMap());

        if (methodResponse == null) {
            throw new IllegalStateException("null friends respones");
        }

        this.log.debug("friends method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        final TypeToken<List<Friends>> jaType = new TypeToken<>() {
            // nothing here
        };

        final List<Friends> data =
                this.gson.fromJson(methodResponse.body(), jaType);

        final List<Friend> friends =
                data.get(0).getData().getFriendsList();

        this.log.debug("friends length: {}", friends.size());

        if (getKarma) {
            decodeAbout(friends, count);
        }

        this.redditOAuth.revokeToken();

        return friends;
    }

    private List<Friend> decodeAbout(final List<Friend> friends,
            final int count)
            throws IOException, InterruptedException {
        final RedditGetMethod client = new RedditGetMethod();

        List<Friend> sublist = friends;

        if (count > 0 && count < friends.size()) {
            sublist = friends.subList(0, count);
        }

        for (final Friend f : sublist) {
            Thread.sleep(1000);

            final String aboutMethod =
                    String.format("user/%s/about", f.getName());

            final HttpResponse<String> aboutMethodResponse = client
                    .getMethod(aboutMethod, Collections.emptyMap());

            if (aboutMethodResponse == null) {
                f.setKarma(0);
            }
            else {
                final String response = aboutMethodResponse.body();

                final FriendAbout fabout = this.gson.fromJson(
                        response, FriendAbout.class);

                if (fabout.getData() == null) {
                    this.log.debug("{}: no about data", f.getName());

                    f.setKarma(0);
                }
                else {
                    this.log.debug("friend about: {}", fabout);

                    f.setKarma(fabout.getData().getTotalKarma());
                }
            }
        }

        return friends;
    }
}
