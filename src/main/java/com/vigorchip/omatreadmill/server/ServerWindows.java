package com.vigorchip.omatreadmill.server;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.ServiceToResult;
import com.vigorchip.omatreadmill.eventbus.setHomeKey;
import com.vigorchip.omatreadmill.eventbus.setMainClickVisibility;
import com.vigorchip.omatreadmill.eventbus.setPH;
import com.vigorchip.omatreadmill.eventbus.setPlayGroundthumb;
import com.vigorchip.omatreadmill.eventbus.setStop;
import com.vigorchip.omatreadmill.fragment.Fragment_Running4;
import com.vigorchip.omatreadmill.interfaci.setSportText;
import com.vigorchip.omatreadmill.interfaci.setSwipeRightAction;
import com.vigorchip.omatreadmill.serialport.newSerial;
import com.vigorchip.omatreadmill.utils.AnimateUtils;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.DensityUtils;
import com.vigorchip.omatreadmill.utils.DoubleUtils;
import com.vigorchip.omatreadmill.utils.TimeUtils;
import com.vigorchip.omatreadmill.utils.WakeLockUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by wr-app1 on 2018/3/27.
 */

public class ServerWindows extends Service implements View.OnClickListener {
    private WindowManager windowManager;
    private AudioManager audioManager;
    private WindowManager.LayoutParams layoutParams;
    private static LinearLayout server_windows, windows;
    private RelativeLayout serviceAdj_speed, serviceAdj_slope;
    private CreatDialog creatDialog;
    public static RelativeLayout steps_layout;
    private ImageView volum_up, Home, Back;
    private TextView tv_top_distance, tv_top_speed;
    public static TextView tv_RunningTime, service_speed, service_slope, service_distance, service_calories, service_hrc, service_steps;
    private SeekBar Seekbar_Volum;//seekbar音量
    private int maxVolume;//最大音量
    private int currentVolume;//当前音量
    private SharedPreferences sp_savePH;
    private SharedPreferences sp_loadPH;
    private SharedPreferences.Editor editor;
    private File spfile = null;
    float lastY;//点击是Y坐标
    boolean isClick;//是否点击数据栏

    public static Timer CountUp_Timer = null;//正计时器
    public static String strCountUpTime;//正计时显示在TextView的时间
    public static TimerTask CountUp_TimerTask = null;//正计时线程
    public static long CountUp_baseTimer;//正计时需要被减去的时间
    public static long CountUpMathTime;//正计时第一次没被暂停过的时间计算结果
    public static long CountUpMathTime_Pause;//正计时被暂停过的时间计算结果保存

    public static Timer CountDown_Timer = null;//倒计时器
    public static String strCountDownTime;//倒计时显示在TextView的时间
    public static TimerTask CountDown_TimerTask = null;//倒计时线程
    public static long CountDown_baseTimer;//倒计时需要被减去的时间
    public static long CountDownMathTime1;//倒计时第一次没被暂停过的时间计算结果
    public static long CountDownMathTime2;//倒计时被暂停过的时间计算结果
    public static long CountDownMathTime_Pause;//倒计时被暂停过的时间计算结果保存

    public static long TrainTime;//训练模式传进来的时间
    public static String strTrainTime;//转成String格式的时间
    public static long Train_i = 0;//需要被减去的自增秒数
    public static Timer Train_Timer = null;//训练模式倒计时器
    public static TimerTask Train_TimerTask = null;//训练模式倒计时线程
    public static long Train_Pause;//训练模式倒计时被暂停过的时间计算结果保存

    public static double Distance;//距离模式的总距离
    public static double residue_Distance;//距离模式的剩余距离

    public static double Calories;//卡路里模式的总距离
    public static double residue_Calories;//卡路里模式的剩余距离

    private static double pause_speed;//暂停时的速度
    private static int pause_slope;//暂停时的坡度
    public static boolean isPause = false;//用于串口判断是否暂停，如果暂停将模式设为待机
    public static int wakeTime;//进入跑步，不让系统休眠，记录上一次的休眠时间，结束后设置回来

    public static long RESULT_TIME = 0;//运动结果时间
    public static double RESULT_KM = 0;//运动结果距离
    public static double RESULT_CAL = 0;//运动结果卡路里
    public static double RESULT_AVG_SPEED = 0;//运动结果平均速度
    public static double MAX_SPEED = 0;//获取最大速度
    public static double RESULT_MAX_SPEED = 0;//获取运动结果最大速度
    public static double RESULT_PACE = 0;//运动结果配速
    public static double RESULT_AVG_HRC = 0.0;//运动结果平均心率
    public static boolean FLAG_GETMAXSPEED = true;
    public static int FLAG_KMRESULT = 0;
    public static int FLAG_CALRESULT = 0;

    public static int check_hrc = 0;//判斷心率模式两个30秒有没有檢測心率
    public static int check_stop = 0;//判断是否是进入15秒后停机状态
    public static int check_minSpeed = 0;//是否是最低速运行
    public static int CHECK_NULL = 0;//判斷心率模式多久沒檢測到就退出跑步
//    public static int CHECK_HRC2 = 0;//使用者心率低于目标心率30次/分钟，则速度增加2.0km/h
    public static int CHECK_HRC3 = 0;//使用者心率低于目标心率6-29次/分钟，则速度增加1.0km/h
//    public static int CHECK_HRC4 = 0;//使用者心率高于目标心率30次/分钟，则速度降低2.0km/h
//    public static int CHECK_HRC5 = 0;//使用者心率高于目标心率6-29次/分钟，则速度降低1.0km/h

//    public static int CHECK_HRC6 = 0;//心率模式沒檢測到，以最低速度运行15秒后停机
//    public static int CHECK_HRC7 = 0;//最低速运行，心率导致减速低于最低速,以最低速度运行15秒后停机
//    public static int CHECK_HRC8 = 0;//心率超过220-年龄,以最低速度运行15秒后停机

    public static setSportText sportText;

    public static void setSportTime(setSportText text) {
        sportText = text;
    }

