package com.vigorchip.omatreadmill.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by wr-app1 on 2018/5/10.
 */

public class WakeLockUtils {

    public static void setWakeLock(Context context, int time) {//设置休眠时间
        Settings.System.putInt(context.getContentResolver(),
                android.provider.Settings.System.SCREEN_OFF_TIMEOUT,time);
    }

    public static int getScreenOffTime(Context context) {//获取休眠时间
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception localException) {

        }
        return screenOffTime;
    }
}
