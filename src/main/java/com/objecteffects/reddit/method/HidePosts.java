package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.objecteffects.reddit.core.RedditPost;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Post;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class HidePosts implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @Inject
    private RedditPost postClient;

    @Inject
    private GetPosts getPosts;

    /**
     */
    public HidePosts() {
    }

    /**
     * @param _getClient the getClient to set
     */
    public void setGetPostsClient(final GetPosts _getPosts) {
    }

    /**
     * @param _postClient the postClient to set
     */
    public void setPostClient(final RedditPost _postClient) {
        this.postClient = _postClient;
    }

    /**
     * @param name
     * @param count
     * @param lastAfter
     * @return String after
     * @throws IOException
     * @throws InterruptedException
     */
    @SuppressWarnings("boxing")
    public String hidePosts(final String name, final Integer count,
            final String lastAfter)
            throws IOException, InterruptedException {
        if (count <= 0) {
            return null;
        }

        final List<Post> posts = this.getPosts
                .getPosts(name, count, lastAfter);

        final String hideUri = "api/hide";

        for (final Post post : posts) {
            Thread.sleep(600);

            this.log.debug("post: {}", post);

            if (post.isHidden()) {
                this.log.debug("skipping hidden post");

                continue;
            }

            final Map<String, String> param =
                    Map.of("id", post.getName());

            final HttpResponse<String> hideResponse =
                    this.postClient.postMethod(hideUri, param);

            this.log.debug("response: {}",
                    hideResponse.statusCode());
        }

        String after = null;

        if (posts.size() > 0) {
            after = posts.get(posts.size() - 1).getName();
        }

        this.log.debug("after: {}", after);

        return after;
    }
}
