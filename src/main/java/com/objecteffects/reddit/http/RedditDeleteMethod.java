package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedditDeleteMethod {
    @SuppressWarnings("unused")
    private final static Logger log = LogManager
            .getLogger(RedditDeleteMethod.class);

    public HttpResponse<String> deleteMethod(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {

        final HttpRequest.Builder deleteBuilder = HttpRequest.newBuilder()
                .DELETE();

        return RedditHttpClient.clientSend(deleteBuilder, method, params);
    }
}
