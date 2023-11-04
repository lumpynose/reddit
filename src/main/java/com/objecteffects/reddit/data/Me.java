package com.objecteffects.reddit.data;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIncludeProperties({ "name", "num_friends", "link_karma", "total_karma" })
public class Me {
    private String name;
    @JsonProperty("num_friends")
    private Integer numFriends;
    private Integer coins;
    @JsonProperty("link_karma")
    private Integer linkKarma;
    @JsonProperty("total_karma")
    private Integer totalKarma;
    @JsonProperty("inbox_count")
    private Integer inboxCount;

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param _name the name to set
     */
    public void setName(final String _name) {
        this.name = _name;
    }

    /**
     * @return the numFriends
     */
    public Integer getNumFriends() {
        return this.numFriends;
    }

    /**
     * @param _numFriends the numFriends to set
     */
    public void setNumFriends(final Integer _numFriends) {
        this.numFriends = _numFriends;
    }

    /**
     * @return the coins
     */
    public Integer getCoins() {
        return this.coins;
    }

    /**
     * @param _coins the coins to set
     */
    public void setCoins(final Integer _coins) {
        this.coins = _coins;
    }

    /**
     * @return the linkKarma
     */
    public Integer getLinkKarma() {
        return this.linkKarma;
    }

    /**
     * @param _linkKarma the linkKarma to set
     */
    public void setLinkKarma(final Integer _linkKarma) {
        this.linkKarma = _linkKarma;
    }

    /**
     * @return the totalKarma
     */
    public Integer getTotalKarma() {
        return this.totalKarma;
    }

    /**
     * @param _totalKarma the totalKarma to set
     */
    public void setTotalKarma(final Integer _totalKarma) {
        this.totalKarma = _totalKarma;
    }

    /**
     * @return the inboxCount
     */
    public Integer getInboxCount() {
        return this.inboxCount;
    }

    /**
     * @param _inboxCount the inboxCount to set
     */
    public void setInboxCount(final Integer _inboxCount) {
        this.inboxCount = _inboxCount;
    }

    @Override
    public String toString() {
        return "Me [name=" + this.name + ", numFriends=" + this.numFriends
                + ", coins=" + this.coins + ", linkKarma=" + this.linkKarma
                + ", totalKarma=" + this.totalKarma + ", inboxCount="
                + this.inboxCount + "]";
    }
}
