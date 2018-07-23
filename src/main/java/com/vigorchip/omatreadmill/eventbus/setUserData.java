package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/8.
 * 用户训练模式开始时的接口，传速度与坡度的数组
 */

public class setUserData {
    private int[] speed;
    private int[] slope;

    public setUserData(int[] speed, int[] slope) {
        this.speed = speed;
        this.slope = slope;
    }

    public int[] getSpeed() {
        return speed;
    }

    public int[] getSlope() {
        return slope;
    }
}
