package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class TestDeleteMethod {
    @SuppressWarnings("unused")
    private final static Logger log = LogManager
            .getLogger(TestDeleteMethod.class);

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

        RedditOAuth.revokeToken();
    }
}
