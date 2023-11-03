package com.objecteffects.reddit.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Friend implements Comparable<Friend> {
    @JsonProperty("rel_id")
    private String relId;
    private Float date;
    private String name;
    private String id;
    private Integer karma;

    public Integer getKarma() {
        return this.karma;
    }

    public void setKarma(final Integer _karma) {
        this.karma = _karma;
    }

    public Float getDate() {
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
    public int compareTo(final Friend friend) {
        return this.karma.intValue() - friend.karma.intValue();
    }

    @Override
    public String toString() {
        return "Friend [date=" + this.date + ", relId=" + this.relId
                + ", name=" + this.name + ", id=" + this.id + ", karma="
                + this.karma + "]";
    }

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

        return this.date == other.date
                && Objects.equals(this.id, other.id)
                && this.karma == other.karma
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.relId, other.relId);
    }

}
