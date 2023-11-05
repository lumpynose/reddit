package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.HidePostsJsonPath;

/**
 *
 */
public class TestHidePostsJsonPath {
    final Logger log =
            LoggerFactory.getLogger(TestHidePostsJsonPath.class);

    private final static AppConfig configuration =
            new AppConfig();

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testPostMethod() throws IOException, InterruptedException {
        final List<String> users = configuration.getHide();

        this.log.debug("configuration: {}", configuration.dumpConfig());

        if (users.isEmpty()) {
            return;
        }

        final HidePostsJsonPath hidePosts = new HidePostsJsonPath();

        for (final String user : users) {
            hidePosts.hidePosts(user, 3, null);
        }
    }
}
