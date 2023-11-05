package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditDeleteMethod;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditOAuthGson;
import com.objecteffects.reddit.core.RedditPutMethodGson;

public class TestDeleteMethod {
    @SuppressWarnings("unused")
    final Logger log =
            LoggerFactory.getLogger(TestDeleteMethod.class);

    private final RedditOAuthGson redditOAuth = new RedditOAuthGson();

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethod() throws IOException, InterruptedException {
        final String user = "daronjay";

        /* */

        final RedditGetMethod getClient = new RedditGetMethod();

        final String aboutMethod = String.format("user/%s/about", user);

        getClient.getMethod(aboutMethod, Collections.emptyMap());

        /* */

        final RedditPutMethodGson putClient = new RedditPutMethodGson();

        final String putMethod = String.format("api/v1/me/friends/%s", user);

        final Map<String, String> map = Map.of("name", user /*
                                                             * , "note",
                                                             * "nothing"
                                                             */);

        putClient.putMethod(putMethod, map);

        /* */

        final String infoMethod = String.format("api/v1/me/friends/%s",
                user);

        getClient.getMethod(infoMethod, Collections.emptyMap());

        /* */

        final RedditDeleteMethod delClient = new RedditDeleteMethod();

        final String deleteMethod = String.format("api/v1/me/friends/%s", user);

        delClient.deleteMethod(deleteMethod, Collections.emptyMap());

        /* */

        this.redditOAuth.revokeToken();
    }
}