    private static Handler Handler_CountUp = new Handler() {//正计时线程
        public void handleMessage(android.os.Message msg) {
//            Log.i(">>>>>>>>>>正计时开始方法走了", String.valueOf(msg.what));
            switch (msg.what) {
                case SportData.RUNNING_MODE_MANUAL://快速模式
//                    Log.e("快速有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    sportText.OnSportText((String) msg.obj, SportData.RUNNING_MODE_MANUAL);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    EventBus.getDefault().postSticky(new setPlayGroundthumb(SportData.getDistance()));//跑道圆点动画刷新
                    break;
                case SportData.RUNNING_MODE_CALORIES://卡路里模式
//                    Log.e("卡路里模式有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    residue_Calories = DoubleUtils.sub(new BigDecimal(Calories).setScale(1, BigDecimal.ROUND_DOWN).doubleValue(),
                            new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue());
                    RESULT_CAL = new BigDecimal(Calories - residue_Calories % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();//一共跑了多少卡路里
                    service_calories.setText(String.valueOf(residue_Calories));
                    if (Calories - SportData.getCalories() % 1000 <= 0) {
                        service_calories.setText(String.valueOf("0.0"));
                        RESULT_CAL = Calories;
                        Log.e("谁发true了", String.valueOf("卡路里"));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        sportText.OnSportText("0.0cal", SportData.RUNNING_MODE_CALORIES);
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_CALORIES));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    EventBus.getDefault().postSticky(new setPlayGroundthumb(SportData.getDistance()));//跑道圆点动画刷新
                    sportText.OnSportText(String.valueOf(residue_Calories) + "cal", SportData.RUNNING_MODE_CALORIES);
                    break;
                case SportData.RUNNING_MODE_DISTANCE://距离模式
//                    Log.e("距离模式有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    residue_Distance = DoubleUtils.sub(new BigDecimal(Distance).setScale(1, BigDecimal.ROUND_DOWN).doubleValue(),
                            new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue());
                    RESULT_KM = DoubleUtils.sub(new BigDecimal(Distance).setScale(1, BigDecimal.ROUND_DOWN).doubleValue(),
                            new BigDecimal(residue_Distance).setScale(1, BigDecimal.ROUND_DOWN).doubleValue());//一共跑了多少公里
                    service_distance.setText(String.valueOf(residue_Distance));
                    EventBus.getDefault().postSticky(new setPlayGroundthumb(SportData.getDistance()));//跑道圆点动画刷新
                    sportText.OnSportText(String.valueOf(residue_Distance) + "km", SportData.RUNNING_MODE_DISTANCE);
                    if (residue_Distance <= 0.0) {
                        service_distance.setText(String.valueOf("0.0km"));
                        Log.e("谁发true了", String.valueOf("距离" + residue_Distance));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        sportText.OnSportText("0.0km", SportData.RUNNING_MODE_DISTANCE);
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_DISTANCE));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_LIVE:
//                    Log.e("实景模式模式有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    break;
            }
        }
    };

    private static Handler Handler_CountDown = new Handler() {//倒计时线程
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SportData.RUNNING_MODE_TIME://时间模式
//                    Log.e("时间模式有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    sportText.OnSportText((String) msg.obj, SportData.RUNNING_MODE_TIME);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    EventBus.getDefault().postSticky(new setPlayGroundthumb(SportData.getDistance()));//跑道圆点动画刷新
                    Log.i("查看时间模式结束时间", tv_RunningTime.getText() + "----" + tv_RunningTime.getText().equals("00:00:00"));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        Log.e("谁发true了", String.valueOf("时间"));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        Log.i("查看时间模式结束时间", tv_RunningTime.getText() + "----" + tv_RunningTime.getText().equals("00:00:00"));
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TIME));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_WARMUP://热身运动
//                    Log.e("热身运动有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    Log.i(">>>>>>>>>>>查看时间", (String) tv_RunningTime.getText());
                    Log.i(">>>>>>>>>>>查看距离", String.valueOf(SportData.getDistance()));
                    Log.i(">>>>>>>>>>>查看速度", String.valueOf(SportData.getSpeed()));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        Log.e("谁发true了", String.valueOf("热身"));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_WARMUP));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                        Log.i(">>>>>>>>>>>>>>>>服务发送停止", "");
                    }
                    break;
                case SportData.RUNNING_MODE_CLIMB://爬山运动
//                    Log.e("爬山运动有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_CLIMB));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_AEROBIC://有氧运动
//                    Log.e("有氧运动有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        Log.e("谁发true了", String.valueOf("有氧"));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_AEROBIC));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_HEALTH://保健运动
//                    Log.e("保健运动有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        Log.e("谁发true了", String.valueOf("保健"));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_HEALTH));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_LOSEWEIGHT://减肥运动
//                    Log.e("减肥运动有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        Log.e("谁发true了", String.valueOf("减肥"));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_LOSEWEIGHT));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_MAJOR://专业运动
//                    Log.e("专业运动有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_MAJOR));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_TRAINING_USER://用户预设模式
//                    Log.e("用户预设模式有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    if (tv_RunningTime.getText().equals("00:00:00")) {
                        Log.e("谁发true了", String.valueOf("用户预设模式"));
                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_USER));//提供给service倒计时结束调到二维码页面
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                    }
                    break;
                case SportData.RUNNING_MODE_TRAINING_HEART:
                    tv_RunningTime.setText((String) msg.obj);
                    SportData.setDistance(SportData.getSpeed(), 1);//1为标志位
                    RESULT_CAL = new BigDecimal(SportData.getCalories() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_calories.setText(String.valueOf(RESULT_CAL));
                    RESULT_KM = new BigDecimal(SportData.getDistance() % 1000).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    service_distance.setText(String.valueOf(RESULT_KM));
                    int T = Fragment_Running4.TARGET_HEART;//目标心率
                    int U = newSerial.heart;//用户心率
                    if (RESULT_TIME >= 60) {//大于1分钟再开始执行心率逻辑

                        check_hrc = check_hrc + 1;

                        if (U == 0) {//如果没有检测到心率
                            Log.e("心率计算进入没有检测到心率", String.valueOf(check_hrc));
                            if (check_hrc == 31) {//连续30次
                                check_hrc = 0;
                                if (CHECK_NULL != 2) {
                                    CHECK_NULL = CHECK_NULL + 1;//两个30次
                                    Log.e("心率计算进入无心率第"+ CHECK_NULL +"次", String.valueOf("-----"));
                                }
                            }

                        } else if (U != 0) {//如果用户心率不等于0
                            Log.e("心率计算进入心率不为0", String.valueOf(check_hrc));
                            CHECK_NULL = 0;
                            if (CHECK_HRC3 == 0) {
                                CHECK_HRC3 = 1;
                            }
                            if (check_hrc == 31 || CHECK_HRC3 == 1) {//30次检测一次心率
                                if (CHECK_HRC3 != 1) {
                                    check_hrc = 0;
                                }
                                CHECK_HRC3 = 2;
                                Log.e("心率计算进入30次检测一次心率", String.valueOf(check_hrc));
                                Log.e("心率计算进入低于目标心率30，加速2km/h", String.valueOf(U <= (T - 30)));
                                Log.e("心率计算进入低于目标心率6-29，加速1km/h", String.valueOf((U < (T - 6)) && (U > (T - 29))));
                                Log.e("心率计算进入高于目标心率30，减速2km/h", String.valueOf(U >= (T + 30)));
                                Log.e("心率计算进入高于目标心率6-29，减速1km/h", String.valueOf((U > (T + 6)) && (U < (T + 29))));
                                if (U <= (T - 30)) {//如果低于目标心率30，加速2km/h
                                    if (isApplication.ph_selector == 1) {//如果km/h 就+2
                                        if ((SportData.getSpeed() + 2) <= SportData.MAXSPEED) {
                                            SportData.setSpeed(SportData.getSpeed() + 2);
                                        }
                                    } else if (isApplication.ph_selector == 2) {//如果mph 就+1.2
                                        if ((SportData.getSpeed() + 1.2) <= SportData.MAXSPEED) {
                                            SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 1.2));
                                        }
                                    }
                                } else if ((U < (T - 6)) && (U > (T - 29))) {//如果低于目标心率6-29，加速1km/h
                                    if (isApplication.ph_selector == 1) {//如果km/h 就+1
                                        if ((SportData.getSpeed() + 1) <= SportData.MAXSPEED) {
                                            SportData.setSpeed(SportData.getSpeed() + 1);
                                        }
                                    } else if (isApplication.ph_selector == 2) {//如果mph 就+0.6
                                        if ((SportData.getSpeed() + 0.6) <= SportData.MAXSPEED) {
                                            SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.6));
                                        }
                                    }
                                } else if (U >= (T + 30)) {//如果高于目标心率30，减速2km/h
                                    if (isApplication.ph_selector == 1) {//如果km/h 就-2
                                        if ((SportData.getSpeed() - 2) >= SportData.MINSPEED) {
                                            SportData.setSpeed(SportData.getSpeed() - 1);
                                        }
                                    } else if (isApplication.ph_selector == 2) {//如果mph 就-1.2
                                        if ((SportData.getSpeed() - 1.2) >= SportData.MINSPEED) {
                                            SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 1.2));
                                        }
                                    }
                                    if (SportData.getSpeed() == SportData.MINSPEED) {
                                        if (check_minSpeed != 2) {
                                            check_minSpeed = check_minSpeed + 1;
                                        }
                                    }
                                } else if ((U > (T + 6)) && (U < (T + 29))) {//如果高于目标心率6-29，减速1km/h
                                    if (isApplication.ph_selector == 1) {//如果km/h 就-1
                                        if ((SportData.getSpeed() - 1) >= SportData.MINSPEED) {
                                            SportData.setSpeed(SportData.getSpeed() - 1);
                                        }
                                    } else if (isApplication.ph_selector == 2) {//如果mph 就-0.6
                                        if ((SportData.getSpeed() - 0.6) >= SportData.MINSPEED) {
                                            SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.6));
                                        }
                                    }
                                    if (SportData.getSpeed() == SportData.MINSPEED) {
                                        check_minSpeed = check_minSpeed + 1;
                                    }
                                }


                            }
                        }

                        if (CHECK_NULL == 2) {//否则如果两次没有检测到心率，以最低速运行15秒后停机
                            check_stop = check_stop + 1;
                            Log.e("心率计算进入连续两次无心率", String.valueOf(check_stop));
                            SportData.setSpeed(SportData.MINSPEED);
                            if (check_stop == 15) {
                                CHECK_NULL = 0;
                                check_stop = 0;
                                check_hrc = 0;
                                EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));//提供给service倒计时结束调到二维码页面
                                EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                            }
                        } else if (check_minSpeed == 2) {//如果速度已经是最低了，不能再减速了
                            check_stop = check_stop + 1;
                            SportData.setSpeed(SportData.MINSPEED);
                            if (check_stop == 16) {
                                check_minSpeed = 0;
                                check_stop = 0;
                                check_hrc = 0;
                                EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));//提供给service倒计时结束调到二维码页面
                                EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                            }
                        } else if (U > (220 - Fragment_Running4.TARGET_AGE)) {
                            check_stop = check_stop + 1;
                            if (check_stop == 16) {
                                check_stop = 0;
                                check_hrc = 0;
                                EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
                                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));//提供给service倒计时结束调到二维码页面
                                EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
                            }
                        }

                    }

