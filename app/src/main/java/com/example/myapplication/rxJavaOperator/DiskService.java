package com.example.myapplication.rxJavaOperator;

import com.example.myapplication.model.YoutubeItemUi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bapvn on 16/01/2018.
 */

public class DiskService {

    /**
     * MAP + FILLTER
     *
     * @return
     */
    public Observable<List<YoutubeItemUi>> getYoutubeListFromDisk() {
        Realm mRealm = Realm.getDefaultInstance();
        Observable<RealmResults<YoutubeItemUi>> youtubeListLocal = mRealm.where(YoutubeItemUi.class)
                .findAllAsync()
                .asFlowable()
                .toObservable();

        return youtubeListLocal
                .filter(youtubeItemUis -> youtubeItemUis != null && youtubeItemUis.size() > 0)
                .map(youtubeItemUis -> {
                    List<YoutubeItemUi> youtubeItemUiList = new ArrayList<>();
                    for (YoutubeItemUi youtubeItemUi : youtubeItemUis) {
                        youtubeItemUiList.add(mRealm.copyFromRealm(youtubeItemUi));
                    }
                    return youtubeItemUiList;
                })
                .doOnComplete(mRealm::close);
    }
}
