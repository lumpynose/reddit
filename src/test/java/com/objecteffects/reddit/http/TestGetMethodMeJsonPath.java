package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;

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
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.data.Me;

/**
 *
 */
public class TestGetMethodMeJsonPath {
    final Logger log =
            LoggerFactory.getLogger(TestGetMethodMeJsonPath.class);

    private final RedditOAuth redditOAuth = new RedditOAuth();

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethod() throws IOException, InterruptedException {
        final RedditGetMethod client = new RedditGetMethod();

        final String body =
                client.getMethod("api/v1/me",
                        Collections.emptyMap()).body();

        this.log.debug("body: {}", body);

        final TypeRef<Me> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(body);

        final Me me = jsonContext.read("$", typeRef);

        this.log.debug("me: {}", me);

        this.redditOAuth.revokeToken();
    }
}
