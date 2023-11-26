package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;

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
import com.objecteffects.reddit.data.Me;

import jakarta.inject.Inject;

/**
 *
 */
@EnableWeld
public class TestGetMethodMe {
    final Logger log =
            LoggerFactory.getLogger(TestGetMethodMe.class);

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditGetMethod.class,
                    RedditHttpClient.class, RedditOAuth.class);

    @Inject
    private RedditGetMethod getClient;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethod() throws IOException, InterruptedException {

        final String body =
                this.getClient.getMethod("api/v1/me",
                        Collections.emptyMap()).body();

        this.log.debug("body: {}", body);

        final TypeRef<Me> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(body);

        final Me me = jsonContext.read("$", typeRef);

        this.log.debug("me: {}", me);
    }
}
