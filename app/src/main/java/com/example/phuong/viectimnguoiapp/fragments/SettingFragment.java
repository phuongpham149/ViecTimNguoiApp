package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.eventBus.object.NetWorkState;
import com.example.phuong.viectimnguoiapp.objects.Setting;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;

/**
 * Created by asiantech on 15/04/2017.
 */
@EFragment(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {
    private static final String TAG = SettingFragment.class.getSimpleName();
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

    @ViewById(R.id.progressBarSave)
    protected ProgressBar mProgressBarSave;

    private DatabaseReference mFirebaseSetting;
    private DatabaseReference mFirebaseCheckSetting;
    private SharedPreferences mSharedPreferencesSetting;
    private SharedPreferences mSharedPreferencesUserLogin;

    @Click(R.id.btnSave)
    public void saveAction() {
        mProgressBarSave.setVisibility(View.VISIBLE);
        SharedPreferences.Editor mEditor = mSharedPreferencesUserLogin.edit();
        mEditor.putString(Constant.SETTING_JOB, getSettingJob());
        mEditor.putString(Constant.SETTING_ADDRESS, getSettingAddress());
        mEditor.putString(Constant.ID_USER_LOGIN, FirebaseAuth.getInstance().getCurrentUser().getUid());
        mEditor.apply();
        pushDataSetting();
    }

    public String getSettingAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mChkHaiChau.isChecked()) {
            stringBuilder.append("1");
        }
        if (mChkLienChieu.isChecked()) {
            stringBuilder.append("7");
        }
        if (mChkCamLe.isChecked()) {
            stringBuilder.append("2");
        }
        if (mChkThanhKhe.isChecked()) {
            stringBuilder.append("3");
        }
        if (mChkSonTra.isChecked()) {
            stringBuilder.append("4");
        }
        if (mChkNguHanhSon.isChecked()) {
            stringBuilder.append("5");
        }
        if (mChkHoaVang.isChecked()) {
            stringBuilder.append("6");
        }
        return stringBuilder.toString();
    }

    public String getSettingJob() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mChkFixElectronicInHouse.isChecked()) {
            stringBuilder.append("1");
        }
        if (mChkCleanHouse.isChecked()) {
            stringBuilder.append("2");
        }
        if (mChkDoLaundry.isChecked()) {
            stringBuilder.append("3");
        }
        if (mChkFixWaterPipe.isChecked()) {
            stringBuilder.append("4");
        }
        if (mChkPaintHouse.isChecked()) {
            stringBuilder.append("5");
        }
        return stringBuilder.toString();
    }

    @Subscribe
    public void receiverStateOnNetwork(NetWorkState netWorkState) {
        if (netWorkState.getState().equals(Constant.WIFI_ENABLE) || netWorkState.getState().equals(Constant.MOBILE_DATA_ENABLE)) {
            checkFirebaseForSetting();
            pushDataSetting();
        }
    }

    public void pushDataSetting() {
        Setting setting = new Setting(mSharedPreferencesUserLogin.getString(Constant.SETTING_JOB, ""), mSharedPreferencesUserLogin.getString(Constant.SETTING_ADDRESS, ""));
        mFirebaseSetting.child("setting").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(setting);
        mProgressBarSave.setVisibility(View.GONE);
        Common.createDialog(getActivity(), "Lưu cài đặt thành công");
    }

  /*  @CheckedChange({R.id.chkHaiChau, R.id.chkLienChieu, R.id.chkCamLe, R.id.chkThanhKhe, R.id.chkSonTra, R.id.chkNguHanhSon, R.id.chkHoaVang, R.id.chkFixElectronicInHouse, R.id.chkCleanHouse, R.id.chkDoLaundry, R.id.chkFixWaterPipe, R.id.chkPaintHouse})
    public void checkSettingJob(CompoundButton v) {
        switch (v.getId()) {
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
    }*/

    @Override
    void inits() {

        mSharedPreferencesUserLogin = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        mFirebaseSetting = FirebaseDatabase.getInstance().getReference("/");
        mFirebaseCheckSetting = FirebaseDatabase.getInstance().getReference("/setting/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        mSharedPreferencesSetting = getActivity().getSharedPreferences(Constant.DATA_SETTING, 0);

        if ((FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mSharedPreferencesSetting.getString(Constant.ID_USER_LOGIN, ""))) && (!mSharedPreferencesSetting.getString(Constant.SETTING_JOB, "defaul").equals("defaul") || !mSharedPreferencesSetting.getString(Constant.SETTING_ADDRESS, "defaul").equals("defaul"))) {
            setViewSetting(mSharedPreferencesSetting.getString(Constant.SETTING_JOB, ""), mSharedPreferencesSetting.getString(Constant.SETTING_ADDRESS, ""));
        } else {
            checkFirebaseForSetting();
        }
    }

    public void setViewSetting(String jobSetting, String addressSetting) {
        char[] jobs = jobSetting.toCharArray();
        char[] addresses = addressSetting.toCharArray();
        for (char job : jobs) {
            switch (Integer.parseInt(String.valueOf(job))) {
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
        for (char address : addresses) {
            switch (Integer.parseInt(String.valueOf(address))) {
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

    public void checkFirebaseForSetting() {
        DatabaseReference mFirebaseCheckSetting = FirebaseDatabase.getInstance().getReference("/setting/");
        mFirebaseCheckSetting.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFirebaseCheckSetting.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                String jobSetting = data.get("jobSetting").toString();
                String addressSetting = data.get("addressSetting").toString();
                setViewSetting(jobSetting, addressSetting);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Click(R.id.imgDownJob)
    public void downJobAction() {
        mImgUpJob.setVisibility(View.VISIBLE);
        mRlChoiceJob.setVisibility(View.VISIBLE);
        mImgDownJob.setVisibility(View.GONE);
    }

    @Click(R.id.imgUpJob)
    public void upJobAction() {
        mImgUpJob.setVisibility(View.GONE);
        mRlChoiceJob.setVisibility(View.GONE);
        mImgDownJob.setVisibility(View.VISIBLE);
    }

    @Click(R.id.imgDownAddress)
    public void downAddressAction() {
        mImgAddress.setVisibility(View.VISIBLE);
        mLlChoiceAddress.setVisibility(View.VISIBLE);
        mImgDownAddress.setVisibility(View.GONE);
    }

    @Click(R.id.imgUpAddress)
    public void upAddressAction() {
        mImgAddress.setVisibility(View.GONE);
        mLlChoiceAddress.setVisibility(View.GONE);
        mImgDownAddress.setVisibility(View.VISIBLE);
    }
}
