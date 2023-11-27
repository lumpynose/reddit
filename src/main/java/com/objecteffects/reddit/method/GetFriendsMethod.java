package com.objecteffects.reddit.method;

import java.io.IOException;
import java.util.List;

import com.objecteffects.reddit.data.Friend;

/**
 */
public interface GetFriendsMethod {
    /**
     * @param count
     * @param getKarma
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Friend> getFriends(int count, boolean getKarma)
            throws IOException, InterruptedException;
}
