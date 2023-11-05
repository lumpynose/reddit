package com.objecteffects.reddit.core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.AppConfig;

/**
 *
 */
public class RedditHttpClient {
    final Logger log =
            LoggerFactory.getLogger(RedditHttpClient.class);

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(RedditHttpClient.timeoutSeconds))
            .version(Version.HTTP_2)
            .followRedirects(Redirect.NORMAL)
            .build();

    public final static String AUTH_URL = "https://www.reddit.com";
    public final static String METHOD_URL = "https://oauth.reddit.com";

    // list of friends
    public final static String FRIENDS_METHOD = "prefs/friends";
    // info about a user
    public final static String ABOUT_METHOD = "user/%s/about";
    // friend or unfriend a user
    public final static String FRIEND_METHOD = "api/v1/me/friends/%s";
    // list of posts by a user
    public final static String SUBMITTED_METHOD = "/user/%s/submitted";
    // hide a post
    public final static String HIDE_METHOD = "/api/hide";
    // info about me
    public final static String ABOUT_ME_METHOD = "api/v1/me";
    // revoke OAuth token
    public final static String REVOKE_TOKEN_METHOD = "api/v1/revoke_token";
    // get OAuth token
    public final static String GET_TOKEN_METHOD = "api/v1/access_token";

    public final static int timeoutSeconds = 15;

    @SuppressWarnings("boxing")
    public final static List<Integer> okCodes =
            Arrays.asList(200, 201, 202, 203, 204);

    private final RedditOAuthJsonPath redditOAuth =
            new RedditOAuthJsonPath();
    private final AppConfig appConfig =
            new AppConfig();

    public static HttpClient getHttpClient() {
        return client;
    }

    /**
     * @param request
     * @param method
     * @param params
     * @return HttpResponse
     * @throws InterruptedException
     * @throws IOException
     */
    @SuppressWarnings("boxing")
    public HttpResponse<String> clientSend(
            final HttpRequest.Builder request,
            final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        final String fullUrl;

        if (!params.isEmpty()) {
            final String form = params.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));

            this.log.debug("form: {}, {}", form, form.length());

            fullUrl = String.format("%s/%s?%s",
                    RedditHttpClient.METHOD_URL, method, form);
        }
        else {
            fullUrl = String.format("%s/%s",
                    RedditHttpClient.METHOD_URL, method);
        }

        this.log.debug("fullUrl: {}", fullUrl);

        // loads the OAuth token for AppConfig.getOAuthToken().
        this.redditOAuth.getAuthToken();

        final HttpRequest buildRequest = request
                .headers("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .header("Authorization",
                        "bearer " + this.appConfig.getOAuthToken())
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(RedditHttpClient.timeoutSeconds))
                .build();

//        log.debug("headers: {}", request.headers());

        HttpResponse<String> response = null;

        try {
            this.log.debug("method: {}", method);

            response = client.send(buildRequest, BodyHandlers.ofString());

            this.log.debug("response status: {}",
                    Integer.valueOf(response.statusCode()));
            this.log.debug("response headers: {}", response.headers());
            // this.log.debug("response body: {}", response.body());
        }
        catch (IOException | InterruptedException e) {
            this.log.debug("exception: {}", e);

            // fall through to retries below

            // response will be null at this point.
            // response = null;
        }

        if (response == null || !okCodes.contains(response.statusCode())) {
            for (int i = 1; i < 11; i++) {
                Thread.sleep(i * 500);

                try {
                    this.log.debug("method: {}", method);

                    response = client.send(buildRequest,
                            BodyHandlers.ofString());

                    this.log.debug("response status: {}",
                            Integer.valueOf(response.statusCode()));
                    this.log.debug("response headers: {}", response.headers());
                    this.log.debug("response body: {}", response.body());

                    if (okCodes.contains(response.statusCode())) {
                        break;
                    }
                }
                catch (IOException | InterruptedException e) {
                    this.log.debug("exception: {}", e);

                    // keep retrying

                    response = null;
                }
            }
        }

        if (response == null || !okCodes.contains(response.statusCode())) {
            return null;
        }

        return response;
    }
}
