package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by bapvn on 16/01/2018.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class YoutubeSearchItem {
    @SerializedName("id")
    private IdSearch idSearch;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class IdSearch {
        private String videoId;
    }
}
