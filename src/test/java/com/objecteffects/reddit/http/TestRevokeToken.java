package com.objecteffects.reddit.http;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class TestRevokeToken {
    private final static Logger log = LogManager
            .getLogger(TestRevokeToken.class);

    @Test
    public void testRevokeToken()
            throws IOException, InterruptedException {
        RedditOAuth.getAuthToken();

        final var response = RedditOAuth.revokeToken();

        log.debug("revoke response status: {}",
                Integer.valueOf(response.statusCode()));
        log.debug("revoke response headers: {}", response.headers());
        log.debug("revoke response body: {}", response.body());
    }
}
