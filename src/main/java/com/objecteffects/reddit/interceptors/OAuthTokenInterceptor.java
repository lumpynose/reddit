package com.objecteffects.reddit.interceptors;

import com.objecteffects.reddit.core.RedditOAuth;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

/**
 */
@OAuthToken
@Interceptor
public class OAuthTokenInterceptor {
    @Inject
    private RedditOAuth redditOAuth;

    /**
     */
    public OAuthTokenInterceptor() {
    }

    /**
     * @param ctx
     * @return
     * @throws Exception
     */
    @AroundInvoke
    public Object aroundInvoke(final InvocationContext ctx) throws Exception {
        // get OAuth token if necessary
        this.redditOAuth.getOAuthToken();

        return ctx.proceed();
    }
}
