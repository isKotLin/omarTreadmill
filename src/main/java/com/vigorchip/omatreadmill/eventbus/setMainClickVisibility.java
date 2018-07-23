package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/20.
 */

/**
 * 设置主页面的启动键分开
 * */

public class setMainClickVisibility {

    private int mainClickVisibility = 0; //1 分开主页面启动键  0显示主页面启动键

    public setMainClickVisibility(int flag) {
        this.mainClickVisibility = flag;
    }

    public int getMainClickVisibility() {
        return mainClickVisibility;
    }

}
