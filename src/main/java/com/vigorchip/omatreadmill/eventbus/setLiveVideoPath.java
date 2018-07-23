package com.vigorchip.omatreadmill.eventbus;

/**
 * Created by wr-app1 on 2018/4/26.
 * EventBus Javabean:
 * 设置实景模式视频路径
 */

public class setLiveVideoPath {
    private String path;

    public setLiveVideoPath(String path) {
        this.path = path;
    }

    public String getLiveVideoPath() {
        return path;
    }

}
