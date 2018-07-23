package com.vigorchip.omatreadmill.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.ServiceToResult;
import com.vigorchip.omatreadmill.eventbus.setFragmentPause;
import com.vigorchip.omatreadmill.eventbus.setMainClickVisibility;
import com.vigorchip.omatreadmill.eventbus.setMainPause;
import com.vigorchip.omatreadmill.eventbus.setSerialRun;
import com.vigorchip.omatreadmill.eventbus.setSerialStop;
import com.vigorchip.omatreadmill.eventbus.setStop;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.start_treadmill_anim.Start_Treadmill;
import com.vigorchip.omatreadmill.utils.AnimateUtils;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.DoubleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by wr-app1 on 2018/3/27.
 */

public class Fragment_Main extends Fragment implements View.OnClickListener {
    private RelativeLayout click_Mode_Time, click_Mode_Distance, click_Mode_Calories,
            click_StartRunning, click_StopRunning, click_PauseRunning, click_Net, click_Mode_Train, click_Mode_Live,
            click_Settings, click_Media, click_More, click_Users, Stopping;
    private TextView tv_main_pause;
    private View view;

    private int FlagMain_PAUSE = 0;

    public long CountUp_PauseTime = 0;//获取正计时暂停时的时间，保存起来
    public long CountDown_PauseTime = 0;//获取倒计时暂停时的时间，保存起来

