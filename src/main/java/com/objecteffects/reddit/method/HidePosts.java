package com.objecteffects.reddit.method;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.objecteffects.reddit.core.RedditPostMethod;
import com.objecteffects.reddit.data.Post;

import jakarta.inject.Named;

/**
 *
 */
@Named
public class HidePosts {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    /**
     * @param name
     * @param count
     * @param lastAfter
     * @return String after
     * @throws IOException
     * @throws InterruptedException
     */
    public String hidePosts(final String name, final int count,
            final String lastAfter)
            throws IOException, InterruptedException {

        final RedditGetMethod getClient = new RedditGetMethod();

        final String submittedMethod =
                String.format("/user/%s/submitted", name);

        final Map<String, String> params =
                new HashMap<>(
                        Map.of("limit", String.valueOf(count),
                                "sort", "new",
                                "type", "links"));

        if (lastAfter != null) {
            params.put("after", lastAfter);
        }

        final HttpResponse<String> methodResponse =
                getClient.getMethod(submittedMethod, params);

        if (methodResponse == null) {
            this.log.debug("null response");

            return "";
        }

        // this.log.debug("posts: {}", methodResponse.body());

        this.log.debug("method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        this.log.debug("method response headers: {}",
                methodResponse.headers());

        final String path = "$['data']['children'][*]['data']";

        final TypeRef<List<Post>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf)
                        .parse(methodResponse.body());

        final List<Post> posts = jsonContext.read(path, typeRef);

        this.log.debug("list size: {}", Integer.valueOf(posts.size()));

        final RedditPostMethod postClient =
                new RedditPostMethod();

        final String hideMethod = String.format("/api/hide");

        for (final Post post : posts) {
            this.log.debug("post: {}", post);

            final Map<String, String> param =
                    Map.of("id", post.getName());

            if (post.isHidden()) {
                continue;
            }

            final HttpResponse<String> hideResponse =
                    postClient.postMethod(hideMethod, param);

            this.log.debug("response: {}",
                    Integer.valueOf(hideResponse.statusCode()));

            Thread.sleep(600);
        }

        String after = null;

        if (posts.size() > 0) {
            after = posts.get(posts.size() - 1).getName();
        }

        this.log.debug("after: {}", after);

        return after;
    }
}
