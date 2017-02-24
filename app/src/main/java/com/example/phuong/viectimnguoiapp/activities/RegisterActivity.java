package com.example.phuong.viectimnguoiapp.activities;

import com.example.phuong.viectimnguoiapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by phuong on 23/02/2017.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @Override
    void inits() {

    }

    @Click(R.id.imgBack)
    public void BackAction(){
        finish();
    }
}
