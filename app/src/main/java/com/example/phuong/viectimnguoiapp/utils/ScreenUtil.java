/*
 * Copyright (c) 2014 AsianTech company. All rights reserved.
 */
package com.example.phuong.viectimnguoiapp.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * This class have some methods that support for get size of screen such
 * as {@link #getWidthScreen(android.content.Context)}.
 * And this have also a function {@link #convertDPToPixels(android.content.Context, int)} to
 * convert db value to pixels
 *
 * @author thangtc
 * @version 1.0
 * @since 15/09/2014
 */
public class ScreenUtil {

    /**
     * This method is used to get width of screen
     *
     * @param context is current context
     * @return return width of screen in pixel
     */
    public static int getWidthScreen(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dimension = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dimension);
        return dimension.widthPixels;
    }

    /**
     * This method is used to convert dp to pixel
     *
     * @param context is current context
     * @param dp      is value you want to convert for
     * @return return value in pixel
     */
    public static int convertDPToPixels(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    public static int getHeightScreen(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dimension = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dimension);
        return dimension.heightPixels;
    }
}
