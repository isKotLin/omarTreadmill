package com.vigorchip.omatreadmill.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.base.BaseObserver;
import com.vigorchip.omatreadmill.bean.Data_Info;
import com.vigorchip.omatreadmill.bean.NetUtil;
import com.vigorchip.omatreadmill.bean.Skins_Selector;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.SerialToHome;
import com.vigorchip.omatreadmill.eventbus.setHomeKey;
import com.vigorchip.omatreadmill.fragment.Fragment_Apps;
import com.vigorchip.omatreadmill.fragment.Fragment_Forget;
import com.vigorchip.omatreadmill.fragment.Fragment_Language;
import com.vigorchip.omatreadmill.fragment.Fragment_Login;
import com.vigorchip.omatreadmill.fragment.Fragment_Main;
import com.vigorchip.omatreadmill.fragment.Fragment_Media;
import com.vigorchip.omatreadmill.fragment.Fragment_MediaApps;
import com.vigorchip.omatreadmill.fragment.Fragment_Mode_Calories;
import com.vigorchip.omatreadmill.fragment.Fragment_Mode_Distance;
import com.vigorchip.omatreadmill.fragment.Fragment_Mode_Live;
import com.vigorchip.omatreadmill.fragment.Fragment_Mode_Time;
import com.vigorchip.omatreadmill.fragment.Fragment_Mode_Train;
import com.vigorchip.omatreadmill.fragment.Fragment_More;
import com.vigorchip.omatreadmill.fragment.Fragment_Regist;
import com.vigorchip.omatreadmill.fragment.Fragment_Result;
import com.vigorchip.omatreadmill.fragment.Fragment_Running1;
import com.vigorchip.omatreadmill.fragment.Fragment_Running2;
import com.vigorchip.omatreadmill.fragment.Fragment_Running3;
import com.vigorchip.omatreadmill.fragment.Fragment_Running4;
import com.vigorchip.omatreadmill.fragment.Fragment_Settings;
import com.vigorchip.omatreadmill.fragment.Fragment_Skins;
import com.vigorchip.omatreadmill.fragment.Fragment_SystemInfo;
import com.vigorchip.omatreadmill.fragment.Fragment_Train_BMI;
import com.vigorchip.omatreadmill.fragment.Fragment_Train_Default;
import com.vigorchip.omatreadmill.fragment.Fragment_Train_Heart;
import com.vigorchip.omatreadmill.fragment.Fragment_Train_User;
import com.vigorchip.omatreadmill.fragment.Fragment_User;
import com.vigorchip.omatreadmill.fragment.Fragment_UserTime;
import com.vigorchip.omatreadmill.interfaci.Api;
import com.vigorchip.omatreadmill.interfaci.setSwipeRightAction;
import com.vigorchip.omatreadmill.receiver.WakeLockReceiver;
import com.vigorchip.omatreadmill.server.ServerWindows;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

import static com.vigorchip.omatreadmill.application.isApplication.currentAge;
import static com.vigorchip.omatreadmill.application.isApplication.currentHeight;
import static com.vigorchip.omatreadmill.application.isApplication.currentWeight;
import static com.vigorchip.omatreadmill.application.isApplication.newPositionFragment;

