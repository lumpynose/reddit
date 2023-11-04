package com.objecteffects.reddit.http;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.objecteffects.reddit.main.Configuration;
import com.objecteffects.reddit.method.UpVotePostsJsonPath;

import jakarta.inject.Inject;

/**
 *
 */
@EnableWeld
public class TestUpVotePosts {
    final Logger log =
            LoggerFactory.getLogger(TestUpVotePosts.class);

    private final Configuration configuration =
            new Configuration();

    @BeforeEach
    public void setup() {
        com.jayway.jsonpath.Configuration
                .setDefaults(new com.jayway.jsonpath.Configuration.Defaults() {

                    private final JsonProvider jsonProvider =
                            new JacksonJsonProvider();
                    private final MappingProvider mappingProvider =
                            new JacksonMappingProvider();

                    @Override
                    public JsonProvider jsonProvider() {
                        return this.jsonProvider;
                    }

                    @Override
                    public MappingProvider mappingProvider() {
                        return this.mappingProvider;
                    }

                    @Override
                    public Set<Option> options() {
                        return EnumSet.noneOf(Option.class);
                    }
                });
    }

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
