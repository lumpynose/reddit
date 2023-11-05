package com.objecteffects.reddit.data;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 *
 */
@JsonIncludeProperties({ "is_suspended", "total_karma" })
public class FriendAboutJsonPath {
    @JsonProperty("is_suspended")
    private boolean isSuspended;
    @JsonProperty("total_karma")
    private Integer totalKarma;

    public boolean getIsSuspended() {
        return this.isSuspended;
    }

    public Integer getTotalKarma() {
        return this.totalKarma;
    }

    @Override
    public String toString() {
        return "[isSuspended=" + this.isSuspended
                + ", totalKarma=" + this.totalKarma + "]";
    }
}
