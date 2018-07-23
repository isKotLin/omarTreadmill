package com.vigorchip.omatreadmill.bean;

/**
 * Created by wr-app1 on 2018/4/2.
 */

public class Mode_Calories_Info {
    private static int ca1 = 0;
    private static int ca2 = 0;
    private static int ca3 = 0;
    private static int ca4 = 0;
    private static int OverCalories = 0;

    public static int getCa1() {
        return ca1;
    }

    public void setCa1(int ca1) {
        this.ca1 = ca1;
    }

    public static int getCa2() {
        return ca2;
    }

    public void setCa2(int ca2) {
        this.ca2 = ca2;
    }

    public static int getCa3() {
        return ca3;
    }

    public void setCa3(int ca3) {
        this.ca3 = ca3;
    }

    public static int getCa4() {
        return ca4;
    }

    public void setCa4(int ca4) {
        this.ca4 = ca4;
    }

    public static String getOverCalories() {
        return String.valueOf(getCa1())
                + String.valueOf(getCa2())
                + String.valueOf(getCa3())
                + String.valueOf(getCa4());
    }

}
