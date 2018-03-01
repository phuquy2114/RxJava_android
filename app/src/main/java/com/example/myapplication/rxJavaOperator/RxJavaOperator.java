package com.example.myapplication.rxJavaOperator;

import com.example.myapplication.model.YoutubeItemUi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bapvn on 16/01/2018.
 */

public class RxJavaOperator {
    private NetWorkService mNetWorkService;
    private DiskService mDiskService;

    public RxJavaOperator() {
        mNetWorkService = new NetWorkService();
        mDiskService = new DiskService();
    }

    public Observable<List<YoutubeItemUi>> searchVideo(String keySearch) {
        return mNetWorkService
                .searchVideo(keySearch)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<YoutubeItemUi>> searchVideoSaveCache(String keySearch) {
        return mNetWorkService
                .searchVideo(keySearch)
                .doOnNext(this::catchData)
                .subscribeOn(Schedulers.io());
    }


    public Observable<YoutubeItemUi> searchVideoWithKeySearchFilterRateDurationOver300000mils(String keySearch) {
        return mNetWorkService
                .searchVideo(keySearch)
                .flatMap(youtubeItemUis -> Observable
                        .fromIterable(youtubeItemUis)
                        .filter(youtubeItemUi -> youtubeItemUi.getVideoDuration() > 300000));
    }

    public Observable<List<YoutubeItemUi>> searchVideoWithKeySearchListMergeOperator(String keySearch1, String keySearch2, String keySearch3) {
        return
                Observable
                        .mergeArrayDelayError(mNetWorkService.searchVideo(keySearch1),
                        mNetWorkService.searchVideo(keySearch2),
                        mNetWorkService.searchVideo(keySearch3));
    }

    public Observable<Integer> searchVideoWithKeySearchListZipOperator(String keySearch1, String keySearch2, String keySearch3) {
        return Observable
                .zip(mNetWorkService.searchVideo(keySearch1),
                        mNetWorkService.searchVideo(keySearch2),
                        mNetWorkService.searchVideo(keySearch3),
                        (youtubeItemUis, youtubeItemUis2, youtubeItemUis3) -> youtubeItemUis.size() + youtubeItemUis2.size() + youtubeItemUis3.size());
    }


    private void catchData(List<YoutubeItemUi> youtubeItemUis) {
        Realm mRealm = Realm.getDefaultInstance();
        try {
            mRealm.executeTransaction(realm -> {
                RealmResults<YoutubeItemUi> realmResults = realm.where(YoutubeItemUi.class)
                        .findAll();
                if (realmResults != null && realmResults.size() > 0) {
                    realmResults.deleteAllFromRealm();
                }

                for (YoutubeItemUi youtubeItemUi : youtubeItemUis) {
                    realm.insert(youtubeItemUi);
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
    }
}
