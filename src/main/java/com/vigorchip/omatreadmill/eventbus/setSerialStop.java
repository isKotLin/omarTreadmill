package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/16.
 * 串口发送的停止
 */

public class setSerialStop {
    private boolean stop = false;
    private boolean error = false;

    public setSerialStop(boolean stop, boolean error) {
        this.stop = stop;
        this.error = error;
    }

    public boolean isStop() {
        return stop;
    }

    public boolean isErrorStop() {
        return error;
    }

}
