package com.objecteffects.reddit.http.gson;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.data.Posts;
import com.objecteffects.reddit.main.AppConfig;

public class TestUpVotedPostsGson {
    final static Logger log =
            LoggerFactory.getLogger(TestUpVotedPostsGson.class);

    private final static AppConfig configuration =
            new AppConfig();

    @Test
    @SuppressWarnings("boxing")
    public void testUpVotedMethod()
            throws IOException, InterruptedException {
        final String user = "gatrouz";

        log.debug("configuration: {}", configuration.dumpConfig());

        final RedditGetMethod getClient = new RedditGetMethod();

        final String upvotedMethod = String.format("/user/%s/upvoted",
                user);

        final Map<String, String> params =
                Map.of("limit", "100", "sort", "new", "type",
                        "links");

        final HttpResponse<String> methodResponse =
                getClient.getMethod(upvotedMethod, params);

        final Gson gson = new Gson();

        final Posts data = gson.fromJson(methodResponse.body(), Posts.class);

        log.debug("data length: {}", data.getData().getChildren().size());
    }
}