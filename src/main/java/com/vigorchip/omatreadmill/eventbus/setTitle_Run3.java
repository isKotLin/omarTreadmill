package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/5/2.
 * 设置实景路径，实景几的java bean
 */

public class setTitle_Run3 {
    private String path;//实景视频路径
    private int index = 0;//实景几

    public setTitle_Run3(String path, int index) {
        this.path = path;
        this.index = index;
    }

    public String getPath() {
        return path;
    }

    public int getIndex() {
        return index;
    }
}
