package com.vigorchip.omatreadmill.bean;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.server.ServerWindows;

import java.math.BigDecimal;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by wr-app1 on 2018/4/18.
 */

public class SportData {
    public static final int MAIN_ACTIVITY = 0;//主页面
    public static final int RUNNING_MODE_MANUAL = 11;//快速模式
    public static final int RUNNING_MODE_TIME = 12;//时间模式
    public static final int RUNNING_MODE_DISTANCE = 13;//距离模式
    public static final int RUNNING_MODE_CALORIES = 14;//热量模式
    public static final int RUNNING_MODE_LIVE = 15;//实景模式
    public static final int RUNNING_MODE_WARMUP = 16;//热身运动
    public static final int RUNNING_MODE_CLIMB = 17;//爬山运动
    public static final int RUNNING_MODE_AEROBIC = 18;//有氧运动
    public static final int RUNNING_MODE_HEALTH = 19;//保健运动
    public static final int RUNNING_MODE_LOSEWEIGHT = 20;//减肥运动
    public static final int RUNNING_MODE_MAJOR = 21;//专业运动
    public static final int RUNNING_MODE_TRAINING_USER = 22;//用户训练程序
    public static final int RUNNING_MODE_TRAINING_HEART = 23;//心率控速程序
    public static final int RUNNING_Scale_BMI = 24;//BMI训练程序

    public static final int START_TREADMILL_ANIM = 100;//跑步动画
    public static final int STOPPING_TREADMILL = 101;//运动停止中

    public static final int ERROR_STATUS = 401;//故障状态

    private static int Status;//当前状态

    public static int RADIX = 1;//1 公制 2英制
    public static double MAXSPEED;//最大速度
    public static double MINSPEED;//最小速度
    public static int MAXSLOPES;//最大坡度
    public static double Speed = 0.0;//当前速度
    public static int Slope = 0;//当前坡度
    public static double Distance = 0.0;//距离
    public static double Calories = 0.0;//卡路里

    public static double getSpeed() {//获取当前速度
        return Speed;
    }

    public static void setSpeed(double mSpeed) {//设置当前速度
        Speed = mSpeed;
        ServerWindows.service_speed.setText(String.valueOf(SportData.getSpeed()));
    }

    public static int getSlope() {//获取当前坡度
        return Slope;
    }

    public static void setSlope(int mSlope) {//设置当前坡度
        SportData.Slope = mSlope;
        ServerWindows.service_slope.setText(String.valueOf(SportData.getSlope()));
    }

    public static double getDistance() {
        return Distance;
    }

    public static void setDistance(double speed, int flag) {//设置距离
        Distance = Distance + speed / 3600.0;
        if (flag == 0) {
            Distance = 0.0;
        }
    }

    public static double getCalories() {//设置卡路里
        if (isApplication.ph_selector == 1) {
            double weight = 0;
            if (SportData.RADIX == 1) {
                weight = isApplication.currentWeight;
            } else if (SportData.RADIX == 2) {
                weight = new BigDecimal(isApplication.sb_currentWeight / 2.2).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
            }
            if (getSpeed() * 0.6213711 <= 3.7) {// * 4.3495984
                Calories += ((1 + (0.768 * getSpeed() * 0.6213712) + (0.137 * getSpeed() * 0.6213712 * (getSlope() + 1))) * weight / 3600);
            } else if (getSpeed() * 0.6213711 > 3.7) {
                Calories += ((1 + (1.532 * getSpeed() * 0.6213712) + (0.0685 * getSpeed() * 0.6213712 * (getSlope() + 1))) * weight / 3600);
            }
//            Log.e("查看卡路里A", String.valueOf(Calories));
//            Log.e("查看体重A", String.valueOf(weight));
        } else if (isApplication.ph_selector == 2) {
            double weight = 0;
            if (SportData.RADIX == 1) {
                weight = new BigDecimal(isApplication.currentWeight * 2.2).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
            } else if (SportData.RADIX == 2) {
                weight = isApplication.sb_currentWeight;
            }
            if (getSpeed() <= 3.7) {// * 4.3495984
                Calories += ((1 + (0.768 * getSpeed()) + (0.137 * getSpeed() * (getSlope()) + 1)) * weight / 7900);
            } else if (getSpeed() > 3.7) {
                Calories += ((1 + (1.532 * getSpeed()) + (0.0685 * getSpeed() * (getSlope() + 1))) * weight / 7900);
            }
//            Log.e("查看体重B", String.valueOf(weight));
//            Log.e("查看卡路里B", String.valueOf(Calories));
        }
        return Calories;
    }

