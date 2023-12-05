package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

// list of friends
// public final static String FRIENDS_URI = "prefs/friends";
// info about a user
// public final static String ABOUT_URI = "user/%s/about";
// friend or unfriend a user
// public final static String FRIEND_URI = "api/v1/me/friends/%s";
// list of posts by a user
// public final static String SUBMITTED_URI = "/user/%s/submitted";
// hide a post
// public final static String HIDE_URI = "/api/hide";
// info about me
// public final static String ABOUT_ME_URI = "api/v1/me";
// revoke OAuth token
// public final static String REVOKE_TOKEN_URI = "api/v1/revoke_token";
// get OAuth token
// public final static String GET_TOKEN_URI = "api/v1/access_token";

/**
 */
@Default
public class RedditHttpClient implements Serializable {
    private static final long serialVersionUID = -1L;

    private final static Logger log =
            LoggerFactory.getLogger(RedditHttpClient.class);

    private final static String METHOD_URI = "https://oauth.reddit.com";

    @SuppressWarnings("boxing")
    private final static List<Integer> okCodes =
            List.of(200, 201, 202, 203, 204);

    @Inject
    private RedditOAuth redditOAuth;

    /**
     */
    public RedditHttpClient() {
    }

    /**
     * @param _redditOAuth the redditOAuth to set
     */
    public void setRedditOAuth(final RedditOAuth _redditOAuth) {
        this.redditOAuth = _redditOAuth;
    }

    /**
     * @param requestBuilder
     * @param method
     * @param params
     * @return HttpResponse
     * @throws InterruptedException
     * @throws IOException
     */
    @SuppressWarnings("boxing")
    public HttpResponse<String> clientSend(
            final HttpRequest.Builder requestBuilder,
            final String method,
            final Map<String, String> params)
            throws InterruptedException, IOException {
        String fullUri;
        final String token = this.redditOAuth.getOAuthToken();

        if (!params.isEmpty()) {
            final String form = urlParams(params);

            log.debug("form, length: {}, {}", form,
                    form.length());

            fullUri = String.format("%s/%s?%s", METHOD_URI, method, form);
        }
        else {
            fullUri = String.format("%s/%s", METHOD_URI, method);
        }

//        final HttpRequest copy = requestBuilder.copy().build();
//
//        if ("POST".compareTo(copy.method()) == 0) {
//            fullUri = "http://localhost:9090/";
//        }

        log.debug("fullUri: {}", fullUri);
        log.debug("token: {}", token);

        final HttpRequest request = requestBuilder
                .setHeader("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .setHeader("Authorization", "bearer " + token)
                .uri(URI.create(fullUri))
                .timeout(Duration.ofSeconds(Utils.timeoutSeconds))
                .build();

        log.debug("request headers: {}", request.headers());
        log.debug("uri: {}", request.uri());

        HttpResponse<String> response = null;

        log.debug("method: {}, {}", method, request.method());

        response = Utils.httpClient().send(request, BodyHandlers.ofString());

        if (response == null) {
            log.debug("null response");

            return null;
        }

        log.debug("response status: {}",
                response.statusCode());
        log.debug("response headers: {}", response.headers());
        // this.log.debug("response body: {}", response.body());

        debugHeaders(response.headers());

        this.redditOAuth.revokeToken();

        if (!okCodes.contains(response.statusCode())) {
            log.debug("bad status code: {}, {}",
                    response.statusCode(), method);

            return null;
        }

        return response;
    }

    public static boolean validResponse(final HttpResponse<String> response) {
        return okCodes.contains(response.statusCode());
    }

    /**
     * @param params
     * @return
     */
    static String urlParams(final Map<String, String> params) {
        final String form = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return form;
    }

    static String urlParamsJson(final Map<String, String> params) {
        final String pj = params.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));

        return "{" + pj + "}";
    }

    /**
     * @param headers
     */
    public static void debugHeaders(final HttpHeaders headers) {
        final Map<String, List<String>> map = headers.map();
        for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
            log.debug("entry: {}, {}", entry.getKey(), entry.getValue());
//            for (final String entryValue : entry.getValue()) {
//                log.debug("value: {}", entryValue);
//            }
        }
    }
}
