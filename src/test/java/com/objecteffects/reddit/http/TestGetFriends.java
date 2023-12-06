package com.objecteffects.reddit.http;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditDelete;
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.GetFriends;
import com.objecteffects.reddit.method.GetPosts;
import com.objecteffects.reddit.method.UnFriend;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestGetFriends {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     */
    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(GetFriends.class, UnFriend.class,
                    RedditGet.class, RedditDelete.class, GetPosts.class,
                    RedditHttpClient.class, RedditOAuth.class, AppConfig.class);

    @Inject
    private GetFriends getFriends;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetFriends() throws IOException, InterruptedException {
        final List<Friend> friends = this.getFriends.getFriends(5, true);

        Collections.sort(friends, Collections.reverseOrder());
        final DecimalFormat df = new DecimalFormat("0");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (final Friend f : friends) {
            this.log.debug("{}, {}, {}",
                    f.getName(), f.getKarma(), df.format(f.getPercentage()));
        }
    }
}
