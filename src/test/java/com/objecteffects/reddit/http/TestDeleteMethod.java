package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditDeleteMethod;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.RedditPutMethod;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestDeleteMethod {
    final Logger log =
            LoggerFactory.getLogger(TestDeleteMethod.class);

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditGetMethod.class, RedditPutMethod.class,
                    RedditDeleteMethod.class, RedditHttpClient.class,
                    RedditOAuth.class);

    @Inject
    private RedditOAuth redditOAuth;

    @Inject
    private RedditGetMethod getClient;

    @Inject
    private RedditPutMethod putClient;

    @Inject
    private RedditDeleteMethod delClient;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testDeleteMethod() throws IOException, InterruptedException {
        final String user = "BotDefense";
        HttpResponse<String> response;

        final String aboutUri = String.format("user/%s/about", user);

        response = this.getClient.getMethod(aboutUri,
                Collections.emptyMap());

        this.log.debug("response: {}", response);

        /* end of GET about */

        final String putUri = String.format("api/v1/me/friends/%s",
                user);

        final Map<String, String> map = Map.of("name", user);

        response = this.putClient.putMethod(putUri, map);

        this.log.debug("response: {}", response);

        /* end of PUT friends */

        final String infoUri = String.format("api/v1/me/friends/%s",
                user);

        response = this.getClient.getMethod(infoUri, Collections.emptyMap());

        this.log.debug("response: {}", response);

        /* end of GET friends user */

        final String deleteUri = String.format("api/v1/me/friends/%s", user);

        response = this.delClient.deleteMethod(deleteUri,
                Collections.emptyMap());

        this.log.debug("response: {}", response);

        /* end of DELETE friends user */

//        this.redditOAuth.revokeToken();
    }
}
