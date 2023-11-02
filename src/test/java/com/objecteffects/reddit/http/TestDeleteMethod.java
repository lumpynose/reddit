package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDeleteMethod {
    @SuppressWarnings("unused")
    final Logger log =
            LoggerFactory.getLogger(TestDeleteMethod.class);

    private final RedditOAuth redditOAuth = new RedditOAuth();

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethod() throws IOException, InterruptedException {
        final String user = "daronjay";

        /* */

        final var getClient = new RedditGetMethod();

        final var aboutMethod = String.format("user/%s/about", user);

        getClient.getMethod(aboutMethod, Collections.emptyMap());

        /* */

        final var putClient = new RedditPutMethod();

        final var putMethod = String.format("api/v1/me/friends/%s", user);

        final Map<String, String> map = Map.of("name", user /*
                                                             * , "note",
                                                             * "nothing"
                                                             */);

        putClient.putMethod(putMethod, map);

        /* */

        final var infoMethod = String.format("api/v1/me/friends/%s",
                user);

        getClient.getMethod(infoMethod, Collections.emptyMap());

        /* */

        final var delClient = new RedditDeleteMethod();

        final var deleteMethod = String.format("api/v1/me/friends/%s", user);

        delClient.deleteMethod(deleteMethod, Collections.emptyMap());

        /* */

        this.redditOAuth.revokeToken();
    }
}
