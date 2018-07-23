package com.vigorchip.omatreadmill.bean;

/**
 * Created by wr-app1 on 2018/4/2.
 */

public class Mode_Distance_Info {
    private int KM = 0;//千米
    private int HM = 0;//百米
    private int M = 0;//米
    private int OverDistance = 0;

    public int getM() {
        return M;
    }

    public void setM(int M) {
        this.M = M;
    }

    public int getHM() {
        return HM;
    }

    public void setHM(int HM) {
        this.HM = HM;
    }

    public int getKM() {
        return KM;
    }

    public void setKM(int KM) {
        this.KM = KM;
    }

    public String getOverDistance() {
        return String.valueOf(getKM())
                + String.valueOf(getHM())
                + "."
                + String.valueOf(getM());
    }


}
