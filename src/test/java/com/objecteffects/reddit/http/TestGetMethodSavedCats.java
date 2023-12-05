package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.RedditHttpClient;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.core.RedditPost;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Post;
import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.GetPosts;

import jakarta.inject.Inject;

/**
 */
@EnableWeld
public class TestGetMethodSavedCats {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @WeldSetup
    private final WeldInitiator weld =
            WeldInitiator.of(RedditGet.class, RedditHttpClient.class,
                    RedditOAuth.class, GetPosts.class, RedditPost.class,
                    AppConfig.class);

    @Inject
    private RedditGet getClient;

    @Inject
    private RedditPost postClient;

    @Inject
    private GetPosts getPosts;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGetMethodSavedCats()
            throws InterruptedException, IOException {
        savePost();

        final Map<String, String> params = Collections.emptyMap();

        final HttpResponse<String> response = this.getClient
                .getMethod("api/saved_categories", params);

        if (response == null) {
            throw new IllegalStateException("null response");
        }

        this.log.debug("saved categories: {}", response.body());

        this.log.debug("method response status: {}", response.statusCode());

        this.log.debug("method response headers: {}", response.headers());

        // this.log.debug("method response body: {}", methodResponse.body());
    }

    /**
     * @throws InterruptedException
     * @throws IOException
     */
    private void savePost() throws InterruptedException, IOException {
        final List<Post> posts = this.getPosts.getPosts("figwax", 1, null);

        final Map<String, String> params = Map.of("id",
                posts.get(0).getName(),
                "category", "figwax");

        final HttpResponse<String> response = this.postClient
                .postMethod("api/save", params);

        if (response == null) {
            throw new IllegalStateException("null save respone");
        }

        this.log.debug("response: {}", response.body());
    }
}
