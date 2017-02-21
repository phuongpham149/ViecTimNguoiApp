package com.example.phuong.viectimnguoiapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tientun on 3/10/15.
 */
public class Helpers {

    public static String sha256(String string){
        try {
            MessageDigest sss = MessageDigest.getInstance("SHA-256");
            sss.update(string.getBytes());
            byte byteData[] = sss.digest();
            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for(byte tmp: byteData) {
                sb.append(Integer.toString((tmp & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String md5(String text) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(text.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for(byte tmp: a) {
                sb.append(Character.forDigit((tmp & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(tmp & 0x0f, 16));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.d("---", e.getMessage());
        }
        return null;
    }

    /**
     * Force show the soft keyboard
     *
     * @param activity the Activity
     * @param view     the current focused view
     */
    public static void showSoftKeyboard(final Activity activity, final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager keyboard = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(view, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 400);
    }

 /**
     * Force hide the soft keyboard
     *
     * @param activity
     *            the Activity
     * @param view
     *            the current focused view
     */
    public static void hideSoftKeyboard(Activity activity, View view) {
        try {

            InputMethodManager keyboard = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() !=null;
    }
}
