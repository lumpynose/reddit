package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class TestGetMethodMe {
    @SuppressWarnings("unused")
    private final static Logger log = LogManager
            .getLogger(TestGetMethodMe.class);

    @Test
    public void testGetMethod() throws IOException, InterruptedException {
        final var client = new RedditGetMethod();

        client.getMethod("api/v1/me", Collections.emptyMap());

        RedditOAuth.revokeToken();
    }
}
