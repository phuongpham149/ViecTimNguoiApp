package com.example.phuong.viectimnguoiapp.activities;

import android.widget.EditText;

import com.example.phuong.viectimnguoiapp.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by phuong on 23/02/2017.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @Extra
    protected String username;
    @Extra
    protected String password;
    @ViewById(R.id.edtUsername)
    EditText mEdtUsername;
    @ViewById(R.id.edtPassword)
    EditText mEdtPassword;

    @Override
    void inits() {
        if (!"".equals(username) && !"".equals(password)) {
            mEdtUsername.setText(username);
            mEdtPassword.setText(password);
        }
    }

    @Click(R.id.imgBack)
    public void BackAction() {
        finish();
    }
}
