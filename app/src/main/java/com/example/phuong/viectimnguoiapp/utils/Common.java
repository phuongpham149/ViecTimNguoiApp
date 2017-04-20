package com.example.phuong.viectimnguoiapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by phuong on 24/02/2017.
 */

public class Common {
    public static void createDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        TextView btnOk = (TextView) dialog.findViewById(R.id.tvBtnOk);

        if (!"".equals(content)) {
            tvContent.setText(content);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static LatLng getRoomLocation(String address, Context context) {
        if (address != null) {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(address, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            } else {
                Toast.makeText(context, context.getString(R.string.message_not_found_address), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
}
