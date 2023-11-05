package com.objecteffects.reddit.core;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 *
 */
public class RedditPutMethodGson {
    @SuppressWarnings("unused")
    private final Logger log =
            LoggerFactory.getLogger(RedditPutMethodGson.class);

    private final RedditHttpClient redditHttpClient = new RedditHttpClient();

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

//        final String formattedParams = params.entrySet().stream()
//                .map(entry -> entry.getKey() + "=" + entry.getValue())
//                .collect(Collectors.joining("&"));
//
//        this.log.debug("form: {}, {}", formattedParams,
//                formattedParams.length());

        final Gson gson = new Gson();

        final String json = gson.toJson(params);

        // log.debug("json: {}", json);

        final HttpRequest.Builder putBuilder = HttpRequest.newBuilder()
                .PUT(BodyPublishers.ofString(json));

        return this.redditHttpClient.clientSend(putBuilder, method,
                Collections.emptyMap());
    }
}
