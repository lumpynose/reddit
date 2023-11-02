package com.objecteffects.reddit.http;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.Configuration;

public class TestUpVotePosts {
    final Logger log =
            LoggerFactory.getLogger(TestUpVotePosts.class);

    private final Configuration configuration =
            new Configuration();

    @Test
    public void testPostMethod() throws IOException,
            InterruptedException {
        this.log.debug("configuration: {}",
                this.configuration.dumpConfig());

        final UpVotePosts upVotePosts = new UpVotePosts();

        upVotePosts.upVotePosts("Dangerous-Welcome961", 100, null);
    }
}
