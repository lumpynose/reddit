package com.objecteffects.reddit.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.reddit.main.Configuration;

/**
 *
 */
public class RedditGetMethod {
    private final static Logger log = LogManager
            .getLogger(RedditGetMethod.class);

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

        return RedditHttpClient.clientSend(getBuilder, method, params);
    }

    @SuppressWarnings({ "unused", "boxing" })
    private HttpResponse<String> getMethodOld(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        final String fullUrl;

        log.debug("method: {}", method);

        if (!params.isEmpty()) {
            final String formattedParams = params.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));

            log.debug("form: {}, {}", formattedParams,
                    formattedParams.length());

            fullUrl = String.format("%s/%s?%s", RedditHttpClient.METHOD_URL,
                    method, formattedParams);
        }
        else {
            fullUrl = String.format("%s/%s", RedditHttpClient.METHOD_URL,
                    method);
        }

        log.debug("fullUrl: {}", fullUrl);

        // loads the OAuth token for Configuration.getOAuthToken().
        RedditOAuth.getAuthToken();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .headers("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .header("Authorization",
                        "bearer " + Configuration.getOAuthToken())
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(15))
                .build();

//        log.debug("headers: {}", request.headers());

        final HttpClient client = RedditHttpClient.getHttpClient();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());

            log.debug("response status: {}",
                    Integer.valueOf(response.statusCode()));
            log.debug("response headers: {}", response.headers());
            log.debug("response body: {}", response.body());
        }
        catch (IOException | InterruptedException e) {
            log.debug("exception: {}", e);
        }

        if (response == null) {
            for (int i = 1; i < 11; i++) {
                Thread.sleep(i * 1500);

                try {
                    response = client.send(request, BodyHandlers.ofString());

                    if (response == null) {
                        break;
                    }

                    log.debug("response status: {}",
                            Integer.valueOf(response.statusCode()));
                    log.debug("response headers: {}", response.headers());
                    log.debug("response body: {}", response.body());
                }
                catch (IOException | InterruptedException e) {
                    log.debug("exception: {}", e);
                }
            }
        }

        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response;
    }
}
