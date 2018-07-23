package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/20.
 *
 * 设置当前是KPH,还是MPH：
 * KPH:距离/km    速度/km/h
 * MPH:距离/mile     速度/MPH
 */

public class setPH {
    private String distancePH;
    private String speedPH;

    public setPH(String distancePH, String speedPH) {
        this.distancePH = distancePH;
        this.speedPH = speedPH;
    }

    public String getDistancePH() {
        return distancePH;
    }

    public String getSpeedPH() {
        return speedPH;
    }
}
