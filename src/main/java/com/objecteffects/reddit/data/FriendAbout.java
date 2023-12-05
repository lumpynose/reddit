package com.objecteffects.reddit.data;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 *
 */
@JsonIncludeProperties({ "is_suspended", "total_karma", "is_blocked" })
public class FriendAbout {
    @JsonProperty("is_suspended")
    private Boolean isSuspended;

    @JsonProperty("total_karma")
    private Integer totalKarma;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    /**
     * @return
     */
    public Boolean getIsSuspended() {
        return this.isSuspended;
    }

    /**
     * @return
     */
    public Integer getTotalKarma() {
        return this.totalKarma;
    }

    /**
     * @return
     */
    public Boolean getIsBlocked() {
        return this.isBlocked;
    }

    @Override
    public String toString() {
        return "FriendAbout [isSuspended=" + this.isSuspended + ", totalKarma="
                + this.totalKarma + ", this.isBlocked=" + this.isBlocked + "]";
    }

//  @JsonProperty("user_is_banned")
//  private Boolean isBanned;

    /**
     * @return
     */
//    public Boolean getIsBanned() {
//        return this.isBanned;
//    }

    // @Override
//    public String toString() {
//        return "[isSuspended=" + this.isSuspended
//                + ", totalKarma=" + this.totalKarma + "]";
//    }
}
