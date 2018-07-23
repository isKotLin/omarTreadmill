package com.vigorchip.omatreadmill.utils;

/**
 * Created by wr-app1 on 2018/4/27.
 * 处理获取距离模式设置的距离的工具类
 */

public class DistanceUtils {
    private static double mDistance;

    public static double DistanceManager(String distance) {
        mDistance = Double.valueOf(distance);
        return mDistance;
    }

}
