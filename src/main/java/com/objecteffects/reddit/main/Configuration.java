package com.objecteffects.reddit.main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {
    private final static Logger log = LogManager
            .getLogger(Configuration.class);

    private static String username = null;
    private static String password = null;
    private static String clientId = null;
    private static String secret = null;
    private static String oauthToken = null;
    private static List<String> hide = null;

    private final static String configFile = "c:/home/lumpy/redditconfig.properties";

    private final static Parameters params = new Parameters();

    private final static FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
            PropertiesConfiguration.class)
            .configure(params.properties()
                    .setFileName(configFile)
                    .setListDelimiterHandler(
                            new DefaultListDelimiterHandler(',')));

    public static void loadConfiguration() {
        try {
            final FileBasedConfiguration config = builder.getConfiguration();

            username = config.getString("username");
            password = config.getString("password");

            clientId = config.getString("client_id");
            secret = config.getString("secret");

            hide = Arrays.asList(config.getStringArray("hide"));
        }
        catch (final ConfigurationException ex) {
            log.error("configuration exception: ", ex);

            username = "";
            password = "";
            clientId = "";
            secret = "";
            hide = Collections.emptyList();
        }
    }

    public static String getUsername() {
        if (username == null) {
            loadConfiguration();
        }

        return username;
    }

    public static String getPassword() {
        if (password == null) {
            loadConfiguration();
        }

        return password;
    }

    public static String getClientId() {
        if (clientId == null) {
            loadConfiguration();
        }

        return clientId;
    }

    public static String getSecret() {
        if (secret == null) {
            loadConfiguration();
        }

        return secret;
    }

    public static String getOAuthToken() {
        return oauthToken;
    }

    public static void setOAuthToken(final String _oauthToken) {
        oauthToken = _oauthToken;
    }

    public static List<String> getHide() {
        if (hide == null) {
            loadConfiguration();
        }

        return hide;
    }

    public static String dumpConfig() {
        return new ToStringBuilder(Configuration.class)
                .append(username)
                .append(password)
                .append(clientId)
                .append(secret)
                .append(oauthToken)
                .append(hide)
                .toString();
    }
}
