package com.objecteffects.reddit.data;

import java.util.List;

public class Posts {
    private String kind;
    private ListingData data;

    public Posts() {
    }

    public String getKind() {
        return this.kind;
    }

    public ListingData getData() {
        return this.data;
    }

    static public class ListingData {
        private int dist;
        private List<Post> children;

        public List<Post> getChildren() {
            return this.children;
        }

        public int getDist() {
            return this.dist;
        }
    }

    static public class Post {
        private String kind;
        private PostData data;

        public String getKind() {
            return this.kind;
        }

        public PostData getPostData() {
            return this.data;
        }
    }

    static public class PostData {
        private String name;
        private String thumbnail;
        private boolean hidden;

        public String getName() {
            return this.name;
        }

        public String getThumbnail() {
            return this.thumbnail;
        }

        public boolean isHidden() {
            return this.hidden;
        }

        @Override
        public String toString() {
            return "PostData [name=" + this.name + ", thumbnail="
                    + this.thumbnail + "]";
        }
    }
}
