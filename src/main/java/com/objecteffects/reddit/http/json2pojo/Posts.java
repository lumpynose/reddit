package com.objecteffects.reddit.http.json2pojo;

import java.util.ArrayList;

public class Posts {
    public class Child {
        public String kind;
        public Data data;
    }

    public class Data {
        public Object approved_at_utc;
        public String subreddit;
        public String selftext;
        public String author_fullname;
        public boolean saved;
        public Object mod_reason_title;
        public int gilded;
        public boolean clicked;
        public String title;
        public ArrayList<LinkFlairRichtext> link_flair_richtext;
        public String subreddit_name_prefixed;
        public boolean hidden;
        public Object pwls;
        public String link_flair_css_class;
        public int downs;
        public int thumbnail_height;
        public Object top_awarded_type;
        public boolean hide_score;
        public String name;
        public boolean quarantine;
        public String link_flair_text_color;
        public double upvote_ratio;
        public String author_flair_background_color;
        public String subreddit_type;
        public int ups;
        public int total_awards_received;
        public MediaEmbed media_embed;
        public int thumbnail_width;
        public String author_flair_template_id;
        public boolean is_original_content;
        public ArrayList<Object> user_reports;
        public Object secure_media;
        public boolean is_reddit_media_domain;
        public boolean is_meta;
        public Object category;
        public SecureMediaEmbed secure_media_embed;
        public String link_flair_text;
        public boolean can_mod_post;
        public int score;
        public Object approved_by;
        public boolean is_created_from_ads_ui;
        public boolean author_premium;
        public String thumbnail;
        public boolean edited;
        public Object author_flair_css_class;
        public ArrayList<Object> author_flair_richtext;
        public Gildings gildings;
        public String post_hint;
        public Object content_categories;
        public boolean is_self;
        public Object mod_note;
        public double created;
        public String link_flair_type;
        public Object wls;
        public String removed_by_category;
        public Object banned_by;
        public String author_flair_type;
        public String domain;
        public boolean allow_live_comments;
        public Object selftext_html;
        public Object likes;
        public String suggested_sort;
        public Object banned_at_utc;
        public String url_overridden_by_dest;
        public Object view_count;
        public boolean archived;
        public boolean no_follow;
        public boolean is_crosspostable;
        public boolean pinned;
        public boolean over_18;
        public Preview preview;
        public ArrayList<Object> all_awardings;
        public ArrayList<Object> awarders;
        public boolean media_only;
        public boolean can_gild;
        public boolean spoiler;
        public boolean locked;
        public String author_flair_text;
        public ArrayList<Object> treatment_tags;
        public boolean visited;
        public Object removed_by;
        public Object num_reports;
        public Object distinguished;
        public String subreddit_id;
        public boolean author_is_blocked;
        public Object mod_reason_by;
        public Object removal_reason;
        public String link_flair_background_color;
        public String id;
        public boolean is_robot_indexable;
        public Object report_reasons;
        public String author;
        public Object discussion_type;
        public int num_comments;
        public boolean send_replies;
        public Object whitelist_status;
        public boolean contest_mode;
        public ArrayList<Object> mod_reports;
        public boolean author_patreon_flair;
        public String author_flair_text_color;
        public String permalink;
        public Object parent_whitelist_status;
        public boolean stickied;
        public String url;
        public int subreddit_subscribers;
        public double created_utc;
        public int num_crossposts;
        public Object media;
        public boolean is_video;
        public String link_flair_template_id;
    }

    public class Gildings {
    }

    public class Image {
        public Source source;
        public ArrayList<Resolution> resolutions;
        public Variants variants;
        public String id;
    }

    public class LinkFlairRichtext {
        public String e;
        public String t;
    }

    public class MediaEmbed {
    }

    public class Preview {
        public ArrayList<Image> images;
        public boolean enabled;
    }

    public class Resolution {
        public String url;
        public int width;
        public int height;
    }

    public class Root {
        public String after;
        public int dist;
        public Object modhash;
        public String geo_filter;
        public ArrayList<Child> children;
        public Object before;
    }

    public class SecureMediaEmbed {
    }

    public class Source {
        public String url;
        public int width;
        public int height;
    }

    public class Variants {
    }

}
