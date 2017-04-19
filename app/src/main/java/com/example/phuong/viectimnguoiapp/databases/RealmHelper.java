package com.example.phuong.viectimnguoiapp.databases;

import android.content.Context;

import com.example.phuong.viectimnguoiapp.objects.District;

import io.realm.Realm;

/**
 * Created by asiantech on 18/04/2017.
 */
public class RealmHelper {
    private static RealmHelper mInstance;
    private Realm mRealm;
    private Context mContext;

    public RealmHelper(Context context) {
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    public void addDistrict(District district) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(district);
        mRealm.commitTransaction();
    }
}
