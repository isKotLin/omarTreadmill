package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/26.
 * 倒计时进入运动记录的数据
 */

public class ServiceToResult {
    private boolean to = false;
    private int mode;
    private String time;
    private String km;
    private String cal;
    private double maxSpeed;
    private double AvgSpeed;

    public ServiceToResult(boolean to, int mode) {
        this.to = to;
        this.mode = mode;
        this.time = time;
        this.km = km;
        this.cal = cal;
    }

    public boolean getTo() {
        return to;
    }

    public String getTime() {
        return time;
    }

    public String getKm() {
        return km;
    }

    public String getCal() {
        return cal;
    }

    public int getMode() {
        return mode;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAvgSpeed() {
        return AvgSpeed;
    }
}
