package com.vigorchip.omatreadmill.interfaci;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by wr-app2 on 2018/5/12.
 */

public interface Api {

    //注册
    @GET("register?")
    Observable<ResponseBody> register(@QueryMap HashMap<String, Object> map);
    String register="register";
    //登陆
    @GET("login?")
    Observable<ResponseBody> login(@QueryMap HashMap<String, Object> map);
    String login="login";
    //获取个人信息
    @GET("getInfo?")
    Observable<ResponseBody> getInfo(@QueryMap HashMap<String, Object> map);
    String getInfo="getInfo";
    //上传跑步记录
    @GET("updataRunrecord?")
    Observable<ResponseBody> updataRunrecord(@QueryMap HashMap<String, Object> map);
    String updataRunrecord="updataRunrecord";
    //获取跑步记录
    @GET("getRunRecord?")
    Observable<ResponseBody> getRunRecord(@QueryMap HashMap<String, Object> map);
    String getRunRecord="getRunRecord";
    //完善个人信息接口
    @GET("compelete?")
    Observable<ResponseBody> compelete(@QueryMap HashMap<String, Object> map);
    String compelete="compelete";

    @GET("deleteRunRecord?")
    Observable<ResponseBody> deleteRunRecord(@QueryMap HashMap<String, Object> map);
    String deleteRunRecord="deleteRunRecord";

    //忘記密碼
    @GET("findbackpswd?")
    Observable<ResponseBody> findbackpswd(@QueryMap HashMap<String, Object> map);
    String findbackpswd="findbackpswd";


}
