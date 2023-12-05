package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
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
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Post;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestUpVotedPosts {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditGet.class, RedditHttpClient.class,
                    RedditOAuth.class, AppConfig.class);

    @Inject
    private RedditGet getClient;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testUpVotedMethod()
            throws IOException, InterruptedException {
        final String user = "lumpynose";

        final String upvotedUri = String.format("/user/%s/upvoted",
                user);

        final Map<String, String> params =
                Map.of("limit", "100", "sort", "new", "type",
                        "links");

        final HttpResponse<String> methodResponse =
                this.getClient.getMethod(upvotedUri, params);

        if (methodResponse == null) {
            this.log.debug("null response");

            return;
        }

        final String path = "$['data']['children'][*]['data']";

        final TypeRef<List<Post>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(methodResponse.body());

        final List<Post> list = jsonContext.read(path, typeRef);

        this.log.debug("list length: {}", list.size());
    }
}
