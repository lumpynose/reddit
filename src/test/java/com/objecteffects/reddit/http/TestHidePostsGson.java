package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.Configuration;

public class TestHidePostsGson {
    final Logger log =
            LoggerFactory.getLogger(TestHidePostsGson.class);

    private final static Configuration configuration =
            new Configuration();

    @Test
    public void testPostMethod() throws IOException, InterruptedException {
        final List<String> users = configuration.getHide();

        this.log.debug("configuration: {}", configuration.dumpConfig());

        if (users.isEmpty()) {
            return;
        }

        final HidePosts hidePosts = new HidePosts();

        for (final String user : users) {
            hidePosts.hidePosts(user, 1, null);
        }
    }
}
