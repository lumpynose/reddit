package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.data.Friend;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

/**
 */
@Priority(100)
@Decorator
public class GetFriendsDecorator implements GetFriendsMethod, Serializable {
    private static final long serialVersionUID = -1220458947814563972L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Inject
    private RedditOAuth redditOAuth;

    @Inject
    @Delegate
    @Any
    GetFriends getFriends;

    @Override
    public List<Friend> getFriends(final int count,
            final boolean getKarma)
            throws IOException, InterruptedException {
        final String token = this.redditOAuth.getOAuthToken();
        this.log.debug("token: {}", token);

        this.getFriends.getFriends(count, getKarma);

        final HttpResponse<String> response = this.redditOAuth.revokeToken();
        this.log.debug("response: {}", response.statusCode());

        return Collections.emptyList();
    }
}
