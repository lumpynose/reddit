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

    public UpVotePosts() {
    }

    /**
     * @param getClient the getClient to set
     */
    public void setGetClient(final RedditGetMethod getClient) {
        this.getClient = getClient;
    }

    /**
     * @param postClient the postClient to set
     */
    public void setPostClient(final RedditPostMethod postClient) {
        this.postClient = postClient;
    }

//    /**
//     * @param _getClient
//     * @param _postClient
//     */
//    public UpVotePosts(final RedditGetMethod _getClient,
//            final RedditPostMethod _postClient) {
//        this.getClient = _getClient;
//        this.postClient = _postClient;
//    }

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
        final String submittedUri =
                String.format("user/%s/submitted", name);

        final Map<String, String> params =
                new HashMap<>(
                        Map.of("limit", String.valueOf(count),
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

        this.log.debug("list size: {}", Integer.valueOf(posts.size()));

        final String upVoteUri = String.format("api/vote");

        for (final Post pd : posts) {
            this.log.debug("post: {}", pd);

            final Map<String, String> param =
                    Map.of("id", pd.getName(),
                            "dir", "0",
                            "rank", "2");

            final HttpResponse<String> upVoteResponse =
                    this.postClient.postMethod(upVoteUri, param);

            if (upVoteResponse == null) {
                this.log.debug("null response");

                continue;
            }

            this.log.debug("response: {}",
                    Integer.valueOf(upVoteResponse.statusCode()));

            Thread.sleep(600);
        }

        String after = null;

        if (posts.size() > 0) {
            after = posts.get(posts.size() - 1).getName();
        }

        this.log.debug("after: {}", after);

        return after;
    }
}
