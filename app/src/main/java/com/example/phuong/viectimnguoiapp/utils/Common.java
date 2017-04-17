package com.example.phuong.viectimnguoiapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;

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
}
