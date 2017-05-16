package com.example.phuong.viectimnguoiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asiantech on 16/05/2017.
 */
public class SharedPreferencesUtils {
    private static final String SHARED_PREF_NAME = SharedPreferencesUtils.class.getSimpleName();
    private static final String LAST_USER_ID = "last_userid";
    private static SharedPreferencesUtils ourInstance = new SharedPreferencesUtils();

    public static SharedPreferencesUtils getInstance() {
        return ourInstance;
    }

    private SharedPreferencesUtils() {
    }

    public void setLastUserId(Context context, String id) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LAST_USER_ID, id);
        editor.apply();
    }

    public void setUserLogin(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constant.IS_USER_LOGIN, "true");
        editor.apply();
    }

    public String isUserLogin(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(Constant.IS_USER_LOGIN, "");
    }

    public String getLastUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(LAST_USER_ID, "");
    }
    public void deleteAll(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public void setSetting(Context context,String settingAddress,String settingJob){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constant.SETTING_JOB, settingJob);
        editor.putString(Constant.SETTING_ADDRESS, settingAddress);
        editor.apply();
    }

    public String getSettingJob(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(Constant.SETTING_JOB, "");
    }

    public String getSettingAddress(Context context){
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(Constant.SETTING_ADDRESS, "");
    }
}
