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
    private static final long serialVersionUID = -6247653093688160678L;

    private final static String AUTH_URI = "https://www.reddit.com";

    private static final int timeoutSeconds = 15;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

//    @Inject
//    private RedditHttpClient redditHttpClient;

    @Inject
    private AppConfig appConfig;

    private String access_token;

    private final Configuration jsonConf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(timeoutSeconds))
            .version(Version.HTTP_2)
            .followRedirects(Redirect.NORMAL)
            .build();

    /**
     * @return HttpResponse
     * @throws IOException
     * @throws InterruptedException
     */
    public String getOAuthToken()
            throws IOException, InterruptedException {
        if (this.access_token != null) {
            return this.access_token;
        }

        final Map<String, String> params = new HashMap<>();

        params.put("grant_type", "password");
        params.put("username", this.appConfig.getUsername());
        params.put("password", this.appConfig.getPassword());

        final String username = this.appConfig.getClientId();
        final String password = this.appConfig.getSecret();

        this.log.debug("client_id: {}", username);
        this.log.debug("secret: {}", password);

        final String uri = "api/v1/access_token";

        final String fullUri = String.format("%s/%s", AUTH_URI, uri);

        this.log.debug("fullUri: {}", fullUri);

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
                .uri(URI.create(fullUri))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        this.log.debug("request headers: {}", request.headers());

        final HttpResponse<String> response = client.send(request,
                BodyHandlers.ofString());

        this.log.debug("auth response status: {}",
                Integer.valueOf(response.statusCode()));

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
                Integer.valueOf(stringMap.size()));

        if (!stringMap.containsKey("access_token")) {
            this.log.error("no access_token");

            throw new IllegalStateException("no access_token");
        }

        this.access_token = stringMap.get("access_token");

        this.log.debug("access_token: {}", this.access_token);

        return this.access_token;
    }

    /**
     * @return HttpResponse
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> revokeToken()
            throws IOException, InterruptedException {
        if (this.access_token == null) {
            return null;
        }

        final Map<String, String> params = new HashMap<>();

        params.put("token", this.access_token);
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

        final HttpResponse<String> response = client.send(request,
                BodyHandlers.ofString());

        if (response == null) {
            this.log.warn("null response from revoke token");

            this.access_token = null;

            return null;
        }

        this.log.debug("revoke response status: {}",
                Integer.valueOf(response.statusCode()));
        this.log.debug("revoke response headers: {}", response.headers());
        this.log.debug("revoke response body: {}", response.body());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "status code: " + response.statusCode());
        }

        this.access_token = null;

        return response;
    }

    private static String basicAuth(final String username,
            final String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes());
    }
}
