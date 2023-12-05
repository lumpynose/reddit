package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.RedditPost;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Post;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class UpVotePosts implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @Inject
    private RedditGet getClient;

    @Inject
    private RedditPost postClient;

    @Inject
    GetPosts getPosts;

    /**
     */
    public UpVotePosts() {
    }

    /**
     * @param _getClient the getClient to set
     */
    public void setGetClient(final RedditGet _getClient) {
        this.getClient = _getClient;
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
     * @return String
     * @throws IOException
     * @throws InterruptedException
     */
    @SuppressWarnings("boxing")
    public String upVotePosts(final String name, final Integer count,
            final String lastAfter)
            throws IOException, InterruptedException {
        if (count <= 0) {
            return null;
        }

        Thread.sleep(600);

        final List<Post> posts = this.getPosts
                .getPosts(name, count, lastAfter);

        final String upVoteUri = String.format("api/vote");

        for (final Post pd : posts) {
            Thread.sleep(600);

            this.log.debug("post: {}", pd);

            final Map<String, String> param =
                    Map.of("id", pd.getName(),
                            "dir", "1",
                            "rank", "2");

            final HttpResponse<String> upVoteResponse =
                    this.postClient.postMethod(upVoteUri, param);

            if (upVoteResponse == null) {
                this.log.debug("null response");

                continue;
            }

            this.log.debug("response: {}",
                    upVoteResponse.statusCode());
        }

        String after = null;

        if (posts.size() > 0) {
            after = posts.get(posts.size() - 1).getName();
        }

        this.log.debug("after: {}", after);

        return after;
    }
}
