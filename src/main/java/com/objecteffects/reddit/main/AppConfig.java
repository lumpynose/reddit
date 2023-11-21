package com.objecteffects.reddit.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AppConfig {
    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final static String configFile =
            "c:/home/lumpy/redditconfig.properties";

    private final static Properties configProps = new Properties();
    private static String oauthToken = null;

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadConfiguration() {
        if (configProps.size() != 0) {
            return;
        }

        this.log.debug("loading configuration");

        try (FileInputStream in = new FileInputStream(configFile)) {
            AppConfig.configProps.load(in);
        }
        catch (final FileNotFoundException e) {
            e.printStackTrace();

            throw new IllegalStateException("missing " + configFile);
        }
        catch (final IOException e) {
            e.printStackTrace();

            throw new IllegalStateException("IOException");
        }

        if (configProps.size() != 4) {
            throw new IllegalStateException("configProps size != 4");
        }
    }

    /**
     * @return
     */
    public String getUsername() {
        final String key = "username";

        loadConfiguration();

        if (AppConfig.configProps.containsKey(key)) {
            return AppConfig.configProps.getProperty(key);
        }

        throw new IllegalStateException("missing " + key);
    }

    /**
     * @return
     */
    public String getPassword() {
        final String key = "password";

        loadConfiguration();

        if (!AppConfig.configProps.containsKey(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return AppConfig.configProps.getProperty(key);
    }

    /**
     * @return
     */
    public String getClientId() {
        final String key = "client_id";

        loadConfiguration();

        if (!AppConfig.configProps.containsKey(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return AppConfig.configProps.getProperty(key);
    }

    /**
     * @return
     */
    public String getSecret() {
        final String key = "secret";

        loadConfiguration();

        if (!AppConfig.configProps.containsKey(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return AppConfig.configProps.getProperty(key);
    }

    /**
     * The OAuthToken is not stored in the properties file but created by
     * RedditOAuth and then stored here.
     *
     * @return
     */
    public String getOAuthToken() {
        return AppConfig.oauthToken;
    }

    /**
     * @param _oauthToken
     */
    public void setOAuthToken(final String _oauthToken) {
        AppConfig.oauthToken = _oauthToken;
    }

    public List<String> getHide() {
        return null;
    }

    public String dumpConfig() {
        return AppConfig.configProps.toString();
    }
}
