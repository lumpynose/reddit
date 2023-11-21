package com.objecteffects.reddit.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objecteffects.reddit.main.AppConfig;

/**
 *
 */
public class RedditGetMethod {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final RedditOAuth redditOAuth =
            new RedditOAuth();
    private final RedditHttpClient redditHttpClient =
            new RedditHttpClient();
    private final AppConfig configuration =
            new AppConfig();

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

    @SuppressWarnings({ "unused", "boxing" })
    private HttpResponse<String> getMethodOld(final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        final String fullUrl;

        this.log.debug("method: {}", method);

        if (!params.isEmpty()) {
            final ObjectMapper mapper = new ObjectMapper();

            final String formattedParams = mapper.writeValueAsString(params);

//            final String formattedParams = params.entrySet().stream()
//                    .map(entry -> entry.getKey() + "=" + entry.getValue())
//                    .collect(Collectors.joining("&"));

            this.log.debug("form: {}, {}", formattedParams,
                    formattedParams.length());

            fullUrl = String.format("%s/%s?%s",
                    RedditHttpClient.METHOD_URL,
                    method, formattedParams);
        }
        else {
            fullUrl = String.format("%s/%s",
                    RedditHttpClient.METHOD_URL,
                    method);
        }

        this.log.debug("fullUrl: {}", fullUrl);

        // loads the OAuth token for AppConfig.getOAuthToken().
        this.redditOAuth.getAuthToken();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .headers("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .header("Authorization",
                        "bearer " + this.configuration.getOAuthToken())
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(15))
                .build();

//        log.debug("headers: {}", request.headers());

        final HttpClient client = RedditHttpClient.getHttpClient();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());

            this.log.debug("response status: {}",
                    Integer.valueOf(response.statusCode()));
            this.log.debug("response headers: {}", response.headers());
            this.log.debug("response body: {}", response.body());
        }
        catch (IOException | InterruptedException e) {
            this.log.debug("exception: {}", e);
        }

        if (response == null) {
            for (int i = 1; i < 11; i++) {
                Thread.sleep(i * 500);

                try {
                    response = client.send(request,
                            BodyHandlers.ofString());

                    if (response == null) {
                        break;
                    }

                    this.log.debug("response status: {}",
                            Integer.valueOf(response.statusCode()));
                    this.log.debug("response headers: {}", response.headers());
                    this.log.debug("response body: {}", response.body());
                }
                catch (IOException | InterruptedException e) {
                    this.log.debug("exception: {}", e);
                }
            }
        }

        if (response == null || response.statusCode() != 200) {
            return null;
        }

        return response;
    }
}
