package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditDelete;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

/**
 */
@Default
public class UnFriend implements Serializable {
    private static final long serialVersionUID = -1L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditDelete redditDelete;

    /**
     */
    public UnFriend() {
    }

    /**
     * @param _deleteMethod the redditDelete to set
     */
    public void setDeleteMethod(final RedditDelete _redditDelete) {
        this.redditDelete = _redditDelete;
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

        final HttpResponse<String> response = this.redditDelete
                .deleteMethod(deleteUri, Collections.emptyMap());

        if (response == null) {
            this.log.warn("null delete friend respones");

            return;
        }

        this.log.debug("delete method response status: {}",
                response.statusCode());
    }
}
