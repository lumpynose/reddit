package com.objecteffects.reddit.data;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
@JsonIncludeProperties({ "name",
        "thumbnail", "hidden", "created_utc", "likes" })
public class Post {
    private String name;
    private String thumbnail;
    private Boolean hidden;
    private Boolean likes;
    @JsonProperty("created_utc")
    private Long created;

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return
     */
    public String getThumbnail() {
        return this.thumbnail;
    }

    /**
     * @return
     */
    public boolean isHidden() {
        return this.hidden;
    }

    /**
     * @return
     */
    public Long getCreated() {
        return this.created;
    }

    /**
     * @param created
     */
    public void setCreated(final Long created) {
        this.created = created;
    }

    /**
     * @return the likes
     */
    public Boolean getLikes() {
        if (this.likes == null) {
            return Boolean.FALSE;
        }

        return this.likes;
    }

    @Override
    public String toString() {
        return "Post [name=" + this.name + ", thumbnail=" + this.thumbnail
                + ", hidden=" + this.hidden + ", likes=" + getLikes()
                + ", created=" + this.created + "]";
    }
}
