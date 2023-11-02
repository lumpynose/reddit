package com.objecteffects.reddit.main;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private final static Logger log = LogManager
            .getLogger(GetFriends.class);
    private final Gson gson = new Gson();
    private final int defaultCount = 0;
    private final boolean defaultGetKarma = false;

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
        final var client = new RedditGetMethod();

        final var methodResponse = client
                .getMethod("prefs/friends", Collections.emptyMap());

        if (methodResponse == null) {
            throw new IllegalStateException("null friends respones");
        }

        log.debug("friends method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        final TypeToken<List<Friends>> jaType = new TypeToken<>() {
            // nothing here
        };

        final List<Friends> data = this.gson.fromJson(methodResponse.body(),
                jaType);

        final List<Friend> friends = data.get(0).getData().getFriendsList();

        log.debug("friends length: {}", friends.size());

        if (getKarma) {
            decodeAbout(friends, count);
        }

        RedditOAuth.revokeToken();

        return friends;
    }

    private List<Friend> decodeAbout(final List<Friend> friends,
            final int count)
            throws IOException, InterruptedException {
        final var client = new RedditGetMethod();

        List<Friend> sublist = friends;

        if (count > 0 && count < friends.size()) {
            sublist = friends.subList(0, count);
        }

        for (final Friend f : sublist) {
            Thread.sleep(1000);

            final var aboutMethod = String.format("user/%s/about", f.getName());

            final var aboutMethodResponse = client
                    .getMethod(aboutMethod, Collections.emptyMap());

            if (aboutMethodResponse == null) {
                f.setKarma(0);
            }
            else {
                final FriendAbout fabout = this.gson.fromJson(
                        aboutMethodResponse.body(), FriendAbout.class);

                if (fabout.getData() == null) {
                    log.debug("{}: no about data", f.getName());

                    f.setKarma(0);
                }
                else {
                    log.debug("friend about: {}", fabout);

                    f.setKarma(fabout.getData().getTotalKarma());
                }
            }
        }

        return friends;
    }
}
