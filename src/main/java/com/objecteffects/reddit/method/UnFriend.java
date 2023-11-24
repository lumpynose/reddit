package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditDeleteMethod;
import com.objecteffects.reddit.core.RedditOAuth;

import jakarta.enterprise.context.ApplicationScoped;

/**
 */
@ApplicationScoped
public class UnFriend implements Serializable {
    private static final long serialVersionUID = -5354019122848858356L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final RedditOAuth redditOAuth = new RedditOAuth();

    private final RedditDeleteMethod deleteClient = new RedditDeleteMethod();

    /**
     * @param name
     * @throws IOException
     * @throws InterruptedException
     */
    public void unFriend(final String name)
            throws InterruptedException, IOException {
        final String deleteMethod =
                String.format("/api/v1/me/friends/%s", name);

        final HttpResponse<String> methodResponse = this.deleteClient
                .deleteMethod(deleteMethod, Collections.emptyMap());

        if (methodResponse == null) {
            throw new IllegalStateException("null delete friend respones");
        }

        this.log.debug("delete method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        this.redditOAuth.revokeToken();
    }
}
