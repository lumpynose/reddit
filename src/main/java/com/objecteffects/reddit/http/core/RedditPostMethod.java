package com.objecteffects.reddit.http.core;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 *
 */
public class RedditPostMethod {
    @SuppressWarnings("unused")
    private final Logger log =
            LoggerFactory.getLogger(RedditPostMethod.class);

    private final RedditHttpClient redditHttpClient = new RedditHttpClient();

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
        final Gson gson = new Gson();

        final String json = gson.toJson(params);

        // log.debug("json: {}", json);

        final HttpRequest.Builder getBuilder =
                HttpRequest.newBuilder()
                        .POST(BodyPublishers.ofString(json));

        return this.redditHttpClient.clientSend(getBuilder,
                method, params);
    }
}
