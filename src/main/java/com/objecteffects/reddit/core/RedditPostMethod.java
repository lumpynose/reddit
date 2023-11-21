package com.objecteffects.reddit.core;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 */
public class RedditPostMethod {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final RedditHttpClient redditHttpClient =
            new RedditHttpClient();

    /**
     * @param method
     * @param params
     * @return HttpResponse
     * @throws InterruptedException
     * @throws IOException
     */
    public HttpResponse<String> postMethod(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        this.log.debug("params: {}", params);

        final ObjectMapper mapper = new ObjectMapper();

        final String pj = mapper.writeValueAsString(params);

//        final String pj = params.entrySet().stream()
//                .map(entry -> entry.getKey() + ":" + entry.getValue())
//                .collect(Collectors.joining(","));
//
//        final String paramsJson = "{" + pj + "}";

        this.log.debug("paramsJson: {}", pj);

        final HttpRequest.Builder getBuilder =
                HttpRequest.newBuilder()
                        .POST(BodyPublishers.ofString(pj));

        return this.redditHttpClient.clientSend(getBuilder,
                method, params);
    }
}
