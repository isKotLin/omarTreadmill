package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/20.
 * 让跑步中的页面停止
 * Stop为确定停止的标志位，Main表示主页面发送的停止，用于跑步中的页面判断
 */

public class setStop {

    private boolean Stop = false;
    private boolean Main = false;

    public setStop(boolean stop, boolean main) {
        this.Stop = stop;
        this.Main = main;
    }

    public boolean getStop() {
        return Stop;
    }

    public boolean isMain() {
        return Main;
    }

}
