package com.vigorchip.omatreadmill.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.ServiceToResult;
import com.vigorchip.omatreadmill.eventbus.setFragmentPause;
import com.vigorchip.omatreadmill.eventbus.setHeartHRC;
import com.vigorchip.omatreadmill.eventbus.setMainClickVisibility;
import com.vigorchip.omatreadmill.eventbus.setMainPause;
import com.vigorchip.omatreadmill.eventbus.setStop;
import com.vigorchip.omatreadmill.eventbus.set_TitleRun4;
import com.vigorchip.omatreadmill.serialport.newSerial;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.utils.DoubleUtils;
import com.vigorchip.omatreadmill.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.vigorchip.omatreadmill.application.isApplication.newPositionFragment;

/**
 * Created by wr-app1 on 2018/5/4.
 * 心率模式跑步界面
 */

public class Fragment_Running4 extends Fragment implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private View view;
    private GifImageView gif_popHeart;
    private Button btn_running4_slope_add, btn_running4_slope_minus, btn_running4_speed_add,
            btn_running4_speed_minus, running4_ing, running4_stop;
    public static Button running4_pause;
    private TextView running4_age, running4_hrc;
    public static TextView tv_check;
    private LinearLayout running4_stopping;

    public static long CountDown_PauseTime = 0;//获取倒计时暂停时的时间，保存起来
    public static int flag_pause = 0;//判断暂停的标志位

    private String time;//心率模式设置的时间
    private GifDrawable gifFromResource = null;//gif
    public static int TARGET_HEART = 0;//目标心率
    public static int TARGET_AGE = 0;//当前年龄

    private double SPEED;//速度
    private boolean FLAG_STOPPING = false;//停止中中的标志位，确保只走一次
    private int LastStatus = 0;//获取上一次的速度
    private Handler stopping_handler = new Handler();
    Runnable stopping_runnable = new Runnable() {//停止中线程
        @Override
        public void run() {
            Log.e("查看走了几遍2", String.valueOf(SPEED));
            if (!FLAG_STOPPING) {
                LastStatus = SportData.getStatus();
                running4_stopping.setVisibility(View.VISIBLE);
                running4_pause.setVisibility(View.GONE);
                running4_stop.setVisibility(View.GONE);
                SportData.setStatus(SportData.STOPPING_TREADMILL);
                CountDown_PauseTime = 0;//清零
                flag_pause = 0;
                FLAG_STOPPING = true;
            }
            if (SPEED != 0.0) {
                SPEED = DoubleUtils.sub(SPEED,0.1);
                stopping_handler.postDelayed(this, 50);
                Log.e("查看停止中的速度", String.valueOf(SPEED));
            } else if (SPEED == 0.0) {
                SportData.setStatus(LastStatus);
                FLAG_STOPPING = false;
                LastStatus = 0;
                running4_stopping.setVisibility(View.GONE);
                running4_pause.setVisibility(View.VISIBLE);
                running4_stop.setVisibility(View.VISIBLE);
                running4_pause.setBackgroundResource(R.color.colorOrange);
                running4_pause.setText(getContext().getString(R.string.main_mode_pause));
                ((MainActivity) getActivity()).showFragment(22, 14);
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));
                EventBus.getDefault().postSticky(new setMainClickVisibility(0));//1 分开主页面启动键  0显示主页面启动键
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_running4, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然接收不到
        }
    }

    private void stopHandler(double speed) {//停止中线程
        this.SPEED = speed;
        stopping_handler.postDelayed(stopping_runnable, 50);
        Log.e("查看走了几遍1", String.valueOf(SPEED));
    }

    private SharedPreferences sp_loadPH;
    private String PH;

    private void init() {
        gif_popHeart = view.findViewById(R.id.gif_popHeart);
        //控制器按键
        btn_running4_speed_add = view.findViewById(R.id.btn_running4_speed_add);
        btn_running4_speed_minus = view.findViewById(R.id.btn_running4_speed_minus);
        btn_running4_slope_add = view.findViewById(R.id.btn_running4_slope_add);
        btn_running4_slope_minus = view.findViewById(R.id.btn_running4_slope_minus);
        tv_check = view.findViewById(R.id.tv_check);
        running4_stop = view.findViewById(R.id.running4_stop);
        running4_stopping = view.findViewById(R.id.running4_stopping);
        running4_pause = view.findViewById(R.id.running4_pause);
        running4_pause = view.findViewById(R.id.running4_pause);
        running4_pause = view.findViewById(R.id.running4_pause);
        running4_age = view.findViewById(R.id.running4_age);
        running4_hrc = view.findViewById(R.id.running4_hrc);
        btn_running4_speed_add.setOnClickListener(this);
        btn_running4_speed_minus.setOnClickListener(this);
        btn_running4_slope_add.setOnClickListener(this);
        btn_running4_slope_minus.setOnClickListener(this);

        btn_running4_speed_add.setOnTouchListener(this);
        btn_running4_speed_minus.setOnTouchListener(this);
        btn_running4_slope_add.setOnTouchListener(this);
        btn_running4_slope_minus.setOnTouchListener(this);

        btn_running4_speed_add.setOnLongClickListener(this);
        btn_running4_speed_minus.setOnLongClickListener(this);
        btn_running4_slope_add.setOnLongClickListener(this);
        btn_running4_slope_minus.setOnLongClickListener(this);

        running4_stop.setOnClickListener(this);
        running4_pause.setOnClickListener(this);
        tv_check.setVisibility(View.VISIBLE);
        tv_check.setText("没有检测到心率");
        try {
            gifFromResource = new GifDrawable(getResources(), R.drawable.pop_heart_bg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gif_popHeart.setImageDrawable(gifFromResource);
        sp_loadPH = getContext().getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        if (sp_loadPH != null) {
            PH = sp_loadPH.getString("PH", "");
            if (PH.equals("KPH")) {
                isApplication.ph_selector = 1;
                SportData.MINSPEED = 1.0;
                if (SportData.MAXSPEED == 8.1) {
                    SportData.MAXSPEED = 13.0;
                } else if (SportData.MAXSPEED == 9.9) {
                    SportData.MAXSPEED = 16.0;
                } else if (SportData.MAXSPEED == 11.2) {
                    SportData.MAXSPEED = 18.0;
                } else if (SportData.MAXSPEED == 12.4) {
                    SportData.MAXSPEED = 20.0;
                } else if (SportData.MAXSPEED == 13.6) {
                    SportData.MAXSPEED = 22.0;
                } else if (SportData.MAXSPEED == 15.5) {
                    SportData.MAXSPEED = 25.0;
                }
            } else if (PH.equals("MPH")) {
                isApplication.ph_selector = 2;
                SportData.MINSPEED = 0.6;
                if (SportData.MAXSPEED == 13.0) {
                    SportData.MAXSPEED = 8.1;
                } else if (SportData.MAXSPEED == 16.0) {
                    SportData.MAXSPEED = 9.9;
                } else if (SportData.MAXSPEED == 18.0) {
                    SportData.MAXSPEED = 11.2;
                } else if (SportData.MAXSPEED == 20.0) {
                    SportData.MAXSPEED = 12.4;
                } else if (SportData.MAXSPEED == 22.0) {
                    SportData.MAXSPEED = 13.6;
                } else if (SportData.MAXSPEED == 25.0) {
                    SportData.MAXSPEED = 15.5;
                }
            }
        } else {
            isApplication.ph_selector = 1;
            SportData.MINSPEED = 1.0;
            if (SportData.MAXSPEED == 8.1) {
                SportData.MAXSPEED = 13.0;
            } else if (SportData.MAXSPEED == 9.9) {
                SportData.MAXSPEED = 16.0;
            } else if (SportData.MAXSPEED == 11.2) {
                SportData.MAXSPEED = 18.0;
            } else if (SportData.MAXSPEED == 12.4) {
                SportData.MAXSPEED = 20.0;
            } else if (SportData.MAXSPEED == 13.6) {
                SportData.MAXSPEED = 22.0;
            } else if (SportData.MAXSPEED == 15.5) {
                SportData.MAXSPEED = 25.0;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onHRC(setHeartHRC hrc) {//检测到心率
        if (hrc.isFlag()) {
            tv_check.setVisibility(View.GONE);
            gif_popHeart.setVisibility(View.VISIBLE);
            gifFromResource.start();
        } else {
            tv_check.setVisibility(View.VISIBLE);
            tv_check.setText(getContext().getString(R.string.NoHeart));
            gif_popHeart.setVisibility(View.GONE);
            gifFromResource.stop();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)//设置标题并设置当前运动模式
    public void onData(set_TitleRun4 title) {
        SportData.setStatus(SportData.RUNNING_MODE_TRAINING_HEART);
        TARGET_HEART = Integer.valueOf(title.getTargetHrc());
        TARGET_AGE = Integer.valueOf(title.getAge());
        running4_age.setText(title.getAge());
        running4_hrc.setText(title.getTargetHrc());
        SportData.setSpeed(SportData.MINSPEED);
        CountDown_PauseTime = 0;
        flag_pause = 0;
        time = title.getMin();//获取从时间模式设置的时间
        ServerWindows.CountDownStart(TimeUtils.TimeManager(time), CountDown_PauseTime,
                SportData.RUNNING_MODE_TRAINING_HEART);
        running4_pause.setBackgroundResource(R.color.colorOrange);
        running4_pause.setText(getContext().getString(R.string.main_mode_pause));
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置主页面启动键
    public void onPause(setFragmentPause pause) {//Fragment_Main发送的暂停
        if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
            if (pause.getPause() == 1) {//暂停
                running4_pause.setBackgroundResource(R.color.colorGreen);
                running4_pause.setText(getContext().getString(R.string.Start));
                flag_pause = 1;
                ServerWindows.CountPause();
                CountDown_PauseTime = ServerWindows.CountDownMathTime_Pause;
            } else if (pause.getPause() == 0) {//启动
                running4_pause.setBackgroundResource(R.color.colorOrange);
                running4_pause.setText(getContext().getString(R.string.main_mode_pause));
                flag_pause = 0;
                ServerWindows.CountDownStart(TimeUtils.TimeManager(time), CountDown_PauseTime, SportData.getStatus());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onStop(setStop stop) {//Fragment_Main发送的停止
        if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
            if (stop.getStop()) {
                if (MainActivity.heart_status == false) {
                    //回到最初状态
                    running4_pause.setBackgroundResource(R.color.colorOrange);
                    running4_pause.setText(getContext().getString(R.string.main_mode_pause));
                    flag_pause = 0;
                    CountDown_PauseTime = 0;//清零
                    if (!stop.isMain()) {
                        ServerWindows.RunningEnd();
                    }
                    ((MainActivity) getActivity()).showFragment(newPositionFragment, 14);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_running4_speed_add:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSpeed() != SportData.MAXSPEED) {
                                SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.1));
                            }
                        }
                    }
                }
                break;
            case R.id.btn_running4_speed_minus:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSpeed() != SportData.MINSPEED) {
                                SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.1));
                            }
                        }
                    }
                }
                break;
            case R.id.btn_running4_slope_add:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSlope() != SportData.MAXSLOPES) {
                                SportData.setSlope(SportData.getSlope() + 1);
                            }
                        }
                    }
                }
                break;
            case R.id.btn_running4_slope_minus:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSlope() != 0) {
                                SportData.setSlope(SportData.getSlope() - 1);
                            }
                        }
                    }
                }
                break;
            case R.id.running4_stop:
                stopHandler(SportData.getSpeed());
                //回到最初状态
                ServerWindows.RunningEnd();
                break;
            case R.id.running4_pause:
                if (flag_pause == 0) {//暂停
                    running4_pause.setBackgroundResource(R.color.colorGreen);
                    running4_pause.setText(getContext().getString(R.string.Start));
                    flag_pause = 1;
                    ServerWindows.CountPause();
                    CountDown_PauseTime = ServerWindows.CountDownMathTime_Pause;
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 1  暂停
                    break;
                } else if (flag_pause == 1) {//启动
                    running4_pause.setBackgroundResource(R.color.colorOrange);
                    running4_pause.setText(getContext().getString(R.string.main_mode_pause));
                    flag_pause = 0;
                    ServerWindows.CountDownStart(TimeUtils.TimeManager(time), CountDown_PauseTime, SportData.getStatus());
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 0  启动
                    break;
                }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_running4_slope_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running4_slope_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running4_slope_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running4_slope_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running4_slope_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running4_slope_minus.setBackgroundResource(R.drawable.btn_minus_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running4_speed_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running4_speed_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running4_speed_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running4_speed_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running4_speed_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running4_speed_minus.setBackgroundResource(R.drawable.btn_minus_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
        }
        return false;
    }

    private Handler longHandler;
    Runnable Run_SpeedAdd = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSpeed() != SportData.MAXSPEED) {
                SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.2));
                longHandler.postDelayed(Run_SpeedAdd, 500);
            }
        }
    };

    Runnable Run_SpeedSub = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSpeed() != SportData.MINSPEED) {
                SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.2));
                longHandler.postDelayed(Run_SpeedSub, 500);
            }
        }
    };

    Runnable Run_SlopeAdd = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSlope() != SportData.MAXSLOPES) {
                SportData.setSlope(SportData.getSlope() + 1);
                longHandler.postDelayed(Run_SlopeAdd, 500);
            }
        }
    };

    Runnable Run_SlopeSub = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSlope() != 0) {
                SportData.setSlope(SportData.getSlope() - 1);
                longHandler.postDelayed(Run_SlopeSub, 500);
            }
        }
    };

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_running4_speed_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedAdd, 500);
                break;
            case R.id.btn_running4_speed_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedSub, 500);
                break;
            case R.id.btn_running4_slope_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeAdd, 500);
                break;
            case R.id.btn_running4_slope_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeSub, 500);
                break;
        }
        return false;
    }

}
