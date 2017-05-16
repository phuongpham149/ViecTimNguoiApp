package com.example.phuong.viectimnguoiapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 03/03/2017.
 */
public class DialogLoading {
    private static DialogLoading ourInstance = new DialogLoading();
    private ProgressDialog progressDialog;

    public static DialogLoading getInstance() {
        return ourInstance;
    }

    private DialogLoading() {
    }

    public void showProgressDialog(Context context, String messing) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(messing);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
