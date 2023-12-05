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
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestGetMethodSavedCats {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditGetMethod.class,
                    RedditHttpClient.class, RedditOAuth.class,
                    AppConfig.class);

    @Inject
    private RedditGetMethod getClient;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethodSavedCats()
            throws InterruptedException, IOException {
        final Map<String, String> params = Collections.emptyMap();

        final HttpResponse<String> response = this.getClient
                .getMethod("api/saved_categories", params);

        if (response == null) {
            throw new IllegalStateException("null saved categories respones");
        }

        this.log.debug("saved categories: {}", response.body());

        this.log.debug("method response status: {}", response.statusCode());

        this.log.debug("method response headers: {}", response.headers());

        // this.log.debug("method response body: {}", methodResponse.body());
    }
}
