package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.objecteffects.reddit.core.gson.RedditOAuthGson;
import com.objecteffects.reddit.data.Post;

/**
 *
 */
public class TestGetMethodPostsJsonPath {
    final Logger log =
            LoggerFactory.getLogger(TestGetMethodPostsJsonPath.class);

    private final RedditOAuthGson redditOAuth = new RedditOAuthGson();

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
    public void testGetMethodPosts()
            throws InterruptedException, IOException {
        final int count = 5;
        final String name = "montgranite";

        final RedditGetMethod getClient = new RedditGetMethod();

        final String submittedMethod =
                String.format("/user/%s/submitted", name);

        final Map<String, String> params =
                new HashMap<>(
                        Map.of("limit", String.valueOf(count),
                                "sort", "new",
                                "type", "links"));

        final HttpResponse<String> methodResponse =
                getClient.getMethod(submittedMethod, params);

        this.log.debug("method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        this.log.debug("method response headers: {}", methodResponse.headers());
        // this.log.debug("method response body: {}", methodResponse.body());

        decodeBodyJsonPath(methodResponse.body());

        this.redditOAuth.revokeToken();
    }

    private void decodeBodyJsonPath(final String body) {
        final String path = "$['data']['children'][*]['data']";

        final TypeRef<List<Post>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(body);

        final List<Post> list = jsonContext.read(path, typeRef);

        // final List<Map<String, String>> list = jsonContext.read(path);

        this.log.debug("list size: {}", Integer.valueOf(list.size()));

        for (final Post post : list) {
            this.log.debug("name: {}", post.getName());
        }

//        for (final Map<String, String> sublist : list) {
//            for (final Map.Entry<String, String> entry : sublist.entrySet()) {
//                this.log.debug("key: {}, value: {}",
//                        entry.getKey(), entry.getValue());
//            }
//        }
    }
}