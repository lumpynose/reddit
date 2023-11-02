package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.objecteffects.reddit.http.data.Posts;

/**
 *
 */
public class UpVotePosts {
    private final static Logger log = LogManager
            .getLogger(UpVotePosts.class);

    /**
     * @param name
     * @param count
     * @param lastAfter
     * @return String
     * @throws IOException
     * @throws InterruptedException
     */
    @SuppressWarnings("boxing")
    public String upVotePosts(final String name, final int count,
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

        log.debug("data length: {}", data.getData().getChildren().size());

        final var postClient = new RedditPostMethod();

        final var upVoteMethod = String.format("api/vote");

        for (final Posts.Post pd : data.getData().getChildren()) {
            log.debug("post: {}", pd.getPostData());

            final var param = Map.of("id", pd.getPostData().getName(),
                    "dir", "1",
                    "rank", "2");

//            if (pd.getPostData().isHidden()) {
//                continue;
//            }

            final var upVoteResponse =
                    postClient.postMethod(upVoteMethod, param);

            log.debug("response: {}", upVoteResponse.statusCode());
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

        log.debug("after: {}", after);

        return after;
    }
}
