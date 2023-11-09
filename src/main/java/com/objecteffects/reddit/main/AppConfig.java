package com.objecteffects.reddit.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class AppConfig {
    private final static String configFile =
            "c:/home/lumpy/redditconfig.properties";

    final Properties configProps = new Properties();
    private String oauthToken = null;

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
    public AppConfig() throws FileNotFoundException, IOException {
        loadConfiguration();
    }

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadConfiguration() throws FileNotFoundException, IOException {
        try (FileInputStream in = new FileInputStream(configFile)) {
            this.configProps.load(in);
        }
    }

    /**
     * @return
     */
    public String getUsername() {
        final String key = "username";

        if (!this.configProps.contains(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return this.configProps.getProperty(key);
    }

    /**
     * @return
     */
    public String getPassword() {
        final String key = "password";

        if (!this.configProps.contains(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return this.configProps.getProperty(key);
    }

    /**
     * @return
     */
    public String getClientId() {
        final String key = "client_id";

        if (!this.configProps.contains(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return this.configProps.getProperty(key);
    }

    /**
     * @return
     */
    public String getSecret() {
        final String key = "secret";

        if (!this.configProps.contains(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return this.configProps.getProperty(key);
    }

    public String getOAuthToken() {
        if (this.oauthToken == null) {
            throw new IllegalStateException("oauthToken not set");
        }

        return this.oauthToken;
    }

    /**
     * @param _oauthToken
     */
    public void setOAuthToken(final String _oauthToken) {
        this.oauthToken = _oauthToken;
    }

    public List<String> getHide() {
        return null;
    }
}
