package com.objecteffects.reddit.http;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.http.core.RedditOAuth;
import com.objecteffects.reddit.main.Configuration;

public class TestAuthToken {
    private final Logger log =
            LoggerFactory.getLogger(TestAuthToken.class);

    private final RedditOAuth redditOAuth = new RedditOAuth();

    private final static Configuration configuration =
            new Configuration();

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetAuthToken() throws IOException, InterruptedException {

        this.redditOAuth.getAuthToken();

        final String access_token = configuration.getOAuthToken();

        this.log.debug("access_token: {}", access_token);
    }
}
