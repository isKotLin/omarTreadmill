package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/24.
 * 正计时发送到服务同步显示
 */

public class setCountUp_Service {
    private long countUp;

    public setCountUp_Service(long countUp) {
        this.countUp = countUp;
    }

    public long getCountUp() {
        return countUp;
    }

}
