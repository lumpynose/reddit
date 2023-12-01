package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestUserSubmitted {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

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
    public void testGetMethodFriends()
            throws InterruptedException, IOException {
        final List<String> users =
                List.of("user", "reddit", "KeyserSosa");

        for (final String user : users) {
            Thread.sleep(600);

            final String uri1 =
                    String.format("user/%s/submitted", user);

            final Map<String, String> params =
                    new HashMap<>(
                            Map.of("limit", "5",
                                    "sort", "new",
                                    "type", "links"));

            final HttpResponse<String> response1 = this.getClient
                    .getMethod(uri1, params);

            debug(response1);
        }
    }

    private void debug(final HttpResponse<String> response) {
        if (response == null) {
            this.log.debug("null response");

            return;
        }

        this.log.debug("method response status: {}",
                response.statusCode());

        this.log.debug("method response headers: {}",
                response.headers());

        this.log.debug("method response body: {}", response.body());
    }

    @SuppressWarnings("unused")
    private void debug3(final String user, final Map<String, String> params)
            throws InterruptedException, IOException {
        final String uri3 =
                String.format("/api/multi/user/%s", user);

        final HttpResponse<String> response3 = this.getClient
                .getMethod(uri3, params);

        debug(response3);
    }

    @SuppressWarnings("unused")
    private void debug2(final String user, final Map<String, String> params)
            throws InterruptedException, IOException {
        final String uri2 =
                String.format("/api/v1/user/%s/trophies", user);

        final HttpResponse<String> response2 = this.getClient
                .getMethod(uri2, params);

        debug(response2);
    }
}
