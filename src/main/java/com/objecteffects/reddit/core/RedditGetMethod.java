package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**
 */
@RequestScoped
public class RedditGetMethod implements Serializable {
    private static final long serialVersionUID = -823906022241457997L;

    @SuppressWarnings("unused")
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditHttpClient redditHttpClient;

    final HttpRequest.Builder getRequest = HttpRequest.newBuilder().GET();

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
