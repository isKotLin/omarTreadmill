package com.vigorchip.omatreadmill.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by wr-app1 on 2018/4/4.
 * 屏幕亮度的工具类
 */

public class BrightnessUtils {

    public static int getScreenBrightness(Context context) {//获得当前屏幕亮度值  0--255
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }

    public static int getScreenMode(Context context){//获得当前屏幕亮度的模式  1 为自动调节屏幕亮度 0  为手动调节屏幕亮度
        int screenMode=0;
        try{
            screenMode =Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        }
        catch (Exception localException){

        }
        return screenMode;
    }

    public static void setScreenMode(Context context, int paramInt){//设置当前屏幕亮度的模式  1 为自动调节屏幕亮度   0  为手动调节屏幕亮度
        try{
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        }catch (Exception localException){
            localException.printStackTrace();
        }
    }

    public static void saveScreenBrightness(Context context, int paramInt) {//设置当前屏幕亮度值  0--255
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

}
