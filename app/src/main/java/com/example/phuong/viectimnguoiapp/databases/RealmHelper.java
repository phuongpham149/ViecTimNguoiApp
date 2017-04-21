package com.example.phuong.viectimnguoiapp.databases;

import android.content.Context;

import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.objects.User;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

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

    public void addNew(NewItem newItem) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(newItem);
        mRealm.commitTransaction();
    }

    public void addUser(User user) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
    }

    public void addCategoryJob(CategoryJob categoryJob) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(categoryJob);
        mRealm.commitTransaction();
    }

    public List<District> getDistricts() {
        return mRealm.where(District.class).findAll();
    }

    public List<NewItem> getNews() {
        return mRealm.where(NewItem.class).findAll();
    }

    public List<CategoryJob> getCategoryJobs() {
        return mRealm.where(CategoryJob.class).findAll();
    }

    public RealmResults<NewItem> getItemNew(String id) {
        return mRealm.where(NewItem.class).equalTo("id", id).findAll();
    }

    public RealmResults<CategoryJob> getCategoryJobItem(String id) {
        return mRealm.where(CategoryJob.class).equalTo("id", id).findAll();
    }

    public RealmResults<District> getDistrictItem(String id) {
        return mRealm.where(District.class).equalTo("id", id).findAll();
    }

    public RealmResults<User> getUser(String id) {
        return mRealm.where(User.class).equalTo("id", id).findAll();
    }
}
