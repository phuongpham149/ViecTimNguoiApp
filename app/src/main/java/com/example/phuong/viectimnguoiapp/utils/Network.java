package com.example.phuong.viectimnguoiapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by phuong on 27/02/2017.
 */

public class Network {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static boolean checkNetWork(Context context, int typeCheck) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        if (typeCheck == Constant.TYPE_WIFI) {
            networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo.isConnected()) {
                return true;
            } else return false;
        } else if (typeCheck == Constant.TYPE_NETWORK) {
            networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            } else return false;
        }
        return false;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = Network.getConnectivityStatus(context);
        String status = null;
        if (conn == Network.TYPE_WIFI) {
            status = Constant.WIFI_ENABLE;
        } else if (conn == Network.TYPE_MOBILE) {
            status = Constant.MOBILE_DATA_ENABLE;
        } else if (conn == Network.TYPE_NOT_CONNECTED) {
            status = Constant.NO_INTERNET;
        }
        return status;
    }
}
