package com.vigorchip.omatreadmill.bean;

/**
 * Created by wr-app1 on 2018/5/21.
 */

public class Item_Message {


    /**
     * id : 727
     * user_id : 48
     * starttime : 0
     * totletime : 0
     * speed : 0.0
     * distance : 0.0
     * kaluli : 0.0
     * xinlv : 0
     * craeted_at : 1526115729
     */

    private String id;

    public Item_Message(String starttime, String totletime, String distance, String kaluli) {
        this.starttime = starttime;
        this.totletime = totletime;
        this.distance = distance;
        this.kaluli = kaluli;
    }

    private String user_id;
    private String starttime;
    private String totletime;
    private String speed;
    private String distance;
    private String kaluli;
    private String xinlv;
    private String craeted_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTotletime() {
        return totletime;
    }

    public void setTotletime(String totletime) {
        this.totletime = totletime;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getKaluli() {
        return kaluli;
    }

    public void setKaluli(String kaluli) {
        this.kaluli = kaluli;
    }

    public String getXinlv() {
        return xinlv;
    }

    public void setXinlv(String xinlv) {
        this.xinlv = xinlv;
    }

    public String getCraeted_at() {
        return craeted_at;
    }

    public void setCraeted_at(String craeted_at) {
        this.craeted_at = craeted_at;
    }
}
