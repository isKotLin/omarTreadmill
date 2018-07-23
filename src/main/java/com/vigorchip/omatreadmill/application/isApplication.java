package com.vigorchip.omatreadmill.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.tencent.bugly.crashreport.CrashReport;
import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.serialport.newSerial;

import java.io.File;

//import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by wr-app1 on 2018/4/2.
 */

public class isApplication extends Application {
    private static isApplication instance;
    public static SharedPreferences sp;
    public static String user_id = "";//可以通过这个判断用户是否是登陆状态
    public static boolean FACTORY_LOCK = false;//可以通过这个判断用户是否可以安装卸载软件
    public static boolean RUNNING3 = false;

    public static int currentAge = 25;//当前年龄的默认值
    public static int indexAge = 24;//当前年龄的默认下标
    public static int currentHeight = 175;//公制当前身高的默认值
    public static int currentWeight = 70;//公制当前体重的默认值
    public static int indexHeight = 75;//公制当前身高的默认下标
    public static int indexWeight = 50;//公制当前体重的默认下标
    public static String user_Name;//用戶名
    public static int user_Sex = 1;//当前性别的默认值  1为男生 2为女生
    public static long Start_SystemTime = 0;//跑步开始的时间戳 用于上传服务器
    public static long End_SystemTime = 0;//跑步结束的时间戳 用于上传服务器
    public static int radix = 1;//进制

    public static int sb_currentHeight = 39;//英制当前身高的默认值
    public static int sb_currentWeight = 44;//英制当前体重的默认值
    public static int sb_indexHeight = 0;//英制当前身高的默认下标
    public static int sb_indexWeight = 0;//英制当前体重的默认下标

    public static int ph_selector = 0;//1 kph 2mph
    private static SharedPreferences sp_loadPH;
    private static File spfile;
    private String PH;

    private static SharedPreferences sp_User;//读取用户信息
    public static SharedPreferences.Editor editor_user;

    public static SharedPreferences sp_RadixUser1;//公制模式身高体重
    public static SharedPreferences.Editor editor_RadixUser1;

    public static SharedPreferences sp_RadixUser2;//英制制模式身高体重
    public static SharedPreferences.Editor editor_RadixUser2;
//    public static boolean first = false;//判斷是不是第一次进来

    public static isApplication getInstance() {
        return instance;
    }

