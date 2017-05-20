package com.example.phuong.viectimnguoiapp.activities;

import android.os.Handler;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.utils.SharedPreferencesUtils;

import org.androidannotations.annotations.EActivity;

/**
 * Created by asiantech on 06/03/2017.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Override
    void inits() {
        if (SharedPreferencesUtils.getInstance().isUserLogin(getApplicationContext()).equals("true")) {
            MainActivity_.intent(SplashActivity.this).start();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity_.intent(SplashActivity.this).start();
                finish();
            }
        }, 2000);
    }


}
