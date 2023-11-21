package com.objecteffects.reddit.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private final static Logger log =
            LoggerFactory.getLogger(Main.class.getSimpleName());

    private final static AppConfig configuration =
            new AppConfig();

    public static void main(final String[] args) {
        log.debug("username: {}", configuration.getUsername());
    }
}
