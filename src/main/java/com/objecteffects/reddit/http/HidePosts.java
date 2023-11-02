package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.objecteffects.reddit.http.data.Posts;

public class HidePosts {
    private final Logger log =
            LoggerFactory.getLogger(HidePosts.class);

    @SuppressWarnings("boxing")
    public String hidePosts(final String name, final int count,
            final String lastAfter)
            throws IOException, InterruptedException {

        final var getClient = new RedditGetMethod();

        final var submittedMethod = String.format("/user/%s/submitted", name);

        final Map<String, String> params =
                new HashMap<>(
                        Map.of("limit", String.valueOf(count),
                                "sort", "new",
                                "type", "links"));

        if (lastAfter != null) {
            params.put("after", lastAfter);
        }

        final var methodResponse = getClient.getMethod(submittedMethod, params);

        final var gson = new Gson();

        final Posts data = gson.fromJson(methodResponse.body(), Posts.class);

        this.log.debug("data length: {}", data.getData().getChildren().size());

        final var postClient = new RedditPostMethod();

        final var hideMethod = String.format("/api/hide");

        for (final Posts.Post pd : data.getData().getChildren()) {
            this.log.debug("post: {}", pd.getPostData());

            final var param = Map.of("id", pd.getPostData().getName());

            if (pd.getPostData().isHidden()) {
                continue;
            }

            final var hideResponse = postClient.postMethod(hideMethod, param);

            this.log.debug("response: {}", hideResponse.statusCode());
        }

        String after;

        if (data.getData().getChildren().size() > 0) {
            after = data.getData().getChildren()
                    .get(data.getData().getChildren().size() - 1).getPostData()
                    .getName();
        }
        else {
            after = null;
        }

        this.log.debug("after: {}", after);

        return after;
    }
}
