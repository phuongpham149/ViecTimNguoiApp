package com.example.phuong.viectimnguoiapp;

import android.app.Application;

import com.firebase.client.Firebase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by asiantech on 18/04/2017.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
