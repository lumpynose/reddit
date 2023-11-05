package com.objecteffects.reddit.http.gson;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.gson.HidePostsGson;

public class TestHidePostsGson {
    final Logger log =
            LoggerFactory.getLogger(TestHidePostsGson.class);

    private final static AppConfig configuration =
            new AppConfig();

    @Test
    public void testPostMethod()
            throws IOException, InterruptedException {
        final List<String> users = configuration.getHide();

        this.log.debug("configuration: {}",
                configuration.dumpConfig());

        if (users.isEmpty()) {
            return;
        }

        final HidePostsGson hidePosts = new HidePostsGson();

        for (final String user : users) {
            hidePosts.hidePosts(user, 1, null);
        }
    }
}
