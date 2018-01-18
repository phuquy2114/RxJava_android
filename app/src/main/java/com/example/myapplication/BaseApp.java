package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.service.core.ApiConfig;
import com.example.myapplication.service.core.ApiYoutube;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;

/**
 * Created by bapvn on 16/01/2018.
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // youtube
        ApiConfig apiYoutube = ApiConfig.builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .context(getApplicationContext())
                .build();
        ApiYoutube.getInstance().init(apiYoutube);

        // realm
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .rxFactory(new RealmObservableFactory())
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        /**
         * set up for view realm db in #chrome://inspect/
         */
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
