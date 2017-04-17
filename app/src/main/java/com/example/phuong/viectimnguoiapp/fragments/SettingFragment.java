package com.example.phuong.viectimnguoiapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.eventBus.objet.NetWorkState;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asiantech on 15/04/2017.
 */
@EFragment(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {
    @ViewById(R.id.imgUpJob)
    protected ImageView mImgUpJob;
    @ViewById(R.id.imgDownJob)
    protected ImageView mImgDownJob;
    @ViewById(R.id.rlChoiceJob)
    protected RelativeLayout mRlChoiceJob;
    @ViewById(R.id.imgUpAddress)
    protected ImageView mImgAddress;
    @ViewById(R.id.imgDownAddress)
    protected ImageView mImgDownAddress;
    @ViewById(R.id.llChoiceAddress)
    protected LinearLayout mLlChoiceAddress;

    @ViewById(R.id.chkFixElectronicInHouse)
    protected CheckBox mChkFixElectronicInHouse;
    @ViewById(R.id.chkCleanHouse)
    protected CheckBox mChkCleanHouse;
    @ViewById(R.id.chkDoLaundry)
    protected CheckBox mChkDoLaundry;
    @ViewById(R.id.chkFixWaterPipe)
    protected CheckBox mChkFixWaterPipe;
    @ViewById(R.id.chkPaintHouse)
    protected CheckBox mChkPaintHouse;


    @ViewById(R.id.chkHaiChau)
    protected CheckBox mChkHaiChau;
    @ViewById(R.id.chkLienChieu)
    protected CheckBox mChkLienChieu;
    @ViewById(R.id.chkCamLe)
    protected CheckBox mChkCamLe;
    @ViewById(R.id.chkThanhKhe)
    protected CheckBox mChkThanhKhe;
    @ViewById(R.id.chkSonTra)
    protected CheckBox mChkSonTra;
    @ViewById(R.id.chkNguHanhSon)
    protected CheckBox mChkNguHanhSon;
    @ViewById(R.id.chkHoaVang)
    protected CheckBox mChkHoaVang;

    private Firebase mFirebaseSetting;
    private SharedPreferences mSharedPreferencesSetting;
    private SharedPreferences mSharedPreferencesUserLogin;
    private String mSettingJob = "";
    private String mSettingAddress = "";

    @Click(R.id.btnSave)
    public void saveAction(){
        SharedPreferences.Editor mEditor = mSharedPreferencesUserLogin.edit();
        mEditor.putString(Constant.SETTING_JOB,mSettingJob);
        mEditor.putString(Constant.SETTING_ADDRESS,mSettingAddress);
        mEditor.apply();

        pushDataSetting();
    }

    @Subscribe
    public void receiverStateOnNetwork(NetWorkState netWorkState) {
        if (netWorkState.getState().equals(Constant.WIFI_ENABLE) || netWorkState.getState().equals(Constant.MOBILE_DATA_ENABLE)) {
            checkFirebaseForSetting();
            pushDataSetting();
        }
    }

    public void pushDataSetting(){
        Log.d("tag112",mSettingJob+" add "+mSettingAddress);
        Map<String,String> mapSetting = new HashMap<>();
        mapSetting.put("jobSetting",mSettingJob);
        mapSetting.put("addressSetting",mSettingAddress);
        mFirebaseSetting.push().setValue(mapSetting);
    }
    @CheckedChange({R.id.chkHaiChau,R.id.chkLienChieu,R.id.chkCamLe,R.id.chkThanhKhe,R.id.chkSonTra,R.id.chkNguHanhSon,R.id.chkHoaVang,R.id.chkFixElectronicInHouse,R.id.chkCleanHouse,R.id.chkDoLaundry,R.id.chkFixWaterPipe,R.id.chkPaintHouse})
    public void checkSettingJob(CompoundButton v){
        switch (v.getId()){
            case R.id.chkHaiChau:
                mSettingAddress += "1,";
                break;
            case R.id.chkLienChieu:
                mSettingAddress += "7,";
                break;
            case R.id.chkCamLe:
                mSettingAddress += "2,";
                break;
            case R.id.chkThanhKhe:
                mSettingAddress += "3,";
                break;
            case R.id.chkSonTra:
                mSettingAddress += "4,";
                break;
            case R.id.chkNguHanhSon:
                mSettingAddress += "5,";
                break;
            case R.id.chkHoaVang:
                mSettingAddress += "6,";
                break;
            case R.id.chkFixElectronicInHouse:
                mSettingJob += "1,";
                break;
            case R.id.chkCleanHouse:
                mSettingJob += "2,";
                break;
            case R.id.chkDoLaundry:
                mSettingJob += "3,";
                break;
            case R.id.chkFixWaterPipe:
                mSettingJob += "4,";
                break;
            case R.id.chkPaintHouse:
                mSettingJob += "5,";
                break;
        }
    }

    @Override
    void inits() {

        mSharedPreferencesUserLogin = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN,0);;
        mFirebaseSetting = new Firebase("https://viectimnguoi-469e6.firebaseio.com/setting/"+mSharedPreferencesUserLogin.getString(Constant.ID_USER_LOGIN,""));
        mSharedPreferencesSetting = getActivity().getSharedPreferences(Constant.DATA_SETTING,0);

        if(!mSharedPreferencesSetting.getString(Constant.SETTING_JOB,"defaul").equals("defaul")||!mSharedPreferencesSetting.getString(Constant.SETTING_ADDRESS,"defaul").equals("defaul")){
            setViewSetting(mSharedPreferencesSetting.getString(Constant.SETTING_JOB,""),mSharedPreferencesSetting.getString(Constant.SETTING_ADDRESS,""));
        }
        else{
            checkFirebaseForSetting();
        }
    }

    public void setViewSetting(String jobSetting, String addressSetting){
        String[] jobs = jobSetting.split(",");
        String[] addresses = addressSetting.split(",");
        for(String job : jobs){
            switch (Integer.parseInt(job)){
                case 1:
                    mChkFixElectronicInHouse.setChecked(true);
                    break;
                case 2:
                    mChkCleanHouse.setChecked(true);
                    break;
                case 3:
                    mChkDoLaundry.setChecked(true);
                    break;
                case 4:
                    mChkFixWaterPipe.setChecked(true);
                    break;
                case 5:
                    mChkPaintHouse.setChecked(true);
                    break;
            }
        }
        for(String address : addresses) {
            switch (Integer.parseInt(address)) {
                case 1:
                    mChkHaiChau.setChecked(true);
                    break;
                case 2:
                    mChkCamLe.setChecked(true);
                    break;
                case 3:
                    mChkThanhKhe.setChecked(true);
                    break;
                case 4:
                    mChkSonTra.setChecked(true);
                    break;
                case 5:
                    mChkNguHanhSon.setChecked(true);
                    break;
                case 6:
                    mChkHoaVang.setChecked(true);
                    break;
                case 7:
                    mChkLienChieu.setChecked(true);
                    break;
            }
        }
    }

    public void checkFirebaseForSetting(){
        mFirebaseSetting.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String jobSetting = map.get("jobSetting").toString();
                String addressSetting = map.get("addressSetting").toString();
                setViewSetting(jobSetting,addressSetting);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    @Click(R.id.imgDownJob)
    public void downJobAction(){
        mImgUpJob.setVisibility(View.VISIBLE);
        mRlChoiceJob.setVisibility(View.VISIBLE);
        mImgDownJob.setVisibility(View.GONE);
    }

    @Click(R.id.imgUpJob)
    public void upJobAction(){
        mImgUpJob.setVisibility(View.GONE);
        mRlChoiceJob.setVisibility(View.GONE);
        mImgDownJob.setVisibility(View.VISIBLE);
    }

    @Click(R.id.imgDownAddress)
    public void downAddressAction(){
        mImgAddress.setVisibility(View.VISIBLE);
        mLlChoiceAddress.setVisibility(View.VISIBLE);
        mImgDownAddress.setVisibility(View.GONE);
    }

    @Click(R.id.imgUpAddress)
    public void upAddressAction(){
        mImgAddress.setVisibility(View.GONE);
        mLlChoiceAddress.setVisibility(View.GONE);
        mImgDownAddress.setVisibility(View.VISIBLE);
    }
}
