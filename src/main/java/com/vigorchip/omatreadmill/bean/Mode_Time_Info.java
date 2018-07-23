package com.vigorchip.omatreadmill.bean;

/**
 * Created by wr-app1 on 2018/4/2.
 */

public class Mode_Time_Info {
    private int MM1 = 0;
    private int MM2 = 0;
    private int SS1 = 0;
    private int SS2 = 0;
    private int OverTime = 0;

    public int getMM1() {
        return MM1;
    }

    public void setMM1(int MM1) {
        this.MM1 = MM1;
    }

    public int getMM2() {
        return MM2;
    }

    public void setMM2(int MM2) {
        this.MM2 = MM2;
    }

    public int getSS1() {
        return SS1;
    }

    public void setSS1(int SS1) {
        this.SS1 = SS1;
    }

    public int getSS2() {
        return SS2;
    }

    public void setSS2(int SS2) {
        this.SS2 = SS2;
    }

    public String getOverTime() {
        return String.valueOf(getMM1())+String.valueOf(getMM2())+String.valueOf(getSS1())+String.valueOf(getSS2());
    }

}
