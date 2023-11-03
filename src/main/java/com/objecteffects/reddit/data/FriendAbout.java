package com.objecteffects.reddit.data;

import com.google.gson.annotations.SerializedName;

/*
 *
 */
public class FriendAbout {
    private String kind;
    private FriendData data;

    public FriendAbout() {
    }

    public String getKind() {
        return this.kind;
    }

    public FriendData getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "FriendAbout [kind=" + this.kind + ", data=" + this.data + "]";
    }

    static public class FriendData {
        @SerializedName("is_suspended")
        private boolean isSuspended;
        @SerializedName("total_karma")
        private int totalKarma;

        public boolean getIsSuspended() {
            return this.isSuspended;
        }

        public int getTotalKarma() {
            return this.totalKarma;
        }

        @Override
        public String toString() {
            return "FriendData [isSuspended=" + this.isSuspended
                    + ", totalKarma=" + this.totalKarma + "]";
        }
    }
}
