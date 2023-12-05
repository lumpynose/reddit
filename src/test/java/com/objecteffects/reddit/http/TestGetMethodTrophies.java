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

import com.jayway.jsonpath.Configuration;
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestGetMethodTrophies {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditGet.class,
                    RedditHttpClient.class, RedditOAuth.class,
                    AppConfig.class);

    @Inject
    private RedditGet getClient;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethodTrophies()
            throws InterruptedException, IOException {
        final Map<String, String> params = Collections.emptyMap();

        final HttpResponse<String> response = this.getClient
                .getMethod("api/v1/me/trophies", params);

        if (response == null) {
            throw new IllegalStateException("null response");
        }

        this.log.debug("method response status: {}",
                response.statusCode());

        this.log.debug("method response headers: {}", response.headers());

        // this.log.debug("method response body: {}", methodResponse.body());
    }
}
