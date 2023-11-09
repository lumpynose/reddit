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
import org.apache.commons.configuration2.io.ConfigurationLogger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AppConfigCommons {
    private final Logger log =
            LoggerFactory.getLogger(AppConfigCommons.class);

    private static String username = null;
    private static String password = null;
    private static String clientId = null;
    private static String secret = null;
    private static String oauthToken = null;
    private static List<String> hide = null;

    private final static String configFile =
            "c:/home/lumpy/redditconfig.properties";

    private final static Parameters params = new Parameters();

    private final static FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(
                    PropertiesConfiguration.class)
                    .configure(params.properties()
                            .setLogger(new ConfigurationLogger(
                                    LoggerFactory.getLogger("reddit")
                                            .getName()))
                            .setFileName(configFile)
                            .setListDelimiterHandler(
                                    new DefaultListDelimiterHandler(',')));

    /**
     *
     */
    public void loadConfiguration() {
        try {
            final FileBasedConfiguration config =
                    builder.getConfiguration();

            username = config.getString("username");
            password = config.getString("password");

            clientId = config.getString("client_id");
            secret = config.getString("secret");

            hide = Arrays.asList(config.getStringArray("hide"));
        }
        catch (final ConfigurationException ex) {
            this.log.error("configuration exception: ", ex);

            username = "";
            password = "";
            clientId = "";
            secret = "";
            hide = Collections.emptyList();
        }
    }

    public String getUsername() {
        if (username == null) {
            loadConfiguration();
        }

        return username;
    }

    public String getPassword() {
        if (password == null) {
            loadConfiguration();
        }

        return password;
    }

    public String getClientId() {
        if (clientId == null) {
            loadConfiguration();
        }

        return clientId;
    }

    public String getSecret() {
        if (secret == null) {
            loadConfiguration();
        }

        return secret;
    }

    public String getOAuthToken() {
        return oauthToken;
    }

    public void setOAuthToken(final String _oauthToken) {
        oauthToken = _oauthToken;
    }

    public List<String> getHide() {
        if (hide == null) {
            loadConfiguration();
        }

        return hide;
    }

    public String dumpConfig() {
        return new ToStringBuilder(AppConfig.class)
                .append(username)
                .append(password)
                .append(clientId)
                .append(secret)
                .append(oauthToken)
                .append(hide)
                .toString();
    }
}
