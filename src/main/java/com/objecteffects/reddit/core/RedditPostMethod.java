package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;

/**
 */
public class RedditPostMethod implements Serializable {
    private static final long serialVersionUID = -6618596030606392654L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditHttpClient redditHttpClient;

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

        this.log.debug("paramsJson: {}", pj);

        final HttpRequest.Builder postRequest =
                HttpRequest.newBuilder()
                        .POST(BodyPublishers.ofString(pj));

        return this.redditHttpClient.clientSend(postRequest, method, params);
    }

    @SuppressWarnings("unused")
    private String paramsJson(final Map<String, String> params) {
        final String pj = params.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));

        final String paramsJson = "{" + pj + "}";

        return paramsJson;
    }
}
