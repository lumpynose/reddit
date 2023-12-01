package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditPostMethod;
import com.objecteffects.reddit.data.Post;

import jakarta.inject.Inject;

/**
 */
public class UpVotePosts implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    @Inject
    private RedditGetMethod getClient;

    @Inject
    private RedditPostMethod postClient;

    /**
     */
    public UpVotePosts() {
    }

    /**
     * @param _getClient the getClient to set
     */
    public void setGetClient(final RedditGetMethod _getClient) {
        this.getClient = _getClient;
    }

    /**
     * @param _postClient the postClient to set
     */
    public void setPostClient(final RedditPostMethod _postClient) {
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

        final String submittedUri =
                String.format("user/%s/submitted", name);

        final Map<String, String> params =
                new HashMap<>(
                        Map.of("limit", count.toString(),
                                "sort", "new",
                                "type", "links"));

        if (lastAfter != null && !lastAfter.isEmpty()) {
            params.put("after", lastAfter);
        }

        final HttpResponse<String> response =
                this.getClient.getMethod(submittedUri, params);

        if (response == null) {
            this.log.debug("null response");

            return null;
        }

        this.log.debug(response.body());

        final String path = "$['data']['children'][*]['data']";

        final TypeRef<List<Post>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext =
                JsonPath.using(this.conf).parse(response.body());

        final List<Post> posts =
                jsonContext.read(path, typeRef);

        this.log.debug("list size: {}", posts.size());

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