//                    if (newSerial.heart == 0) {//沒有檢測到心率
//                        CHECK_HRC1 = CHECK_HRC1 + 1;//心率判斷時間計時
//                        Log.e("正在进入60秒未检测心率的判断", String.valueOf(CHECK_HRC1));
//                    } else {//檢測到心率
//                        CHECK_HRC1 = 0;//不計時
//                        check_hrc = 0;
//                        Log.e("正在进入60秒检测到心率的判断，且不计时", String.valueOf(CHECK_HRC1));
//                    }
//                    if (newSerial.heart != 0) {//如果当前心率不等于0
//                        if (newSerial.heart < Fragment_Running4.TARGET_HEART) {
//                            if (CHECK_HRC2 == 30) {//使用者心率低于目标心率30次/分钟，则速度增加2.0km/h
//                                Log.e("使用者心率低于目标心率30次/分钟，则速度增加2.0km/h", String.valueOf(CHECK_HRC2));
//                                if (isApplication.ph_selector == 1) {//如果km/h 就+2
//                                    if (RESULT_TIME >= 60) {
//                                        if ((SportData.getSpeed() + 2) <= SportData.MAXSPEED) {
//                                            SportData.setSpeed(SportData.getSpeed() + 2);
//                                        }
//                                    } else if (isApplication.ph_selector == 2) {//如果mph 就+1.2
//                                        if ((SportData.getSpeed() + 1.2) <= SportData.MAXSPEED) {
//                                            SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 1.2));
//                                        }
//                                    }
//                                }
//                                CHECK_HRC2 = 0;
//                            } else {
//                                CHECK_HRC2 = CHECK_HRC2 + 1;
//                            }
//                        }
//                        if (newSerial.heart < Fragment_Running4.TARGET_HEART) {
//                            if (CHECK_HRC3 > 6 && CHECK_HRC3 < 29) {//使用者心率低于目标心率6-29次/分钟，则速度增加1.0km/h
//                                Log.e("使用者心率低于目标心率6-29次/分钟，则速度增加1.0km/h", String.valueOf(CHECK_HRC3));
//                                if (isApplication.ph_selector == 1) {//如果km/h 就+1
//                                    if (RESULT_TIME >= 60) {
//                                        if ((SportData.getSpeed() + 1) <= SportData.MAXSPEED) {
//                                            SportData.setSpeed(SportData.getSpeed() + 1);
//                                        }
//                                    } else if (isApplication.ph_selector == 2) {//如果mph 就+0.6
//                                        if ((SportData.getSpeed() + 0.6) <= SportData.MAXSPEED) {
//                                            SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.6));
//                                        }
//                                    }
//                                }
//                                CHECK_HRC3 = 0;
//                            } else {
//                                CHECK_HRC3 = CHECK_HRC3 + 1;
//                            }
//                        }
//                        if (newSerial.heart > Fragment_Running4.TARGET_HEART) {
//                            if (CHECK_HRC4 == 30) {//使用者心率高于目标心率30次/分钟，则速度降低2.0km/h
//                                if (isApplication.ph_selector == 1) {//如果km/h 就-2
//                                    if (RESULT_TIME >= 60) {
//                                        if ((SportData.getSpeed() - 2) >= SportData.MINSPEED) {
//                                            SportData.setSpeed(SportData.getSpeed() - 1);
//                                        }
//                                    } else if (isApplication.ph_selector == 2) {//如果mph 就-1.2
//                                        if ((SportData.getSpeed() - 1.2) >= SportData.MINSPEED) {
//                                            SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 1.2));
//                                        }
//                                    }
//                                }
//                                CHECK_HRC4 = 0;
//                            } else {
//                                CHECK_HRC4 = CHECK_HRC4 + 1;
//                            }
//                        }
//                        if (newSerial.heart > Fragment_Running4.TARGET_HEART) {
//                            if (CHECK_HRC5 > 6 && CHECK_HRC5 < 29) {//使用者心率高于目标心率6-29次/分钟，则速度降低1.0km/h
//                                if (SportData.getSpeed() == SportData.MINSPEED) {
//                                    if ((SportData.getSpeed() - 1) < SportData.MINSPEED) {//最低速运行，心率导致减速低于最低速就退出
//                                        Log.e("最低速运行，心率导致减速低于最低速就退出", String.valueOf(CHECK_HRC7));
//                                        SportData.setSpeed(SportData.MINSPEED);
//                                        CHECK_HRC7 = CHECK_HRC7 + 1;
//                                    }
//                                }
//                                if (isApplication.ph_selector == 1) {//如果km/h 就-1
//                                    if (RESULT_TIME >= 60) {
//                                        if ((SportData.getSpeed() - 1) >= SportData.MINSPEED) {
//                                            SportData.setSpeed(SportData.getSpeed() - 1);
//                                        }
//                                    } else if (isApplication.ph_selector == 2) {//如果mph 就-0.6
//                                        if ((SportData.getSpeed() - 0.6) >= SportData.MINSPEED) {
//                                            SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.6));
//                                        }
//                                    }
//                                }
//                                CHECK_HRC5 = 0;
//                            } else {
//                                CHECK_HRC5 = CHECK_HRC5 + 1;
//                            }
//                        }
//                    } else if (newSerial.heart == 0) {//如果当前心率等于0，则将所有标志位重置
//                        CHECK_HRC2 = 0;
//                        CHECK_HRC3 = 0;
//                        CHECK_HRC4 = 0;
//                        CHECK_HRC5 = 0;
//                        CHECK_HRC7 = 0;
//                        CHECK_HRC8 = 0;
//                    }
//
//                    if (CHECK_HRC1 == 31 || CHECK_HRC1 >= 61) {//如果两个30秒沒有檢測到心率就最低速运行
//                        if (check_hrc != 2) {
//                            check_hrc = check_hrc + 1;
//                            Log.e("心率模式查看有没有到两个三十秒都没有检测到心率", String.valueOf(check_hrc));
//                        }
//                        if (check_hrc == 2) {
//                            SportData.setSpeed(SportData.MINSPEED);
//                            CHECK_HRC6 = CHECK_HRC6 + 1;
//                            Log.e("心率模式正在进入两个三十秒，15秒后停机", String.valueOf(CHECK_HRC6));
//                        }
//                    } else if (newSerial.heart > (220 - Fragment_Running4.TARGET_AGE)) {//心率超过220-年龄就退出
//                        Log.e("心率超过220-年龄就退出", String.valueOf(CHECK_HRC8));
//                        SportData.setSpeed(SportData.MINSPEED);
//                        CHECK_HRC8 = CHECK_HRC8 + 1;
//                    }
//
//                    if (CHECK_HRC7 == 16) {//最低速运行，心率导致减速低于最低速就退出
//                        Log.e("最低速运行，心率导致减速低于最低速就退出", String.valueOf(CHECK_HRC7));
//                        Log.e("谁发true了", String.valueOf("111"));
//                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
//                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));//提供给service倒计时结束调到二维码页面
//                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
//                        check_hrc = 0;
//                        CHECK_HRC1 = 0;
//                        CHECK_HRC2 = 0;
//                        CHECK_HRC3 = 0;
//                        CHECK_HRC4 = 0;
//                        CHECK_HRC5 = 0;
//                        CHECK_HRC6 = 0;
//                        CHECK_HRC7 = 0;
//                        CHECK_HRC8 = 0;
//                    } else if (CHECK_HRC8 == 16) {
//                        Log.e("心率超过220-年龄就退出", String.valueOf(CHECK_HRC8));
//                        Log.e("谁发true了", String.valueOf("222"));
//                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
//                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));//提供给service倒计时结束调到二维码页面
//                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
//                        check_hrc = 0;
//                        CHECK_HRC1 = 0;
//                        CHECK_HRC2 = 0;
//                        CHECK_HRC3 = 0;
//                        CHECK_HRC4 = 0;
//                        CHECK_HRC5 = 0;
//                        CHECK_HRC6 = 0;
//                        CHECK_HRC7 = 0;
//                        CHECK_HRC8 = 0;
//                    } else if (CHECK_HRC6 == 16) {//如果60秒沒有檢測到心率,15秒最低速运行就退出
//                        Log.e("60秒沒有檢測到心率就退出", String.valueOf(CHECK_HRC6));
//                        Log.e("谁发true了", String.valueOf("333"));
//                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
//                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));//提供给service倒计时结束调到二维码页面
//                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
//                        check_hrc = 0;
//                        CHECK_HRC1 = 0;
//                        CHECK_HRC2 = 0;
//                        CHECK_HRC3 = 0;
//                        CHECK_HRC4 = 0;
//                        CHECK_HRC5 = 0;
//                        CHECK_HRC6 = 0;
//                        CHECK_HRC7 = 0;
//                        CHECK_HRC8 = 0;
//                    } else if (tv_RunningTime.getText().equals("00:00:00")) {//否则时间倒计完毕就退出
//                        Log.e("心率时间倒计完毕就退出", String.valueOf(CHECK_HRC1));
//                        Log.e("谁发true了", String.valueOf("444"));
//                        EventBus.getDefault().postSticky(new setStop(true, false));//发送给跑步界面回初始状态
//                        EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));//提供给service倒计时结束调到二维码页面
//                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给Fragment_Running1回初始状态
//                        check_hrc = 0;
//                        CHECK_HRC1 = 0;
//                        CHECK_HRC2 = 0;
//                        CHECK_HRC3 = 0;
//                        CHECK_HRC4 = 0;
//                        CHECK_HRC5 = 0;
//                        CHECK_HRC6 = 0;
//                        CHECK_HRC7 = 0;
//                        CHECK_HRC8 = 0;
//                    }
                    if (MainActivity.heart_status == true) {
                        MainActivity.heart_status = false;
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
        if (!isAppOnForeground()) {//切换语言，app重启后回到栈顶
            setBackPressed(KeyEvent.KEYCODE_HOME);
        }
    }

    private void initialize() {
        windowManager = (WindowManager) getSystemService(getApplication().WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        layoutParams.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM; //调整悬浮窗显示位置
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        server_windows = (LinearLayout) inflater.inflate(R.layout.server_windows, null);
        initializeView();
        initializeAudio();

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                return;
            }
        } else {
            windowManager.addView(server_windows, layoutParams);
        }
        windowManager.addView(server_windows, layoutParams);

        mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        mPackageName = getPackageName();
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onPH(setPH PH) {
        if (PH.getDistancePH().equals("km")) {
            tv_top_distance.setText(getApplicationContext().getString(R.string.server_distance) + PH.getDistancePH());
            if (PH.getSpeedPH().equals("km/h")) {
                tv_top_speed.setText(getApplicationContext().getString(R.string.server_speed) + PH.getSpeedPH());
            }
        } else if (PH.getDistancePH().equals("mile")) {
            tv_top_distance.setText(getApplicationContext().getString(R.string.server_distance) + PH.getDistancePH());
            if (PH.getSpeedPH().equals("MPH")) {
                tv_top_speed.setText(getApplicationContext().getString(R.string.server_speed) + PH.getSpeedPH());
            }
        }
    }


    private void initializeView() {
        windows = server_windows.findViewById(R.id.windows);
        serviceAdj_speed = server_windows.findViewById(R.id.serviceAdj_speed);
        serviceAdj_slope = server_windows.findViewById(R.id.serviceAdj_slope);
        steps_layout = server_windows.findViewById(R.id.steps_layout);
        tv_top_distance = server_windows.findViewById(R.id.tv_top_distance);
        tv_top_speed = server_windows.findViewById(R.id.tv_top_speed);
        tv_RunningTime = server_windows.findViewById(R.id.tv_RunningTime);
        service_speed = server_windows.findViewById(R.id.service_speed);
        service_slope = server_windows.findViewById(R.id.service_slope);
        service_distance = server_windows.findViewById(R.id.service_distance);
        service_calories = server_windows.findViewById(R.id.service_calories);
        service_hrc = server_windows.findViewById(R.id.service_hrc);
        service_steps = server_windows.findViewById(R.id.service_steps);
        volum_up = server_windows.findViewById(R.id.volum_up);
        Home = server_windows.findViewById(R.id.Home);
        Back = server_windows.findViewById(R.id.Back);
        creatDialog = new CreatDialog();
        service_speed.setOnClickListener(this);
        service_slope.setOnClickListener(this);
        volum_up.setOnClickListener(this);
        Home.setOnClickListener(this);
        Back.setOnClickListener(this);
        AnimateUtils.setClickAnimation(getApplicationContext(), volum_up);
        AnimateUtils.setClickAnimation(getApplicationContext(), Home);
        AnimateUtils.setClickAnimation(getApplicationContext(), Back);
        sp_savePH = getApplicationContext().getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        spfile = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/sp_PH.xml");
        if (!spfile.exists()) {//系统第一次进入，设置默认值
            editor = sp_savePH.edit();
            editor.putString("PH", "KPH");
            editor.commit();
            tv_top_distance.setText(getApplicationContext().getString(R.string.server_distance) + "km");
            tv_top_speed.setText(getApplicationContext().getString(R.string.server_speed) + "km/h");
        }
        //从本地文件获取保存的PH状态
        sp_loadPH = getApplicationContext().getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        if (spfile.exists()) {
            if (sp_loadPH.getString("PH", "").equals("KPH")) {
                tv_top_distance.setText(getApplicationContext().getString(R.string.server_distance) + "km");
                tv_top_speed.setText(getApplicationContext().getString(R.string.server_speed) + "km/h");
            } else if (sp_loadPH.getString("PH", "").equals("MPH")) {
                tv_top_distance.setText(getApplicationContext().getString(R.string.server_distance) + "mile");
                tv_top_speed.setText(getApplicationContext().getString(R.string.server_speed) + "MPH");
            }
        } else {
            tv_top_distance.setText(getApplicationContext().getString(R.string.server_distance) + "km");
            tv_top_speed.setText(getApplicationContext().getString(R.string.server_speed) + "km/h");
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然收不到
        }
        server_windows.setVisibility(View.VISIBLE);
        server_windows.setOnTouchListener(new View.OnTouchListener() {//控制底部导航栏的滑动事件
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastY = motionEvent.getRawY();
                        isClick = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (motionEvent.getRawY() - lastY < 0) {
                            windows.setY(0);
                        } else if (motionEvent.getRawY() - lastY > windows.getHeight() / 2) {
                            windows.setY(windows.getHeight() / 2);
                        } else {
                            windows.setY(motionEvent.getRawY() - lastY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((motionEvent.getRawY() - lastY) > DensityUtils.px2dip(getApplicationContext(), 30)) {
                            animDown();
                        } else {
                            windows.setY(0);
                        }
                        break;
                }
                return false;
            }
        });
        Log.e("查看語言轉換后的字體", String.valueOf(this.getString(R.string.server_distance)));
    }

    private void initializeAudio() {
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE); // 实例化系统声音管理类
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
        audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        audioManager.setStreamMute(AudioManager.STREAM_DTMF, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(server_windows);
    }

    Handler volum_handler = new Handler();//判斷用戶松手后的綫程

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.volum_up:
                if (CreatDialog.dialog != null) {
                    if (CreatDialog.dialog.isShowing()) {
                        CreatDialog.dialog.dismiss();
                    }
                }
                if (CreatDialog.RunningDialog != null) {
                    if (CreatDialog.RunningDialog.isShowing()) {
                        CreatDialog.RunningDialog.dismiss();
                    }
                }
                if (CreatDialog.AdjSpeedDialog != null) {
                    if (CreatDialog.AdjSpeedDialog.isShowing()) {
                        CreatDialog.AdjSpeedDialog.dismiss();
                    }
                }
                if (CreatDialog.AdjSlopeDialog != null) {
                    if (CreatDialog.AdjSlopeDialog.isShowing()) {
                        CreatDialog.AdjSlopeDialog.dismiss();
                    }
                }
                if (CreatDialog.dialog != null) {
                    if (!CreatDialog.dialog.isShowing()) {
                        CreatDialog.showDialog(getApplicationContext(), R.layout.dialog_volum, true);
                        Seekbar_Volum = CreatDialog.dialog.findViewById(R.id.Seekbar_Volum);
                        setVolum();
                        Seekbar_Volum.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                switch (motionEvent.getAction()) {
                                    case MotionEvent.ACTION_UP:
                                        volum_handler.postDelayed(new Runnable() {
                                            public void run() {
                                                CreatDialog.dialog.dismiss();
                                            }
                                        }, 3000);
                                        break;
                                    case MotionEvent.ACTION_DOWN:
                                        volum_handler.removeCallbacksAndMessages(null);
                                        break;
                                }
                                return false;
                            }
                        });
                    }
                } else {
                    CreatDialog.showDialog(getApplicationContext(), R.layout.dialog_volum, true);
                    Seekbar_Volum = CreatDialog.dialog.findViewById(R.id.Seekbar_Volum);
                    setVolum();
                    Seekbar_Volum.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            switch (motionEvent.getAction()) {
                                case MotionEvent.ACTION_UP:
                                    volum_handler.postDelayed(new Runnable() {
                                        public void run() {
                                            CreatDialog.dialog.dismiss();
                                        }
                                    }, 3000);
                                    break;
                                case MotionEvent.ACTION_DOWN:
                                    volum_handler.removeCallbacksAndMessages(null);
                                    break;
                            }
                            return false;
                        }
                    });
                }
                volum_handler.postDelayed(new Runnable() {
                    public void run() {
                        CreatDialog.dialog.dismiss();
                    }
                }, 3000);
                break;
            case R.id.Home:
                Log.i("查看栈顶包名1", getLauncherTopApp(isApplication.getInstance().getApplicationContext()));
                Log.i("查看栈顶包名6", String.valueOf(1 + 1));
                Log.i("查看栈顶包名7", String.valueOf(isAppOnForeground()));
                if (CreatDialog.dialog != null) {
                    if (CreatDialog.dialog.isShowing()) {
                        CreatDialog.dialog.dismiss();
                    }
                }
                if (CreatDialog.RunningDialog != null) {
                    if (CreatDialog.RunningDialog.isShowing()) {
                        CreatDialog.RunningDialog.dismiss();
                    }
                }
                if (CreatDialog.AdjSpeedDialog != null) {
                    if (CreatDialog.AdjSpeedDialog.isShowing()) {
                        CreatDialog.AdjSpeedDialog.dismiss();
                    }
                }
                if (CreatDialog.AdjSlopeDialog != null) {
                    if (CreatDialog.AdjSlopeDialog.isShowing()) {
                        CreatDialog.AdjSlopeDialog.dismiss();
                    }
                }
                if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
