package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.data.Friends.Friend;
import com.objecteffects.reddit.method.GetFriends;

public class TestGetFriends {
    final Logger log =
            LoggerFactory.getLogger(TestGetFriends.class);

    @Test
    public void testGetFriends() throws IOException, InterruptedException {
        final GetFriends getFriends = new GetFriends();

        final List<Friend> friends = getFriends.getFriends(100);

        // Collections.sort(friends, Collections.reverseOrder());

        for (final Friend f : friends) {
            this.log.debug("{}", f.getName());
        }
    }
}
