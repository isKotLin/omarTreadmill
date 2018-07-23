package com.vigorchip.omatreadmill.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.ServiceToResult;
import com.vigorchip.omatreadmill.eventbus.setFragmentPause;
import com.vigorchip.omatreadmill.eventbus.setLiveVideoPath;
import com.vigorchip.omatreadmill.eventbus.setMainClickVisibility;
import com.vigorchip.omatreadmill.eventbus.setMainPause;
import com.vigorchip.omatreadmill.eventbus.setStop;
import com.vigorchip.omatreadmill.interfaci.setPlayerSpeed;
import com.vigorchip.omatreadmill.serialport.newSerial;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.DoubleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

import static android.widget.ImageView.ScaleType.FIT_XY;
import static com.vigorchip.omatreadmill.application.isApplication.RUNNING3;

/**
 * Created by wr-app1 on 2018/4/26.
 * 实景运动界面：
 * 实景跑步模式，背景音乐和视频声音是分开播放的
 */

public class Fragment_Running3 extends Fragment implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private View view;
    public static boolean hide;
    public static io.vov.vitamio.widget.VideoView liveVideo;
    private ImageView video_img;
    private LinearLayout control_slope, control_speed, control_stop_pause, control_stopping;
    private Button btn_running3_slope_add, btn_running3_slope_minus, btn_running3_speed_add,
            btn_running3_speed_minus, running3_ing, running3_stop, running3_pause;

    private static MediaPlayer mediaPlayer;//视频mediaplayer
    private android.media.MediaPlayer mp_bgMusic;//背景音乐mediaplayer
    private float currentPlaySpeed = 0.6f;//当前视频播放倍速
    private long cur_bgMusic = 0;
    private static long cur_mPlayer = 0;

    //实景开始跑步动画
    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private Animation animation4;
    private Animation animation5;
    private Animation animation6;
    private Dialog dialog;

    //VideoView点击动画
    private Animation VideoClickShow;
    private Animation VideoClickHidden;

    private long CountUp_PauseTime = 0;//获取正计时暂停时的时间，保存起来
    public static int flag_pause = 0;//判断暂停的标志位

    private File file;//视频文件
    private String path;

