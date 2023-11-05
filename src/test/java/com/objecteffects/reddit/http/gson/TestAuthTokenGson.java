package com.objecteffects.reddit.http.gson;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.gson.RedditOAuthGson;
import com.objecteffects.reddit.main.AppConfig;

/**
 *
 */
public class TestAuthTokenGson {
    private final Logger log =
            LoggerFactory.getLogger(TestAuthTokenGson.class);

    private final RedditOAuthGson redditOAuth = new RedditOAuthGson();

    private final static AppConfig configuration =
            new AppConfig();

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