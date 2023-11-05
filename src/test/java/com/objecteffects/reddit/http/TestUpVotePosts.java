package com.objecteffects.reddit.http;

import java.io.IOException;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.main.AppConfig;
import com.objecteffects.reddit.method.UpVotePostsJsonPath;

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
            WeldInitiator.of(UpVotePostsJsonPath.class);

    @Inject
    private UpVotePostsJsonPath upVotePosts;

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testPostMethod() throws IOException,
            InterruptedException {
        this.log.debug("configuration: {}",
                this.configuration.dumpConfig());

//        final UpVotePostsJsonPath upVotePosts =
//                new UpVotePostsJsonPath();

        this.upVotePosts.upVotePosts("figwax", 1, null);
    }
}
