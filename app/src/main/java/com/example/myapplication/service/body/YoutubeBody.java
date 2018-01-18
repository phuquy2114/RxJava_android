package com.example.myapplication.service.body;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by bapvn on 10/10/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YoutubeBody {
    private String part;
    private String chart;
    private String regionCode;
    private String key;
    private int maxResult;
    private String nextPageToken;
    private String videoId;
    private String keySearch;
    @SerializedName("id")
    private String videoIdList;
    private String type;
}
