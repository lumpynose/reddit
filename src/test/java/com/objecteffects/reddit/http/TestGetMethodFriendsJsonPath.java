package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.objecteffects.reddit.http.data.Friend;

/**
 *
 */
public class TestGetMethodFriendsJsonPath {
    final Logger log =
            LoggerFactory.getLogger(TestGetMethodFriendsJsonPath.class);

    private final RedditOAuth redditOAuth = new RedditOAuth();

    @BeforeEach
    public void setup() {
        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider =
                    new JacksonJsonProvider();
            private final MappingProvider mappingProvider =
                    new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return this.jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return this.mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethod()
            throws InterruptedException, IOException {
        final RedditGetMethod client = new RedditGetMethod();

        // doesn't work (ignored) with friends
        // final Map<String, String> params = Map.of("limit", "15");

        final HttpResponse<String> methodResponse = client
                .getMethod("prefs/friends", Collections.emptyMap());

        this.log.debug("method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        this.log.debug("method response headers: {}", methodResponse.headers());
        // this.log.debug("method response body: {}", methodResponse.body());

        decodeBodyJsonPath(methodResponse.body());

        this.redditOAuth.revokeToken();
    }

    private void decodeBodyJsonPath(final String body) {
        final String path = "$[0]['data']['children']";

        final TypeRef<List<Friend>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext = JsonPath.parse(body);
        final List<Friend> list = jsonContext.read(path, typeRef);

        for (final Friend friend : list) {
            this.log.debug("name: {}", friend.getName());
        }
    }
}
