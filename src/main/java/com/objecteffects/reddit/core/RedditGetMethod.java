package com.objecteffects.reddit.core;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 */
@ApplicationScoped
public class RedditGetMethod {
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
    public HttpResponse<String> getMethod(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {

        final HttpRequest.Builder getBuilder = HttpRequest.newBuilder().GET();

        return this.redditHttpClient.clientSend(getBuilder,
                method, params);
    }
}
