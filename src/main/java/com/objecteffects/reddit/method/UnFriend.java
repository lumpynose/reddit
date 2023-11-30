package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditDeleteMethod;

import jakarta.inject.Inject;

/**
 */
public class UnFriend implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditDeleteMethod deleteMethod;

    /**
     */
    public UnFriend() {
    }

    /**
     * @param _deleteMethod the deleteMethod to set
     */
    public void setDeleteMethod(final RedditDeleteMethod _deleteMethod) {
        this.deleteMethod = _deleteMethod;
    }

    /**
     * @param name
     * @throws IOException
     * @throws InterruptedException
     */
    public void unFriend(final String name)
            throws InterruptedException, IOException {
        final String deleteUri =
                String.format("api/v1/me/friends/%s", name);

        final HttpResponse<String> methodResponse = this.deleteMethod
                .deleteMethod(deleteUri, Collections.emptyMap());

        if (methodResponse == null) {
            this.log.warn("null delete friend respones");

            return;
        }

        this.log.debug("delete method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));
    }
}
