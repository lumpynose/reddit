package com.objecteffects.reddit.core;

/**
 *
 */
public enum RedditMethod {
    // list of friends
    FriendsMethod("prefs/friends"),
    // info about a user
    AboutMethod("user/%s/about"),
    // friend or unfriend a user
    FriendMethod("api/v1/me/friends/%s"),
    // list of posts by a user
    SubmittedMethod("/user/%s/submitted"),
    // hide a post
    HideMethod("/api/hide"),
    // info about me
    AboutMeMethod("api/v1/me"),
    // revoke OAuth token
    RevokeTokenMethod("api/v1/revoke_token"),
    // get OAuth token
    GetTokenMethod("api/v1/access_token");

    private final String url;

    RedditMethod(final String _url) {
        this.url = _url;
    }

    String getUrl() {
        return this.url;
    }
}
