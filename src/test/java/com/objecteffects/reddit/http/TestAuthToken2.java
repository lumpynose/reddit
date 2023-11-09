package com.objecteffects.reddit.http;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.main.AppConfig;

/**
 *
 */
public class TestAuthToken2 {
    private final Logger log =
            LoggerFactory.getLogger(TestAuthToken2.class);

    private final RedditOAuth redditOAuth =
            new RedditOAuth();

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetAuthToken() throws IOException, InterruptedException {
        final AppConfig configuration =
                new AppConfig();

        this.log.debug(configuration.dumpConfig());
    }
}
