package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class RedditGet implements Serializable {
    private static final long serialVersionUID = -1L;

    @SuppressWarnings("unused")
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditHttpClient redditHttpClient;

    /**
     */
    public RedditGet() {
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
    public HttpResponse<String> getMethod(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        final HttpRequest.Builder builder = HttpRequest.newBuilder().GET();

        return this.redditHttpClient.clientSend(builder, method, params);
    }
}
