package com.objecteffects.reddit.http.jsonschema2pojo;

import java.util.List;

public class Posts {
    public class Data {

        public Object approvedAtUtc;

        public String subreddit;

        public String selftext;

        public String authorFullname;

        public Boolean saved;

        public Object modReasonTitle;

        public Integer gilded;

        public Boolean clicked;

        public String title;

        public List<Object> linkFlairRichtext;

        public String subredditNamePrefixed;

        public Boolean hidden;

        public Object pwls;

        public Object linkFlairCssClass;

        public Integer downs;

        public Integer thumbnailHeight;

        public Object topAwardedType;

        public Boolean hideScore;

        public String name;

        public Boolean quarantine;

        public String linkFlairTextColor;

        public Double upvoteRatio;

        public Object authorFlairBackgroundColor;

        public String subredditType;

        public Integer ups;

        public Integer totalAwardsReceived;

        public MediaEmbed mediaEmbed;

        public Integer thumbnailWidth;

        public Object authorFlairTemplateId;

        public Boolean isOriginalContent;

        public List<Object> userReports;

        public Object secureMedia;

        public Boolean isRedditMediaDomain;

        public Boolean isMeta;

        public Object category;

        public SecureMediaEmbed secureMediaEmbed;

        public Object linkFlairText;

        public Boolean canModPost;

        public Integer score;

        public Object approvedBy;

        public Boolean isCreatedFromAdsUi;

        public Boolean authorPremium;

        public String thumbnail;

        public Boolean edited;

        public Object authorFlairCssClass;

        public List<Object> authorFlairRichtext;

        public Gildings gildings;

        public String postHint;

        public Object contentCategories;

        public Boolean isSelf;

        public Object modNote;

        public Double created;

        public String linkFlairType;

        public Object wls;

        public Object removedByCategory;

        public Object bannedBy;

        public String authorFlairType;

        public String domain;

        public Boolean allowLiveComments;

        public Object selftextHtml;

        public Object likes;

        public Object suggestedSort;

        public Object bannedAtUtc;

        public String urlOverriddenByDest;

        public Object viewCount;

        public Boolean archived;

        public Boolean noFollow;

        public Boolean isCrosspostable;

        public Boolean pinned;

        public Boolean over18;

        public Preview preview;

        public List<Object> allAwardings;

        public List<Object> awarders;

        public Boolean mediaOnly;

        public Boolean canGild;

        public Boolean spoiler;

        public Boolean locked;

        public Object authorFlairText;

        public List<Object> treatmentTags;

        public Boolean visited;

        public Object removedBy;

        public Object numReports;

        public Object distinguished;

        public String subredditId;

        public Boolean authorIsBlocked;

        public Object modReasonBy;

        public Object removalReason;

        public String linkFlairBackgroundColor;

        public String id;

        public Boolean isRobotIndexable;

        public Object reportReasons;

        public String author;

        public Object discussionType;

        public Integer numComments;

        public Boolean sendReplies;

        public Object whitelistStatus;

        public Boolean contestMode;

        public List<Object> modReports;

        public Boolean authorPatreonFlair;

        public Object authorFlairTextColor;

        public String permalink;

        public Object parentWhitelistStatus;

        public Boolean stickied;

        public String url;

        public Integer subredditSubscribers;

        public Double createdUtc;

        public Integer numCrossposts;

        public Object media;

        public Boolean isVideo;

    }

    public class Example {

        public String kind;

        public Data data;

    }

    public class Gildings {

    }

    public class Image {

        public Source source;

        public List<Resolution> resolutions;

        public Variants variants;

        public String id;

    }

    public class MediaEmbed {

    }

    public class Preview {

        public List<Image> images;

        public Boolean enabled;

    }

    public class Resolution {

        public String url;

        public Integer width;

        public Integer height;

    }

    public class SecureMediaEmbed {

    }

    public class Source {

        public String url;

        public Integer width;

        public Integer height;

    }

    public class Variants {

    }
}
