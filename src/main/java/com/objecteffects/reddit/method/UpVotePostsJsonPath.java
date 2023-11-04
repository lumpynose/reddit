package com.objecteffects.reddit.method;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditPostMethod;
import com.objecteffects.reddit.data.Post;

import jakarta.inject.Named;

/**
 *
 */
@Named
public class UpVotePostsJsonPath {
    private final Logger log =
            LoggerFactory.getLogger(UpVotePostsJsonPath.class);

    public UpVotePostsJsonPath() {
        // empty
    }

    /**
     * @param name
     * @param count
     * @param lastAfter
     * @return String
     * @throws IOException
     * @throws InterruptedException
     */
    public String upVotePosts(final String name, final int count,
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

        final String body =
                getClient.getMethod(submittedMethod, params).body();

        this.log.debug(body);

        final String path = "$['data']['children'][*]['data']";

        final TypeRef<List<Post>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.parse(body);

        final List<Post> posts = jsonContext.read(path, typeRef);

        this.log.debug("list size: {}", Integer.valueOf(posts.size()));

        final RedditPostMethod postClient = new RedditPostMethod();

        final String upVoteMethod = String.format("api/vote");

        for (final Post pd : posts) {
            this.log.debug("post: {}", pd);

            final Map<String, String> param =
                    Map.of("id", pd.getName(),
                            "dir", "1",
                            "rank", "2");

            final HttpResponse<String> upVoteResponse =
                    postClient.postMethod(upVoteMethod, param);

            this.log.debug("response: {}",
                    Integer.valueOf(upVoteResponse.statusCode()));
        }

        String after = null;

        if (posts.size() > 0) {
            after = posts.get(posts.size() - 1).getName();
        }

        this.log.debug("after: {}", after);

        return after;
    }
}
