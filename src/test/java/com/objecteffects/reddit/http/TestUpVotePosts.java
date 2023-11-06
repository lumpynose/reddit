package com.objecteffects.reddit.http;

import java.io.IOException;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.UpVotePosts;

import jakarta.inject.Inject;

/**
 *
 */
@EnableWeld
public class TestUpVotePosts {
    final Logger log =
            LoggerFactory.getLogger(TestUpVotePosts.class);

    private final AppConfig configuration =
            new AppConfig();

    /**
     */
    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.of(UpVotePosts.class);

    @Inject
    private UpVotePosts upVotePosts;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testPostMethod() throws IOException,
            InterruptedException {
        this.log.debug("configuration: {}",
                this.configuration.dumpConfig());

//        final UpVotePosts upVotePosts =
//                new UpVotePosts();

        this.upVotePosts.upVotePosts("figwax", 1, null);
    }
}
