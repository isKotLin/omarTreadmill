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
import com.vigorchip.omatreadmill.eventbus.setMainClickVisibility;
import com.vigorchip.omatreadmill.eventbus.setMainPause;
import com.vigorchip.omatreadmill.eventbus.setStop;
import com.vigorchip.omatreadmill.eventbus.setTitle_Run2;
import com.vigorchip.omatreadmill.serialport.newSerial;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.utils.DoubleUtils;
import com.vigorchip.omatreadmill.utils.TimeUtils;
import com.vigorchip.omatreadmill.view.RunningView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wr-app1 on 2018/4/23.
 * 柱状图运动界面：
 * 预设训练模式，用户训练程序，心率控速模式
 */

public class Fragment_Running2 extends Fragment implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private View view;
    private RunningView rv_1;
    private Button btn_running2_slope_add, btn_running2_slope_minus, btn_running2_speed_add, btn_running2_speed_minus;
    private Button running2_stop, running2_ing;
    public static Button running2_pause;
    private TextView running2_title;
    private LinearLayout running2_pause_ing_layout;

    public long CountDown_PauseFlag = 0;//判断有没有被暂停过的标志位,0代表没有暂停过，1代表暂停过
    private String time;//预设训练模式设置的时间
    public static int racIndex = 0;//当前柱子下标

    public static double[] Arr_speed = new double[10];
    public static double[] Arr_slope = new double[10];

    private static int flag_pause = 0;//判断暂停的标志位
    private static boolean pop = true;//闪动的标志位

    private static int racTime = 0;//一条能量柱时间
    private static int surplusTime = 0;//减去一条能量柱后所剩余的时间

    static Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rv_1.upDataRec(Arr_speed, racIndex);
            if (racIndex < Arr_speed.length - 1) {
                if (ServerWindows.TrainTime == surplusTime) {
                    racIndex = racIndex + 1;
                    surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime * (racIndex + 1));
                    SportData.setSpeed(Arr_speed[racIndex]);
                    SportData.setSlope((int) Arr_slope[racIndex]);
                    isApplication.setMediaPlayer(getContext(),R.raw.alert);
                    Log.i(">>>>>>>>>>>>>>>>减去一条能量柱后所剩余的时间", String.valueOf(surplusTime));
                }
            }
            if (pop) {
                rv_1.setIsShan(true);
                pop = false;
            } else {
                rv_1.setIsShan(false);
                pop = true;
            }
            handler.postDelayed(runnable, 500);
        }
    };


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
                running2_pause_ing_layout.setVisibility(View.VISIBLE);
                running2_pause.setVisibility(View.GONE);
                running2_stop.setVisibility(View.GONE);
                handler.removeCallbacksAndMessages(null);
                SportData.setStatus(SportData.STOPPING_TREADMILL);
                FLAG_STOPPING = true;
                flag_pause = 0;
                isApplication.setMediaPlayer(getContext(),R.raw.alert);
            }
            if (SPEED != 0.0) {
                SPEED = DoubleUtils.sub(SPEED, 0.1);
                stopping_handler.postDelayed(this, 50);
                Log.e("查看停止中的速度", String.valueOf(SPEED));
            } else if (SPEED == 0.0) {
                SportData.setStatus(LastStatus);
                FLAG_STOPPING = false;
                LastStatus = 0;
                flag_pause = 0;
                running2_pause_ing_layout.setVisibility(View.GONE);
                running2_pause.setVisibility(View.VISIBLE);
                running2_stop.setVisibility(View.VISIBLE);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                ((MainActivity) getActivity()).showFragment(17, 14);
                sendResult(SportData.getStatus());
                EventBus.getDefault().postSticky(new setMainClickVisibility(0));//1 分开主页面启动键  0显示主页面启动键
                Log.e("查看走了几遍3", String.valueOf(SPEED));
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_running2, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void stopHandler(double speed) {//停止中线程
        this.SPEED = speed;
        stopping_handler.postDelayed(stopping_runnable, 50);
        Log.e("查看走了几遍1", String.valueOf(SPEED));
    }

    private SharedPreferences sp_loadPH;
    private String PH;

    private void init() {
        rv_1 = view.findViewById(R.id.rv_1);
        running2_pause_ing_layout = view.findViewById(R.id.running2_pause_ing_layout);
        btn_running2_slope_add = view.findViewById(R.id.btn_running2_slope_add);
        btn_running2_slope_minus = view.findViewById(R.id.btn_running2_slope_minus);
        btn_running2_speed_add = view.findViewById(R.id.btn_running2_speed_add);
        btn_running2_speed_minus = view.findViewById(R.id.btn_running2_speed_minus);
        running2_stop = view.findViewById(R.id.running2_stop);
        running2_pause = view.findViewById(R.id.running2_pause);
        running2_ing = view.findViewById(R.id.running2_ing);
        running2_title = view.findViewById(R.id.running2_title);
        btn_running2_slope_add.setOnClickListener(this);
        btn_running2_slope_minus.setOnClickListener(this);
        btn_running2_speed_add.setOnClickListener(this);
        btn_running2_speed_minus.setOnClickListener(this);
        running2_stop.setOnClickListener(this);
        running2_pause.setOnClickListener(this);

        btn_running2_slope_add.setOnTouchListener(this);
        btn_running2_slope_minus.setOnTouchListener(this);
        btn_running2_speed_add.setOnTouchListener(this);
        btn_running2_speed_minus.setOnTouchListener(this);

        btn_running2_slope_add.setOnLongClickListener(this);
        btn_running2_slope_minus.setOnLongClickListener(this);
        btn_running2_speed_add.setOnLongClickListener(this);
        btn_running2_speed_minus.setOnLongClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然接收不到
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
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置标题并设置当前运动模式
    public void onTitle(setTitle_Run2 title) {
        running2_title.setText(title.getmTitle());
        switch (title.getMode()) {
            case SportData.RUNNING_MODE_WARMUP://热身运动
                SportData.setStatus(SportData.RUNNING_MODE_WARMUP);
                CountDown_PauseFlag = 0;
                time = title.getTime();
                Arr_speed = title.getSpeed();
                Arr_slope = title.getSlope();
                flag_pause = 0;
                racIndex = 0;
                surplusTime = 0;
                rv_1.upDataRec(Arr_speed, 0);
                rv_1.upDataLine(Arr_slope);
                racTime = TimeUtils.RacTime(time);//一条能量柱时间
                surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime);//减去一条能量柱所剩余的时间
                SportData.setSpeed(Arr_speed[racIndex]);
                SportData.setSlope((int) Arr_slope[racIndex]);
                handler.postDelayed(runnable, 500);
                ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                        SportData.RUNNING_MODE_WARMUP);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_CLIMB://爬山运动
                SportData.setStatus(SportData.RUNNING_MODE_CLIMB);
                CountDown_PauseFlag = 0;
                time = title.getTime();
                Arr_speed = title.getSpeed();
                Arr_slope = title.getSlope();
                flag_pause = 0;
                racIndex = 0;
                surplusTime = 0;
                rv_1.upDataRec(Arr_speed, 0);
                rv_1.upDataLine(Arr_slope);
                racTime = TimeUtils.RacTime(time);//一条能量柱时间
                surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime);//减去一条能量柱所剩余的时间
                SportData.setSpeed(Arr_speed[racIndex]);
                SportData.setSlope((int) Arr_slope[racIndex]);
                handler.postDelayed(runnable, 500);
                ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                        SportData.RUNNING_MODE_CLIMB);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_AEROBIC://有氧运动
                SportData.setStatus(SportData.RUNNING_MODE_AEROBIC);
                CountDown_PauseFlag = 0;
                time = title.getTime();
                Arr_speed = title.getSpeed();
                Arr_slope = title.getSlope();
                flag_pause = 0;
                racIndex = 0;
                surplusTime = 0;
                rv_1.upDataRec(Arr_speed, 0);
                rv_1.upDataLine(Arr_slope);
                racTime = TimeUtils.RacTime(time);//一条能量柱时间
                surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime);//减去一条能量柱所剩余的时间
                SportData.setSpeed(Arr_speed[racIndex]);
                SportData.setSlope((int) Arr_slope[racIndex]);
                handler.postDelayed(runnable, 500);
                ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                        SportData.RUNNING_MODE_AEROBIC);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_HEALTH://保健运动
                SportData.setStatus(SportData.RUNNING_MODE_HEALTH);
                CountDown_PauseFlag = 0;
                time = title.getTime();
                Arr_speed = title.getSpeed();
                Arr_slope = title.getSlope();
                flag_pause = 0;
                racIndex = 0;
                surplusTime = 0;
                rv_1.upDataRec(Arr_speed, 0);
                rv_1.upDataLine(Arr_slope);
                racTime = TimeUtils.RacTime(time);//一条能量柱时间
                surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime);//减去一条能量柱所剩余的时间
                SportData.setSpeed(Arr_speed[racIndex]);
                SportData.setSlope((int) Arr_slope[racIndex]);
                handler.postDelayed(runnable, 500);
                ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                        SportData.RUNNING_MODE_HEALTH);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_LOSEWEIGHT://减肥运动
                SportData.setStatus(SportData.RUNNING_MODE_LOSEWEIGHT);
                CountDown_PauseFlag = 0;
                time = title.getTime();
                Arr_speed = title.getSpeed();
                Arr_slope = title.getSlope();
                flag_pause = 0;
                racIndex = 0;
                surplusTime = 0;
                rv_1.upDataRec(Arr_speed, 0);
                rv_1.upDataLine(Arr_slope);
                racTime = TimeUtils.RacTime(time);//一条能量柱时间
                surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime);//减去一条能量柱所剩余的时间
                SportData.setSpeed(Arr_speed[racIndex]);
                SportData.setSlope((int) Arr_slope[racIndex]);
                handler.postDelayed(runnable, 500);
                ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                        SportData.RUNNING_MODE_LOSEWEIGHT);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_MAJOR://专业运动
                SportData.setStatus(SportData.RUNNING_MODE_MAJOR);
                CountDown_PauseFlag = 0;
                time = title.getTime();
                Arr_speed = title.getSpeed();
                Arr_slope = title.getSlope();
                flag_pause = 0;
                racIndex = 0;
                surplusTime = 0;
                rv_1.upDataRec(Arr_speed, 0);
                rv_1.upDataLine(Arr_slope);
                racTime = TimeUtils.RacTime(time);//一条能量柱时间
                surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime);//减去一条能量柱所剩余的时间
                SportData.setSpeed(Arr_speed[racIndex]);
                SportData.setSlope((int) Arr_slope[racIndex]);
                handler.postDelayed(runnable, 500);
                ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                        SportData.RUNNING_MODE_MAJOR);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_TRAINING_USER://用户预设模式
                SportData.setStatus(SportData.RUNNING_MODE_TRAINING_USER);
                CountDown_PauseFlag = 0;
                time = title.getTime();
                Arr_speed = title.getSpeed();
                Arr_slope = title.getSlope();
                flag_pause = 0;
                racIndex = 0;
                surplusTime = 0;
                rv_1.upDataRec(Arr_speed, 0);
                rv_1.upDataLine(Arr_slope);
                racTime = TimeUtils.RacTime(time);//一条能量柱时间
                surplusTime = TimeUtils.MinusTime(TimeUtils.TimeManager(time), racTime);//减去一条能量柱所剩余的时间
                SportData.setSpeed(Arr_speed[racIndex]);
                SportData.setSlope((int) Arr_slope[racIndex]);
                handler.postDelayed(runnable, 500);
                ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                        SportData.RUNNING_MODE_TRAINING_USER);
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置主页面启动键
    public void onPause(setFragmentPause pause) {//Fragment_Main发送的暂停
        if (SportData.isTrainRunning()) {
            if (pause.getPause() == 1) {//暂停
                running2_pause.setBackgroundResource(R.color.colorGreen);
                running2_pause.setText(getContext().getString(R.string.Start));
                flag_pause = 1;
                ServerWindows.CountPause();
                handler.removeCallbacksAndMessages(null);
                if (SportData.getStatus() == SportData.RUNNING_MODE_WARMUP) {
                    CountDown_PauseFlag = 1;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_CLIMB) {
                    CountDown_PauseFlag = 1;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC) {
                    CountDown_PauseFlag = 1;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_HEALTH) {
                    CountDown_PauseFlag = 1;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT) {
                    CountDown_PauseFlag = 1;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_MAJOR) {
                    CountDown_PauseFlag = 1;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_USER) {
                    CountDown_PauseFlag = 1;
                }
            } else if (pause.getPause() == 0) {//启动
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                flag_pause = 0;
                handler.postDelayed(runnable, 500);
                CountDown_PauseFlag = 1;//判断有无暂停过的标志位
                if (SportData.getStatus() == SportData.RUNNING_MODE_WARMUP) {
                    ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                            SportData.RUNNING_MODE_WARMUP);
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_CLIMB) {
                    ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                            SportData.RUNNING_MODE_CLIMB);
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC) {
                    ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                            SportData.RUNNING_MODE_AEROBIC);
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_HEALTH) {
                    ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                            SportData.RUNNING_MODE_HEALTH);
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT) {
                    ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                            SportData.RUNNING_MODE_LOSEWEIGHT);
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_MAJOR) {
                    ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                            SportData.RUNNING_MODE_MAJOR);
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_USER) {
                    ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                            SportData.RUNNING_MODE_TRAINING_USER);
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置主页面启动键
    public void onStop(setStop stop) {//Fragment_Main发送的停止
        if (SportData.isTrainRunning()) {
            if (stop.getStop() == true) {
                //回到最初状态
                Log.i(">>>>>>>>>>>>>>>>服务发送过来的停止", "");
                running2_pause.setBackgroundResource(R.color.colorOrange);
                running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                if (!stop.isMain()) {
                    ServerWindows.RunningEnd();
                }
                TrainEnd();
                handler.removeCallbacksAndMessages(null);
                ((MainActivity) getActivity()).showFragment(isApplication.newPositionFragment, 14);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_running2_speed_add:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSpeed() != SportData.MAXSPEED) {
                                Arr_speed[racIndex] = Arr_speed[racIndex] + 0.1;
                                SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.1));
                            }
                        }
                    }
                }
                break;
            case R.id.btn_running2_speed_minus:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSpeed() != SportData.MINSPEED) {
                                Arr_speed[racIndex] = Arr_speed[racIndex] - 0.1;
                                SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.1));
                            }
                        }
                    }
                }
                break;
            case R.id.btn_running2_slope_add:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSlope() != SportData.MAXSLOPES) {
                                Arr_slope[racIndex] = (int) Arr_slope[racIndex] + 1;
                                SportData.setSlope(SportData.getSlope() + 1);
                            }
                        }
                    }
                }
                break;
            case R.id.btn_running2_slope_minus:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning()) {
                            if (SportData.getSlope() != 0) {
                                Arr_slope[racIndex] = (int) Arr_slope[racIndex] - 1;
                                SportData.setSlope(SportData.getSlope() - 1);
                            }
                        }
                    }
                }
                break;
            case R.id.running2_stop:
                stopHandler(SportData.getSpeed());
                ServerWindows.RunningEnd();
                TrainEnd();
                break;
            case R.id.running2_pause:
                if (flag_pause == 0) {//暂停
                    running2_pause.setBackgroundResource(R.color.colorGreen);
                    running2_pause.setText(getContext().getString(R.string.Start));
                    flag_pause = 1;
                    ServerWindows.CountPause();
                    handler.removeCallbacksAndMessages(null);
                    CountDown_PauseFlag = 1;
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 1  暂停
                    break;
                } else if (flag_pause == 1) {
                    running2_pause.setBackgroundResource(R.color.colorOrange);
                    running2_pause.setText(getContext().getString(R.string.main_mode_pause));
                    flag_pause = 0;
                    handler.postDelayed(runnable, 500);
                    CountDown_PauseFlag = 1;
                    if (SportData.getStatus() == SportData.RUNNING_MODE_WARMUP) {
                        ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                                SportData.RUNNING_MODE_WARMUP);
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_CLIMB) {
                        ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                                SportData.RUNNING_MODE_CLIMB);
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_AEROBIC) {
                        ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                                SportData.RUNNING_MODE_AEROBIC);
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_HEALTH) {
                        ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                                SportData.RUNNING_MODE_HEALTH);
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LOSEWEIGHT) {
                        ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                                SportData.RUNNING_MODE_LOSEWEIGHT);
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_MAJOR) {
                        ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                                SportData.RUNNING_MODE_MAJOR);
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_USER) {
                        ServerWindows.CountDown_Train(TimeUtils.TimeManager(time), CountDown_PauseFlag,
                                SportData.RUNNING_MODE_TRAINING_USER);
                    }
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 0  启动
                    break;
                }

                break;
            case R.id.running2_ing:

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_running2_slope_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running2_slope_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running2_slope_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running2_slope_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running2_slope_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running2_slope_minus.setBackgroundResource(R.drawable.btn_minus_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running2_speed_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running2_speed_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running2_speed_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running2_speed_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running2_speed_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running2_speed_minus.setBackgroundResource(R.drawable.btn_minus_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
        }
        return false;
    }


    private void sendResult(int status) {//提供给service倒计时结束调到二维码页面
        switch (status) {
            case SportData.RUNNING_MODE_WARMUP:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_WARMUP));
                break;
            case SportData.RUNNING_MODE_CLIMB:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_CLIMB));
                break;
            case SportData.RUNNING_MODE_AEROBIC:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_AEROBIC));
                break;
            case SportData.RUNNING_MODE_HEALTH:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_HEALTH));
                break;
            case SportData.RUNNING_MODE_LOSEWEIGHT:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_LOSEWEIGHT));
                break;
            case SportData.RUNNING_MODE_MAJOR:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_MAJOR));
                break;
            case SportData.RUNNING_MODE_TRAINING_USER:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_USER));
                break;
        }
    }

    public static void TrainEnd() {
        flag_pause = 0;
        racTime = 0;
        surplusTime = 0;
        racIndex = 0;
        pop = true;
    }

    // 判断按钮是否快速点击
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private Handler longHandler;
    Runnable Run_SpeedAdd = new Runnable() {
        @Override
        public void run() {
                if (SportData.getSpeed() != SportData.MAXSPEED) {
                    Arr_speed[racIndex] = Arr_speed[racIndex] + 0.2;
                    SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.2));
                }
            longHandler.postDelayed(Run_SpeedAdd, 500);
        }
    };

    Runnable Run_SpeedSub = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSpeed() != SportData.MINSPEED) {
                Arr_speed[racIndex] = Arr_speed[racIndex] - 0.2;
                SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.2));
            }
            longHandler.postDelayed(Run_SpeedSub, 500);
        }
    };

    Runnable Run_SlopeAdd = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSlope() != SportData.MAXSLOPES) {
                Arr_slope[racIndex] = (int) Arr_slope[racIndex] + 1;
                SportData.setSlope(SportData.getSlope() + 1);
            }
            longHandler.postDelayed(Run_SlopeAdd, 500);
        }
    };

    Runnable Run_SlopeSub = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSlope() != 0) {
                Arr_slope[racIndex] = (int) Arr_slope[racIndex] - 1;
                SportData.setSlope(SportData.getSlope() - 1);
            }
            longHandler.postDelayed(Run_SlopeSub, 500);
        }
    };

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_running2_speed_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedAdd, 500);
                break;
            case R.id.btn_running2_speed_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedSub, 500);
                break;
            case R.id.btn_running2_slope_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeAdd, 500);
                break;
            case R.id.btn_running2_slope_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeSub, 500);
                break;
        }
        return false;
    }
}
