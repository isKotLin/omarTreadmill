package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/5.
 */

public class set_TitleRun4 {
    private String age;
    private String targetHrc;
    private String min;

    public set_TitleRun4(String age, String targetHrc, String min) {
        this.age = age;
        this.targetHrc = targetHrc;
        this.min = min;
    }

    public String getAge() {
        return age;
    }

    public String getTargetHrc() {
        return targetHrc;
    }

    public String getMin() {
        return min;
    }
}
