package com.example.myapplication.model;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by bapvn on 16/01/2018.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YoutubeItemUi extends RealmObject {
    private String videoTitle;
    private long videoDuration;
    private String videoRating;
}
