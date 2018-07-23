package com.vigorchip.omatreadmill.bean;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by wr-app1 on 2018/4/4.
 */

public class WakeLock_Info {
    private Context context;
    private long time;

    public WakeLock_Info(Context context, long time) {
        this.context = context;
        this.time = time;
    }

    public void setWakeLock() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "SimpleTimer");
        mWakeLock.acquire(time);//设置超时时间，30s后释放休眠锁，30秒亮屏时间；
    }

    public long getWakeLock() {
        return time;
    }

}
