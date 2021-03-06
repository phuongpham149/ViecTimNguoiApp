package com.example.phuong.viectimnguoiapp.databases;

import android.content.Context;

import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.objects.NewSave;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.objects.UserChat;

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

    public void addNewSave(NewSave newSave) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(newSave);
        mRealm.commitTransaction();
    }

    public void addCategoryJob(CategoryJob categoryJob) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(categoryJob);
        mRealm.commitTransaction();
    }

    public void addHistoryPing(HistoryPing historyPing) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(historyPing);
        mRealm.commitTransaction();
    }

    public void addUserChat(UserChat userChat) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(userChat);
        mRealm.commitTransaction();
    }

    public void clearData() {
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();
    }

    public List<NewSave> getNewSaves() {
        return mRealm.where(NewSave.class).findAll();
    }

    public List<HistoryPing> getHistoryPings() {
        return mRealm.where(HistoryPing.class).findAll();
    }

    public List<District> getDistricts() {
        return mRealm.where(District.class).findAll();
    }

    public List<UserChat> getUserChats() {
        return mRealm.where(UserChat.class).findAll();
    }

    public void deleteNewSave(String idNewSave) {
        mRealm.beginTransaction();
        RealmResults<NewSave> result = mRealm.where(NewSave.class).equalTo("id", idNewSave).findAll();
        result.clear();
        mRealm.commitTransaction();
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

    public CategoryJob getCategoryJobItem(String id) {
        return mRealm.where(CategoryJob.class).equalTo("id", id).findFirst();
    }

    public RealmResults<District> getDistrictItem(String id) {
        return mRealm.where(District.class).equalTo("id", id).findAll();
    }

    public User getUser(String id) {
        mRealm.beginTransaction();
        User user = mRealm.where(User.class).equalTo("id", id).findFirst();
        mRealm.commitTransaction();
        return user;
    }

    public RealmResults<HistoryPing> getHistoryPing(String id) {
        return mRealm.where(HistoryPing.class).equalTo("idPost", id).findAll();
    }
}
