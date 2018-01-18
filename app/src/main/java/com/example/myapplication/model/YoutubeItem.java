package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by bapvn on 16/01/2018.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YoutubeItem {
    private Snippet snippet;
    private ContentDetail contentDetails;
    private Statistics statistics;
    @SerializedName("id")
    private String videoId;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class Snippet {
        private String title;
        private Thumbnail thumbnails;
        private ResourceId resourceId;
        private String publishedAt;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ContentDetail {
        String duration;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class Statistics {
        private String viewCount;
        private String likeCount;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class Thumbnail {
        @SerializedName("high")
        private ThumbnailHigh thumbHight;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ThumbnailHigh {
        private String url;
        private int width;
        private int height;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ResourceId {
        private String videoId;
    }
}