    public static void setCalories(double calories) {
        Calories = calories;
    }

    public static int getStatus() {//获取当前跑步机状态
        return Status;
    }

    public static void setStatus(int status) {
        Status = status;
    }

    public static boolean isRunning() {//跑步状态
        if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL
                || SportData.getStatus() == SportData.RUNNING_MODE_TIME
                || SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE
                || SportData.getStatus() == SportData.RUNNING_MODE_CALORIES
                || SportData.getStatus() == SportData.RUNNING_MODE_LIVE
                || SportData.getStatus() == SportData.RUNNING_MODE_WARMUP
                || SportData.getStatus() == SportData.RUNNING_MODE_CLIMB
                || SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC
                || SportData.getStatus() == SportData.RUNNING_MODE_HEALTH
                || SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT
                || SportData.getStatus() == SportData.RUNNING_MODE_MAJOR
                || SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_USER
                || SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART
                || SportData.getStatus() == SportData.RUNNING_Scale_BMI) {
            return true;
        } else {
            return false;
        }
    }

    private static String indexMode;

    public static String IndexSportMode(int getMode) {
        if (getMode == SportData.RUNNING_MODE_MANUAL) {
            indexMode = getApplicationContext().getString(R.string.main_mode_quick);
        } else if (getMode == SportData.RUNNING_MODE_TIME) {
            indexMode = getApplicationContext().getString(R.string.main_mode_time);
        } else if (getMode == SportData.RUNNING_MODE_DISTANCE) {
            indexMode = getApplicationContext().getString(R.string.main_mode_distance);
        } else if (getMode == SportData.RUNNING_MODE_CALORIES) {
            indexMode = getApplicationContext().getString(R.string.main_mode_calories);
        } else if (getMode == SportData.RUNNING_MODE_LIVE) {
            indexMode = getApplicationContext().getString(R.string.main_mode_live);
        } else if (getMode == SportData.RUNNING_MODE_WARMUP) {
            indexMode = String.valueOf(getApplicationContext().getString(R.string.Warm))
                    + String.valueOf(getApplicationContext().getString(R.string.Up));
        } else if (getMode == SportData.RUNNING_MODE_CLIMB) {
            indexMode = getApplicationContext().getString(R.string.Climb);
        } else if (getMode == SportData.RUNNING_MODE_AEROBIC) {
            indexMode = getApplicationContext().getString(R.string.Oxygen);
        } else if (getMode == SportData.RUNNING_MODE_HEALTH) {
            indexMode = getApplicationContext().getString(R.string.Health);
        } else if (getMode == SportData.RUNNING_MODE_LOSEWEIGHT) {
            indexMode = getApplicationContext().getString(R.string.Slim);
        } else if (getMode == SportData.RUNNING_MODE_MAJOR) {
            indexMode = getApplicationContext().getString(R.string.Expert);
        } else if (getMode == SportData.RUNNING_MODE_TRAINING_USER) {
            indexMode = getApplicationContext().getString(R.string.Customization);
        } else if (getMode == SportData.RUNNING_MODE_TRAINING_HEART) {
            indexMode = getApplicationContext().getString(R.string.HRC);
        }
        return indexMode;
    }

    public static boolean isMainRunning() {//主页面运动模式
        if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL
                || SportData.getStatus() == SportData.RUNNING_MODE_TIME
                || SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE
                || SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTrainRunning() {//所有的训练模式
        if (SportData.getStatus() == SportData.RUNNING_MODE_WARMUP
                || SportData.getStatus() == SportData.RUNNING_MODE_CLIMB
                || SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC
                || SportData.getStatus() == SportData.RUNNING_MODE_HEALTH
                || SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT
                || SportData.getStatus() == SportData.RUNNING_MODE_MAJOR
                || SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_USER) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDefultTrainRunning() {//预设训练模式
        if (SportData.getStatus() == SportData.RUNNING_MODE_WARMUP
                || SportData.getStatus() == SportData.RUNNING_MODE_CLIMB
                || SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC
                || SportData.getStatus() == SportData.RUNNING_MODE_HEALTH
                || SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT
                || SportData.getStatus() == SportData.RUNNING_MODE_MAJOR) {
            return true;
        } else {
            return false;
        }
    }


//    public static boolean isStandby() {
//        if (SportData.getStatus() == SportData.USER_INFO
//                ||SportData.getStatus() == SportData.USER_MEDIA)
//    }


}
