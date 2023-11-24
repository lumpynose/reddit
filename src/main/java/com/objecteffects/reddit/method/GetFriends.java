package com.objecteffects.reddit.method;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.objecteffects.reddit.core.RedditGetMethod;
import com.objecteffects.reddit.core.RedditOAuth;
import com.objecteffects.reddit.data.Friend;
import com.objecteffects.reddit.data.FriendAbout;

import jakarta.enterprise.context.ApplicationScoped;

/**
 */
@ApplicationScoped
public class GetFriends implements Serializable {
    private static final long serialVersionUID = 9162663642350966578L;

    private final Logger log =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final UnFriend unFriend = new UnFriend();

    private final RedditOAuth redditOAuth = new RedditOAuth();

    private final int defaultCount = 0;
    private final boolean defaultGetKarma = false;

    private final Configuration conf =
            new Configuration.ConfigurationBuilder()
                    .jsonProvider(new JacksonJsonProvider())
                    .mappingProvider(new JacksonMappingProvider())
                    .options(EnumSet.noneOf(Option.class))
                    .build();

    /**
     * Gets all friends, no karma.
     *
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends() throws IOException, InterruptedException {
        return getFriends(this.defaultCount, this.defaultGetKarma);
    }

    /**
     * Gets all friends, with karma.
     *
     * @param getKarma
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends(final boolean getKarma)
            throws IOException, InterruptedException {
        return getFriends(this.defaultCount, getKarma);
    }

    /**
     * Gets some friends, with karma.
     *
     * @param count
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends(final int count)
            throws IOException, InterruptedException {
        return getFriends(count, true);
    }

    /**
     * Gets all or some friends, with or without karma.
     *
     * @param count
     * @param getKarma
     * @return List of Friend
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends(final int count,
            final boolean getKarma)
            throws IOException, InterruptedException {
        final RedditGetMethod client = new RedditGetMethod();

        final HttpResponse<String> methodResponse = client
                .getMethod("prefs/friends", Collections.emptyMap());

        if (methodResponse == null) {
            throw new IllegalStateException("null friends respones");
        }

        this.log.debug("friends method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));

        final String path = "$[0]['data']['children']";

        final TypeRef<List<Friend>> typeRef = new TypeRef<>() {
            // empty
        };

        final DocumentContext jsonContext = JsonPath
                .using(this.conf).parse(methodResponse.body());

        final List<Friend> friends = jsonContext.read(path, typeRef);

        this.log.debug("friends length: {}",
                Integer.valueOf(friends.size()));

        List<Friend> result = friends;

        if (getKarma) {
            result = decodeAbout(friends, count);
        }

        this.redditOAuth.revokeToken();

        return result;
    }

    @SuppressWarnings("boxing")
    private List<Friend> decodeAbout(final List<Friend> friends,
            final int count)
            throws IOException, InterruptedException {
        final RedditGetMethod client = new RedditGetMethod();

        List<Friend> sublist = friends;

        if (count > 0 && count < friends.size()) {
            sublist = friends.subList(0, count);
        }

        for (final Friend f : sublist) {
            Thread.sleep(600);

            final String aboutMethod =
                    String.format("user/%s/about", f.getName());

            final HttpResponse<String> aboutMethodResponse = client
                    .getMethod(aboutMethod, Collections.emptyMap());

            if (aboutMethodResponse == null) {
                this.log.debug("null response, unfriending: {}", f.getName());

                this.unFriend.unFriend(f.getName());

                f.setKarma(0);
            }
            else {
                final String response = aboutMethodResponse.body();

                this.log.debug("about response: {}", response);

                final String path = "$['data']";

                final DocumentContext jsonContext = JsonPath
                        .using(this.conf).parse(response);

                final FriendAbout fabout =
                        jsonContext.read(path,
                                FriendAbout.class);

                this.log.debug("fabout: {}", fabout);

                f.setKarma(fabout.getTotalKarma());
                f.setIsBlocked(fabout.getIsBlocked());

                if (fabout.getIsSuspended() == null) {
                    f.setIsSuspended(Boolean.FALSE);
                }
                else {
                    f.setIsSuspended(fabout.getIsSuspended());
                }

                if (f.getIsSuspended()) {
                    this.log.debug("unfriending: {}", f.getName());

                    this.unFriend.unFriend(f.getName());
                }
            }
        }

        return sublist;
    }
}
