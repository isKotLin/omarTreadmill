package com.vigorchip.omatreadmill.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wr-app1 on 2018/5/10.
 * 屏幕休眠广播
 */

public class WakeLockReceiver extends BroadcastReceiver {
    private String SCREEN_ON = "android.intent.action.SCREEN_ON";
    private String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    public static boolean isSCREEN = true;//判断屏幕休眠的标志位

    @Override
    public void onReceive(Context context, Intent intent) {
        // 屏幕唤醒
        if (SCREEN_ON.equals(intent.getAction())) {
            isSCREEN = true;
            Log.e("屏幕唤醒了", SCREEN_ON);
        }
        // 屏幕休眠
        else if (SCREEN_OFF.equals(intent.getAction())) {
            isSCREEN = false;
            Log.e("屏幕休眠了", SCREEN_OFF);
        }
    }
}
