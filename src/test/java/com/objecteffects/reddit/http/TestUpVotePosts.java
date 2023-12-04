package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.List;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.RedditPostMethod;
import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.UpVotePosts;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestUpVotePosts {
    @SuppressWarnings("unused")
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(UpVotePosts.class, RedditGetMethod.class,
                    RedditPostMethod.class, RedditHttpClient.class,
                    RedditOAuth.class, AppConfig.class);

    @Inject
    private UpVotePosts upVotePosts;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testPostMethod() throws IOException,
            InterruptedException {
        final List<String> users =
                List.of("user", "figwax", "reddit");

        for (final String user : users) {
            this.upVotePosts.upVotePosts(user, 3, null);
        }
    }
}
