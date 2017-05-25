package com.example.phuong.viectimnguoiapp.fragments;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.eventBus.BusProvider;
import com.example.phuong.viectimnguoiapp.eventBus.object.NetWorkState;
import com.example.phuong.viectimnguoiapp.objects.Setting;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.SharedPreferencesUtils;
import com.example.phuong.viectimnguoiapp.utils.SubscribeSettingHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    @ViewById(R.id.chkIronTheClothes)
    protected CheckBox mChkIronTheClothes;
    @ViewById(R.id.swNotification)
    protected Switch swNotification;

    @ViewById(R.id.progressBarSave)
    protected ProgressBar mProgressBarSave;

    private DatabaseReference mFirebaseSetting;
    private DatabaseReference mFirebaseCheckSetting;

    @Click(R.id.btnSave)
    public void saveAction() {
        mProgressBarSave.setVisibility(View.VISIBLE);
        SharedPreferencesUtils.getInstance().setSetting(getContext(), getSettingAddress(), getSettingJob());
        SharedPreferencesUtils.getInstance().setEnableSubscribeSetting(getContext(), swNotification.isChecked());
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
        if (mChkIronTheClothes.isChecked()) {
            stringBuilder.append("6");
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
        Setting setting = new Setting(SharedPreferencesUtils.getInstance().getSettingJob(getContext()), SharedPreferencesUtils.getInstance().getSettingAddress(getContext()));
        mFirebaseSetting.child("setting").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(setting);
        mProgressBarSave.setVisibility(View.GONE);

        SubscribeSettingHelper.getInstance().updateSetting(getContext());

        Common.createDialog(getActivity(), "Lưu cài đặt thành công");
    }

    @Override
    void inits() {
        mFirebaseSetting = FirebaseDatabase.getInstance().getReference("/");
        mFirebaseCheckSetting = FirebaseDatabase.getInstance().getReference("/setting/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        if ((!SharedPreferencesUtils.getInstance().getSettingJob(getContext()).equals("") || !SharedPreferencesUtils.getInstance().getSettingAddress(getContext()).equals(""))) {
            setViewSetting(SharedPreferencesUtils.getInstance().getSettingJob(getContext()),
                    SharedPreferencesUtils.getInstance().getSettingAddress(getContext()),
                    SharedPreferencesUtils.getInstance().getEnableSubscribeSetting(getContext()));
        } else {
            checkFirebaseForSetting();
        }
    }

    public void setViewSetting(String jobSetting, String addressSetting, boolean enableSubscribeSetting) {
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
                case 6:
                    mChkIronTheClothes.setChecked(true);
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
        swNotification.setChecked(enableSubscribeSetting);
    }

    public void checkFirebaseForSetting() {
        DatabaseReference mFirebaseCheckSetting = FirebaseDatabase.getInstance().getReference("/setting/");
        mFirebaseCheckSetting.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFirebaseCheckSetting.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(data.getKey())) {
                        HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                        if (map != null) {
                            String jobSetting = map.get("jobSetting").toString();
                            String addressSetting = map.get("addressSetting").toString();
                            setViewSetting(jobSetting, addressSetting, SharedPreferencesUtils.getInstance().getEnableSubscribeSetting(getContext()));
                        }
                    }
                }
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
