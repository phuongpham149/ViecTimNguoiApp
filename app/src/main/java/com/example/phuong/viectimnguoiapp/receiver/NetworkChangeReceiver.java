package com.example.phuong.viectimnguoiapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.eventBus.BusProvider;
import com.example.phuong.viectimnguoiapp.eventBus.objet.NetWorkState;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;

/**
 * Created by phuong on 28/02/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = Network.getConnectivityStatusString(context);
        if(status.equals(Constant.MOBILE_DATA_ENABLE) || status.equals(Constant.WIFI_ENABLE)){
            BusProvider.getInstance().register(context);
            NetWorkState netWorkState = new NetWorkState(status);
            BusProvider.getInstance().post(netWorkState);
        }
    }
}
