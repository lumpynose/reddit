package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uwyn.urlencoder.UrlEncoder;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class RedditPostMethod implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditHttpClient redditHttpClient;

    /**
     */
    public RedditPostMethod() {
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
    public HttpResponse<String> postMethod(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        this.log.debug("params: {}", params);

        final ObjectMapper mapper = new ObjectMapper();

//        final String pj = mapper.writeValueAsString(params);
        final String pj = RedditHttpClient.urlParams(params);

        this.log.debug("params joined: {}", pj);

        final var encoded = UrlEncoder.encode(pj);
        this.log.debug("params urlencoded: {}", encoded);

        final HttpRequest.Builder requestBuilder =
                HttpRequest.newBuilder()
                        .setHeader("Content-Type",
                                "application/x-www-form-urlencoded")
                        .POST(BodyPublishers.ofString(pj));

        return this.redditHttpClient.clientSend(requestBuilder, method,
                params /* Collections.emptyMap() */);
    }
}
