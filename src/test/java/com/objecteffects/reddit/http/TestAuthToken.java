package com.objecteffects.reddit.http;

import java.io.IOException;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;

import jakarta.inject.Inject;

/**
 *
 */
@EnableWeld
public class TestAuthToken {
    private final Logger log =
            LoggerFactory.getLogger(TestAuthToken.class);

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditHttpClient.class, RedditOAuth.class);

    @Inject
    private RedditOAuth redditOAuth;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetAuthToken() throws IOException, InterruptedException {
        final String access_token = this.redditOAuth.getOAuthToken();

        this.log.debug("access_token: {}", access_token);
    }
}
