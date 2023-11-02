package com.objecteffects.reddit.http.data;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class Friends {
    private String kind;
    private FriendsData data;

    public Friends() {
    }

    public String getKind() {
        return this.kind;
    }

    public FriendsData getData() {
        return this.data;
    }

    static public class FriendsData {
        private List<Friend> children;

        public List<Friend> getFriendsList() {
            return this.children;
        }

        @Override
        public String toString() {
            return "FriendsData [friendsList=" + this.children + "]";
        }
    }

    static public class Friend implements Comparable<Friend> {
        @SerializedName("rel_id")
        private String relId;
        private float date;
        private String name;
        private String id;
        private int karma;

        public int getKarma() {
            return this.karma;
        }

        public void setKarma(final int _karma) {
            this.karma = _karma;
        }

        public float getDate() {
            return this.date;
        }

        public String getRelId() {
            return this.relId;
        }

        public String getName() {
            return this.name;
        }

        public String getId() {
            return this.id;
        }

        @Override
        public int compareTo(final Friends.Friend friend) {
            return this.karma - friend.karma;
        }

        @Override
        public String toString() {
            return "Friend [date=" + this.date + ", relId=" + this.relId
                    + ", name=" + this.name + ", id=" + this.id + ", karma="
                    + this.karma + "]";
        }

        @SuppressWarnings("boxing")
        @Override
        public int hashCode() {
            return Objects.hash(this.date, this.id, this.karma, this.name,
                    this.relId);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;

            if (obj == null)
                return false;

            if (getClass() != obj.getClass())
                return false;

            final Friend other = (Friend) obj;

            return Float.floatToIntBits(this.date) == Float
                    .floatToIntBits(other.date)
                    && Objects.equals(this.id, other.id)
                    && this.karma == other.karma
                    && Objects.equals(this.name, other.name)
                    && Objects.equals(this.relId, other.relId);
        }

    }
}
