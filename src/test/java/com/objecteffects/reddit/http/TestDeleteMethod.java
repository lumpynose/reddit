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

import com.objecteffects.reddit.core.RedditDelete;
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.RedditPut;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestDeleteMethod {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditGet.class, RedditPut.class,
                    RedditDelete.class, RedditHttpClient.class,
                    RedditOAuth.class, AppConfig.class);

    @Inject
    private RedditGet getClient;

    @Inject
    private RedditPut putClient;

    @Inject
    private RedditDelete delClient;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testDeleteMethod() throws IOException, InterruptedException {
        final String user = "user";
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
    }
}
