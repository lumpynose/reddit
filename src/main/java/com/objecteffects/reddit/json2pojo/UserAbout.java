package com.objecteffects.reddit.json2pojo;

import java.util.ArrayList;

public class UserAbout {
    public String kind;
    public Data data;

    static public class Data {
        public boolean is_employee;
        public boolean is_friend;
        public Subreddit subreddit;
        public Object snoovatar_size;
        public int awardee_karma;
        public String id;
        public boolean verified;
        public boolean is_gold;
        public boolean is_mod;
        public int awarder_karma;
        public boolean has_verified_email;
        public String icon_img;
        public boolean hide_from_robots;
        public int link_karma;
        public boolean pref_show_snoovatar;
        public boolean is_blocked;
        public int total_karma;
        public boolean accept_chats;
        public String name;
        public double created;
        public double created_utc;
        public String snoovatar_img;
        public int comment_karma;
        public boolean accept_followers;
        public boolean has_subscribed;
        public boolean accept_pms;

        static public class Subreddit {
            public boolean default_set;
            public boolean user_is_contributor;
            public String banner_img;
            public ArrayList<Object> allowed_media_in_comments;
            public boolean user_is_banned;
            public boolean free_form_reports;
            public Object community_icon;
            public boolean show_media;
            public String icon_color;
            public Object user_is_muted;
            public String display_name;
            public Object header_img;
            public String title;
            public ArrayList<Object> previous_names;
            public boolean over_18;
            public ArrayList<Integer> icon_size;
            public String primary_color;
            public String icon_img;
            public String description;
            public String submit_link_label;
            public Object header_size;
            public boolean restrict_posting;
            public boolean restrict_commenting;
            public int subscribers;
            public String submit_text_label;
            public boolean is_default_icon;
            public String link_flair_position;
            public String display_name_prefixed;
            public String key_color;
            public String name;
            public boolean is_default_banner;
            public String url;
            public boolean quarantine;
            public Object banner_size;
            public boolean user_is_moderator;
            public boolean accept_followers;
            public String public_description;
            public boolean link_flair_enabled;
            public boolean disable_contributor_requests;
            public String subreddit_type;
            public boolean user_is_subscriber;
        }
    }
}
