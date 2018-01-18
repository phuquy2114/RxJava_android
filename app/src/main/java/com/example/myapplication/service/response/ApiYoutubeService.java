package com.example.myapplication.service.response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bapvn on 09/10/2017.
 */

public interface ApiYoutubeService {
    /**
     * get youtube list base on region
     *
     * @param part
     * @param chart
     * @param regionCode
     * @param pageToken
     * @param maxResult
     * @param key
     * @return
     */
    @GET("videos")
    Observable<YoutubeListResponse> getYoutubeListRegion(@Query("part") String part,
                                                         @Query("chart") String chart,
                                                         @Query("regionCode") String regionCode,
                                                         @Query("pageToken") String pageToken,
                                                         @Query("maxResults") int maxResult,
                                                         @Query("key") String key);

    /**
     * get detail video list base on video Id list(@param videoIdList)
     *
     * @param part
     * @param videoIdList
     * @param key
     * @return
     */
    @GET("videos")
    Observable<YoutubeListResponse> getDetailVideoList(@Query("part") String part,
                                                       @Query("id") String videoIdList,
                                                       @Query("key") String key);


    /**
     * get video list base on key search user confirm(@param searchKey)
     *
     * @param part
     * @param pageToken
     * @param type
     * @param maxResult
     * @param searchKey
     * @param key
     * @return
     */
    @GET("search")
    Observable<YoutubeSearchListResponse> getSearchVideoList(@Query("part") String part,
                                                             @Query("pageToken") String pageToken,
                                                             @Query("type") String type,
                                                             @Query("maxResults") int maxResult,
                                                             @Query("q") String searchKey,
                                                             @Query("key") String key);


}
