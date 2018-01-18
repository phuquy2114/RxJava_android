package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.model.YoutubeItemUi;
import com.example.myapplication.rxJavaOperator.RxJavaOperator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    public static final String YOUTUBE_API = "AIzaSyAzSW90lFTtZhCO81-fvllI-gswhq4Sd8I";
    private CompositeDisposable mCompositeDisposable;
    private RxJavaOperator mRxJavaOperator;
    private List<YoutubeItemUi> mYoutubeItemUiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCompositeDisposable = new CompositeDisposable();
        mRxJavaOperator = new RxJavaOperator();
        mergeOperatorSaveCache();
    }

    private void zipOperator() {
        mRxJavaOperator
                .searchVideoWithKeySearchListZipOperator("bong da", "gai dep", "android")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("xxx", "ZIP : " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void RxJavaWithAPI() {
        mRxJavaOperator
                .searchVideo("bong da")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<YoutubeItemUi>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<YoutubeItemUi> youtubeItemUis) {
                        for (YoutubeItemUi youtubeItemUi : youtubeItemUis) {
                            Log.d("xxx", "Search Without Filter: " + youtubeItemUi.getVideoTitle() + "duration " + youtubeItemUi.getVideoDuration());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void filterOperator() {
        mRxJavaOperator
                .searchVideoWithKeySearchFilterRateDurationOver300000mils("bong da")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YoutubeItemUi>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(YoutubeItemUi youtubeItemUi) {
                        Log.d("xxx", "List Search With Filter Duration > 300000 : " + youtubeItemUi.getVideoDuration());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void mergeOperator() {
        mRxJavaOperator
                .searchVideoWithKeySearchListMergeOperator("bong da", "gai dep", "android")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<YoutubeItemUi>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<YoutubeItemUi> youtubeItemUis) {
                        Log.d("xxx", "onNext: " + youtubeItemUis.get(0).getVideoTitle());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void mergeOperatorSaveCache() {
        mRxJavaOperator.searchVideoSaveCache("bong da")
                .subscribe(new Observer<List<YoutubeItemUi>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<YoutubeItemUi> youtubeItemUis) {
                        for (YoutubeItemUi youtubeItemUi : youtubeItemUis) {
                            Log.d("xxx", "Search Without Cache: " + youtubeItemUi.getVideoTitle() + "duration " + youtubeItemUi.getVideoDuration());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