    private double SPEED;//速度
    private boolean FLAG_STOPPING = false;//停止中中的标志位，确保只走一次
    private int LastStatus = 0;//获取上一次的速度
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {//停止中线程
        @Override
        public void run() {
            Log.e("查看走了几遍2", String.valueOf(SPEED));
            Log.e("查看走了几遍a", String.valueOf(ServerWindows.RESULT_MAX_SPEED));
            if (!FLAG_STOPPING) {
                LastStatus = SportData.getStatus();
                Stopping.setVisibility(View.VISIBLE);
                click_StartRunning.clearAnimation();
                click_StopRunning.clearAnimation();
                click_PauseRunning.clearAnimation();
                click_StartRunning.setVisibility(View.GONE);
                click_StopRunning.setVisibility(View.GONE);
                click_PauseRunning.setVisibility(View.GONE);
                SportData.setStatus(SportData.STOPPING_TREADMILL);
                FLAG_STOPPING = true;
            }
            if (SPEED != 0.0) {
                SPEED = DoubleUtils.sub(SPEED, 0.1);
                handler.postDelayed(this, 50);
                Log.e("查看停止中的速度", String.valueOf(SPEED));
            } else if (SPEED == 0.0) {
                SportData.setStatus(LastStatus);
                click_PauseRunning.setBackgroundResource(R.color.colorOrange);
                tv_main_pause.setText(getContext().getString(R.string.main_mode_pause));
                FlagMain_PAUSE = 0;
                FLAG_STOPPING = false;
                click_StartRunning.clearAnimation();
                click_StopRunning.clearAnimation();
                click_PauseRunning.clearAnimation();
                click_StartRunning.setVisibility(View.VISIBLE);
                Stopping.setVisibility(View.GONE);
                Log.e("查看走了几遍c", String.valueOf(ServerWindows.RESULT_MAX_SPEED));
                EventBus.getDefault().postSticky(new setStop(true,true));//发送给跑步界面回初始状态
                sendResult(SportData.getStatus());//提供给service倒计时结束调到二维码页面
                LastStatus = 0;
//                ((MainActivity) getActivity()).showFragment(isApplication.newPositionFragment, 14);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    private void stopHandler(double speed) {//停止中线程
        this.SPEED = speed;
        handler.postDelayed(runnable, 50);
        Log.e("查看走了几遍1", String.valueOf(SPEED));
    }

    private void initView() {
        tv_main_pause = view.findViewById(R.id.tv_main_pause);
        click_Mode_Time = view.findViewById(R.id.click_Mode_Time);
        click_Mode_Distance = view.findViewById(R.id.click_Mode_Distance);
        click_Mode_Calories = view.findViewById(R.id.click_Mode_Calories);
        click_Mode_Train = view.findViewById(R.id.click_Mode_Train);
        click_Mode_Live = view.findViewById(R.id.click_Mode_Live);
        click_Mode_Time.setOnClickListener(this);
        click_Mode_Distance.setOnClickListener(this);
        click_Mode_Calories.setOnClickListener(this);
        click_Mode_Train.setOnClickListener(this);
        click_Mode_Live.setOnClickListener(this);
        AnimateUtils.setClickAnimation(getContext(), click_Mode_Time);
        AnimateUtils.setClickAnimation(getContext(), click_Mode_Distance);
        AnimateUtils.setClickAnimation(getContext(), click_Mode_Calories);
        AnimateUtils.setClickAnimation(getContext(), click_Mode_Train);
        AnimateUtils.setClickAnimation(getContext(), click_Mode_Live);

        click_StartRunning = view.findViewById(R.id.click_StartRunning);
        Stopping = view.findViewById(R.id.Stopping);
        click_StopRunning = view.findViewById(R.id.click_StopRunning);
        click_PauseRunning = view.findViewById(R.id.click_PauseRunning);
        click_Net = view.findViewById(R.id.click_Net);
        click_More = view.findViewById(R.id.click_More);
        click_Users = view.findViewById(R.id.click_Users);
        click_Settings = view.findViewById(R.id.click_Settings);
        click_Media = view.findViewById(R.id.click_Media);
        click_StartRunning.setOnClickListener(this);
        click_StopRunning.setOnClickListener(this);
        click_PauseRunning.setOnClickListener(this);
        click_Net.setOnClickListener(this);
        click_More.setOnClickListener(this);
        click_Users.setOnClickListener(this);
        click_Settings.setOnClickListener(this);
        click_Media.setOnClickListener(this);
        AnimateUtils.setClickAnimation(getContext(), click_StartRunning);
        AnimateUtils.setClickAnimation(getContext(), click_StopRunning);
        AnimateUtils.setClickAnimation(getContext(), click_PauseRunning);
        AnimateUtils.setClickAnimation(getContext(), click_Net);
        AnimateUtils.setClickAnimation(getContext(), click_More);
        AnimateUtils.setClickAnimation(getContext(), click_Users);
        AnimateUtils.setClickAnimation(getContext(), click_Settings);
        AnimateUtils.setClickAnimation(getContext(), click_Media);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然收不到
        }

    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onPause(setMainPause pause) {
        if (pause.getPause() == 0) {//0 启动
            click_StartRunning.clearAnimation();
            click_PauseRunning.clearAnimation();
            click_PauseRunning.setBackgroundResource(R.color.colorOrange);
            tv_main_pause.setText(getContext().getString(R.string.main_mode_pause));
            FlagMain_PAUSE = 0;
        } else if (pause.getPause() == 1) {//1 暂停
            click_PauseRunning.clearAnimation();
            click_PauseRunning.setBackgroundResource(R.color.colorGreen);
            tv_main_pause.setText(getContext().getString(R.string.Start));
            FlagMain_PAUSE = 1;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onVisibility(setMainClickVisibility flag) {
        if (flag.getMainClickVisibility() == 1) {//1 分开启动键
            FlagMain_PAUSE = 0;
            click_PauseRunning.setBackgroundResource(R.color.colorOrange);
            tv_main_pause.setText(getContext().getString(R.string.main_mode_pause));
            click_StartRunning.clearAnimation();
            click_StartRunning.setVisibility(View.GONE);
            click_StopRunning.setVisibility(View.VISIBLE);
            click_PauseRunning.setVisibility(View.VISIBLE);
        } else if (flag.getMainClickVisibility() == 0) {//0 显示启动键
            click_PauseRunning.setBackgroundResource(R.color.colorOrange);
            tv_main_pause.setText(getContext().getString(R.string.main_mode_pause));
            FlagMain_PAUSE = 0;
            click_StopRunning.clearAnimation();
            click_PauseRunning.clearAnimation();
            click_StartRunning.setVisibility(View.VISIBLE);
            click_StopRunning.setVisibility(View.GONE);
            click_PauseRunning.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onSerialRun(setSerialRun serialRun) {//串口开始键
        if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
            if (serialRun.getSerialRun()) {
                if (SportData.isRunning() &&
                        SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {
                    ((MainActivity) getActivity()).showFragment(0, 16);
                } else if (SportData.isRunning() &&
                        SportData.getStatus() != SportData.RUNNING_MODE_MANUAL) {
                    CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                } else if (!SportData.isRunning()) {
                    Start_Treadmill.AnimDialog(getContext(), Fragment_Main.this,
                            isApplication.newPositionFragment, 16,
                            getContext().getString(R.string.main_mode_quick), SportData.RUNNING_MODE_MANUAL, 1, "0", "0", "0");
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onSerialStop(setSerialStop serialStop) {//串口暂停键
        if (serialStop.isStop() == true && serialStop.isErrorStop() == false) {
            stopHandler(SportData.getSpeed());
            ServerWindows.RunningEnd();
        } else if (serialStop.isStop() == true && serialStop.isErrorStop() == true) {
            click_PauseRunning.setBackgroundResource(R.color.colorOrange);
            tv_main_pause.setText(getContext().getString(R.string.main_mode_pause));
            FlagMain_PAUSE = 0;
            click_StartRunning.clearAnimation();
            click_StopRunning.clearAnimation();
            click_PauseRunning.clearAnimation();
            click_StartRunning.setVisibility(View.VISIBLE);
            click_StopRunning.setVisibility(View.GONE);
            click_PauseRunning.setVisibility(View.GONE);
//            sendResult(SportData.getStatus());//提供给service倒计时结束调到二维码页面
        }
    }

    public static boolean forward = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_StartRunning:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning() &&
                                SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {
                            ((MainActivity) getActivity()).showFragment(0, 16);
                        } else if (SportData.isRunning() &&
                                SportData.getStatus() != SportData.RUNNING_MODE_MANUAL) {
                            CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                        } else if (!SportData.isRunning()) {
                            Start_Treadmill.AnimDialog(getContext(), Fragment_Main.this, 0, 16,
                                    getContext().getString(R.string.main_mode_quick), SportData.RUNNING_MODE_MANUAL, 1, "0", "0", "0");
                        }
                    }
                }
                break;
            case R.id.click_Mode_Time:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning() &&
                                SportData.getStatus() == SportData.RUNNING_MODE_TIME) {
                            ((MainActivity) getActivity()).showFragment(0, 16);
                        } else if (SportData.isRunning() &&
                                SportData.getStatus() != SportData.RUNNING_MODE_TIME) {
                            CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                        } else if (!SportData.isRunning()) {
                            ((MainActivity) getActivity()).showFragment(0, 1);
                        }
                    }
                }
                break;
            case R.id.click_Mode_Distance:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning() &&
                                SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
                            ((MainActivity) getActivity()).showFragment(0, 16);
                        } else if (SportData.isRunning() &&
                                SportData.getStatus() != SportData.RUNNING_MODE_DISTANCE) {
                            CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                        } else if (!SportData.isRunning()) {
                            ((MainActivity) getActivity()).showFragment(0, 2);
                        }
                    }
                }
                break;
            case R.id.click_Mode_Calories:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning() &&
                                SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
                            ((MainActivity) getActivity()).showFragment(0, 16);
                        } else if (SportData.isRunning() &&
                                SportData.getStatus() != SportData.RUNNING_MODE_CALORIES) {
                            CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                        } else if (!SportData.isRunning()) {
                            ((MainActivity) getActivity()).showFragment(0, 3);
                        }
                    }
                }
                break;
            case R.id.click_Mode_Train:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        ((MainActivity) getActivity()).showFragment(0, 4);
                    }
                }
                break;
            case R.id.click_Mode_Live:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.isRunning() &&
                                SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            ((MainActivity) getActivity()).showFragment(0, 21);
                        } else if (SportData.isRunning() &&
                                SportData.getStatus() != SportData.RUNNING_MODE_LIVE) {
                            CreatDialog.showRunningDialog(getContext(), SportData.IndexSportMode(SportData.getStatus()));
                        } else if (!SportData.isRunning()) {
                            ((MainActivity) getActivity()).showFragment(0, 5);
                        }
                    }
                }
                break;
            case R.id.click_Media:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        ((MainActivity) getActivity()).showFragment(0, 6);
                    }
                }
                break;
            case R.id.click_Settings:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        ((MainActivity) getActivity()).showFragment(0, 7);
                    }
                }
                break;
            case R.id.click_StopRunning:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        stopHandler(SportData.getSpeed());
                        ServerWindows.RunningEnd();
                    }
                }
                break;
            case R.id.click_PauseRunning:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (FlagMain_PAUSE == 0) {//暂停
                            click_StartRunning.clearAnimation();
                            click_StopRunning.clearAnimation();
                            click_PauseRunning.clearAnimation();
                            click_PauseRunning.setBackgroundResource(R.color.colorGreen);
                            tv_main_pause.setText(getContext().getString(R.string.Start));
                            FlagMain_PAUSE = 1;
                            EventBus.getDefault().postSticky(new setFragmentPause(1));//发送 0  暂停
                            Log.i("暂停还是启动1", String.valueOf(FlagMain_PAUSE));
                            break;
                        } else if (FlagMain_PAUSE == 1) {//启动
                            if (Fragment_Running3.flag_pause == 1) {
                                forward = true;
                            }
                            click_StartRunning.clearAnimation();
                            click_StopRunning.clearAnimation();
                            click_PauseRunning.clearAnimation();
                            click_PauseRunning.setBackgroundResource(R.color.colorOrange);
                            tv_main_pause.setText(getContext().getString(R.string.main_mode_pause));
                            FlagMain_PAUSE = 0;
                            EventBus.getDefault().postSticky(new setFragmentPause(0));//发送 1  启动
                            if (SportData.isMainRunning()) {
                                ((MainActivity) getActivity()).showFragment(0, 16);
                            } else if (SportData.isTrainRunning()) {
                                ((MainActivity) getActivity()).showFragment(0, 17);
                            } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                                ((MainActivity) getActivity()).showFragment(0, 21);
                            } else if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
                                ((MainActivity) getActivity()).showFragment(0, 22);
                            }
                            Log.i("暂停还是启动2", String.valueOf(FlagMain_PAUSE));
                            break;
                        }
                    }
                }
                break;
            case R.id.click_Net:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (getActivity().getPackageManager().
                                getLaunchIntentForPackage("com.android.chrome") != null) {
                            startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.android.chrome"));
                        }
                    }
                }
                break;
            case R.id.click_Users:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        ((MainActivity) getActivity()).showFragment(0, 8);
                    }
                }
                break;
            case R.id.click_More:
                if (isApplication.newPositionFragment != 14) {//防止结束页面重叠
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        ((MainActivity) getActivity()).showFragment(0, 9);
                    }
                }
                break;
        }
    }

    private void sendResult(int status) {//提供给service倒计时结束调到二维码页面
        switch (status) {
            case SportData.RUNNING_MODE_MANUAL:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_MANUAL));
                break;
            case SportData.RUNNING_MODE_TIME:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TIME));
                break;
            case SportData.RUNNING_MODE_DISTANCE:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_DISTANCE));
                break;
            case SportData.RUNNING_MODE_CALORIES:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_CALORIES));
                break;
            case SportData.RUNNING_MODE_LIVE:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_LIVE));
                break;
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
            case SportData.RUNNING_MODE_TRAINING_HEART:
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_TRAINING_HEART));
                break;
        }
    }

}