/**
 * 这个项目代码写的很乱，耐心看
 */

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment_Main fragment_main;
    private Fragment_Media fragment_media;
    private Fragment_Settings fragment_settings;
    private Fragment_User fragment_user;
    private Fragment_More fragment_more;
    private Fragment_Mode_Calories fragment_mode_calories;
    private Fragment_Mode_Distance fragment_mode_distance;
    private Fragment_Mode_Time fragment_mode_time;
    private Fragment_Mode_Live fragment_mode_live;
    private Fragment_Mode_Train fragment_mode_train;
    private Fragment_Apps fragment_apps;
    private Fragment_Language fragment_language;
    private Fragment_SystemInfo fragment_systemInfo;
    private Fragment_Skins fragment_skins;
    private Fragment_Result fragment_result;
    private Fragment_Train_Default fragment_train_default;
    private Fragment_Train_User fragment_train_user;
    private Fragment_Running1 fragment_running1;
    private Fragment_Running2 fragment_running2;
    private Fragment_Train_BMI fragment_train_bmi;
    private Fragment_Train_Heart fragment_train_heart;
    private Fragment_Running3 fragment_running3;
    private Fragment_Running4 fragment_running4;
    private Fragment_UserTime fragment_userTime;
    private Fragment_Login fragment_login;
    private Fragment_Regist fragment_regist;
    private Fragment_Forget fragment_forget;
    private Fragment_MediaApps fragment_mediaApps;

    private int currentLevel;
    public RelativeLayout activity_main;
    //皮肤
    private Skins_Selector skins_selector;
    public SharedPreferences sharedPreferences_loadSkins;
    private SharedPreferences sp_saveSkins;
    private File spfile = null;
    private SharedPreferences.Editor editor;
    private String shareGETmark;
    //屏幕监听广播
    private WakeLockReceiver wakeLockReceiver;
    //home键广播监听
    private HomeWatcherReceiver mHomeKeyReceiver = null;

    //判断用户有无登录
    private File file;
    private SharedPreferences sp_account;
    private Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("onCreate is invoke!!!", String.valueOf(1 + 1));
        if (savedInstanceState != null) {
            isApplication.newPositionFragment = savedInstanceState.getInt("type", 0);
//            showFragment(0, isApplication.newPositionFragment);
            Log.i(">>>>>>>>Home savedInstanceState有无保存数据?", String.valueOf(savedInstanceState.getInt("type", 0)));
        }
        //初始化串口
        isApplication.initializeSerialPort(this);
        //初始化fragment
        init();
        initFragment();
        //开启底部控制栏服务
        startWindows();
        //休眠广播
        registWakeLockReceiver();
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        activity_main.setOnTouchListener(this);
        //初始化背景
        creatSkins();
        //申请修改手机系统权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
            }
        }
        //申请文件修改权限
        verifyStoragePermissions(MainActivity.this);
        //home键注册
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        this.registerReceiver(mHomeKeyReceiver, homeFilter);
        //activitymanager管理
        mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        mPackageName = getPackageName();
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (!isUseGranted()) {
//                //开启应用授权界面
//                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        }
        //查看是否是登录状态
        file = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/sp_account.xml");
        sp_account = getSharedPreferences("sp_account", Context.MODE_PRIVATE);
        if (file.exists()) {
            if (map == null) {
                map = new HashMap<>();
            }
            map.clear();
            map.put("phone", sp_account.getString("user_account", ""));
            map.put("password", sp_account.getString("user_password", ""));
            NetUtil.request(Api.login, new BaseObserver(this, "正在登陆中...") {
                @Override
                protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                    if (code.equals("true")) {
                        //用户id
                        String data = value.getString("data");
                        Data_Info data_info = new Gson().fromJson(data, Data_Info.class);
                        isApplication.user_id = data_info.getUser_id();
                        map.clear();
                        map.put("user_id", isApplication.user_id);
                        getData(map);
                    }
                }
            }, map);
        }
        //如果安装apk权限文件存在，就删除
        final File canInstallApp_file = new File("/data/canInstallApp");
        if (canInstallApp_file.exists()) {
            canInstallApp_file.delete();
        }
        RightAction();//运动状态右滑回到主界面广播接口处理
    }

    private void getData(Map<String, Object> map) {
        NetUtil.request(Api.getInfo, new BaseObserver(this, true) {
            @Override
            protected void success(JSONObject value, String code, String message, Disposable disposable) throws IOException, JSONException {
                if (code.equals("true")) {
                    String data = value.getString("data");
                    Data_Info data_info = new Gson().fromJson(data, Data_Info.class);
                    isApplication.currentAge = Integer.parseInt(data_info.getAge());
                    if (SportData.RADIX == 1) {
                        isApplication.currentHeight = Integer.parseInt(data_info.getTall());
                        isApplication.currentWeight = Integer.parseInt(data_info.getWeight());
                    } else if (SportData.RADIX == 2) {
                        isApplication.sb_currentHeight = Integer.parseInt(data_info.getTall());
                        isApplication.sb_currentWeight = Integer.parseInt(data_info.getWeight());
                    }
                    Log.e("查看年龄身高体重", currentAge + "  " + currentHeight + "  " + currentWeight + "  ");
                }
            }
        }, map);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void init() {
        //一级菜单
        fragment_main = new Fragment_Main();
        fragmentList.add(fragment_main);//0    1级
        //二级菜单
        fragment_media = new Fragment_Media();
        fragment_settings = new Fragment_Settings();
        fragment_user = new Fragment_User();
        fragment_more = new Fragment_More();
        fragment_mode_calories = new Fragment_Mode_Calories();
        fragment_mode_distance = new Fragment_Mode_Distance();
        fragment_mode_time = new Fragment_Mode_Time();
        fragment_mode_train = new Fragment_Mode_Train();
        fragment_mode_live = new Fragment_Mode_Live();
        fragmentList.add(fragment_mode_time);//1    2级
        fragmentList.add(fragment_mode_distance);//2    2级
        fragmentList.add(fragment_mode_calories);//3   2级
        fragmentList.add(fragment_mode_train);//4    2级
        fragmentList.add(fragment_mode_live);//5    2级
        fragmentList.add(fragment_media);//6    2级
        fragmentList.add(fragment_settings);//7    2级
        fragmentList.add(fragment_user);//8    2级
        fragmentList.add(fragment_more);//9    2级
        //三级菜单
        fragment_language = new Fragment_Language();
        fragment_systemInfo = new Fragment_SystemInfo();
        fragment_apps = new Fragment_Apps();
        fragment_skins = new Fragment_Skins();
        fragment_result = new Fragment_Result();
        fragment_train_default = new Fragment_Train_Default();
        fragment_running1 = new Fragment_Running1();
        fragment_running2 = new Fragment_Running2();
        fragment_train_user = new Fragment_Train_User();
        fragment_train_bmi = new Fragment_Train_BMI();
        fragment_train_heart = new Fragment_Train_Heart();
        fragment_running3 = new Fragment_Running3();
        fragment_running4 = new Fragment_Running4();
        fragment_login = new Fragment_Login();
        fragment_mediaApps = new Fragment_MediaApps();
        fragmentList.add(fragment_language);//10    3级
        fragmentList.add(fragment_systemInfo);//11    3级
        fragmentList.add(fragment_apps);//12    3级
        fragmentList.add(fragment_skins);//13    3级
        fragmentList.add(fragment_result);//14    3级
        fragmentList.add(fragment_train_default);//15    3级
        fragmentList.add(fragment_running1);//16    3级
        fragmentList.add(fragment_running2);//17    3级
        fragmentList.add(fragment_train_user);//18    3级
        fragmentList.add(fragment_train_bmi);//19    3级
        fragmentList.add(fragment_train_heart);//20    3级
        fragmentList.add(fragment_running3);//21    3级
        fragmentList.add(fragment_running4);//22    3级
        fragmentList.add(fragment_login);//23    3级

        fragment_userTime = new Fragment_UserTime();
        fragment_regist = new Fragment_Regist();
        fragment_forget = new Fragment_Forget();
        fragmentList.add(fragment_userTime);//24    4级
        fragmentList.add(fragment_regist);//25    4级
        fragmentList.add(fragment_forget);//26    4级
        fragmentList.add(fragment_mediaApps);//27    3级
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然接收不到
        }
    }

    public void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_layout, fragmentList.get(0));
        transaction.show(fragmentList.get(0));
        transaction.commitAllowingStateLoss();
    }

    //心率模式有个bug，实景播放回到主界面停止，进入运动结果界面home键，再开始心率模式，心率不执行直接跳到上一次的结果页面，没找到问题，所以加一个标志位限制
    public static boolean heart_status = false;

    public void showFragment(int oldPositionFragment, int newPositionFragment) {
        Log.e("查看show走了没有", "old" + oldPositionFragment + "  new" + newPositionFragment);
        heart_status = true;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        isApplication.getInstance().setOldPositionFragment(oldPositionFragment);
        isApplication.getInstance().setNewPositionFragment(newPositionFragment);
        if (!fragmentList.get(newPositionFragment).isAdded()) {
            transaction.add(R.id.frame_layout, fragmentList.get(newPositionFragment))
                    .hide(fragmentList.get(oldPositionFragment));//.remove(fragmentList.get(oldPositionFragment))
            transaction.show(fragmentList.get(newPositionFragment));
        } else {
            transaction.hide(fragmentList.get(oldPositionFragment));
            transaction.show(fragmentList.get(newPositionFragment));
        }
        if (isApplication.oldPositionFragment == 25 && newPositionFragment == 23) {//防止用户注册账号中点击返回登录按钮的循环
            isApplication.getInstance().setOldPositionFragment(8);
        }
        if (isApplication.oldPositionFragment == 26 && newPositionFragment == 23) {//防止用户忘记密码中点击返回登录按钮的循环
            isApplication.getInstance().setOldPositionFragment(8);
        }
        transaction.commitAllowingStateLoss();
    }

    public int LevelMenu(int currentFragment) {
        if (currentFragment == 1 || currentFragment == 2 || currentFragment == 3 ||
                currentFragment == 4 || currentFragment == 5 || currentFragment == 6 ||
                currentFragment == 7 || currentFragment == 8 || currentFragment == 9) {
            currentLevel = 1;//二级菜单
        }
        if (currentFragment == 10 || currentFragment == 11 || currentFragment == 12 ||
                currentFragment == 13 || currentFragment == 15 || currentFragment == 18 ||
                currentFragment == 19 || currentFragment == 20 || currentFragment == 23
                || currentFragment == 27) {
            currentLevel = 2;//三级菜单
        }
        if (currentFragment == 24) {
            currentLevel = 3;//四级菜单
        }
        if (currentFragment == 25 || currentFragment == 26) {
            currentLevel = 4;//四级菜单
        }
        if (currentFragment == 14 || currentFragment == 16 || currentFragment == 17 ||
                currentFragment == 21 || currentFragment == 22) {
            currentLevel = 88;//跑步界面和结果，单独返回88
        }
        return currentLevel;//返回菜单级
    }

    public void startWindows() {
        Intent intent = new Intent(MainActivity.this, ServerWindows.class);
        startService(intent);
    }

    private void registWakeLockReceiver() {
        wakeLockReceiver = new WakeLockReceiver();
        IntentFilter wakeLockIntent = new IntentFilter();
        wakeLockIntent.addAction(Intent.ACTION_SCREEN_ON);
        wakeLockIntent.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(wakeLockReceiver, wakeLockIntent);
    }

    public void stopWindows() {
        Intent intent = new Intent(MainActivity.this, ServerWindows.class);
        stopService(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        setDefaultFragment(savedInstanceState);
        Log.e("Home onRestoreInstanceState", String.valueOf(savedInstanceState != null));
        super.onRestoreInstanceState(savedInstanceState);
    }

    //会保证一定在activity被回收之前调用
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //切换语言会重启activity 将重启前的页面保存下来
        outState.putInt("type", isApplication.getInstance().oldPositionFragment);
        Log.i(">>>>>>>>Home savedInstanceState进入保存数据?", String.valueOf(isApplication.getInstance().oldPositionFragment));
    }

    public static float touch_X;
    public static float mtouch_X;
    public static float is_X;//15.6寸会出现左滑时右滑广播发出，增加判断用

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            /**点击的开始位置*/
            case MotionEvent.ACTION_DOWN:
                is_X = event.getX();
                mtouch_X = event.getX();
                if (SportData.isRunning()) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (isApplication.newPositionFragment == 16 ||
                                isApplication.newPositionFragment == 17 ||
                                isApplication.newPositionFragment == 21 ||
                                isApplication.newPositionFragment == 22) {
                            Log.e("点击手指位置", String.valueOf(is_X));
                            touch_X = event.getX();
                        }
                    }
                }
                Log.e("起始位置：(" + touch_X + ",", "  " + is_X);
                break;
            /**触屏实时位置*/
            case MotionEvent.ACTION_MOVE:
                Log.e("实时位置：(" + touch_X + ",", "  " + event.getY());
                break;
            /**离开屏幕的位置*/
            case MotionEvent.ACTION_UP:
                mtouch_X = event.getX();
                Log.e("touch_X位置：" + "  " + touch_X, "  " + String.valueOf(mtouch_X));
                Log.e("is_X位置：" + "  ", "  " + String.valueOf(is_X));
                if (is_X < 200) {
                    if (touch_X > 0 && touch_X < 85) {
                        if ((mtouch_X - touch_X) > 50) {
                            Log.e("抬起手指位置", "成功进入返回主页面1  " + touch_X);
                            if (SportData.isRunning()) {
                                if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                                    if (isApplication.newPositionFragment == 16 ||
                                            isApplication.newPositionFragment == 17 ||
                                            isApplication.newPositionFragment == 21 ||
                                            isApplication.newPositionFragment == 22) {
                                        Log.e("抬起手指位置", "成功进入返回主页面2  " + isApplication.newPositionFragment);
                                        showFragment(isApplication.newPositionFragment, 0);
                                    }
                                }
                            }
                        }
                    }
                    touch_X = 0;
                }
                Log.e("结束位置：(" + event.getX() + ",", "  " + event.getY());
                Log.e("查看MainActivity.is_X1", String.valueOf(MainActivity.is_X));
                break;
            default:
                break;
        }
        /**
         *  注意返回值
         *  true：view继续响应Touch操作；
         *  false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
         */
        return true;
    }

    private void RightAction() {
        ServerWindows.setRightAction(new setSwipeRightAction() {
            @Override
            public void OnRightAction(boolean arg0) {
                Log.e("广播位置走了", String.valueOf(isApplication.newPositionFragment));
                if (SportData.isRunning()) {
                    if (SportData.isMainRunning()) {
                        if (isApplication.newPositionFragment != 16) {
                            showFragment(isApplication.newPositionFragment, 16);
                        }
                    } else if (SportData.isTrainRunning()) {
                        if (isApplication.newPositionFragment != 17) {
                            showFragment(isApplication.newPositionFragment, 17);
                        }
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.newPositionFragment != 21) {
                            showFragment(isApplication.newPositionFragment, 21);
                        }
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
                        if (isApplication.newPositionFragment != 22) {
                            showFragment(isApplication.newPositionFragment, 22);
                        }
                    }
                    ServerWindows.isAction = false;
                }
            }
        });
    }

    private class HomeWatcherReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                Log.i("============Home键的监听", intent.getAction().toString()
                        + "  oldPositionFragment:" + isApplication.oldPositionFragment + "      "
                        + "newPositionFragment:" + isApplication.newPositionFragment);
                if (SportData.getStatus() != SportData.START_TREADMILL_ANIM) {
                    showFragment(newPositionFragment, 0);
                }
            }
        }
    }

    private ActivityManager mActivityManager;
    private String mPackageName;

    public boolean isAppOnForeground() {
        List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            Log.i("top Activity = ", tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (mPackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断  用户查看使用情况的权利是否给予app
     *
     * @return
     */
    private boolean isUseGranted() {
        Context appContext = isApplication.getInstance().getApplicationContext();
        AppOpsManager appOps = (AppOpsManager) appContext
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = -1;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mode = appOps.checkOpNoThrow("android:get_usage_stats",
                    android.os.Process.myUid(), appContext.getPackageName());
        }
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onHome(setHomeKey homeKey) {
        if (homeKey.isHomeClick() == true) {
            if (SportData.getStatus() != SportData.START_TREADMILL_ANIM) {
                showFragment(isApplication.newPositionFragment, 0);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void SerialToHome(SerialToHome serialToHome) {//錯誤代碼彈框消失后返回主頁
        if (serialToHome.isSend()) {
            showFragment(isApplication.newPositionFragment, 0);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("当前页面:" + isApplication.getInstance().newPositionFragment, "当前菜单:" + LevelMenu(isApplication.getInstance().newPositionFragment));
        Log.i("查看栈顶的包名", ServerWindows.getLauncherTopApp(MainActivity.this));
//        if (ServerWindows.getLauncherTopApp(MainActivity.this).equals("com.android.settings")) {
//            showFragment(7, 0);
//            Log.i("com.android.settings栈顶的包名", ServerWindows.getLauncherTopApp(MainActivity.this));
//        } else
        if (isApplication.newPositionFragment != 0) {
            if (LevelMenu(isApplication.getInstance().newPositionFragment) == 1) {
                showFragment(isApplication.getInstance().newPositionFragment, 0);
            } else if (LevelMenu(isApplication.getInstance().newPositionFragment) == 2) {
                showFragment(isApplication.getInstance().newPositionFragment, isApplication.getInstance().oldPositionFragment);
            } else if (LevelMenu(isApplication.getInstance().newPositionFragment) == 88) {
                showFragment(isApplication.getInstance().newPositionFragment, 0);
            } else if (LevelMenu(isApplication.getInstance().newPositionFragment) == 3) {
                showFragment(isApplication.getInstance().newPositionFragment, isApplication.getInstance().oldPositionFragment);
                isApplication.getInstance().oldPositionFragment = 4;
            } else if (LevelMenu(isApplication.getInstance().newPositionFragment) == 4) {
                showFragment(isApplication.getInstance().newPositionFragment, isApplication.getInstance().oldPositionFragment);
                isApplication.getInstance().oldPositionFragment = 8;
            }
        }
    }

    private void creatSkins() {
        skins_selector = new Skins_Selector();
        sharedPreferences_loadSkins = getApplicationContext().getSharedPreferences("sp_skins", Context.MODE_PRIVATE);
        sp_saveSkins = getApplicationContext().getSharedPreferences("sp_skins", Context.MODE_PRIVATE);
        spfile = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/sp_skins.xml");

        if (spfile.exists()) {
            Log.i("shareGETmark", String.valueOf(shareGETmark));
            shareGETmark = sharedPreferences_loadSkins.getString("mark", "");
            Log.i("shareGETmark", String.valueOf(shareGETmark));
            if (!shareGETmark.equals("0")) {
                if (shareGETmark.equals("1")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[1]);
                } else if (shareGETmark.equals("2")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[2]);
                } else if (shareGETmark.equals("3")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[3]);
                } else if (shareGETmark.equals("4")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[4]);
                } else if (shareGETmark.equals("5")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[5]);
                } else if (shareGETmark.equals("6")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[6]);
                } else if (shareGETmark.equals("7")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[7]);
                } else if (shareGETmark.equals("8")) {
                    activity_main.setBackgroundResource(skins_selector.arr_drawable[0]);
                }
            }
        } else {
            editor = sp_saveSkins.edit();
            editor.putString("mark", "3");
            editor.commit();
            activity_main.setBackgroundResource(skins_selector.arr_drawable[3]);
        }
    }


    private static long lastClickTime;

    private boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime >= 500) {
            Log.e("时间戳1", (time - lastClickTime < 500) + "");
            return true;
        }
        lastClickTime = time;
        Log.e("时间戳2", (time - lastClickTime < 500) + "");
        return false;
    }


    //    /**
