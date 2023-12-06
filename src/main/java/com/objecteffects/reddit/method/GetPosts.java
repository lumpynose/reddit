package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.objecteffects.reddit.core.RedditGet;
import com.objecteffects.reddit.core.Utils;
import com.objecteffects.reddit.data.Post;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class GetPosts implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf = Utils.jsonConf();

    @Inject
    private RedditGet getClient;

    /**
     * @param _get
     */
    public void setGet(final RedditGet _get) {
        this.getClient = _get;
    }

    /**
     * @param name
     * @param limit
     * @param lastAfter
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Post> getPosts(final String name, final Integer limit,
            final String lastAfter) throws InterruptedException, IOException {
        if (limit <= 0) {
            return null;
        }

        final String submittedUri =
                String.format("user/%s/submitted", name);

        final Map<String, String> params =
                new HashMap<>(
                        Map.of("limit", String.valueOf(limit),
                                "sort", "new",
                                "type", "links"));

        if (lastAfter != null) {
            params.put("after", lastAfter);
        }

        final HttpResponse<String> methodResponse =
                this.getClient.getMethod(submittedUri, params);

        if (methodResponse == null) {
            this.log.debug("null response");

            return Collections.emptyList();
        }

        // this.log.debug("posts: {}", methodResponse.body());

        this.log.debug("method response status: {}",
                methodResponse.statusCode());

//        this.log.debug("method response headers: {}",
//                methodResponse.headers());

        final String path = "$['data']['children'][*]['data']";

        final TypeRef<List<Post>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf)
                        .parse(methodResponse.body());

        final List<Post> posts = jsonContext.read(path, typeRef);

        this.log.debug("posts list size: {}", posts.size());

        return posts;
    }
}
