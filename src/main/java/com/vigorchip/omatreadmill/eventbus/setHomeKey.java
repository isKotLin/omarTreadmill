package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/15.
 */

public class setHomeKey {
    private boolean homeClick = false;

    public setHomeKey(boolean homeClick) {
        this.homeClick = homeClick;
    }

    public boolean isHomeClick() {
        return homeClick;
    }
}
