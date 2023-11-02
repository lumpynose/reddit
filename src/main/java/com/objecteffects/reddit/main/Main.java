package com.objecteffects.reddit.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private final static Logger log = LogManager
            .getLogger(Main.class);

    public static void main(final String[] args) {
        log.debug("username: {}", Configuration.getUsername());
    }
}
