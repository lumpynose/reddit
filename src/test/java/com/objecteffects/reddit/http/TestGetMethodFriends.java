package com.objecteffects.reddit.http;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.objecteffects.reddit.http.data.FriendAbout;
import com.objecteffects.reddit.http.data.Friends;
import com.objecteffects.reddit.http.data.Friends.Friend;

public class TestGetMethodFriends {
    private final static Logger log = LogManager
            .getLogger(TestGetMethodFriends.class);

    // @Test
    public void testGetMethod() throws IOException, InterruptedException {
        final var client = new RedditGetMethod();

        // doesn't work (ignored) with friends
//        final var params = Map.of("limit", "15");

        final var methodResponse = client
                .getMethod("prefs/friends", Collections.emptyMap());

        log.debug("method response status: {}",
                Integer.valueOf(methodResponse.statusCode()));
//        log.debug("method response headers: {}", methodResponse.headers());
//        log.debug("method response body: {}", methodResponse.body());

        decodeBody(methodResponse.body(), client);

        RedditOAuth.revokeToken();
    }

    @SuppressWarnings("boxing")
    private void decodeBody(final String body,
            final RedditGetMethod client)
            throws IOException, InterruptedException {
        final var gson = new Gson();

        final TypeToken<List<Friends>> jaType = new TypeToken<>() {
            // nothing here
        };

        final List<Friends> data = gson.fromJson(body, jaType);

        log.debug("data length: {}",
                data.get(0).getData().getFriendsList().size());

        final List<Friend> nullList = new ArrayList<>();
        final List<Friend> suspendList = new ArrayList<>();
        final List<Friend> karmaList = new ArrayList<>();

        for (final Friend f : data.get(0).getData().getFriendsList()) {
            log.debug("{}", f.getName());

            final var aboutMethod = String.format("user/%s/about",
                    f.getName());

            final var aboutMethodResponse = client
                    .getMethod(aboutMethod, Collections.emptyMap());

            // client.getMethod returns null if there was any error
            if (aboutMethodResponse == null) {
                nullList.add(f);

                continue;
            }

//            log.debug("about response body: {}", aboutMethodResponse.body());

            final FriendAbout fabout = gson.fromJson(aboutMethodResponse.body(),
                    FriendAbout.class);

//            log.debug("friend about: {}", fabout);

            if (fabout.getData() == null) {
                log.debug("{}: no about data", f.getName());

                f.setKarma(0);
            }
            else {
                f.setKarma(fabout.getData().getTotalKarma());
            }

            if (fabout.getData().getIsSuspended()) {
                log.debug("{}: suspended", f.getName());

                suspendList.add(f);

                continue;
            }

            if (f.getKarma() == 0) {
                karmaList.add(f);
            }

//            log.debug("{}, total karma: {}", f.getName(), f.getKarma());

            Thread.sleep(1005);
        }

        printList("null", nullList);
        printList("suspended", suspendList);
        printList("zero karma", karmaList);

//      extracted(data);
    }

    private void printList(final String label, final List<Friend> list)
            throws IOException {
        final var fileName = "d:/tmp/duds.txt";

        Collections.sort(list, Collections.reverseOrder());

        try (var writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(label);

            for (final var f : list) {
                final var line = String.format("%s, %s", f.getName(),
                        Integer.valueOf(f.getKarma()));

                log.debug(line);

                writer.println(line);

                if (f.getKarma() == 0) {
                    final var delClient = new RedditDeleteMethod();

                    final var deleteMethod = String
                            .format("api/v1/me/friends/%s", f.getName());

                    try {
                        delClient.deleteMethod(deleteMethod,
                                Collections.emptyMap());
                    }
                    catch (InterruptedException | IOException e) {
                        log.debug("unfriend", e);
                    }
                }
            }

            writer.println("");
        }
    }

    @SuppressWarnings({ "boxing", "unused" })
    private void testBanned(final RedditGetMethod client,
            final Gson gson) throws IOException, InterruptedException {
        final var userNmae = "ECUlightBBC";

        final var aboutMethod = String.format("user/%s/about",
                userNmae);

        final var aboutResponse = client
                .getMethod(aboutMethod, Collections.emptyMap());

        log.debug("about response status: {}",
                aboutResponse.statusCode());
        log.debug("about response body: {}", aboutResponse.body());

        final var fabout = gson.fromJson(aboutResponse.body(),
                FriendAbout.class);

        if (fabout.getData() == null) {
            log.debug("{}: no about data", userNmae);

            return;
        }

        log.debug("isSuspended: {}", fabout.getData().getIsSuspended());

        final var submittedMethod = String.format("user/%s/submitted",
                userNmae);

        final var submittedResponse = client
                .getMethod(submittedMethod, Collections.emptyMap());

        log.debug("submitted response status: {}",
                submittedResponse.statusCode());
        log.debug("submitted response body: {}",
                submittedResponse.body());

    }

    @SuppressWarnings({ "unused", "boxing" })
    private void extracted(final List<Friends> data) throws IOException {
        Collections.sort(data.get(0).getData().getFriendsList(),
                Collections.reverseOrder());

        final var fileName = "d:/tmp/friends.txt";

        try (var writer = new PrintWriter(
                new FileWriter(fileName, false))) {
            for (final var f : data.get(0).getData().getFriendsList()) {

                log.debug("{}, {}", f.getName(), f.getKarma());

                final var line = String.format(
                        "<a href='https://www.reddit.com/user/%s/submitted/?sort=new' target='_blank'>%s</a>, %d<br />%n",
                        f.getName(), f.getName(), f.getKarma());
                writer.append(line);
            }
        }
    }
}
