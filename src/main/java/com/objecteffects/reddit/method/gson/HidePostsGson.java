package com.objecteffects.reddit.method.gson;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.gson.RedditPostMethodGson;
import com.objecteffects.reddit.data.Posts;

public class HidePostsGson {
    private final Logger log =
            LoggerFactory.getLogger(HidePostsGson.class);

    @SuppressWarnings("boxing")
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

        final Gson gson = new Gson();

        final Posts data = gson.fromJson(methodResponse.body(), Posts.class);

        this.log.debug("data length: {}", data.getData().getChildren().size());

        final RedditPostMethodGson postClient = new RedditPostMethodGson();

        final String hideMethod = String.format("/api/hide");

        for (final Posts.Post pd : data.getData().getChildren()) {
            this.log.debug("post: {}", pd.getPostData());

            final Map<String, String> param =
                    Map.of("id", pd.getPostData().getName());

            if (pd.getPostData().isHidden()) {
                continue;
            }

            final HttpResponse<String> hideResponse =
                    postClient.postMethod(hideMethod, param);

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
