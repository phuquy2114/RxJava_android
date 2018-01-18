package com.example.myapplication.service.core;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.service.response.ApiYoutubeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bapvn on 09/10/2017.
 */

public class ApiYoutube {
    private static final long TIME_OUT = 300000;
    private static ApiYoutube sInstance;
    private ApiYoutubeService mApiYoutube;

    public ApiYoutube() {
    }

    /**
     * Api client
     *
     * @return
     */
    public synchronized static ApiYoutube getInstance() {
        if (sInstance == null) {
            sInstance = new ApiYoutube();
        }
        return sInstance;
    }

    /**
     * Service
     *
     * @return
     */
    public synchronized static ApiYoutubeService getYoutubeService() {
        return getInstance().mApiYoutube;
    }

    /**
     * init confirm url
     *
     * @param config
     */
    public void init(ApiConfig config) {
        // init
        BooleanAdapter booleanAdapter = new BooleanAdapter();
        IntegerAdapter integerAdapter = new IntegerAdapter();
        DoubleAdapter doubleAdapter = new DoubleAdapter();
        // init Gson
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Boolean.class, booleanAdapter)
                .registerTypeAdapter(boolean.class, booleanAdapter)
                .registerTypeAdapter(Integer.class, integerAdapter)
                .registerTypeAdapter(int.class, integerAdapter)
                .registerTypeAdapter(Double.class, doubleAdapter)
                .registerTypeAdapter(double.class, doubleAdapter)
                .disableHtmlEscaping()
                .create();
        // init OkHttpClient
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder();
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        // Log
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.interceptors().add(logInterceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getBaseUrl())
                .client(okHttpBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiYoutube = retrofit.create(ApiYoutubeService.class);
    }
}
