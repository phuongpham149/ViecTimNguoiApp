package com.example.phuong.viectimnguoiapp.activities;

import android.content.Intent;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Helpers;
import com.firebase.client.Firebase;

import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by asiantech on 14/03/2017.
 */
@EActivity(R.layout.activity_splash)
public class PushData extends BaseActivity {
    private Firebase mFirebase;

    @Override
    void inits() {
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://viectimnguoi-469e6.firebaseio.com/categoryJobs");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(Constant.TYPE_FIX_ELECTRONIC_IN_HOUSE));
        map.put("name", getResources().getString(R.string.chk_text_fix_electronic));
        mFirebase.push().setValue(map);

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("id", String.valueOf(Constant.TYPE_CLEAN_HOUSE));
        map1.put("name", getResources().getString(R.string.chk_clean_house));
        mFirebase.push().setValue(map1);

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("id", String.valueOf(Constant.TYPE_DO_LAUNDRY));
        map2.put("name", getResources().getString(R.string.textview_text_do_laundry));
        mFirebase.push().setValue(map2);

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("id", String.valueOf(Constant.TYPE_FIX_WATER_PIPE));
        map3.put("name", getResources().getString(R.string.textview_text_fix_water_pipe));
        mFirebase.push().setValue(map3);

        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("id", String.valueOf(Constant.TYPE_PAINT_HOUSE));
        map4.put("name", getResources().getString(R.string.textview_text_paint_house));
        mFirebase.push().setValue(map4);
    }
}
