package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

/**
 */
@ApplicationScoped
public class RedditDeleteMethod implements Serializable {
    private static final long serialVersionUID = -2067248916726106902L;

    @SuppressWarnings("unused")
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final RedditHttpClient redditHttpClient = new RedditHttpClient();

    final HttpRequest.Builder deleteBuilder = HttpRequest.newBuilder()
            .DELETE();

    /**
     * @param method
     * @param params
     * @return HttpResponse
     * @throws InterruptedException
     * @throws IOException
     */
    public HttpResponse<String> deleteMethod(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        return this.redditHttpClient.clientSend(this.deleteBuilder, method,
                params);
    }
}
