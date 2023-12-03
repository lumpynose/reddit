package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
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

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final int timeoutSeconds = 15;

//    private static final HttpClient client = HttpClient.newBuilder()
//            .connectTimeout(Duration.ofSeconds(timeoutSeconds))
//            .version(Version.HTTP_2)
//            .followRedirects(Redirect.NORMAL)
//            .build();

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
        final String fullUri;
        final String token = this.redditOAuth.getOAuthToken();

        if (!params.isEmpty()) {
            final String form = params.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));

            this.log.debug("form, length: {}, {}", form, form.length());

            fullUri = String.format("%s/%s?%s", METHOD_URI, method, form);
        }
        else {
            fullUri = String.format("%s/%s", METHOD_URI, method);
        }

        this.log.debug("fullUri: {}", fullUri);
        this.log.debug("token: {}", token);

        final HttpRequest request = requestBuilder
                .setHeader("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .setHeader("Authorization", "bearer " + token)
                .uri(URI.create(fullUri))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        this.log.debug("request headers: {}", request.headers());

        HttpResponse<String> response = null;

        try {
            this.log.debug("method: {}, {}", method, request.method());

            response = this.redditOAuth.getClient().send(request,
                    BodyHandlers.ofString());

            this.log.debug("response status: {}", response.statusCode());
            this.log.debug("response headers: {}", response.headers());
            // this.log.debug("response body: {}", response.body());
        }
        catch (IOException | InterruptedException | NullPointerException e) {
            this.log.debug("exception: {}", e);

            // fall through to retries below

            // response may be null at this point?
            // response = null;
        }

//        if (response == null || !okCodes.contains(response.statusCode())) {
//            for (int i = 0; i < 3; i++) {
//                Thread.sleep(600 * i);
//
//                try {
//                    this.log.debug("method: {}, {}", method,
//                            buildRequest.method());
//
//                    response = this.redditOAuth.getClient()
//                            .send(buildRequest, BodyHandlers.ofString());
//
//                    this.log.debug("response status: {}",
//                            Integer.valueOf(response.statusCode()));
//                    this.log.debug("response headers: {}", response.headers());
//                    this.log.debug("response body: {}", response.body());
//
//                    if (okCodes.contains(response.statusCode())) {
//                        break;
//                    }
//                }
//                catch (IOException | InterruptedException e) {
//                    this.log.debug("exception: {}", e);
//
//                    // keep retrying
//
//                    response = null;
//                }
//            }
//        }

        this.redditOAuth.revokeToken();

        if (response == null) {
            this.log.debug("null response");

            return null;
        }

        if (!okCodes.contains(response.statusCode())) {
            this.log.debug("bad status code: {}, {}",
                    response.statusCode(), method);

            return null;
        }

        return response;
    }
}