    public static int newPositionFragment, oldPositionFragment;
    public static int OLD_RADIX;//旧的进制

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashReport.initCrashReport(getApplicationContext(), "3611a7c516", false);//bugly
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        init();
    }

    private void init() {
        sp_User = getInstance().getApplicationContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        editor_user = sp_User.edit();
        sp_RadixUser1 = getInstance().getApplicationContext().getSharedPreferences("sp_RadixUser1", Context.MODE_PRIVATE);
        editor_RadixUser1 = sp_RadixUser1.edit();
        sp_RadixUser2 = getInstance().getApplicationContext().getSharedPreferences("sp_RadixUser2", Context.MODE_PRIVATE);
        editor_RadixUser2 = sp_RadixUser2.edit();
        user_Name = getInstance().getString(R.string.NotSet2);
        UserData("user_name");
        UserData("user_sex");
        UserData("user_age");
//        UserData("user_height");
//        UserData("user_weight");
        UserData("user_index_age");
        UserData("radix");
        sp_loadPH = getApplicationContext().getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        spfile = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/sp_PH.xml");
        if (spfile.exists()) {
            PH = sp_loadPH.getString("PH", "");
            if (PH.equals("KPH")) {
                isApplication.ph_selector = 1;
                SportData.MINSPEED = 1.0;
            } else if (PH.equals("MPH")) {
                isApplication.ph_selector = 2;
                SportData.MINSPEED = 0.6;
            }
        }
    }

    public static void initializeSerialPort(Context context) {
        new newSerial().CreateSerial(context);
    }

    public void setNewPositionFragment(int newPositionFragment) {
        this.newPositionFragment = newPositionFragment;
    }

    public void setOldPositionFragment(int oldPositionFragment) {
        this.oldPositionFragment = oldPositionFragment;
    }

    public static void UserData(String key) {//获取用户没有登录的情况下，默认的用户信息
        switch (key) {
            case "user_name":
                if (!sp_User.getString(key, "").equals("")) {
                    user_Name = sp_User.getString(key, "");
                } else {
                    editor_user.putString("user_name", String.valueOf(user_Name));
                    editor_user.commit();
                    user_Name = sp_User.getString(key, "");
                }
                Log.e("查看user_name", sp_User.getString(key, ""));
                break;
            case "user_sex":
                if (!sp_User.getString(key, "").equals("")) {
                    user_Sex = Integer.parseInt(sp_User.getString(key, ""));
                } else {
                    editor_user.putString("user_sex", String.valueOf(user_Sex));
                    editor_user.commit();
                    user_Sex = Integer.parseInt(sp_User.getString(key, ""));
                }
                Log.e("查看user_sex", sp_User.getString(key, ""));
                break;
            case "user_age":
                if (!sp_User.getString(key, "").equals("")) {
                    currentAge = Integer.parseInt(sp_User.getString(key, ""));
                } else {
                    editor_user.putString("user_age", String.valueOf(currentAge));
                    editor_user.commit();
                    currentAge = Integer.parseInt(sp_User.getString(key, ""));
                }
                Log.e("查看user_age", sp_User.getString(key, ""));
                break;
            case "user_index_age":
                if (!sp_User.getString(key, "").equals("")) {
                    indexAge = Integer.parseInt(sp_User.getString(key, ""));
                } else {
                    editor_user.putString("user_index_age", String.valueOf(indexAge));
                    editor_user.commit();
                    indexAge = Integer.parseInt(sp_User.getString(key, ""));
                }
                Log.e("查看user_index_age", sp_User.getString(key, ""));
                break;
            case "user_height":
                if (!sp_User.getString(key, "").equals("")) {
                    currentHeight = Integer.parseInt(sp_User.getString(key, ""));
                } else {
                    editor_user.putString("user_height", String.valueOf(currentHeight));
                    editor_user.commit();
                    currentHeight = Integer.parseInt(sp_User.getString(key, ""));
                }
                Log.e("查看user_height", sp_User.getString(key, ""));
                break;
            case "user_index_height":
                if (!sp_User.getString(key, "").equals("")) {
                    indexHeight = Integer.parseInt(sp_User.getString(key, ""));
                } else {
//                    first = true;
                    editor_user.putString("user_index_height", String.valueOf(indexHeight));
                    editor_user.commit();
                    indexHeight = Integer.parseInt(sp_User.getString(key, ""));
                }
                Log.e("查看user_index_height", sp_User.getString(key, ""));
                break;
            case "user_weight":
                if (!sp_User.getString(key, "").equals("")) {
                    currentWeight = Integer.parseInt(sp_User.getString(key, ""));
                } else {
                    editor_user.putString("user_weight", String.valueOf(currentWeight));
                    editor_user.commit();
                    currentWeight = Integer.parseInt(sp_User.getString(key, ""));
                }
                Log.e("查看user_weight", sp_User.getString(key, ""));
                break;
            case "user_index_weight":
                if (!sp_User.getString(key, "").equals("")) {
                    indexWeight = Integer.parseInt(sp_User.getString(key, ""));
                } else {
                    editor_user.putString("user_index_weight", String.valueOf(indexWeight));
                    editor_user.commit();
                    indexWeight = Integer.parseInt(sp_User.getString(key, ""));
                }
                Log.e("查看user_index_weight", sp_User.getString(key, ""));
                break;
            case "radix":
                if (!sp_User.getString(key, "").equals("")) {
                    radix = Integer.parseInt(sp_User.getString(key, ""));
                    SportData.RADIX = radix;
                    OLD_RADIX = radix;
                    radixUserInfo("user_height", radix);
                    radixUserInfo("user_weight", radix);
                } else {
                    editor_user.putString("radix", String.valueOf(radix));
                    editor_user.commit();
                    radix = Integer.parseInt(sp_User.getString(key, ""));
                    OLD_RADIX = radix;
                    radixUserInfo("user_height", radix);
                    radixUserInfo("user_weight", radix);
                }
                Log.e("查看radix", sp_User.getString(key, ""));
                break;
        }
    }

    public static void radixUserInfo(String key, int radix) {//公英制采用兩個文件保存
        Log.e("查看身高体重下标111",isApplication.indexHeight + "  " + isApplication.indexWeight);
        if (radix == 1) {//如果公制
            if (key.equals("user_height")) {//獲取公制身高和下標
                if (!sp_RadixUser1.getString(key, "").equals("")) {//如果不爲空獲取
                    currentHeight = Integer.parseInt(sp_RadixUser1.getString(key, ""));
                } else {//爲空創建獲取
                    editor_RadixUser1.putString("user_height", String.valueOf(currentHeight));
                    editor_RadixUser1.commit();
                    currentHeight = Integer.parseInt(sp_RadixUser1.getString(key, ""));
                }
                if (!sp_RadixUser1.getString("user_index_height", "").equals("")) {//如果不爲空獲取下標
                    indexHeight = Integer.parseInt(sp_RadixUser1.getString("user_index_height", ""));
                } else {//爲空創建獲取下標
                    editor_RadixUser1.putString("user_index_height", String.valueOf(indexHeight));
                    editor_RadixUser1.commit();
                    indexHeight = Integer.parseInt(sp_RadixUser1.getString("user_index_height", ""));
                }
            } else if (key.equals("user_weight")) {//獲取公制體重和下標
                if (!sp_RadixUser1.getString(key, "").equals("")) {//如果不爲空獲取
                    currentWeight = Integer.parseInt(sp_RadixUser1.getString(key, ""));
                } else {//爲空創建獲取
                    editor_RadixUser1.putString("user_weight", String.valueOf(currentWeight));
                    editor_RadixUser1.commit();
                    currentWeight = Integer.parseInt(sp_RadixUser1.getString(key, ""));
                }
                if (!sp_RadixUser1.getString("user_index_weight", "").equals("")) {//如果體重不爲空獲取下標
                    indexWeight = Integer.parseInt(sp_RadixUser1.getString("user_index_weight", ""));
                } else {//體重下標爲空創建獲取下標
                    editor_RadixUser1.putString("user_index_weight", String.valueOf(indexWeight));
                    editor_RadixUser1.commit();
                    indexWeight = Integer.parseInt(sp_RadixUser1.getString("user_index_weight", ""));
                }
            }
        } else if (radix == 2) {
            if (key.equals("user_height")) {//獲取英制身高和下標
                if (!sp_RadixUser2.getString(key, "").equals("")) {//如果不爲空獲取
                    sb_currentHeight = Integer.parseInt(sp_RadixUser2.getString(key, ""));
                } else {//爲空創建獲取
                    editor_RadixUser2.putString("user_height", String.valueOf(sb_currentHeight));
                    editor_RadixUser2.commit();
                    sb_currentHeight = Integer.parseInt(sp_RadixUser2.getString(key, ""));
                }
                if (!sp_RadixUser2.getString("user_index_height", "").equals("")) {//如果不爲空獲取下標
                    Log.e("isApplication.sb_indexHeight1", String.valueOf(isApplication.sb_indexHeight));
                    sb_indexHeight = Integer.parseInt(sp_RadixUser2.getString("user_index_height", ""));
                    Log.e("isApplication.sb_indexHeight2", String.valueOf(isApplication.sb_indexHeight));
                } else {//爲空創建獲取下標
                    editor_RadixUser2.putString("user_index_height", String.valueOf(sb_indexHeight));
                    editor_RadixUser2.commit();
                    sb_indexHeight = Integer.parseInt(sp_RadixUser2.getString("user_index_height", ""));
                }
            } else if (key.equals("user_weight")) {//獲取英制體重和下標
                if (!sp_RadixUser2.getString(key, "").equals("")) {//如果不爲空獲取
                    sb_currentWeight = Integer.parseInt(sp_RadixUser2.getString(key, ""));
                } else {//爲空創建獲取
                    editor_RadixUser2.putString("user_weight", String.valueOf(sb_currentWeight));
                    editor_RadixUser2.commit();
                    sb_currentWeight = Integer.parseInt(sp_RadixUser2.getString(key, ""));
                }
                if (!sp_RadixUser2.getString("user_index_weight", "").equals("")) {//如果體重不爲空獲取下標
                    sb_indexWeight = Integer.parseInt(sp_RadixUser2.getString("user_index_weight", ""));
                } else {//體重下標爲空創建獲取下標
                    editor_RadixUser2.putString("user_index_weight", String.valueOf(sb_indexWeight));
                    editor_RadixUser2.commit();
                    sb_indexWeight = Integer.parseInt(sp_RadixUser2.getString("user_index_weight", ""));
                }
            }
        }
    }

    private static MediaPlayer imp;
    private static int oldPath;

    public static void setMediaPlayer(final Context context, final int path) {
        if (path == oldPath && imp != null && imp.isPlaying()) {
            oldPath = path;
            return;
        }
        if (oldPath != path && imp != null) {
            imp.pause();
            imp.release();
            imp = null;
            imp = MediaPlayer.create(context, path);
        }
        oldPath = path;
        imp = MediaPlayer.create(context, path);
        imp.start();
        imp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                imp = null;
                imp = MediaPlayer.create(context, path);
            }
        });
    }

}
