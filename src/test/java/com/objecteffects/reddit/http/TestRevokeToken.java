package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.gson.RedditOAuthGson;

public class TestRevokeToken {
    final Logger log = LoggerFactory.getLogger(TestRevokeToken.class);

    private final RedditOAuthGson redditOAuth = new RedditOAuthGson();

    @Test
    public void testRevokeToken()
            throws IOException, InterruptedException {
        this.redditOAuth.getAuthToken();

        final HttpResponse<String> response = this.redditOAuth.revokeToken();

        this.log.debug("revoke response status: {}",
                Integer.valueOf(response.statusCode()));
        this.log.debug("revoke response headers: {}", response.headers());
        this.log.debug("revoke response body: {}", response.body());
    }
}
