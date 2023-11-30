package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestGetMethodFriends {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

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
        // doesn't work (ignored) with friends; always get
        // the whole enchillada.
        // final Map<String, String> params = Map.of("limit", "15");

        final HttpResponse<String> methodResponse = this.getClient
                .getMethod("prefs/friends", Collections.emptyMap());

        this.log.debug("method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        this.log.debug("method response headers: {}", methodResponse.headers());
        // this.log.debug("method response body: {}", methodResponse.body());

        decodeBody(methodResponse.body());
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
