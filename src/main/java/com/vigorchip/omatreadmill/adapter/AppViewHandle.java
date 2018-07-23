package com.vigorchip.omatreadmill.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;


public class AppViewHandle {
    public ImageView apps_iv;
    public TextView apps_tv;
    public LinearLayout app_ll_item;

    public AppViewHandle(View view) {
        app_ll_item = (LinearLayout) view.findViewById(R.id.app_item);
        apps_iv = (ImageView) view.findViewById(R.id.app_iv);
        apps_tv = (TextView) view.findViewById(R.id.app_name);
    }
}