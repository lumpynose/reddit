package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.objecteffects.reddit.main.Configuration;

public class TestHidePosts2 {
    private final static Logger log = LogManager
            .getLogger(TestHidePosts2.class);

    @Test
    public void testPostMethod() throws IOException, InterruptedException {
        final List<String> users = Configuration.getHide();

        log.debug("configuration: {}", Configuration.dumpConfig());

        if (users.isEmpty()) {
            return;
        }

        final var hidePosts = new HidePosts();

        for (final String user : users) {
            hidePosts.hidePosts(user, 1, null);
        }
    }
}
