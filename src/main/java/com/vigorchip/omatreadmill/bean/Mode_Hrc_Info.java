package com.vigorchip.omatreadmill.bean;

/**
 * Created by wr-app1 on 2018/5/5.
 * 心率模式java bean
 */

public class Mode_Hrc_Info {
    private int age1;
    private int age2;
    private int hrc1;
    private int hrc2;
    private int hrc3;
    private int min1;
    private int min2;
    private String overAge;
    private String overHrc;
    private String overMin;

    public int getAge1() {
        return age1;
    }

    public void setAge1(int age1) {
        this.age1 = age1;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public int getHrc1() {
        return hrc1;
    }

    public void setHrc1(int hrc1) {
        this.hrc1 = hrc1;
    }

    public int getHrc2() {
        return hrc2;
    }

    public void setHrc2(int hrc2) {
        this.hrc2 = hrc2;
    }

    public int getHrc3() {
        return hrc3;
    }

    public void setHrc3(int hrc3) {
        this.hrc3 = hrc3;
    }

    public int getMin1() {
        return min1;
    }

    public void setMin1(int min1) {
        this.min1 = min1;
    }

    public int getMin2() {
        return min2;
    }

    public void setMin2(int min2) {
        this.min2 = min2;
    }

    public String getOverAge() {
        overAge = String.valueOf(age1) + String.valueOf(age2);
        return overAge;
    }

    public String getOverHrc() {
        overHrc = String.valueOf(hrc1) + String.valueOf(hrc2) + String.valueOf(hrc3);
        return overHrc;
    }

    public String getOverMin() {
        overMin = "00" + String.valueOf(min1) + String.valueOf(min2);
        return overMin;
    }
}
