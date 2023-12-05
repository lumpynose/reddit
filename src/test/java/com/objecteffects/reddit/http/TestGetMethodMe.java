package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;

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
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Me;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestGetMethodMe {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditHttpClient.class,
                    RedditOAuth.class, AppConfig.class,
                    RedditGet.class);

    @Inject
    private RedditGet getClient;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethod() throws IOException, InterruptedException {
        final HttpResponse<String> response =
                this.getClient.getMethod("api/v1/me",
                        Collections.emptyMap());

        if (response == null) {
            throw new IllegalStateException("null respone");
        }

        this.log.debug("body: {}", response.body());

        final TypeRef<Me> typeRef = new TypeRef<>() {
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(response.body());

        final Me me = jsonContext.read("$", typeRef);

        this.log.debug("me: {}", me);
    }
}
