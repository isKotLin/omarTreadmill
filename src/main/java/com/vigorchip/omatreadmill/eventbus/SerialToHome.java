package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/31.
 * 运行错误返回主頁面
 */

public class SerialToHome {
    private boolean send = false;

    public SerialToHome(boolean send) {
        this.send = send;
    }

    public boolean isSend() {
        return send;
    }
}
