package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Base64;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.objecteffects.reddit.main.AppConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 */
@ApplicationScoped
public class RedditOAuth implements Serializable {
    private static final long serialVersionUID = -6247653093688160678L;

    public final static String AUTH_URL = "https://www.reddit.com";

    private static final int timeoutSeconds = 15;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditHttpClient redditHttpClient;

    private final AppConfig configuration =
            new AppConfig();

    private String access_token;

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    public RedditOAuth() {
        // empty
    }

    /**
     * @return HttpResponse
     * @throws IOException
     * @throws InterruptedException
     */
    public String getOAuthToken()
            throws IOException, InterruptedException {
//        if (this.configuration.getOAuthToken() != null) {
//            this.log.debug("auth token already loaded");
//
//            return null;
//        }

        if (this.access_token != null) {
            return this.access_token;
        }

        final Map<String, String> params = new HashMap<>();

        params.put("grant_type", "password");
        params.put("username", this.configuration.getUsername());
        params.put("password", this.configuration.getPassword());

        final String username = this.configuration.getClientId();
        final String password = this.configuration.getSecret();

        this.log.debug("client_id: {}", username);
        this.log.debug("secret: {}", password);

        final String method = "api/v1/access_token";

        final String fullUrl = String.format("%s/%s", AUTH_URL, method);

        this.log.debug("fullUrl: {}", fullUrl);

        final String form = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        this.log.debug("form: {}", form);

        final HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .header("Authorization", basicAuth(username, password))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        this.log.debug("request headers: {}", request.headers());

//        final HttpClient client = RedditHttpClient.getHttpClient();
        final HttpClient client = this.redditHttpClient.getHttpClient();

        final HttpResponse<String> response = client.send(request,
                BodyHandlers.ofString());

        this.log.debug("auth response status: {}",
                Integer.valueOf(response.statusCode()));

        if (response.statusCode() != 200) {
//            this.configuration.setOAuthToken(null);

            throw new IllegalStateException(
                    "status code: " + response.statusCode());
        }

        this.log.debug("auth response headers: {}", response.headers());
        this.log.debug("auth response body: {}", response.body());

        final String path = "$";

        final TypeRef<Map<String, String>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext = JsonPath.using(this.conf)
                .parse(response.body());

        final Map<String, String> stringMap =
                jsonContext.read(path, typeRef);

        this.log.debug("stringMap size: {}",
                Integer.valueOf(stringMap.size()));

        if (!stringMap.containsKey("access_token")) {
            this.log.error("no access_token");

            throw new IllegalStateException("no access_token");
        }

        this.access_token = stringMap.get("access_token");

        this.log.debug("access_token: {}", this.access_token);

//        this.configuration.setOAuthToken(access_token);

        return this.access_token;
    }

    /**
     * @return HttpResponse
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> revokeToken()
            throws IOException, InterruptedException {
        final Map<String, String> params = new HashMap<>();

//        final String access_token = this.configuration.getOAuthToken();

        if (this.access_token == null) {
            return null;
        }

        params.put("token", this.access_token);
        params.put("token_type_hint", "access_token");

        final String username = this.configuration.getClientId();
        final String password = this.configuration.getSecret();

        final String form = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        this.log.debug("form: {}", form);

        final String method = "api/v1/revoke_token";
        final String fullUrl = String.format("%s/%s",
                AUTH_URL, method);

        this.log.debug("fullUrl: " + fullUrl);

        final HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .header("Authorization", basicAuth(username, password))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        this.log.debug("headers: {}", request.headers());

        final HttpClient client = this.redditHttpClient.getHttpClient();

        final HttpResponse<String> response = client.send(request,
                BodyHandlers.ofString());

        this.log.debug("revoke response status: {}",
                Integer.valueOf(response.statusCode()));
        this.log.debug("revoke response headers: {}", response.headers());
        this.log.debug("revoke response body: {}", response.body());

        if (response.statusCode() != 200) {
//            this.configuration.setOAuthToken(null);

            throw new IllegalStateException(
                    "status code: " + response.statusCode());
        }

//        this.configuration.setOAuthToken(null);

        this.access_token = null;

        return response;
    }

    private static String basicAuth(final String username,
            final String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes());
    }
}
