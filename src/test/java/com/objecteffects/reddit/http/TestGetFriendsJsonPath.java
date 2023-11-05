package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.method.GetFriendsJsonPath;

public class TestGetFriendsJsonPath {
    final Logger log =
            LoggerFactory.getLogger(TestGetFriendsJsonPath.class);

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetFriends()
            throws IOException, InterruptedException {
        final GetFriendsJsonPath getFriends = new GetFriendsJsonPath();

        final List<Friend> friends = getFriends.getFriends(10);

        Collections.sort(friends, Collections.reverseOrder());

        for (final Friend f : friends) {
            this.log.debug("{}, {}", f.getName(), f.getKarma());
        }
    }
}
