package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditOAuth;

/**
 *
 */
public class TestGetMethodMe {
    final Logger log =
            LoggerFactory.getLogger(TestGetMethodMe.class);

    private final RedditOAuth redditOAuth = new RedditOAuth();

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethod() throws IOException, InterruptedException {
        final RedditGetMethod client = new RedditGetMethod();

        final String me =
                client.getMethod("api/v1/me",
                        Collections.emptyMap()).body();

        this.log.debug("me: {}", me);

        final DocumentContext jsonContext = JsonPath.parse(me);
        final String all = jsonContext.read("$.is_employee").toString();

        this.log.debug("all: {}", all);

        this.redditOAuth.revokeToken();
    }
}
