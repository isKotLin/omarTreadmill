package com.vigorchip.omatreadmill.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.appcompat.BuildConfig;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.vigorchip.omatreadmill.interfaci.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by wr-app2 on 2018/5/12.
 */

public class UtilRetrofit {
    static String name;

    static String value;
    final static String BASE_URL = "http://39.108.225.155/vc_tp/Home/Api/";

    /**
     * 创建retrofit客户端
     * @return
     */
    public static Retrofit create(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel
                (HttpLoggingInterceptor.Level.HEADERS));
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 携带Cookies
     * @return
     */
    public static Api create(final Context context) {

        final SharedPreferences sp = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //其他配置
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        Retrofit build = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return build.create(Api.class);
    }
}
