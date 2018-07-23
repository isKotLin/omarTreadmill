package com.vigorchip.omatreadmill.bean;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

/**
 * Created by wr-app1 on 2018/4/8.
 */

public class System_Info {
    private String appVersionName;
    private int appVersionCode;
    private int screenWidth;
    private int screenHeight;
    private int densityDpi;

    public String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }

    public int getAppVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            appVersionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionCode;
    }

    public float getDensity(Fragment fragment) {
        DisplayMetrics dm = new DisplayMetrics();
        fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        densityDpi = dm.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        return densityDpi;
    }

    public String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public String getDriverName() {
        return android.os.Build.DEVICE;
    }

    public int getResolutionWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        return screenWidth;
    }

    public int getResolutionHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenHeight = dm.densityDpi;
        return screenHeight;
    }

}
