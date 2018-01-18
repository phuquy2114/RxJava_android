package com.example.myapplication.service.response;

import com.example.myapplication.model.YoutubeSearchItem;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by bapvn on 12/10/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YoutubeSearchListResponse {
    private List<YoutubeSearchItem> items;
    private String nextPageToken;
}
