package com.vigorchip.omatreadmill.utils;

import android.app.backup.BackupManager;
import android.content.res.Configuration;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * TODO<更新系统语言>
 * 
 * @author Xiho
 * @versionCode 1 <每次修改提交前+1>
 */
@SuppressWarnings("unchecked")
public class LanguageUtils {
  public static void updateLanguage(Locale locale) {
    try {
      Object objIActMag, objActMagNative;
      Class clzIActMag = Class.forName("android.app.IActivityManager");
      Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
      //amn = ActivityManagerNative.getDefault(); 
      Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
      objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
       // objIActMag = amn.getConfiguration(); 
      Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
      Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
      // set the locale to the new value 
      config.locale = locale;
      //持久化  config.userSetLocale = true; 
      Class clzConfig = Class.forName("android.content.res.Configuration");
      java.lang.reflect.Field userSetLocale = clzConfig.getField("userSetLocale");
      userSetLocale.set(config, true);
      // 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
      // 会重新调用 onCreate();
      Class[] clzParams = { Configuration.class };
      // objIActMag.updateConfiguration(config);
      Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod("updateConfiguration", clzParams);
      mtdIActMag$updateConfiguration.invoke(objIActMag, config);
      BackupManager.dataChanged("com.android.providers.settings");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
 
}