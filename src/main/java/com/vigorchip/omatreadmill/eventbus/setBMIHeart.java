package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/3.
 */

public class setBMIHeart {
    private boolean isBMI;

    public setBMIHeart(boolean isBMI) {
        this.isBMI = isBMI;
    }

    public boolean isBMI() {
        return isBMI;
    }
}
