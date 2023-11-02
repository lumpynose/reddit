package com.objecteffects.reddit.http;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.Configuration;

public class TestRevokeToken {
    final Logger log = LoggerFactory.getLogger(Configuration.class);

    private final RedditOAuth redditOAuth = new RedditOAuth();

    @Test
    public void testRevokeToken()
            throws IOException, InterruptedException {
        this.redditOAuth.getAuthToken();

        final var response = this.redditOAuth.revokeToken();

        this.log.debug("revoke response status: {}",
                Integer.valueOf(response.statusCode()));
        this.log.debug("revoke response headers: {}", response.headers());
        this.log.debug("revoke response body: {}", response.body());
    }
}
