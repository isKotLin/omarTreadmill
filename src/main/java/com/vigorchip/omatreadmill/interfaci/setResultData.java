package com.vigorchip.omatreadmill.interfaci;

/**
 * Created by wr-app1 on 2018/5/4.
 * 显示在运动记录页面的数据：时间、距离、卡路里、最大速度、平均速度。
 */

public interface setResultData {
    void OnData(String time, String km, String cal, String maxSpeed, String avgSpeed);
}