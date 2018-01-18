package com.example.myapplication.service.response;

import com.example.myapplication.model.YoutubeItem;

import java.util.List;

/**
 * Created by bapvn on 09/10/2017.
 */
public class YoutubeListResponse {
    private List<YoutubeItem> items;

    public List<YoutubeItem> getItems() {
        return items;
    }

    public void setItems(List<YoutubeItem> items) {
        this.items = items;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    private String nextPageToken;
}
