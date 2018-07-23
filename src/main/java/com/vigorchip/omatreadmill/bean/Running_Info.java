package com.vigorchip.omatreadmill.bean;

/**
 * Created by wr-app1 on 2018/4/4.
 */

public class Running_Info {
    private String PH = "距离/km";
    private static String title;

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPH() {
        return PH;
    }

    public void setPH(String PH) {
        this.PH = PH;
    }
}
