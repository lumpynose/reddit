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

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestGetMethodFriends {
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
    public void testGetMethodFriends()
            throws InterruptedException, IOException {
        // "raw_json", String.valueOf(1)
        // "show", "all"
        final Map<String, String> params = new HashMap<>(
                Map.of("limit", String.valueOf(5)));

        // .getMethod("", Collections.emptyMap());
        // .getMethod("api/v1/me/friends", Collections.emptyMap());
        final HttpResponse<String> response = this.getClient
                .getMethod("prefs/friends", params);

        if (response == null) {
            throw new IllegalStateException("null friends respones");
        }

        this.log.debug("method response status: {}",
                response.statusCode());

        this.log.debug("method response headers: {}", response.headers());

        // this.log.debug("method response body: {}", methodResponse.body());

        decodeBody(response.body());
    }

    private void decodeBody(final String body) {
        final String path = "$[0]['data']['children']";

        final TypeRef<List<Friend>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(body);

        final List<Friend> list = jsonContext.read(path, typeRef);

        for (final Friend friend : list) {
            this.log.debug("name: {}, karma: {}", friend.getName(),
                    friend.getKarma());
        }
    }
}
