package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.List;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.HidePosts;

import jakarta.inject.Inject;

/**
 *
 */
@EnableWeld
public class TestHidePosts {
    final Logger log =
            LoggerFactory.getLogger(TestHidePosts.class);

    private final static AppConfig configuration =
            new AppConfig();

    /**
     */
    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.of(HidePosts.class);

    @Inject
    private HidePosts hidePosts;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testPostMethod()
            throws IOException, InterruptedException {
        final List<String> users = configuration.getHide();

        this.log.debug("configuration: {}", configuration.dumpConfig());

        if (users.isEmpty()) {
            return;
        }

        // final HidePosts hidePosts = new HidePosts();

        for (final String user : users) {
            this.hidePosts.hidePosts(user, 3, null);
        }
    }
}
