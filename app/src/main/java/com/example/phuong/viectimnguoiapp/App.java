package com.example.phuong.viectimnguoiapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by asiantech on 18/04/2017.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        configFacebookSdk();
        configFireBase();
    }

    private void configFacebookSdk() {
        FacebookSdk.sdkInitialize(this);
    }

    private void configFireBase() {
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().goOnline();
    }
}
