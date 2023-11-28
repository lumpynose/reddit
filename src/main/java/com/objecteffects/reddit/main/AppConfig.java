package com.objecteffects.reddit.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 */
@ApplicationScoped
public class AppConfig implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final static String configFile =
            "c:/home/lumpy/redditconfig.properties";

    private final static Properties configProps = new Properties();

    public AppConfig() {
    }

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
            configProps.load(in);
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

        if (configProps.containsKey(key)) {
            return configProps.getProperty(key);
        }

        throw new IllegalStateException("missing " + key);
    }

    /**
     * @return
     */
    public String getPassword() {
        final String key = "password";

        loadConfiguration();

        if (!configProps.containsKey(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return configProps.getProperty(key);
    }

    /**
     * @return
     */
    public String getClientId() {
        final String key = "client_id";

        loadConfiguration();

        if (!configProps.containsKey(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return configProps.getProperty(key);
    }

    /**
     * @return
     */
    public String getSecret() {
        final String key = "secret";

        loadConfiguration();

        if (!configProps.containsKey(key)) {
            throw new IllegalStateException("missing " + key);
        }

        return configProps.getProperty(key);
    }

    /**
     * @return
     */
    public List<String> getHide() {
        return null;
    }

    /**
     * @return
     */
    public String dumpConfig() {
        return AppConfig.configProps.toString();
    }
}
