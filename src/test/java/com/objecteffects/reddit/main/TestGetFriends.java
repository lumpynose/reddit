package com.objecteffects.reddit.main;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.objecteffects.reddit.http.data.Friends.Friend;

public class TestGetFriends {
    private final static Logger log = LogManager
            .getLogger(TestGetFriends.class);

    @Test
    public void testGetFriends() throws IOException, InterruptedException {
        final GetFriends getFriends = new GetFriends();

        final List<Friend> friends = getFriends.getFriends(100);

        // Collections.sort(friends, Collections.reverseOrder());

        for (final Friend f : friends) {
            log.debug("{}", f.getName());
        }
    }
}
