package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/23.
 *
 * 设置标题，模式的java bean
 *
 * time = 时间模式设置的时间
 * distance = 距离模式设置的时间
 * cal = 热量模式设置的时间
 *
 */

public class setTitle_Run2 {
    private String title;//标题
    private int mode = 0;//模式
    private String time;//时间
    private double[] speed;//速度
    private double[] slope;//坡度

    public setTitle_Run2(String title, int mode, String time, double[] speed, double[] slope){
        this.title = title;
        this.mode = mode;
        this.time = time;
        this.speed = speed;
        this.slope = slope;
    }

    public String getmTitle() {
        return title;
    }

    public void setmTitle(String title) {
        this.title = title;
    }

    public int getMode() {
        return mode;
    }

    public String getTime() {
        return time;
    }

    public double[] getSpeed() {
        return speed;
    }

    public double[] getSlope() {
        return slope;
    }
}
