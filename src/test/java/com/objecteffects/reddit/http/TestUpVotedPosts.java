package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.objecteffects.reddit.http.data.Posts;
import com.objecteffects.reddit.main.Configuration;

public class TestUpVotedPosts {
    private final static Logger log = LogManager
            .getLogger(TestUpVotedPosts.class);

    @Test
    @SuppressWarnings("boxing")
    public void testUpVotedMethod()
            throws IOException, InterruptedException {
        final String user = "gatrouz";

        log.debug("configuration: {}", Configuration.dumpConfig());

        final var getClient = new RedditGetMethod();

        final var upvotedMethod = String.format("/user/%s/upvoted",
                user);

        final var params = Map.of("limit", "100", "sort", "new", "type",
                "links");

        final var methodResponse = getClient.getMethod(upvotedMethod, params);

        final var gson = new Gson();

        final Posts data = gson.fromJson(methodResponse.body(), Posts.class);

        log.debug("data length: {}", data.getData().getChildren().size());
    }
}
