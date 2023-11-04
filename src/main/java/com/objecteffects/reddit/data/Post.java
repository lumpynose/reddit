package com.objecteffects.reddit.data;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

/**
 *
 */
@JsonIncludeProperties({ "name", "thumbnail", "hidden" })
public class Post {
    private String name;
    private String thumbnail;
    private boolean hidden;

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

    @Override
    public String toString() {
        return "PostData [name=" + this.name + ", thumbnail="
                + this.thumbnail + "]";
    }
}
