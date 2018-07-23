package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/19.
 * 设置跑步界面标题，并且返回当前模式，模式从SportData获取：
 * mode: 11 = 快速模式
 *       12 = 时间模式
 *       13 = 距离模式
 *       14 = 热量模式
 */

public class setTitle {
    private String title;
    private int mode = 0;
    private String time;
    private String distance;
    private String cal;

    public setTitle(String title,int mode, String time, String distance, String cal){
        this.title = title;
        this.mode = mode;
        this.time = time;
        this.distance = distance;
        this.cal = cal;
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

    public String getDistance() {
        return distance;
    }

    public String getCal() {
        return cal;
    }
}
