package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/8.
 * 心率模式的eventbus接口
 */

public class setHeartHRC {
    private boolean flag = false;

    public setHeartHRC(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }
}
