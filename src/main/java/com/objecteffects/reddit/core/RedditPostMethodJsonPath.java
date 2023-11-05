package com.objecteffects.reddit.core;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RedditPostMethodJsonPath {
    private final Logger log =
            LoggerFactory.getLogger(RedditPostMethodJsonPath.class);

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

        final String pj = params.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));

        final String paramsJson = "{" + pj + "}";

        this.log.debug("paramsJson: {}", paramsJson);

        final HttpRequest.Builder getBuilder =
                HttpRequest.newBuilder()
                        .POST(BodyPublishers.ofString(paramsJson));

        return this.redditHttpClient.clientSend(getBuilder,
                method, params);
    }
}
