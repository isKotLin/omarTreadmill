package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/25.
 * 串口发送的开始跑步
 */

public class setSerialRun {
    private boolean serialRun;

    public setSerialRun(boolean serialRun) {
        this.serialRun = serialRun;
    }

    public boolean getSerialRun() {
        return serialRun;
    }

}