//                    EventBus.getDefault().postSticky(new setHomeKey(true));
                    if (!isAppOnForeground()) {
                        setBackPressed(KeyEvent.KEYCODE_BACK);
                        setBackPressed(KeyEvent.KEYCODE_HOME);
                        Log.i("查看栈顶包名2", getLauncherTopApp(isApplication.getInstance().getApplicationContext()));
                    } else {
                        EventBus.getDefault().postSticky(new setHomeKey(true));
                    }
                }
                break;
            case R.id.Back:
                if (CreatDialog.dialog != null) {
                    if (CreatDialog.dialog.isShowing()) {
                        CreatDialog.dialog.dismiss();
                    }
                }
                if (CreatDialog.RunningDialog != null) {
                    if (CreatDialog.RunningDialog.isShowing()) {
                        CreatDialog.RunningDialog.dismiss();
                    }
                }
                if (CreatDialog.AdjSpeedDialog != null) {
                    if (CreatDialog.AdjSpeedDialog.isShowing()) {
                        CreatDialog.AdjSpeedDialog.dismiss();
                    }
                }
                if (CreatDialog.AdjSlopeDialog != null) {
                    if (CreatDialog.AdjSlopeDialog.isShowing()) {
                        CreatDialog.AdjSlopeDialog.dismiss();
                    }
                }
                if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                    Log.i("查看栈顶包名3", getLauncherTopApp(isApplication.getInstance().getApplicationContext()));
                    setBackPressed(KeyEvent.KEYCODE_BACK);
                }
                break;
            case R.id.service_speed:
                if (SportData.isRunning()) {
                    if (creatDialog.AdjSlopeDialog != null) {
                        creatDialog.AdjSlopeDialog.dismiss();
                    }
                    if (creatDialog.AdjSpeedDialog != null) {
                        if (!creatDialog.AdjSpeedDialog.isShowing()) {
                            creatDialog.AdjSpeedDialog(getApplicationContext(), true);
                        }
                    } else {
                        creatDialog.AdjSpeedDialog(getApplicationContext(), true);
                    }
                }
                break;
            case R.id.service_slope:
                if (SportData.isRunning()) {
                    if (creatDialog.AdjSpeedDialog != null) {
                        creatDialog.AdjSpeedDialog.dismiss();
                    }
                    if (creatDialog.AdjSlopeDialog != null) {
                        if (!creatDialog.AdjSlopeDialog.isShowing()) {
                            creatDialog.AdjSlopeDialog(getApplicationContext(), true);
                        }
                    } else {
                        creatDialog.AdjSlopeDialog(getApplicationContext(), true);
                    }
                }
                break;
        }
    }

    private static ActivityManager mActivityManager;
    private static String mPackageName;

    public static boolean isAppOnForeground() {
        if (mActivityManager != null) {
            List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
            if (tasksInfo.size() > 0) {
                Log.i("top Activity = ", tasksInfo.get(0).topActivity.getPackageName());
                // 应用程序位于堆栈的顶层
                if (mPackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void CaloriesDownStart(double calories, final long pauseTime, final int what) {
        Calories = calories;
        sportText.OnSportText(String.valueOf(calories) + "cal", SportData.RUNNING_MODE_CALORIES);
        service_calories.setText(String.valueOf(calories));
        CountUpStart(pauseTime, what);
    }

    public static void DistanceDownStart(double distance, final long pauseTime, final int what) {//距离倒数模式
        Distance = distance;
        sportText.OnSportText(String.valueOf(distance) + "km", SportData.RUNNING_MODE_DISTANCE);
        service_distance.setText(String.valueOf(distance));
        CountUpStart(pauseTime, what);
    }

    public static void CountUpStart(final long pauseTime, final int what) {//正计时开始方法
        isPause = false;
        wakeTime = WakeLockUtils.getScreenOffTime(isApplication.getInstance().getApplicationContext());
        WakeLockUtils.setWakeLock(isApplication.getInstance().getApplicationContext(),
                Integer.MAX_VALUE);
        Log.i("查看休眠时间", String.valueOf(WakeLockUtils.
                getScreenOffTime(isApplication.getInstance().getApplicationContext())));
        if (pauseTime != 0) {//pauseTime=0 说明是暂停过的，再把速度设置进来
            SportData.setSpeed(pause_speed);
            SportData.setSlope(pause_slope);
            Log.i("正计时暂停速度进来了", String.valueOf(pause_speed));
        }
        Log.i("查看正计时暂停速度", String.valueOf(pause_speed));
        if (CountUp_Timer == null) {
            CountUp_Timer = new Timer();
        }
        if (CountUp_TimerTask == null) {
            service_speed.setText(String.valueOf(SportData.getSpeed()));
            CountUp_baseTimer = SystemClock.elapsedRealtime();
            CountUp_Timer.scheduleAtFixedRate(CountUp_TimerTask = new TimerTask() {
                @Override
                public void run() {
                    CountUpMathTime = (int) ((SystemClock.elapsedRealtime() - CountUp_baseTimer) / 1000) + (int) pauseTime;
                    RESULT_TIME = CountUpMathTime;
                    if (SportData.getSpeed() > MAX_SPEED) {//获取运动结果的最大速度
                        MAX_SPEED = SportData.getSpeed();
                    }
//                    Log.e("CountUpStart有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    String hh = new DecimalFormat("00").format(CountUpMathTime / 3600);
                    String mm = new DecimalFormat("00").format(CountUpMathTime % 3600 / 60);
                    String ss = new DecimalFormat("00").format(CountUpMathTime % 60);
                    strCountUpTime = new String(hh + ":" + mm + ":" + ss);
                    Message msg = new Message();
                    msg.obj = strCountUpTime;
                    msg.what = what;
                    Handler_CountUp.sendMessage(msg);
                }
            }, 0, 1000L);
        }
    }

    public static void CountDownStart(final long time, final long pauseTime, final int what) {//倒计时开始方法
        isPause = false;
        wakeTime = WakeLockUtils.getScreenOffTime(isApplication.getInstance().getApplicationContext());
        WakeLockUtils.setWakeLock(isApplication.getInstance().getApplicationContext(),
                Integer.MAX_VALUE);
        Log.i("查看休眠时间", String.valueOf(WakeLockUtils.
                getScreenOffTime(isApplication.getInstance().getApplicationContext())));
        if (pauseTime != 0) {//pauseTime=0 说明是暂停过的，再把速度设置进来
            SportData.setSpeed(pause_speed);
            SportData.setSlope(pause_slope);
            Log.i("倒计时暂停速度进来了", String.valueOf(pause_speed));
        }
        Log.i("查看倒计时暂停速度", String.valueOf(pause_speed));
        if (CountDown_Timer == null) {
            CountDown_Timer = new Timer();
        }
        if (CountDown_TimerTask == null) {
            service_speed.setText(String.valueOf(SportData.getSpeed()));
            CountDown_baseTimer = SystemClock.elapsedRealtime();
            CountDown_Timer.scheduleAtFixedRate(CountDown_TimerTask = new TimerTask() {
                @Override
                public void run() {
                    CountDownMathTime1 = (int) ((SystemClock.elapsedRealtime() - CountDown_baseTimer) / 1000) + (int) pauseTime;
                    CountDownMathTime2 = time - CountDownMathTime1;//总时间减去自加的时间会呈现倒计时
                    RESULT_TIME = time - CountDownMathTime2;//跑了多少时间
                    if (SportData.getSpeed() > MAX_SPEED) {//获取运动结果的最大速度
                        MAX_SPEED = SportData.getSpeed();
                    }
//                    Log.e("CountDownStart有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    String hh = new DecimalFormat("00").format(CountDownMathTime2 / 3600);
                    String mm = new DecimalFormat("00").format(CountDownMathTime2 % 3600 / 60);
                    String ss = new DecimalFormat("00").format(CountDownMathTime2 % 60);
                    strCountDownTime = new String(hh + ":" + mm + ":" + ss);
                    Message msg = new Message();
                    msg.obj = strCountDownTime;
                    msg.what = what;
                    Handler_CountDown.sendMessage(msg);
                }
            }, 0, 1000L);
        }
    }

    public static void CountDown_Train(final long time, final long flag_pause, final int what) {//训练模式倒计时方法
        isPause = false;
        wakeTime = WakeLockUtils.getScreenOffTime(isApplication.getInstance().getApplicationContext());
        WakeLockUtils.setWakeLock(isApplication.getInstance().getApplicationContext(),
                Integer.MAX_VALUE);
        Log.i("查看休眠时间", String.valueOf(WakeLockUtils.
                getScreenOffTime(isApplication.getInstance().getApplicationContext())));
        Log.i("查看训练倒计时暂停时间", String.valueOf(flag_pause));
        if (flag_pause != 0) {//pause!=0 说明是暂停过的，再把速度设置进来
            SportData.setSpeed(pause_speed);
            SportData.setSlope(pause_slope);
            Log.i("训练倒计时暂停速度进来了", String.valueOf(pause_speed));
        }
        Log.i("查看训练倒计时暂停速度", String.valueOf(pause_speed));
        if (Train_Timer == null) {
            Train_Timer = new Timer();
        }
        if (Train_TimerTask == null) {
            Log.e(">>>>>>>>>>>>>>>pause", String.valueOf(Train_i));
            Train_Timer.scheduleAtFixedRate(Train_TimerTask = new TimerTask() {
                @Override
                public void run() {
                    TrainTime = time - (Train_i++);
                    RESULT_TIME = time - TrainTime;//跑了多少时间
                    if (SportData.getSpeed() > MAX_SPEED) {//获取运动结果的最大速度
                        MAX_SPEED = SportData.getSpeed();
                    }
//                    Log.e("CountDown_Train有东西没有关闭哦", String.valueOf(Handler_CountUp == null) + String.valueOf(Handler_CountDown == null));
                    Log.i("跑了多久", TimeUtils.TimeFomat(RESULT_TIME));
                    String hh = new DecimalFormat("00").format(TrainTime / 3600);
                    String mm = new DecimalFormat("00").format(TrainTime % 3600 / 60);
                    String ss = new DecimalFormat("00").format(TrainTime % 60);
                    strTrainTime = new String(hh + ":" + mm + ":" + ss);
                    Message msg = new Message();
                    msg.obj = strTrainTime;
                    msg.what = what;
                    Handler_CountDown.sendMessage(msg);
                }
            }, 0, 1100L);
        }
    }

    public static void CountPause() {//正计时、倒计时暂停
        isPause = true;
        pause_speed = SportData.getSpeed();
        pause_slope = SportData.getSlope();
        SportData.setSpeed(0.0);
        SportData.setSlope(0);
        if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL
                || SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE
                || SportData.getStatus() == SportData.RUNNING_MODE_CALORIES
                || SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
            CountUpMathTime_Pause = CountUpMathTime;
            tv_RunningTime.setText(strCountUpTime);
            if (CountUp_Timer != null) {
                CountUp_Timer.cancel();
                CountUp_Timer = null;
            }
            if (CountUp_TimerTask != null) {
                CountUp_TimerTask.cancel();
                CountUp_TimerTask = null;
            }
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME
                || SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
            CountDownMathTime_Pause = CountDownMathTime1;
            tv_RunningTime.setText(strCountDownTime);
            if (CountDown_Timer != null) {
                CountDown_Timer.cancel();
                CountDown_Timer = null;
            }
            if (CountDown_TimerTask != null) {
                CountDown_TimerTask.cancel();
                CountDown_TimerTask = null;
            }
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_WARMUP
                || SportData.getStatus() == SportData.RUNNING_MODE_CLIMB
                || SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC
                || SportData.getStatus() == SportData.RUNNING_MODE_HEALTH
                || SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT
                || SportData.getStatus() == SportData.RUNNING_MODE_MAJOR) {
            tv_RunningTime.setText(strTrainTime);
            if (Train_Timer != null) {
                Train_Timer.cancel();
                Train_Timer = null;
            }
            if (Train_TimerTask != null) {
                Train_TimerTask.cancel();
                Train_TimerTask = null;
            }
        }
    }

    static Handler tv_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_RunningTime.setText("00:00:00");
                    MAX_SPEED = 0;
                    break;
            }
        }
    };

    public static void RunningEnd() {//结束跑步
        Log.i(">>>>>>>>>>>>>>当前在什么模式", String.valueOf(SportData.getStatus()));
        if (RESULT_TIME != 0) {
            RESULT_AVG_SPEED = new BigDecimal(((SportData.getDistance()) / (((double) RESULT_TIME) / 3600))).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
            Log.i("查看平均速度", String.valueOf(RESULT_AVG_SPEED) + "************" + SportData.getDistance() + "************" + (((double) RESULT_TIME) / 3600));
            RESULT_PACE = new BigDecimal((SportData.getDistance() * 100 / RESULT_TIME)).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
        }
        RESULT_MAX_SPEED = MAX_SPEED;
        MAX_SPEED = 0;
        CountUpMathTime = 0;
        CountDownMathTime1 = 0;
        CountDownMathTime2 = 0;
        SportData.setSpeed(0.0);
        SportData.setSlope(0);
        SportData.setDistance(0.0, 0);
        tv_RunningTime.setText("00:00:00");
        SportData.setCalories(0.0);
        Message message = new Message();
        message.what = 1;
        tv_handler.sendMessage(message);
        Log.i("查看运动结束的textview", String.valueOf(tv_RunningTime.getText()));
        service_calories.setText("0.0");
        service_distance.setText("0.0");
        service_speed.setText("0.0");
        service_slope.setText("0");
        isPause = false;
        if (CountUp_Timer != null) {//正计时器
            CountUp_Timer.cancel();
            CountUp_Timer = null;
        } else if (CountDown_Timer != null) {//倒计时器
            CountDown_Timer.cancel();
            CountDown_Timer = null;
        } else if (Train_Timer != null) {//训练模式倒计时器
            Train_Timer.cancel();
            Train_Timer = null;
        }
        if (CountUp_TimerTask != null) {//正计时线程
            CountUp_TimerTask.cancel();
            CountUp_TimerTask = null;
        } else if (CountDown_TimerTask != null) {//倒计时线程
            CountDown_TimerTask.cancel();
            CountDown_TimerTask = null;
        } else if (Train_TimerTask != null) {//训练模式倒计时线程
            Train_TimerTask.cancel();
            Train_TimerTask = null;
        }
        if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {
            CountUpMathTime_Pause = 0;
            sportText.OnSportText("00:00:00", SportData.RUNNING_MODE_MANUAL);
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME) {
            CountDownMathTime_Pause = 0;
            sportText.OnSportText("00:00:00", SportData.RUNNING_MODE_TIME);
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
            CountUpMathTime_Pause = 0;
            sportText.OnSportText("0.0km", SportData.RUNNING_MODE_DISTANCE);
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
            CountUpMathTime_Pause = 0;
            sportText.OnSportText("0.0cal", SportData.RUNNING_MODE_CALORIES);
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
            CountUpMathTime_Pause = 0;
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_WARMUP) {
            Train_Pause = 0;
            Train_i = 0;
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_CLIMB) {
            Train_Pause = 0;
            Train_i = 0;
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC) {
            Train_Pause = 0;
            Train_i = 0;
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_HEALTH) {
            check_hrc = 0;
            CHECK_NULL = 0;
//            CHECK_HRC2 = 0;
            CHECK_HRC3 = 0;
//            CHECK_HRC4 = 0;
//            CHECK_HRC5 = 0;
//            CHECK_HRC6 = 0;
//            CHECK_HRC7 = 0;
//            CHECK_HRC8 = 0;
            Train_Pause = 0;
            Train_i = 0;
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT) {
            Train_Pause = 0;
            Train_i = 0;
        } else if (SportData.getStatus() == SportData.RUNNING_MODE_MAJOR) {
            Train_Pause = 0;
            Train_i = 0;
        }
        if (Handler_CountUp != null) {
            Handler_CountUp.removeCallbacksAndMessages(null);
            CountUp_TimerTask = null;
            Log.i(">>>>>>>>>Handler_CountUp线程为空吗?", String.valueOf(CountUp_TimerTask == null));
        }
        if (Handler_CountDown != null) {
            Handler_CountDown.removeCallbacksAndMessages(null);
            CountDown_TimerTask = null;
            Log.i(">>>>>>>>>Handler_CountDown线程为空吗?", String.valueOf(CountDown_TimerTask == null));
        }
        WakeLockUtils.setWakeLock(isApplication.getInstance().getApplicationContext(),
                wakeTime);
        Log.i("查看结束运动后的休眠时间", String.valueOf(WakeLockUtils.
                getScreenOffTime(isApplication.getInstance().getApplicationContext())));
    }

    private void setVolum() {
        Seekbar_Volum.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Seekbar_Volum.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        Seekbar_Volum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //设置系统音量
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    seekBar.setProgress(currentVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public static void setBackPressed(int value) {//home键
        final int repeatCount = (KeyEvent.FLAG_VIRTUAL_HARD_KEY & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent evDown = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_DOWN, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD,
                0, KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        final KeyEvent evUp = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_UP, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        Class<?> ClassInputManager;
        try {
            ClassInputManager = Class.forName("android.hardware.input.InputManager");
            Method[] methods = ClassInputManager.getMethods();
            Method methodInjectInputEvent = null;
            Method methodGetInstance = null;
            for (Method method : methods) {
                if (method.getName().contains("getInstance")) {
                    methodGetInstance = method;
                }
                if (method.getName().contains("injectInputEvent")) {
                    methodInjectInputEvent = method;
                }
            }
            Object instance = methodGetInstance.invoke(ClassInputManager, (Object[]) null); // boolean bool = InputManager.class.isInstance(instance); // methodInjectInputEvent =InputManager.getMethod("injectInputEvent",KeyEvent.class, Integer.class);
            methodInjectInputEvent.invoke(instance, evDown, 0);
            methodInjectInputEvent.invoke(instance, evUp, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static UsageStatsManager sUsageStatsManager;

    public static String getLauncherTopApp(Context context) {//获取栈顶包名
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            if (sUsageStatsManager == null) {
                sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            }
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    private boolean isTop = true;

    private void setHomePressed(int value) {//home键
//        if (isAppOnForeground() && isTop == true) {
//            MainActivity.showFragment(isApplication.newPositionFragment, 0);
//            isTop = false;
//            Log.e("查看是否处于栈顶1", String.valueOf(isAppOnForeground()));
//        } else {
//            Log.e("查看是否处于栈顶2", String.valueOf(isAppOnForeground()));
        final int repeatCount = (KeyEvent.FLAG_VIRTUAL_HARD_KEY & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent evDown = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_DOWN, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD,
                0, KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        final KeyEvent evUp = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_UP, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        Class<?> ClassInputManager;
        try {
            ClassInputManager = Class.forName("android.hardware.input.InputManager");
            Method[] methods = ClassInputManager.getMethods();
            Method methodInjectInputEvent = null;
            Method methodGetInstance = null;
            for (Method method : methods) {
                if (method.getName().contains("getInstance")) {
                    methodGetInstance = method;
                }
                if (method.getName().contains("injectInputEvent")) {
                    methodInjectInputEvent = method;
                }
            }
            Object instance = methodGetInstance.invoke(ClassInputManager, (Object[]) null); // boolean bool = InputManager.class.isInstance(instance); // methodInjectInputEvent =InputManager.getMethod("injectInputEvent",KeyEvent.class, Integer.class);
            methodInjectInputEvent.invoke(instance, evDown, 0);
            methodInjectInputEvent.invoke(instance, evUp, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }

    public void animDown() {//底部数据栏下去的动画
        Animation animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.water_down);
        windows.startAnimation(animations);
        animations.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                server_windows.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static class windowsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("intent.getAction()", intent.getAction().toString());
            if (intent.getAction().equals("com.android.show.navigation.bar")) {
                Log.e("intent.getAction()", intent.getAction().toString());
                if (server_windows.getVisibility() == View.GONE) {
                    server_windows.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(context, R.anim.water_up);
                    windows.startAnimation(anim);
                    windows.setY(0);
                }
            }
        }
    }

    public static setSwipeRightAction swipeRightAction;

    public static void setRightAction(setSwipeRightAction s) {
        swipeRightAction = s;
    }

    public static boolean isAction = false;

    public static class server_RightReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("intent.getAction()", intent.getAction().toString());
            Log.e("广播已发出", String.valueOf(MainActivity.is_X));
            if (intent.getAction().equals("com.android.swipe.from.right")) {
                if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                    if (SportData.isRunning()) {
                        if (!isAppOnForeground()) {
                            MainActivity.is_X = 1001;
                        }
                        if (MainActivity.is_X > 1000) {
                            Log.e("广播已发出,收到并且执行了动作", String.valueOf(isAppOnForeground()));
                            if (!isAppOnForeground()) {
                                Log.e("查看app是否在栈顶", String.valueOf(isAppOnForeground()));
                                setBackPressed(KeyEvent.KEYCODE_BACK);
                                setBackPressed(KeyEvent.KEYCODE_HOME);
                            } else {
                                swipeRightAction.OnRightAction(true);
                            }
                            isAction = true;
                        }
                        MainActivity.is_X = 0;
                    }
                }
            }
        }
    }
}
