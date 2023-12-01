package com.objecteffects.reddit.core;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
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

import jakarta.inject.Inject;

/**
 */
public class RedditOAuth implements Serializable {
    private static final long serialVersionUID = -1L;

    private final static String AUTH_URI = "https://www.reddit.com";

    private static final int timeoutSeconds = 15;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private AppConfig appConfig;

    private static String access_token;

    private final Configuration jsonConf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(timeoutSeconds))
            .version(Version.HTTP_2)
            .followRedirects(Redirect.NORMAL)
            .build();

    /**
     */
    public RedditOAuth() {
    }

    /**
     * @param _appConfig the appConfig to set
     */
    public void setAppConfig(final AppConfig _appConfig) {
        this.appConfig = _appConfig;
    }

    /**
     * @return the client
     */
    public HttpClient getClient() {
        return this.client;
    }

    /**
     * @return HttpResponse
     * @throws IOException
     * @throws InterruptedException
     */
    public String getOAuthToken()
            throws IOException, InterruptedException {
        if (RedditOAuth.access_token != null) {
            return RedditOAuth.access_token;
        }

        final Map<String, String> params = new HashMap<>();

//        params.put("grant_type", "password");
//        params.put("username", this.appConfig.getUsername());
//        params.put("password", this.appConfig.getPassword());
        params.put("grant_type", "client_credentials");
        params.put("scope", "read,history,mysubreddits,vote,report");

        final String clientId = this.appConfig.getClientId();
        final String secret = this.appConfig.getSecret();

        this.log.debug("client_id: {}", clientId);
        this.log.debug("secret: {}", secret);

        final String uri = "api/v1/access_token";

        final String fullUri = String.format("%s/%s", AUTH_URI, uri);

        this.log.debug("fullUri: {}", fullUri);

        /*
         * Generates the string
         * grant_type=password&username=whatever&password=secret etc.
         */
        final String form = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        this.log.debug("form: {}", form);

        final HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", basicAuth(clientId, secret))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .uri(URI.create(fullUri))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();
//      .headers("Content-Type", "application/x-www-form-urlencoded")
//      .headers("User-Agent",
//      "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")

        this.log.debug("request headers: {}", request.headers());

        final HttpResponse<String> response = this.client
                .send(request, BodyHandlers.ofString());

        this.log.debug("auth response status: {}",
                response.statusCode());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "status code: " + response.statusCode());
        }

        this.log.debug("auth response headers: {}", response.headers());
        this.log.debug("auth response body: {}", response.body());

        final String path = "$";

        final TypeRef<Map<String, String>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext = JsonPath.using(this.jsonConf)
                .parse(response.body());

        final Map<String, String> stringMap =
                jsonContext.read(path, typeRef);

        this.log.debug("stringMap size: {}",
                stringMap.size());

        if (!stringMap.containsKey("access_token")) {
            this.log.error("no access_token");

            throw new IllegalStateException("no access_token");
        }

        RedditOAuth.access_token = stringMap.get("access_token");

        this.log.debug("access_token: {}", RedditOAuth.access_token);

        return RedditOAuth.access_token;
    }

    /**
     * @return HttpResponse
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> revokeToken()
            throws IOException, InterruptedException {
        if (RedditOAuth.access_token == null) {
            return null;
        }

        this.log.debug("revoking: {}", RedditOAuth.access_token);

        final Map<String, String> params = new HashMap<>();

        params.put("token", RedditOAuth.access_token);
        params.put("token_type_hint", "access_token");

        final String username = this.appConfig.getClientId();
        final String password = this.appConfig.getSecret();

        final String form = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        this.log.debug("form: {}", form);

        final String uri = "api/v1/revoke_token";
        final String fullUri = String.format("%s/%s", AUTH_URI, uri);

        this.log.debug("fullUrl: " + fullUri);

        final HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("User-Agent",
                        "java:com.objecteffects.reddit:v0.0.1 (by /u/lumpynose)")
                .header("Authorization", basicAuth(username, password))
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .uri(URI.create(fullUri))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        this.log.debug("headers: {}", request.headers());

        final HttpResponse<String> response = this.client
                .send(request, BodyHandlers.ofString());

        if (response == null) {
            this.log.warn("null response from revoke token");

            RedditOAuth.access_token = null;

            return null;
        }

        this.log.debug("revoke response status: {}",
                response.statusCode());
        this.log.debug("revoke response headers: {}", response.headers());
        this.log.debug("revoke response body: {}", response.body());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "status code: " + response.statusCode());
        }

        RedditOAuth.access_token = null;

        return response;
    }

    private static String basicAuth(final String username,
            final String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes());
    }
}
