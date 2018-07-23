package com.vigorchip.omatreadmill.bean;

/**
 * Created by wr-app1 on 2018/4/21.
 */

public class TimeHelper {

    private static int SportTime;//跑步时间
    private static int SportStatus;//跑步状态
    private static int Speed;//速度
    private static int Slope;//坡度
    private static int lastSpeed;//上一次的速度
    private static int lastSlope;//上一次的坡度
    private static double Mileage;//里程
    private static int Heart;//心率
    private static double Clories;//卡路里

    private boolean isStart;//是否开始跑步

    public void StartTime() {//开始跑步
        isStart = true;
        SportTime = 0;

    }





}