//    private static Handler handler_player = new Handler();
//
//    private Runnable run_player = new Runnable() {
//        long currentPosition;
//        public void run() {
//            //获得当前播放时间和当前视频的长度
//            currentPosition = liveVideo.getCurrentPosition();
//            Log.e("查看有没有走这个线程1", String .valueOf(currentPosition) + "    " +
//                    String.valueOf(cur_mPlayer) + "    " +
//                    String.valueOf(mediaPlayer.isPlaying()));
//            if (flag_pause == 1) {
//                forwardSong();
//                handler_player.removeCallbacks(run_player);
//            }
//        }
//    };

    private static Handler handler_seek = new Handler();
    private int m = 1;
    private Runnable run_seek = new Runnable() {
        long currentPosition;
        long currentPosition1;

        public void run() {
            // 获得当前播放时间和当前视频的长度
            currentPosition = mediaPlayer.getCurrentPosition();
            if (m == 1) {
                m = 2;
                currentPosition1 = mediaPlayer.getCurrentPosition();
            }
            Log.e("这个方法牛逼坏了3", String.valueOf(currentPosition) + "  " + currentPosition1 + mediaPlayer.isPlaying());
            if (currentPosition > currentPosition1) {
                if (flag_pause == 1) {
                    mediaPlayer.pause();
                } else if (flag_pause == 0) {
                    mediaPlayer.start();
                }
                handler_seek.removeCallbacks(run_seek);
                m = 1;
            } else {
                handler_seek.postDelayed(run_seek, 1);
            }
        }
    };

    private static Handler handler = new Handler();
    private Runnable run = new Runnable() {//显示隐藏时发送的线程
        public void run() {
            if (flag_pause == 0) {//开始状态
                if (mediaPlayer != null) {
                    forwardSong();
                    Log.e("查看当前开始的视频时间", String.valueOf(mediaPlayer.getCurrentPosition()));
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
                if (mp_bgMusic != null) {
                    Log.e("查看当前开始的背景音乐时间", String.valueOf(mediaPlayer.getCurrentPosition()));
                    if (!mp_bgMusic.isPlaying()) {
                        mp_bgMusic.start();
                    }
                }
            } else if (flag_pause == 1) {//暂停状态
                Log.e("查看有没有走这个线程", String.valueOf(mediaPlayer.isPlaying()) + "    " + String.valueOf(handler == null));
                forwardSong();
                mediaPlayer.pause();
            }
//            Log.e("查看有没有走这个线程1", String.valueOf(currentPosition) + "    " + String.valueOf(cur_mPlayer));
//            if (flag_pause == 1) {
//                mediaPlayer.pause();
//                handler.removeCallbacks(run);
//                Log.e("查看有没有走这个线程2", String.valueOf(mediaPlayer.isPlaying()) + "    " + String.valueOf(handler == null));
//            } else if (flag_pause == 0) {
//                handler.postDelayed(run, 1);
//                Log.e("查看有没有走这个线程3", String.valueOf(currentPosition) + "    " + String.valueOf(cur_mPlayer));
//            }
        }
    };

    private static long isPosition;

    public void forwardSong() {//此方法是用来当实景运动时，切换别的应用再切换回来，seekTo无效时做处理
        if (mediaPlayer != null) {
            isPosition = mediaPlayer.getCurrentPosition();
            Log.e("这个方法牛逼坏了4", String.valueOf(cur_mPlayer) + "  " + String.valueOf(isPosition + cur_mPlayer <= mediaPlayer.getDuration()));
            if (isPosition + cur_mPlayer <= mediaPlayer.getDuration()) {
                if (isPosition != cur_mPlayer) {
                    mediaPlayer.seekTo((cur_mPlayer) - 500);
                    handler_seek.post(run_seek);
                }
                Log.e("这个方法牛逼坏了1", String.valueOf(isPosition) + cur_mPlayer + "  " + mediaPlayer.isPlaying());
            }
        }
    }

    private int clickTime = 3000;
    Handler ClickHandler = new Handler() {//控制器显示隐藏的线程
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (control_slope.getVisibility() == View.VISIBLE &&
                            control_speed.getVisibility() == View.VISIBLE &&
                            control_stop_pause.getVisibility() == View.VISIBLE) {
                        control_slope.startAnimation(VideoClickHidden);
                        control_speed.startAnimation(VideoClickHidden);
                        control_stop_pause.startAnimation(VideoClickHidden);
                        control_slope.setVisibility(View.GONE);
                        control_speed.setVisibility(View.GONE);
                        control_stop_pause.setVisibility(View.GONE);
                    }
                    Log.i("打印查看线程", String.valueOf(msg.what));
                    break;
            }
        }
    };

    private double SPEED = 0.0;//速度
    private boolean FLAG_STOPPING = false;//停止中中的标志位，确保只走一次
    private int LastStatus = 0;//获取上一次的速度
    private Handler stopping_handler = new Handler();
    Runnable stopping_runnable = new Runnable() {//停止中线程
        @Override
        public void run() {
            Log.e("查看走了几遍2", String.valueOf(SPEED));
            if (!FLAG_STOPPING) {
                LastStatus = SportData.getStatus();
                SportData.setStatus(SportData.STOPPING_TREADMILL);
                control_stopping.setVisibility(View.VISIBLE);
                running3_pause.setVisibility(View.GONE);
                running3_stop.setVisibility(View.GONE);
                FLAG_STOPPING = true;
                CountUp_PauseTime = 0;//清零
                flag_pause = 0;
                mediaPlayer.pause();
            }
            if (SPEED != 0.0) {
                SPEED = DoubleUtils.sub(SPEED, 0.1);
                stopping_handler.postDelayed(this, 50);
                Log.e("查看停止中的速度", String.valueOf(SPEED));
            } else if (SPEED == 0.0) {
                SportData.setStatus(LastStatus);
                FLAG_STOPPING = false;
                LastStatus = 0;
                control_stopping.setVisibility(View.GONE);
                running3_pause.setVisibility(View.GONE);
                running3_stop.setVisibility(View.GONE);
                running3_ing.setVisibility(View.VISIBLE);
                running3_pause.setBackgroundResource(R.color.colorOrange);
                running3_pause.setText("暂停");
                mediaPlayer.seekTo(0);
                ClickHandler.removeMessages(1);
                ClickHandler.removeCallbacksAndMessages(null);
                mediaPlayer.stop();
                mediaPlayer = null;
                control_slope.setVisibility(View.VISIBLE);
                control_speed.setVisibility(View.VISIBLE);
                control_stop_pause.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).showFragment(21, 14);
                EventBus.getDefault().postSticky(new ServiceToResult(true, SportData.RUNNING_MODE_LIVE));
                EventBus.getDefault().postSticky(new setMainClickVisibility(0));//1 分开主页面启动键  0显示主页面启动键
                liveVideo.setVisibility(View.GONE);
                Log.e("查看走了几遍3", String.valueOf(SPEED));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_running3, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Fragment_Running3, onResume方法", String.valueOf(hide));
        if (isApplication.RUNNING3 == false) {
            RUNNING3 = true;
            init();
            Vitamio.isInitialized(getContext());
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然收不到
            }
        }
    }

    Handler mp_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    Log.e("mediaPlayer是否在app退出栈顶回来后自动播放1", String.valueOf(mediaPlayer.isPlaying()) + "  " + flag_pause + "  " + String.valueOf(mediaPlayer != null) + "    "+cur_mPlayer);
