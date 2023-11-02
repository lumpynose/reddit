package com.objecteffects.reddit.http;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.objecteffects.reddit.main.Configuration;

public class TestUpVotePosts {
    private final static Logger log = LogManager
            .getLogger(TestUpVotePosts.class);

    @Test
    public void testPostMethod() throws IOException, InterruptedException {

        log.debug("configuration: {}", Configuration.dumpConfig());

        final var upVotePosts = new UpVotePosts();

        upVotePosts.upVotePosts("Dangerous-Welcome961", 100, null);
    }
}
