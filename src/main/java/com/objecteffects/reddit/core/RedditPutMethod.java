package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;

/**
 */
public class RedditPutMethod implements Serializable {
    private static final long serialVersionUID = -7339995311514312150L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

//    private final RedditHttpClient redditHttpClient = new RedditHttpClient();
    @Inject
    private RedditHttpClient redditHttpClient;

    /**
     * @param method
     * @param params
     * @return HttpResponse
     * @throws InterruptedException
     * @throws IOException
     */
    public HttpResponse<String> putMethod(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {

        final ObjectMapper mapper = new ObjectMapper();

        final String pj = mapper.writeValueAsString(params);

//        final String pj = params.entrySet().stream()
//                .map(entry -> entry.getKey() + ":" + entry.getValue())
//                .collect(Collectors.joining(","));
//
//        final String paramsJson = "{" + pj + "}";

        this.log.debug("paramsJson: {}", pj);

        final HttpRequest.Builder putBuilder = HttpRequest.newBuilder()
                .PUT(BodyPublishers.ofString(pj));

        return this.redditHttpClient.clientSend(putBuilder, method,
                Collections.emptyMap());
    }
}