//                    if (flag_pause == 1) {
//                        if (mediaPlayer != null) {
//                            Log.e("mediaPlayer是否在app退出栈顶回来后自动播放2", String.valueOf(mediaPlayer.isPlaying()) + "    "+cur_mPlayer);
////                            mediaPlayer.seekTo(25191);
//                            if (mediaPlayer.isPlaying()) {
//                                mediaPlayer.pause();
//                                Log.e("mediaPlayer是否在app退出栈顶回来后自动播放3", String.valueOf(mediaPlayer.isPlaying()) + "    "+cur_mPlayer);
//                            }
//                        }
//                    } else if (flag_pause == 0) {
//                        if (mediaPlayer != null) {
////                            mediaPlayer.seekTo(cur_mPlayer);
////                            mediaPlayer.seekTo(25191);
//                            if (!mediaPlayer.isPlaying()) {
//                                mediaPlayer.start();
//                                Log.e("mediaPlayer是否在app退出栈顶回来后自动播放4", String.valueOf(mediaPlayer.isPlaying()) + "    "+cur_mPlayer);
//                            }
//                        }
//                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (hide) {
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                    }
                                    cur_mPlayer = liveVideo.getCurrentPosition();
                                    Log.e("查看当前暂停的视频时间1", String.valueOf(cur_mPlayer));
                                }
                                if (mp_bgMusic != null) {
                                    if (mp_bgMusic.isPlaying()) {
                                        mp_bgMusic.pause();
                                    }
                                    Log.e("查看当前暂停的背景音乐时间", String.valueOf(mediaPlayer.getCurrentPosition()));
                                }
                            } else {
                                if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                                    handler.post(run);
                                    Log.e("这里走了几遍", String.valueOf(cur_mPlayer));
                                }
                            }
                        }
                    }).start();
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hide = hidden;
        Message message = new Message();
        message.what = 1;
        mp_handler.handleMessage(message);
        Log.e("Fragment_Running3, onHiddenChanged方法", String.valueOf(hide));
    }

    private void stopHandler(double speed) {//停止中线程
        this.SPEED = speed;
        stopping_handler.postDelayed(stopping_runnable, 50);
        Log.e("查看走了几遍1", String.valueOf(SPEED));
    }

    private SharedPreferences sp_loadPH;
    private String PH;

    private void init() {
        //视频播放器
        liveVideo = view.findViewById(R.id.liveVideo);
        liveVideo.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);//设置播放画质 高画质
        video_img = view.findViewById(R.id.video_img);
        //控制器布局
        control_slope = view.findViewById(R.id.control_slope);
        control_speed = view.findViewById(R.id.control_speed);
        control_stop_pause = view.findViewById(R.id.control_stop_pause);
        control_stopping = view.findViewById(R.id.control_stopping);
        //控制器按键
        btn_running3_slope_add = view.findViewById(R.id.btn_running3_slope_add);
        btn_running3_slope_minus = view.findViewById(R.id.btn_running3_slope_minus);
        btn_running3_speed_add = view.findViewById(R.id.btn_running3_speed_add);
        btn_running3_speed_minus = view.findViewById(R.id.btn_running3_speed_minus);
        running3_ing = view.findViewById(R.id.running3_ing);
        running3_stop = view.findViewById(R.id.running3_stop);
        running3_pause = view.findViewById(R.id.running3_pause);
        liveVideo.setOnTouchListener(this);
        running3_ing.setOnClickListener(this);
        running3_stop.setOnClickListener(this);
        running3_pause.setOnClickListener(this);

        btn_running3_slope_add.setOnClickListener(this);
        btn_running3_slope_minus.setOnClickListener(this);
        btn_running3_speed_add.setOnClickListener(this);
        btn_running3_speed_minus.setOnClickListener(this);

        btn_running3_slope_add.setOnLongClickListener(this);
        btn_running3_slope_minus.setOnLongClickListener(this);
        btn_running3_speed_add.setOnLongClickListener(this);
        btn_running3_speed_minus.setOnLongClickListener(this);

        btn_running3_slope_add.setOnTouchListener(this);
        btn_running3_slope_minus.setOnTouchListener(this);
        btn_running3_speed_add.setOnTouchListener(this);
        btn_running3_speed_minus.setOnTouchListener(this);
        VideoClickShow = AnimationUtils.loadAnimation(getContext(), R.anim.video_clickin);
        VideoClickHidden = AnimationUtils.loadAnimation(getContext(), R.anim.video_clickout);
        CreatDialog.setPlayerSpeed(new setPlayerSpeed() {
            @Override
            public void OnPlayerSpeed(float arg0) {
                mediaPlayer.setPlaybackSpeed(arg0);
                Log.i("dialog传来的倍速", String.valueOf(arg0));
            }
        });
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

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//接收传过来的实景视频路径
    public void onVideoPath(setLiveVideoPath videoPath) {
        if (!videoPath.getLiveVideoPath().equals("")) {
            file = new File(videoPath.getLiveVideoPath());
            Log.i(">>>>查看视频路径：", videoPath.getLiveVideoPath());
            Log.i(">>>>查看视频文件：", String.valueOf(file) + "    判断:" + String.valueOf(file == null));
            if (file.exists()) {
                path = videoPath.getLiveVideoPath();
                switch (path) {
                    case "/system/treadmill/video_01.mp4":
                        video_img.setImageResource(R.drawable.video_img1);
                        video_img.setVisibility(View.VISIBLE);
                        if (liveVideo.getVisibility() == View.VISIBLE) {
                            liveVideo.setVisibility(View.GONE);
                        }
                        break;
                    case "/system/treadmill/video_02.mp4":
                        video_img.setImageResource(R.drawable.video_img2);
                        video_img.setScaleType(FIT_XY);
                        video_img.setVisibility(View.VISIBLE);
                        if (liveVideo.getVisibility() == View.VISIBLE) {
                            liveVideo.setVisibility(View.GONE);
                        }
                        break;
                    case "/system/treadmill/video_03.mp4":
                        video_img.setImageResource(R.drawable.video_img3);
                        video_img.setVisibility(View.VISIBLE);
                        if (liveVideo.getVisibility() == View.VISIBLE) {
                            liveVideo.setVisibility(View.GONE);
                        }
                        break;
                    case "/system/treadmill/video_04.mp4":
                        video_img.setImageResource(R.drawable.video_img4);
                        video_img.setVisibility(View.VISIBLE);
                        if (liveVideo.getVisibility() == View.VISIBLE) {
                            liveVideo.setVisibility(View.GONE);
                        }
                        break;
                    case "/system/treadmill/video_05.mp4":
                        video_img.setImageResource(R.drawable.video_img5);
                        video_img.setVisibility(View.VISIBLE);
                        if (liveVideo.getVisibility() == View.VISIBLE) {
                            liveVideo.setVisibility(View.GONE);
                        }
                        break;
                    case "/system/treadmill/video_06.mp4":
                        video_img.setImageResource(R.drawable.video_img6);
                        video_img.setVisibility(View.VISIBLE);
                        if (liveVideo.getVisibility() == View.VISIBLE) {
                            liveVideo.setVisibility(View.GONE);
                        }
                        break;
                }
            } else {
                Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                toast.show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置主页面启动键
    public void onPause(setFragmentPause pause) {//Fragment_Main发送的暂停
        Log.i("主页面发送给实景的暂停", String.valueOf(pause.getPause()) + "  " +
                String.valueOf(SportData.getStatus() == SportData.RUNNING_MODE_LIVE));
        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
            if (pause.getPause() == 1) {//暂停
                running3_pause.setBackgroundResource(R.color.colorGreen);
                running3_pause.setText(getContext().getString(R.string.Start));
                flag_pause = 1;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                if (mp_bgMusic.isPlaying()) {
                    mp_bgMusic.pause();
                }
                ServerWindows.CountPause();
                CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
            } else if (pause.getPause() == 0) {//启动
                running3_pause.setBackgroundResource(R.color.colorOrange);
                running3_pause.setText(getContext().getString(R.string.main_mode_pause));
                flag_pause = 0;
                mediaPlayer.setPlaybackSpeed(currentPlaySpeed);
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                if (!mp_bgMusic.isPlaying()) {
                    mp_bgMusic.start();
                }
                ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置主页面启动键
    public void onStop(setStop stop) {//Fragment_Main发送的停止
        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
            if (stop.getStop()) {
                //回到最初状态
                flag_pause = 0;
                CountUp_PauseTime = 0;//清零
                running3_pause.setBackgroundResource(R.color.colorOrange);
                running3_pause.setText(getContext().getString(R.string.main_mode_pause));
                running3_ing.setVisibility(View.VISIBLE);
                running3_pause.setVisibility(View.GONE);
                running3_stop.setVisibility(View.GONE);
                ClickHandler.removeMessages(1);
                ClickHandler.removeCallbacksAndMessages(null);
                control_slope.setVisibility(View.VISIBLE);
                control_speed.setVisibility(View.VISIBLE);
                control_stop_pause.setVisibility(View.VISIBLE);
                liveVideo.setVisibility(View.GONE);
                MusicPlayerStop();
                if (!stop.isMain()) {
                    ServerWindows.RunningEnd();
                }
                ((MainActivity) getActivity()).showFragment(isApplication.newPositionFragment, 14);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_running3_speed_add:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (SportData.getSpeed() != SportData.MAXSPEED) {
                                adj_playSpeed();
                                SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.1));
                            }
                        }
                        Log.i("查看当前倍速:", String.valueOf(currentPlaySpeed));
                    }
                }
                break;
            case R.id.btn_running3_speed_minus:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (SportData.getSpeed() != SportData.MINSPEED) {
                                adj_playSpeed();
                                SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.1));
                            }
                        }
                        Log.i("查看当前倍速:", String.valueOf(currentPlaySpeed));
                    }
                }
                break;
            case R.id.btn_running3_slope_add:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (SportData.getSlope() != SportData.MAXSLOPES) {
                                if (SportData.getSlope() < 15) {
                                    SportData.setSlope(SportData.getSlope() + 1);
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.btn_running3_slope_minus:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.getStatus() != SportData.STOPPING_TREADMILL) {
                        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (SportData.getSlope() != 0) {
                                SportData.setSlope(SportData.getSlope() - 1);
                            }
                        }
                    }
                }
                break;
            case R.id.running3_ing:
                if (file.exists()) {
                    LiveAnimDialog(getContext(), SportData.MINSPEED, 0);
                } else {
                    Toast toast = Toast.makeText(getContext(), "No File", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 380);
                    toast.show();
                }
                break;
            case R.id.running3_pause:
                if (flag_pause == 0) {//暂停
                    running3_pause.setBackgroundResource(R.color.colorGreen);
                    running3_pause.setText(R.string.Start);
                    flag_pause = 1;
                    ServerWindows.CountPause();
                    CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
                    mediaPlayer.pause();
                    mp_bgMusic.pause();
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 1  暂停
                    break;
                } else if (flag_pause == 1) {//启动
                    running3_pause.setBackgroundResource(R.color.colorOrange);
                    running3_pause.setText(R.string.main_mode_pause);
                    flag_pause = 0;
                    mediaPlayer.start();
                    mp_bgMusic.start();
                    ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 0  启动
                    break;
                }
                break;
            case R.id.running3_stop:
                stopHandler(SportData.getSpeed());
                ServerWindows.RunningEnd();
                MusicPlayerStop();
                break;
        }
    }

    private void play() {//mediaplayer播放
        if (mediaPlayer != null) {
            mediaPlayer.start();
            running3_ing.setVisibility(View.GONE);
            running3_pause.setVisibility(View.VISIBLE);
            running3_stop.setVisibility(View.VISIBLE);
            running3_pause.setBackgroundResource(R.color.colorOrange);
            running3_pause.setText(R.string.main_mode_pause);
        }
        Log.e("看看这里走了没", String.valueOf(4));
    }

    private void First_play(final int path) {//第一次开始实景跑步时，调用此方法播放实景背景音乐
        mp_bgMusic = android.media.MediaPlayer.create(getContext(), path);
        mp_bgMusic.start();
        mp_bgMusic.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mp) {
                MusicPlayerCallback(mp, path);
                Log.e("看看这里走了没", String.valueOf(1));
            }
        });
        Log.e("看看这里走了没", String.valueOf(2));
    }

    private void MusicPlayerCallback(android.media.MediaPlayer mp, final int path) {//视频播放完毕的背景音乐回调
        if (mp_bgMusic != null) {
            if (mp_bgMusic.isPlaying()) {
                mp_bgMusic.pause();
            }
            mp.release();
            mp_bgMusic = null;
            mp_bgMusic = android.media.MediaPlayer.create(getContext(), path);
            mp_bgMusic.start();
            Log.e("看看这里走了没", String.valueOf(3));
        }
    }

    private void MusicPlayerStop() {//实景背景音乐停止
        Log.e("看看这里走了没", String.valueOf(7));
        if (mp_bgMusic != null) {
            if (mp_bgMusic.isPlaying()) {
                mp_bgMusic.pause();
            }
            mp_bgMusic.release();
            mp_bgMusic = null;
        }
    }

    private void VideoPlay_Stop() {
        if (mediaPlayer != null) {
            cur_mPlayer = liveVideo.getCurrentPosition();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
//            liveVideo.stopPlayback();
        }
    }

    private void VideoPlay_Play() {
        liveVideo.setVideoURI(Uri.parse(path));
        liveVideo.seekTo(cur_mPlayer);
        liveVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
            }
        });
    }

    private void VideoClick() {//VideoView点击事件
        if (control_slope.getVisibility() == View.VISIBLE &&
                control_speed.getVisibility() == View.VISIBLE &&
                control_stop_pause.getVisibility() == View.VISIBLE) {
            control_slope.startAnimation(VideoClickHidden);
            control_speed.startAnimation(VideoClickHidden);
            control_stop_pause.startAnimation(VideoClickHidden);
            control_slope.setVisibility(View.GONE);
            control_speed.setVisibility(View.GONE);
            control_stop_pause.setVisibility(View.GONE);
        } else if (control_slope.getVisibility() == View.GONE &&
                control_speed.getVisibility() == View.GONE &&
                control_stop_pause.getVisibility() == View.GONE) {
            control_slope.startAnimation(VideoClickShow);
            control_speed.startAnimation(VideoClickShow);
            control_stop_pause.startAnimation(VideoClickShow);
            control_slope.setVisibility(View.VISIBLE);
            control_speed.setVisibility(View.VISIBLE);
            control_stop_pause.setVisibility(View.VISIBLE);
        }
    }

    //实景模式开始动画
    private void LiveAnimDialog(final Context context, final double speed,
                                final int slope) {
        if (SportData.getStatus() != SportData.START_TREADMILL_ANIM) {
            dialog = new Dialog(context, R.style.CommentStyle);
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false; //默认返回 false
                    }
                }
            });
            SportData.setStatus(SportData.START_TREADMILL_ANIM);
            dialog.show();
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.start_animation);
            final ImageView Treadmill_StartRun = window.findViewById(R.id.Treadmill_StartRun);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.FILL_PARENT;
            window.setAttributes(lp);
            animation1 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation2 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation3 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation4 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation5 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation6 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            Treadmill_StartRun.startAnimation(animation1);
            isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(), R.raw.alert);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_4);
                    Treadmill_StartRun.startAnimation(animation2);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(), R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_3);
                    Treadmill_StartRun.startAnimation(animation3);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(), R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_2);
                    Treadmill_StartRun.startAnimation(animation4);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(), R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation4.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_1);
                    Treadmill_StartRun.startAnimation(animation5);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(), R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation5.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_go);
                    Treadmill_StartRun.startAnimation(animation6);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(), R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation6.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (SportData.getStatus() != SportData.ERROR_STATUS) {
                        liveVideo.setVisibility(View.VISIBLE);
                        SportData.setStatus(SportData.RUNNING_MODE_LIVE);
                        //1 分开主页面启动键  0显示主页面启动键
                        EventBus.getDefault().postSticky(new setMainClickVisibility(1));
                        CountUp_PauseTime = 0;//第一次进入将正计时、倒计时的暂停时间设置成0
                        flag_pause = 0;
                        running3_pause.setBackgroundResource(R.color.colorOrange);
                        running3_pause.setText(R.string.main_mode_pause);
                        SportData.setSpeed(speed);
                        SportData.setSlope(slope);
                        running3_ing.setVisibility(View.GONE);
                        running3_pause.setVisibility(View.VISIBLE);
                        running3_stop.setVisibility(View.VISIBLE);
                        video_img.setVisibility(View.GONE);
                        isApplication.Start_SystemTime = System.currentTimeMillis();
                        Log.e("看看这里走了没", String.valueOf(10));
                        liveVideo.setVideoURI(Uri.parse(path));
                        liveVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer = mp;
                                mp.setVolume(0.0f, 0.0f);
                                mediaPlayer.setPlaybackSpeed(currentPlaySpeed);
                            }
                        });
                        First_play(R.raw.scene_music);
                        Log.e("看看这里走了没", String.valueOf(5));
                        liveVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.seekTo(0);
                                MusicPlayerCallback(mp_bgMusic, R.raw.scene_music);
                                play();
                                Log.e("看看这里走了没", String.valueOf(6));
                            }
                        });
                        ServerWindows.CountUpStart(CountUp_PauseTime, SportData.RUNNING_MODE_LIVE);
                        Message msg = new Message();
                        msg.what = 1;
                        ClickHandler.sendMessageDelayed(msg, clickTime);
                    }
                    dialog.dismiss();
                    dialog = null;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.liveVideo:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            VideoClick();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            Message msg = new Message();
                            msg.what = 1;
                            ClickHandler.sendMessageDelayed(msg, clickTime);
                        }
                        break;
                }
                break;
            case R.id.btn_running3_slope_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running3_slope_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running3_slope_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running3_slope_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running3_slope_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running3_slope_minus.setBackgroundResource(R.drawable.btn_minus_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running3_speed_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running3_speed_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running3_speed_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running3_speed_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running3_speed_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running3_speed_minus.setBackgroundResource(R.drawable.btn_minus_false);
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
            case R.id.btn_running3_speed_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedAdd, 500);
                break;
            case R.id.btn_running3_speed_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedSub, 500);
                break;
            case R.id.btn_running3_slope_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeAdd, 500);
                break;
            case R.id.btn_running3_slope_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeSub, 500);
                break;
        }
        return false;
    }

    private void adj_playSpeed() {//不同的ph单位和不同的速度最大值，播放速率都不一样，这里是调整播放倍速的方法
        if (isApplication.ph_selector == 1) {
            Log.i("查看当前速度:", String.valueOf(SportData.getSpeed()));
            if (SportData.getSpeed() <= 1) {
                if (currentPlaySpeed != 0.5f) {
                    currentPlaySpeed = 0.5f;
                }
            } else if (SportData.getSpeed() <= 2) {
                if (currentPlaySpeed != 0.6f) {
                    currentPlaySpeed = 0.6f;
                }
            } else if (SportData.getSpeed() <= 3) {
                if (currentPlaySpeed != 0.7f) {
                    currentPlaySpeed = 0.7f;
                }
            } else if (SportData.getSpeed() <= 4) {
                if (currentPlaySpeed != 0.8f) {
                    currentPlaySpeed = 0.8f;
                }
            } else if (SportData.getSpeed() <= 5) {
                if (currentPlaySpeed != 0.9f) {
                    currentPlaySpeed = 0.9f;
                }
            } else if (SportData.getSpeed() <= 6) {
                if (currentPlaySpeed != 1.0f) {
                    currentPlaySpeed = 1.0f;
                }
            } else if (SportData.getSpeed() <= 7) {
                if (currentPlaySpeed != 1.1f) {
                    currentPlaySpeed = 1.1f;
                }
            } else if (SportData.getSpeed() <= 8) {
                if (currentPlaySpeed != 1.2f) {
                    currentPlaySpeed = 1.2f;
                }
            } else if (SportData.getSpeed() <= 9) {//  9
                if (SportData.MAXSPEED == 13.0) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                } else if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 1.3f) {
                        currentPlaySpeed = 1.3f;
                    }
                } else if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                        || SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                    if (currentPlaySpeed != 1.2f) {
                        currentPlaySpeed = 1.2f;
                    }
                }
            } else if (SportData.getSpeed() <= 10) {//  10
                if (SportData.MAXSPEED == 13.0) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                } else if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                } else if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                        || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.3f) {
                        currentPlaySpeed = 1.3f;
                    }
                }
            } else if (SportData.getSpeed() <= 11) {//  11
                if (SportData.MAXSPEED == 13.0) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 1.5f) {
                        currentPlaySpeed = 1.5f;
                    }
                } else if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                        || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                } else if (SportData.MAXSPEED == 25) {
                    if (currentPlaySpeed != 1.3f) {
                        currentPlaySpeed = 1.3f;
                    }
                }
            } else if (SportData.getSpeed() <= 12) {//  12
                if (SportData.MAXSPEED == 13.0) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                } else if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                } else if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                        || SportData.MAXSPEED == 25.0) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                } else if (SportData.MAXSPEED == 22) {
                    if (currentPlaySpeed != 1.5f) {
                        currentPlaySpeed = 1.5f;
                    }
                }
            } else if (SportData.getSpeed() <= 13) {//  13
                if (SportData.MAXSPEED == 13.0) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                } else if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                        || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.5f) {
                        currentPlaySpeed = 1.5f;
                    }
                }
            } else if (SportData.getSpeed() <= 14) {//  14
                if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                        || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                }
            } else if (SportData.getSpeed() <= 15) {//  15
                if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                } else if (SportData.MAXSPEED == 18.0) {
                    if (currentPlaySpeed != 1.7) {
                        currentPlaySpeed = 1.7f;
                    }
                } else if (SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                } else if (SportData.MAXSPEED == 25) {
                    if (currentPlaySpeed != 1.5f) {
                        currentPlaySpeed = 1.5f;
                    }
                }
            } else if (SportData.getSpeed() <= 16) {//  16
                if (SportData.MAXSPEED == 16.0) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 18.0) {
                    if (currentPlaySpeed != 1.8) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                }
            } else if (SportData.getSpeed() <= 17) {//  17
                if (SportData.MAXSPEED == 18.0) {
                    if (currentPlaySpeed != 1.9) {
                        currentPlaySpeed = 1.9f;
                    }
                } else if (SportData.MAXSPEED == 25) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                }
            } else if (SportData.getSpeed() <= 18) {//  18
                if (SportData.MAXSPEED == 18.0) {
                    if (currentPlaySpeed != 2.0) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 25) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                }
            } else if (SportData.getSpeed() <= 19) {//  19
                if (SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                } else if (SportData.MAXSPEED == 25) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                }
            } else if (SportData.getSpeed() <= 20) {//  20
                if (SportData.MAXSPEED == 20.0) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                }
            } else if (SportData.getSpeed() <= 21) {//  21
                if (SportData.MAXSPEED == 22.0) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 25) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                }
            } else if (SportData.getSpeed() <= 23) {//  23
                if (SportData.MAXSPEED == 25) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                }
            }
        } else if (isApplication.ph_selector == 2) {
            if (SportData.getSpeed() <= 1) {// 1
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 0.5f) {
                        currentPlaySpeed = 0.5f;
                    }
                }
            } else if (SportData.getSpeed() <= 2) {// 2
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 0.8f) {
                        currentPlaySpeed = 0.8f;
                    }
                } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2
                        || SportData.MAXSPEED == 12.4 || SportData.MAXSPEED == 13.6
                        || SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 0.6f) {
                        currentPlaySpeed = 0.6f;
                    }
                }
            } else if (SportData.getSpeed() <= 3) {// 3
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 1.0f) {
                        currentPlaySpeed = 1.0f;
                    }
                } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2
                        || SportData.MAXSPEED == 12.4 || SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 0.8f) {
                        currentPlaySpeed = 0.8f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 0.7f) {
                        currentPlaySpeed = 0.7f;
                    }
                }
            } else if (SportData.getSpeed() <= 4) {// 4
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 1.2f) {
                        currentPlaySpeed = 1.2f;
                    }
                } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2
                        || SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.0f) {
                        currentPlaySpeed = 1.0f;
                    }
                } else if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 0.9f) {
                        currentPlaySpeed = 0.9f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 0.8f) {
                        currentPlaySpeed = 0.8f;
                    }
                }
            } else if (SportData.getSpeed() <= 5) {// 5
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2) {
                    if (currentPlaySpeed != 1.2f) {
                        currentPlaySpeed = 1.2f;
                    }
                } else if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 1.0f) {
                        currentPlaySpeed = 1.0f;
                    }
                } else if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.1f) {
                        currentPlaySpeed = 1.1f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 0.9f) {
                        currentPlaySpeed = 0.9f;
                    }
                }
            } else if (SportData.getSpeed() <= 6) {// 6
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                } else if (SportData.MAXSPEED == 12.4 || SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.2f) {
                        currentPlaySpeed = 1.2f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.0f) {
                        currentPlaySpeed = 1.0f;
                    }
                }
            } else if (SportData.getSpeed() <= 7) {// 7
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                } else if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 1.5f) {
                        currentPlaySpeed = 1.5f;
                    }
                } else if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.1f) {
                        currentPlaySpeed = 1.1f;
                    }
                }
            } else if (SportData.getSpeed() <= 8) {// 8
                if (SportData.MAXSPEED == 8.1) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 9.9) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 11.2) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                } else if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                } else if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.5f) {
                        currentPlaySpeed = 1.5f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.2f) {
                        currentPlaySpeed = 1.2f;
                    }
                }
            } else if (SportData.getSpeed() <= 9) {// 9
                if (SportData.MAXSPEED == 9.9) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 11.2) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                } else if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.3f) {
                        currentPlaySpeed = 1.3f;
                    }
                }
            } else if (SportData.getSpeed() <= 10) {// 10
                if (SportData.MAXSPEED == 11.2) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                } else if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.4f) {
                        currentPlaySpeed = 1.4f;
                    }
                }
            } else if (SportData.getSpeed() <= 11) {// 11
                if (SportData.MAXSPEED == 11.2) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                } else if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.8f) {
                        currentPlaySpeed = 1.8f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.5f) {
                        currentPlaySpeed = 1.5f;
                    }
                }
            } else if (SportData.getSpeed() <= 12) {// 12
                if (SportData.MAXSPEED == 12.4) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.6f) {
                        currentPlaySpeed = 1.6f;
                    }
                }
            } else if (SportData.getSpeed() <= 13) {// 13
                if (SportData.MAXSPEED == 13.6) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                } else if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.7f) {
                        currentPlaySpeed = 1.7f;
                    }
                }
            } else if (SportData.getSpeed() <= 14) {// 14
                if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 1.9f) {
                        currentPlaySpeed = 1.9f;
                    }
                }
            } else if (SportData.getSpeed() <= 14) {// 15
                if (SportData.MAXSPEED == 15.5) {
                    if (currentPlaySpeed != 2.0f) {
                        currentPlaySpeed = 2.0f;
                    }
                }
            }
        }
        mediaPlayer.setPlaybackSpeed(currentPlaySpeed);
    }

}
