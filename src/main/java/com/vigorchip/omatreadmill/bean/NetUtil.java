package com.vigorchip.omatreadmill.bean;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.vigorchip.omatreadmill.base.BaseObserver;
import com.vigorchip.omatreadmill.interfaci.Api;
import com.vigorchip.omatreadmill.utils.UtilRetrofit;

import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


/**
 * Created by wr-app2 on 2018/5/11.
 */

public class NetUtil {
    /**
     * 普通的get  post 请求
     * @param methodName
     * @param observer
     * @param parameter
     */
    public static void request(String methodName, BaseObserver observer, Object... parameter) {
        Class<Api> apiClass = Api.class;
        Context context = observer.getContext();
        try {
            Method[] methods = apiClass.getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.equals(methodName)) {
                    Observable<ResponseBody> observable = (Observable) method.invoke(UtilRetrofit.create(context), parameter);
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                }
            }
        } catch (Exception e) {
            Log.e("print",e.toString()+"ll");
            e.printStackTrace();
        }
    }


    /**
     * 上传JSON数据
     * @param methodName
     * @param observer
     */
    public static void postJson(String methodName,BaseObserver observer,Object obj) {
        Class<Api> apiClass = Api.class;
        Context context = observer.getContext();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        try {
            Method[] methods = apiClass.getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.equals(methodName)) {
                    Observable<ResponseBody> observable = (Observable) method.invoke(UtilRetrofit.create(context), RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json));
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通的get  post 请求
     * @param methodName
     * @param observer
     */
    public static void request(String methodName, BaseObserver observer, Map<String,Object> map) {
        Class<Api> apiClass = Api.class;
        Context context = observer.getContext();
        try {
            Method[] methods = apiClass.getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (name.equals(methodName)) {
                    Observable<ResponseBody> observable = (Observable) method.invoke(UtilRetrofit.create(context), map);
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);

                }
            }
        } catch (Exception e) {
            Log.e("print",e.toString()+"ll");
            e.printStackTrace();
        }
    }
}
