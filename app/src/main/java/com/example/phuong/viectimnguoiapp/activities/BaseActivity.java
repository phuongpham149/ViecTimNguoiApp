package com.example.phuong.viectimnguoiapp.activities;

import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by phuong on 20/02/2017.
 */
@EActivity
public abstract class BaseActivity extends AppCompatActivity {
    @AfterViews
    abstract void inits();
}
