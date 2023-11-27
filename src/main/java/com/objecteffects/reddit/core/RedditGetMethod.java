package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

/**
 */
public class RedditGetMethod implements Serializable {
    private static final long serialVersionUID = -823906022241457997L;

    @SuppressWarnings("unused")
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditHttpClient redditHttpClient;

    private final HttpRequest.Builder getRequest =
            HttpRequest.newBuilder().GET();

    /**
     * @param redditHttpClient the redditHttpClient to set
     */
    public void setRedditHttpClient(final RedditHttpClient redditHttpClient) {
        this.redditHttpClient = redditHttpClient;
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
        return this.redditHttpClient.clientSend(this.getRequest, method,
                params);
    }
}
