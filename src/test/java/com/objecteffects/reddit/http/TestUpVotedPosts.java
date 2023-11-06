package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.EnumSet;
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
import com.objecteffects.reddit.data.Post;
import com.objecteffects.reddit.main.AppConfig;

/**
 *
 */
public class TestUpVotedPosts {
    final static Logger log =
            LoggerFactory.getLogger(TestUpVotedPosts.class);

    private final static AppConfig configuration =
            new AppConfig();

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
    public void testUpVotedMethod()
            throws IOException, InterruptedException {
        final String user = "figwax";

        log.debug("configuration: {}", configuration.dumpConfig());

        final RedditGetMethod getClient = new RedditGetMethod();

        final String upvotedMethod = String.format("/user/%s/upvoted",
                user);

        final Map<String, String> params =
                Map.of("limit", "100", "sort", "new", "type",
                        "links");

        final HttpResponse<String> methodResponse =
                getClient.getMethod(upvotedMethod, params);

        if (methodResponse == null) {
            log.debug("null response");

            return;
        }

        final String path = "$['data']['children'][*]['data']";

        final TypeRef<List<Post>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(methodResponse.body());

        final List<Post> list = jsonContext.read(path, typeRef);

        log.debug("list length: {}", Integer.valueOf(list.size()));
    }
}