//     * Activity从后台重新回到前台时被调用
//     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart is invoke!!!!", String.valueOf(ServerWindows.isAction));
//        if (ServerWindows.isAction) {
//            if (SportData.isRunning()) {
//                if (SportData.isMainRunning()) {
//                    if (isApplication.newPositionFragment != 16) {
//                        Log.e("接收到广播了吗1", String.valueOf(isApplication.newPositionFragment));
//                        showFragment(isApplication.newPositionFragment, 16);
//                        Log.e("接收到广播了吗2", String.valueOf(isApplication.newPositionFragment));
//                    }
//                } else if (SportData.isTrainRunning()) {
//                    if (isApplication.newPositionFragment != 17) {
//                        showFragment(isApplication.newPositionFragment, 17);
//                    }
//                } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
//                    if (isApplication.newPositionFragment != 21) {
//                        showFragment(isApplication.newPositionFragment, 21);
//                    }
//                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
//                    if (isApplication.newPositionFragment != 22) {
//                        showFragment(isApplication.newPositionFragment, 22);
//                    }
//                }
//                ServerWindows.isAction = false;
//            }
//        }
    }

    //
//    /**
//     *Activity创建或者从后台重新回到前台时被调用
//     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart is invoke!!!", String.valueOf(1 + 1));
    }

    /**
     * Activity创建或者从被覆盖、后台重新回到前台时被调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume is invoke!!!", String.valueOf(1 + 1));
        if (ServerWindows.isAction) {
            if (SportData.isRunning()) {
                if (SportData.isMainRunning()) {
                    if (isApplication.newPositionFragment != 16) {
                        Log.e("接收到广播了吗1", String.valueOf(isApplication.newPositionFragment));
                        showFragment(isApplication.newPositionFragment, 16);
                        Log.e("接收到广播了吗2", String.valueOf(isApplication.newPositionFragment));
                    }
                } else if (SportData.isTrainRunning()) {
                    if (isApplication.newPositionFragment != 17) {
                        showFragment(isApplication.newPositionFragment, 17);
                    }
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                    if (isApplication.newPositionFragment != 21) {
                        showFragment(isApplication.newPositionFragment, 21);
                    }
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
                    if (isApplication.newPositionFragment != 22) {
                        showFragment(isApplication.newPositionFragment, 22);
                    }
                }
                ServerWindows.isAction = false;
            }
        }
    }

    /**
     * Activity被覆盖到下面或者锁屏时被调用
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause is invoke!!!", String.valueOf(1 + 1));
    }

    /**
     * 退出当前Activity或者跳转到新Activity时被调用
     */
    @Override
    protected void onStop() {
        super.onStop();

        Log.e("onStop is invoke!!!", String.valueOf(1 + 1));
    }
//

    /**
     * 退出当前Activity时被调用,调用之后Activity就结束了
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy is invoke!!!", String.valueOf(1 + 1));
        unregisterReceiver(mHomeKeyReceiver);
        EventBus.getDefault().unregister(this);
        /**杀死整个进程**/
        android.os.Process.killProcess(android.os.Process.myPid());
        /**重启app**/
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.vigorchip.omatreadmill");
        startActivity(LaunchIntent);
    }
}
