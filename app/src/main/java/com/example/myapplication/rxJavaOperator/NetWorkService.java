package com.example.myapplication.rxJavaOperator;

import com.example.myapplication.constant.Constant;
import com.example.myapplication.model.YoutubeItem;
import com.example.myapplication.model.YoutubeItemUi;
import com.example.myapplication.model.YoutubeSearchItem;
import com.example.myapplication.service.core.ApiYoutube;
import com.example.myapplication.service.response.ApiYoutubeService;
import com.example.myapplication.service.response.YoutubeListResponse;
import com.example.myapplication.service.response.YoutubeSearchListResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bapvn on 16/01/2018.
 */

public class NetWorkService {
    public Observable<List<YoutubeItemUi>> searchVideo(String key) {
        ApiYoutubeService apiYoutubeService = ApiYoutube.getYoutubeService();
        Observable<YoutubeSearchListResponse> observable = apiYoutubeService
                .getSearchVideoList("snippet", "",
                        "", 10, key, Constant.YOUTUBE_API);
        return observable
                .subscribeOn(Schedulers.io())
                .map(youtubeSearchListResponse -> {
                    List<YoutubeSearchItem> youtubeSearchItemList = youtubeSearchListResponse.getItems();
                    String videoIdList = "";
                    for (YoutubeSearchItem youtubeSearchItem : youtubeSearchItemList) {
                        if (videoIdList.equals("")) {
                            videoIdList = videoIdList + youtubeSearchItem.getIdSearch().getVideoId();
                        } else {
                            videoIdList = videoIdList + "," + youtubeSearchItem.getIdSearch().getVideoId();
                        }
                    }
                    return videoIdList;
                }).filter(s -> s != null)
                .flatMap(this::getDetailVideoVideo);
    }

    public Observable<List<YoutubeItemUi>> getDetailVideoVideo(String videoIdList) {
        ApiYoutubeService apiYoutubeService = ApiYoutube.getYoutubeService();
        Observable<YoutubeListResponse> observable = apiYoutubeService
                .getDetailVideoList("snippet,contentDetails,statistics", videoIdList, Constant.YOUTUBE_API);
        return observable
                .subscribeOn(Schedulers.io())
                .map(youtubeListResponse -> convertFromYoutubeItem(youtubeListResponse.getItems()));
    }

    private List<YoutubeItemUi> convertFromYoutubeItem(List<YoutubeItem> youtubeItems) {
        List<YoutubeItemUi> youtubeItemUiList = new ArrayList<>();
        for (YoutubeItem item : youtubeItems) {
            YoutubeItemUi youtubeItemUi = new YoutubeItemUi();
            youtubeItemUi.setVideoTitle(item.getSnippet() != null ? item.getSnippet().getTitle() : "");
            youtubeItemUi.setVideoDuration(item.getContentDetails() != null ? getDuration(item.getContentDetails().getDuration()) : 0);
            youtubeItemUi.setVideoRating(item.getStatistics() != null ? item.getStatistics().getViewCount() : "");
            youtubeItemUiList.add(youtubeItemUi);
        }
        return youtubeItemUiList;
    }

    /**
     * convert Duration Iso format to HH:MM:SS format
     *
     * @param durationString
     * @return
     */
    public long getDuration(String durationString) {
        long duration = 0L;
        try {
            String time = durationString.substring(2);
            Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
            for (int i = 0; i < indexs.length; i++) {
                int index = time.indexOf((String) indexs[i][0]);
                if (index != -1) {
                    String value = time.substring(0, index);
                    duration += Integer.parseInt(value) * (int) indexs[i][1] * 1000;
                    time = time.substring(value.length() + 1);
                }
            }
            return duration;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return duration;
        }
    }
}
