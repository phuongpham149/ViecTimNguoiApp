package com.example.phuong.viectimnguoiapp.activities;

import android.os.Handler;

import com.example.phuong.viectimnguoiapp.R;

import org.androidannotations.annotations.EActivity;

import lombok.libs.org.objectweb.asm.Handle;

/**
 * Created by asiantech on 06/03/2017.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @Override
    void inits() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity_.intent(SplashActivity.this).start();
            }
        }, 2000);
    }
}
