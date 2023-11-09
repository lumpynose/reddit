package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.method.GetFriends;

import jakarta.inject.Inject;

/**
 *
 */
@EnableWeld
public class TestGetFriends {
    final Logger log =
            LoggerFactory.getLogger(TestGetFriends.class);

    /**
     */
    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.of(GetFriends.class);

    @Inject
    private GetFriends getFriends;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetFriends()
            throws IOException, InterruptedException {

        final List<Friend> friends = this.getFriends.getFriends(10, true);

        Collections.sort(friends, Collections.reverseOrder());

        for (final Friend f : friends) {
            this.log.debug("{}, {}", f.getName(), f.getKarma());
        }
    }
}
