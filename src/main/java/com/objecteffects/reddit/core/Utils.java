package com.objecteffects.reddit.core;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.EnumSet;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

/**
 */
public class Utils {
    public static final int timeoutSeconds = 15;

    private final static Configuration jsonConf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(timeoutSeconds))
            .version(Version.HTTP_1_1)
            .followRedirects(Redirect.ALWAYS)
            .build();

    /**
     * @return
     */
    public static Configuration jsonConf() {
        return jsonConf;
    }

    /**
     * @return
     */
    public static HttpClient httpClient() {
        return client;
    }
}
