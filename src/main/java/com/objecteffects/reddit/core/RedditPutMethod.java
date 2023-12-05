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

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class RedditPutMethod implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditHttpClient redditHttpClient;

    /**
     */
    public RedditPutMethod() {
    }

    /**
     * @param _redditHttpClient the redditHttpClient to set
     */
    public void setRedditHttpClient(final RedditHttpClient _redditHttpClient) {
        this.redditHttpClient = _redditHttpClient;
    }

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

        this.log.debug("paramsJson: {}", pj);

        final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .PUT(BodyPublishers.ofString(pj));

        return this.redditHttpClient.clientSend(requestBuilder, method,
                Collections.emptyMap());
    }
}
