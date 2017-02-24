package com.example.phuong.viectimnguoiapp.activities;

import android.widget.Button;

import com.example.phuong.viectimnguoiapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by phuong on 23/02/2017.
 */
@EActivity(R.layout.activity_opening_screen)
public class OpeningScreenActivity extends BaseActivity {
    @ViewById(R.id.btnCreateAccount)
    Button mBtnRegister;
    @ViewById(R.id.btnLogin)
    Button mBtnLogin;

    @Override
    void inits() {
    }

    @Click(R.id.btnLogin)
    public void LoginAction() {
        LoginActivity_.intent(this).start();
        this.overridePendingTransition(R.anim.anim_left_to_right,R.anim.anim_nothing);
    }

    @Click(R.id.btnCreateAccount)
    public void RegisterAction() {
        RegisterActivity_.intent(this).start();
        this.overridePendingTransition(R.anim.anim_left_to_right,R.anim.anim_nothing);
    }
}
