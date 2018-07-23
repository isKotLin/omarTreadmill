package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/20.
 */

public class setMainPause {

    private int Pause;//1暂停  0启动

    public setMainPause(int Pause) {
        this.Pause = Pause;
    }

    public int getPause() {
        return Pause;
    }

}
