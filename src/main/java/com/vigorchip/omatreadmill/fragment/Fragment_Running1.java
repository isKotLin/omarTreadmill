package com.vigorchip.omatreadmill.fragment;

import android.animation.ObjectAnimator;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
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
import com.vigorchip.omatreadmill.eventbus.setPlayGroundthumb;
import com.vigorchip.omatreadmill.eventbus.setStop;
import com.vigorchip.omatreadmill.eventbus.setTitle;
import com.vigorchip.omatreadmill.interfaci.setSportText;
import com.vigorchip.omatreadmill.serialport.newSerial;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.utils.DoubleUtils;
import com.vigorchip.omatreadmill.utils.TimeUtils;
import com.vigorchip.omatreadmill.view.PlayGround;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.vigorchip.omatreadmill.server.ServerWindows.RunningEnd;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


/**
 * Created by wr-app1 on 2018/4/17.
 * 跑道运动界面：
 * 快速模式，时间模式，距离模式，热量模式
 */
public class Fragment_Running1 extends Fragment implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private View view;
    public static boolean hide;
    private PlayGround playGround;
    private TextView running1_title;//模式标题
    private TextView sport_time, sport_km, sport_cal;
    private Button running1_stop;
    public static LinearLayout running_pause_ing_layout;
    public static Button running1_pause;
    private Button btn_running1_slope_add, btn_running1_slope_minus, btn_running1_speed_add, btn_running1_speed_minus;

    public static long CountUp_PauseTime = 0;//获取正计时暂停时的时间，保存起来
    public static long CountDown_PauseTime = 0;//获取倒计时暂停时的时间，保存起来
    public static int flag_pause = 0;//判断暂停的标志位

    private String time;//时间模式设置的时间
    private String distance;//距离模式设置的距离
    private String calories;//热量模式设置的卡路里

    //-----------------------------------------------------------------------------------
    private ObjectAnimator objectAnimator1;

    private ImageView thumb;
    private float this_Radius = 105;//鍗婂緞
    private float pi = 3.14159F;//
    private int STANDARD_TRACK_LENGTH = 400;     //鏍囧噯璺戦亾闀垮害
    private int TRACK_LINE_LENGTH = 87;        //400绫宠窇閬撶洿閬撻暱搴?87绫?
    private int TRACK_ARC_LENGTH = 113;      //400绫宠窇閬撳集閬撻暱搴?113绫?
    private float this_LeftCenterX;        //宸﹀崐鍦嗙殑X鍧愭爣
    private float this_LeftCenterY;        //宸﹀崐鍦嗙殑Y鍧愭爣
    private float this_RightCenterX;        //鍙冲崐鍦嗙殑X鍧愭爣
    private float this_RightCenterY;       //鍙冲崐鍦嗙殑Y鍧愭爣
    private float this_ZhouChang;
    private float TrackArcLength;        //璺戦亾鍗婂渾寮х殑闀垮害
    private float TrackLineLength;    //璺戦亾鐩撮亾鐨勯暱搴?
    private float Previous_X;        //鍓嶄竴娆＄粯鍒剁殑灏忓渾鐐逛腑蹇僗鍧愭爣
    private float Previous_Y;        //鍓嶄竴娆＄粯鍒剁殑灏忓渾鐐逛腑蹇僘鍧愭爣
    private float X;            //缁樺埗鐨勫皬鍦嗙偣涓績X鍧愭爣
    private float Y;            //缁樺埗鐨勫皬鍦嗙偣涓績Y鍧愭爣
    private int icon_width;
    private int icon_heigth;
    private float imgx;
    private float imgy;
    private boolean ishow = true;
    private boolean ishow01 = true;
    private ObjectAnimator o;
    private int runWidth;
    private int runHeigth;

    private double SPEED;//速度
    private boolean FLAG_STOPPING = false;//停止中中的标志位，确保只走一次
    private int LastStatus = 0;//获取上一次的速度
    private Handler stopping_handler = new Handler();
    Runnable runnable = new Runnable() {//停止中线程
        @Override
        public void run() {
            Log.e("查看走了几遍2", String.valueOf(SPEED));
            if (!FLAG_STOPPING) {
                LastStatus = SportData.getStatus();
                running_pause_ing_layout.setVisibility(View.VISIBLE);
                running1_pause.setVisibility(View.GONE);
                running1_stop.setVisibility(View.GONE);
                if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {
                    CountUp_PauseTime = 0;//清零
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME) {
                    CountDown_PauseTime = 0;//清零
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
                    CountUp_PauseTime = 0;//清零
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
                    CountUp_PauseTime = 0;//清零
                }
                flag_pause = 0;
                SportData.setStatus(SportData.STOPPING_TREADMILL);
                FLAG_STOPPING = true;
            }
            if (SPEED != 0.0) {
                SPEED = DoubleUtils.sub(SPEED, 0.1);
                stopping_handler.postDelayed(this, 50);
                Log.e("查看停止中的速度", String.valueOf(SPEED));
            } else if (SPEED == 0.0) {
                SportData.setStatus(LastStatus);
                FLAG_STOPPING = false;
                LastStatus = 0;
                running_pause_ing_layout.setVisibility(View.GONE);
                running1_pause.setVisibility(View.VISIBLE);
                running1_stop.setVisibility(View.VISIBLE);
                running1_pause.setBackgroundResource(R.color.colorOrange);
                running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                ((MainActivity) getActivity()).showFragment(16, 14);
                sendResult(SportData.getStatus());//提供给service倒计时结束调到二维码页面
                EventBus.getDefault().postSticky(new setMainClickVisibility(0));//1 分开主页面启动键  0显示主页面启动键
                Log.e("查看走了几遍3", String.valueOf(SPEED));
            }
        }
    };

    //-----------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_running1, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        setSportText();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然接收不到
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hide = hidden;
        if (hidden) {
            if (thumb != null) {
                thumb.setVisibility(View.GONE);
            }
        } else {
            thumb.setVisibility(View.VISIBLE);
        }
    }

    private void stopHandler(double speed) {//停止中线程
        this.SPEED = speed;
        stopping_handler.postDelayed(runnable, 50);
        Log.e("查看走了几遍1", String.valueOf(SPEED));
    }

    private SharedPreferences sp_loadPH;
    private String PH;

    private void init() {
        playGround = view.findViewById(R.id.playGround);
        thumb = view.findViewById(R.id.thumb);
        running1_title = view.findViewById(R.id.running1_title);
        running1_stop = view.findViewById(R.id.running1_stop);
        running1_pause = view.findViewById(R.id.running1_pause);
        running_pause_ing_layout = view.findViewById(R.id.running_pause_ing_layout);
        btn_running1_slope_add = view.findViewById(R.id.btn_running1_slope_add);
        btn_running1_slope_minus = view.findViewById(R.id.btn_running1_slope_minus);
        btn_running1_speed_add = view.findViewById(R.id.btn_running1_speed_add);
        btn_running1_speed_minus = view.findViewById(R.id.btn_running1_speed_minus);
        sport_time = view.findViewById(R.id.sport_time);
        sport_km = view.findViewById(R.id.sport_km);
        sport_cal = view.findViewById(R.id.sport_cal);
        running1_stop.setOnClickListener(this);
        running1_pause.setOnClickListener(this);
        btn_running1_slope_add.setOnClickListener(this);
        btn_running1_slope_minus.setOnClickListener(this);
        btn_running1_speed_add.setOnClickListener(this);
        btn_running1_speed_minus.setOnClickListener(this);

        btn_running1_slope_add.setOnTouchListener(this);
        btn_running1_slope_minus.setOnTouchListener(this);
        btn_running1_speed_add.setOnTouchListener(this);
        btn_running1_speed_minus.setOnTouchListener(this);

        btn_running1_slope_add.setOnLongClickListener(this);
        btn_running1_slope_minus.setOnLongClickListener(this);
        btn_running1_speed_add.setOnLongClickListener(this);
        btn_running1_speed_minus.setOnLongClickListener(this);


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

//-----------------------------------------------------------------------------------

        ViewTreeObserver otn = thumb.getViewTreeObserver();
        otn.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (ishow) {
                    imgx = thumb.getX();
                    imgy = thumb.getY();
                    Log.e("print", imgx + "—th—" + imgy);
                    icon_width = thumb.getWidth();
                    icon_heigth = thumb.getHeight();
                    ishow = false;
                }
            }
        });
        ViewTreeObserver treeObserver = playGround.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (ishow01) {
                    runHeigth = playGround.getHeight();
                    runWidth = playGround.getWidth();
                    getmessage();
                    ishow01 = false;
                }
            }
        });
//        o = ObjectAnimator.ofFloat(thumb, "alpha", 1f, 0f);
//        o.setRepeatCount(-1);
//        o.setDuration(1000);
//        o.start();
    }

    private void getmessage() {
        this_ZhouChang = ((runHeigth - 2 * icon_width) * pi + (runWidth - runHeigth - 2 * icon_width) * 2);
        this_Radius = runHeigth / 2 - icon_width;
        this_RightCenterX = ((runWidth / 2) - (runHeigth / 2) + imgx);
        this_RightCenterY = (imgy - (runHeigth / 2));
        this_LeftCenterY = (imgy - (runHeigth / 2));
        this_LeftCenterX = imgx - (runWidth / 2) + (runHeigth / 2);
        TrackArcLength = (runHeigth - icon_width - 2 * icon_width) * pi / 2;
        TrackLineLength = (runWidth - runHeigth - icon_width - 2 * icon_width);
//        Log.e("print", runHeigth + "__" + runWidth + "---" + imgx + "---" + imgy + "----" + icon_width + "===" + icon_heigth + "__");
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onPlayground(final setPlayGroundthumb isthumb) {
        drawpoint(isthumb.getDistance());
    }

    public void updata() {
        thumb.setX(imgx);
        thumb.setY(imgy);
    }

    private void drawpoint(double a) {
        a = a * 1000;
        float Angle, ArcLength;
        double temp1, temp2;
        a = a % STANDARD_TRACK_LENGTH;
        if (a >= 0 && a <= TRACK_LINE_LENGTH / 2) {//鍦ㄧ涓€璺戝埌鐩村埌

            X = (float) (imgx + a / STANDARD_TRACK_LENGTH * this_ZhouChang);
            Y = imgy;
        } else if ((a > TRACK_LINE_LENGTH / 2) && (a <= (TRACK_LINE_LENGTH / 2 + TRACK_ARC_LENGTH))) {//绗竴涓集閬?

            a = a - TRACK_LINE_LENGTH / 2;//寮亾闀垮害
            ArcLength = (float) (TrackArcLength * a / TRACK_ARC_LENGTH);        //鏍规嵁鐧惧垎姣旇绠楁墍璺戠殑寮ч暱
            Angle = ArcLength * 180 / (pi * this_Radius);            //鏍规嵁寮ч暱璁＄畻瑙掑害
            if (Angle < 90) {
                temp1 = this_Radius * sin(Angle * pi / 180);
                temp2 = this_Radius * cos(Angle * pi / 180);
//                Log.e("print", temp1 + "__90-___" + temp2);
                if (temp2 == 0)
                    X = this_RightCenterX + this_Radius;
                else
                    X = (float) (this_RightCenterX + temp1);
                if (temp1 == 0)
                    Y = this_RightCenterY + this_Radius + icon_width;
                else
                    Y = (float) (this_RightCenterY + temp2 + icon_width);
            } else {
                temp1 = this_Radius * sin((Angle - 90) * pi / 180);
                temp2 = this_Radius * cos((Angle - 90) * pi / 180);
//                Log.e("print", temp1 + "__90+___" + temp2);
                if (temp1 == 0)
                    X = this_RightCenterX + this_Radius;
                else
                    X = (float) (this_RightCenterX + temp2);

                if (temp2 == 0)
                    Y = this_RightCenterY - this_Radius + icon_width * 2;
                else
                    Y = (float) (this_RightCenterY - temp1 + icon_width * 2);
            }
        } else if ((a > (TRACK_LINE_LENGTH / 2 + TRACK_ARC_LENGTH)) && (a <= (TRACK_LINE_LENGTH / 2 + TRACK_LINE_LENGTH + TRACK_ARC_LENGTH)))     // 绗簩娈电洿閬?
        {
            a = a - TRACK_LINE_LENGTH / 2 - TRACK_ARC_LENGTH;
            X = (float) (this_RightCenterX - TrackLineLength * a / TRACK_LINE_LENGTH);
            Y = this_RightCenterY - this_Radius + 2 * icon_width;
        } else if ((a > (TRACK_LINE_LENGTH / 2 + TRACK_LINE_LENGTH + TRACK_ARC_LENGTH)) && (a <= (TRACK_LINE_LENGTH / 2 + TRACK_LINE_LENGTH + TRACK_ARC_LENGTH * 2))) {
            a = a - TRACK_LINE_LENGTH / 2 - TRACK_LINE_LENGTH - TRACK_ARC_LENGTH;//寮亾闀垮害
            ArcLength = (float) (TrackArcLength * a / TRACK_ARC_LENGTH);        //鏍规嵁鐧惧垎姣旇绠楁墍璺戠殑寮ч暱
            Angle = ArcLength * 180 / (pi * this_Radius);            //鏍规嵁寮ч暱璁＄畻瑙掑害
            if (Angle <= 90) {
                temp1 = this_Radius * sin(Angle * pi / 180);
                temp2 = this_Radius * cos(Angle * pi / 180);
                if (temp2 == 0)
                    X = this_LeftCenterX - this_Radius;
                else
                    X = (float) (this_LeftCenterX - temp1);
                if (temp1 == 0)
                    Y = this_LeftCenterY - this_Radius + icon_width * 3 / 2;
                else
                    Y = (float) (this_LeftCenterY - temp2) + icon_width * 3 / 2;
            } else {
                temp1 = this_Radius * sin((Angle - 90) * pi / 180);
                temp2 = this_Radius * cos((Angle - 90) * pi / 180);
                if (temp1 == 0)
                    X = this_LeftCenterX + this_Radius;
                else
                    X = (float) (this_LeftCenterX - temp2);

                if (temp2 == 0)
                    Y = this_LeftCenterY - this_Radius + icon_width;
                else
                    Y = (float) (this_LeftCenterY + temp1 + icon_width);
            }
        } else {
            a = a - TRACK_LINE_LENGTH / 2 - TRACK_LINE_LENGTH - TRACK_ARC_LENGTH - TRACK_ARC_LENGTH;
            X = (float) (this_LeftCenterX + a / STANDARD_TRACK_LENGTH * this_ZhouChang);
            Y = imgy;
        }
        thumb.setX(X);
        thumb.setY(Y);
        X = 0;
        Y = 0;
    }

//-----------------------------------------------------------------------------------

    private void StartAnimate() {
//        objectAnimator1 = ObjectAnimator.ofFloat(thumb,"translationX",0,213);
        objectAnimator1.setDuration(10000);
        objectAnimator1.start();
    }

    private void setSportText() {//通过服务的接口显示各模式的显示数据
        ServerWindows.setSportTime(new setSportText() {
            @Override
            public void OnSportText(String arg, int MODE) {
                switch (MODE) {
                    case SportData.RUNNING_MODE_MANUAL:
                        sport_time.setText(arg);
                        break;
                    case SportData.RUNNING_MODE_TIME:
                        sport_time.setText(arg);
                        break;
                    case SportData.RUNNING_MODE_DISTANCE:
                        sport_km.setText(arg);
                        break;
                    case SportData.RUNNING_MODE_CALORIES:
                        sport_cal.setText(arg);
                        break;
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置标题并设置当前运动模式
    public void onTitle(setTitle title) {
        running1_title.setText(title.getmTitle());
        switch (title.getMode()) {
            case SportData.RUNNING_MODE_MANUAL://快速模式
                SportData.setStatus(SportData.RUNNING_MODE_MANUAL);
                sport_time.setVisibility(View.VISIBLE);
                sport_km.setVisibility(View.GONE);
                sport_cal.setVisibility(View.GONE);
                SportData.setSpeed(SportData.MINSPEED);
                CountUp_PauseTime = 0;//第一次进入将正计时、倒计时的暂停时间设置成0
                CountDown_PauseTime = 0;
                flag_pause = 0;
                ServerWindows.CountUpStart(CountUp_PauseTime, SportData.RUNNING_MODE_MANUAL);
                running1_pause.setBackgroundResource(R.color.colorOrange);
                running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_TIME://时间模式
                SportData.setStatus(SportData.RUNNING_MODE_TIME);
                sport_time.setVisibility(View.VISIBLE);
                sport_km.setVisibility(View.GONE);
                sport_cal.setVisibility(View.GONE);
                SportData.setSpeed(SportData.MINSPEED);
                CountUp_PauseTime = 0;//第一次进入将正计时、倒计时的暂停时间设置成0
                CountDown_PauseTime = 0;
                flag_pause = 0;
                time = title.getTime();//获取从时间模式设置的时间
                ServerWindows.CountDownStart(TimeUtils.TimeManager(time), CountDown_PauseTime,
                        SportData.RUNNING_MODE_TIME);
                running1_pause.setBackgroundResource(R.color.colorOrange);
                running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_DISTANCE://距离模式
                SportData.setStatus(SportData.RUNNING_MODE_DISTANCE);
                sport_km.setVisibility(View.VISIBLE);
                sport_time.setVisibility(View.GONE);
                sport_cal.setVisibility(View.GONE);
                distance = title.getDistance();
                SportData.setSpeed(Double.valueOf(distance));
                SportData.setSpeed(SportData.MINSPEED);
                CountUp_PauseTime = 0;//第一次进入将正计时、倒计时的暂停时间设置成0
                CountDown_PauseTime = 0;
                flag_pause = 0;
                ServerWindows.DistanceDownStart(Double.valueOf(distance), CountUp_PauseTime,
                        SportData.RUNNING_MODE_DISTANCE);
                running1_pause.setBackgroundResource(R.color.colorOrange);
                running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
            case SportData.RUNNING_MODE_CALORIES://卡路里模式
                SportData.setStatus(SportData.RUNNING_MODE_CALORIES);
                sport_cal.setVisibility(View.VISIBLE);
                sport_km.setVisibility(View.GONE);
                sport_time.setVisibility(View.GONE);
                calories = title.getCal();
                SportData.setSpeed(SportData.MINSPEED);
                CountUp_PauseTime = 0;//第一次进入将正计时、倒计时的暂停时间设置成0
                CountDown_PauseTime = 0;
                flag_pause = 0;
                ServerWindows.CaloriesDownStart(Integer.valueOf(calories), CountUp_PauseTime,
                        SportData.RUNNING_MODE_CALORIES);
                running1_pause.setBackgroundResource(R.color.colorOrange);
                running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置主页面启动键
    public void onPause(setFragmentPause pause) {//Fragment_Main发送的暂停
        if (SportData.isMainRunning()) {
            if (pause.getPause() == 1) {//暂停
                running1_pause.setBackgroundResource(R.color.colorGreen);
                running1_pause.setText(getContext().getString(R.string.Start));
                flag_pause = 1;
                ServerWindows.CountPause();
                if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {//正计时暂停时获取暂停的时间
                    CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME) {//倒计时暂停时获取暂停的时间
                    CountDown_PauseTime = ServerWindows.CountDownMathTime_Pause;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
                    CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
                    CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
                }
            } else if (pause.getPause() == 0) {//启动
                running1_pause.setBackgroundResource(R.color.colorOrange);
                running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                flag_pause = 0;
                if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {//暂停后启动将获取的暂停时间设置进来
                    ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME) {
                    ServerWindows.CountDownStart(TimeUtils.TimeManager(time),
                            CountDown_PauseTime, SportData.getStatus());
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
                    ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
                    ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)//设置主页面启动键
    public void onStop(setStop stop) {//Fragment_Main发送的停止
        if (SportData.isMainRunning()) {
            if (stop.getStop()) {
                //回到最初状态
                running1_pause.setBackgroundResource(R.color.colorOrange);
                running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                flag_pause = 0;
                if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {
                    CountUp_PauseTime = 0;//清零
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME) {
                    CountDown_PauseTime = 0;//清零
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
                    CountUp_PauseTime = 0;//清零
                } else if (SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
                    CountUp_PauseTime = 0;//清零
                }
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
            case R.id.btn_running1_speed_add:
                Log.e("查看最大速度", String.valueOf(SportData.MAXSPEED));
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
            case R.id.btn_running1_speed_minus:
                Log.e("查看最小速度", String.valueOf(SportData.MINSPEED));
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
            case R.id.btn_running1_slope_add:
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
            case R.id.btn_running1_slope_minus:
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
            case R.id.running1_stop:
                stopHandler(SportData.getSpeed());
                //回到最初状态
                RunningEnd();
                break;
            case R.id.running1_pause:
                if (flag_pause == 0) {//暂停
                    running1_pause.setBackgroundResource(R.color.colorGreen);
                    running1_pause.setText(getContext().getString(R.string.Start));
                    flag_pause = 1;
                    ServerWindows.CountPause();
                    SportData.setSpeed(0.0);
                    if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {
                        CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME) {
                        CountDown_PauseTime = ServerWindows.CountDownMathTime_Pause;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
                        CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
                        CountUp_PauseTime = ServerWindows.CountUpMathTime_Pause;
                    }
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 1  暂停
                    break;
                } else if (flag_pause == 1) {//启动
                    running1_pause.setBackgroundResource(R.color.colorOrange);
                    running1_pause.setText(getContext().getString(R.string.main_mode_pause));
                    flag_pause = 0;
                    if (SportData.getStatus() == SportData.RUNNING_MODE_MANUAL) {
                        ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_TIME) {
                        ServerWindows.CountDownStart(TimeUtils.TimeManager(time), CountDown_PauseTime, SportData.getStatus());
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_DISTANCE) {
                        ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_CALORIES) {
                        ServerWindows.CountUpStart(CountUp_PauseTime, SportData.getStatus());
                    }
                    EventBus.getDefault().postSticky(new setMainPause(flag_pause));//发送 0  启动
                    break;
                }
                break;
        }
    }

    private void sendResult(int status) {//提供给service结束调到二维码页面
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
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_running1_slope_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running1_slope_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running1_slope_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running1_slope_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running1_slope_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running1_slope_minus.setBackgroundResource(R.drawable.btn_minus_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running1_speed_add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running1_speed_add.setBackgroundResource(R.drawable.btn_add_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running1_speed_add.setBackgroundResource(R.drawable.btn_add_false);
                        if (longHandler != null) {
                            longHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                }
                break;
            case R.id.btn_running1_speed_minus:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_running1_speed_minus.setBackgroundResource(R.drawable.btn_minus_true);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_running1_speed_minus.setBackgroundResource(R.drawable.btn_minus_false);
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
                SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.1));
            }
            longHandler.postDelayed(Run_SpeedAdd, 500);
        }
    };

    Runnable Run_SpeedSub = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSpeed() > SportData.MINSPEED) {
                SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.2));
                longHandler.postDelayed(Run_SpeedSub, 500);
            }
        }
    };

    Runnable Run_SlopeAdd = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSlope() < SportData.MAXSLOPES) {
                SportData.setSlope(SportData.getSlope() + 1);
                longHandler.postDelayed(Run_SlopeAdd, 500);
            }
        }
    };

    Runnable Run_SlopeSub = new Runnable() {
        @Override
        public void run() {
            if (SportData.getSlope() > 0) {
                SportData.setSlope(SportData.getSlope() - 1);
                longHandler.postDelayed(Run_SlopeSub, 500);
            }
        }
    };

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_running1_speed_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedAdd, 500);
                break;
            case R.id.btn_running1_speed_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SpeedSub, 500);
                break;
            case R.id.btn_running1_slope_add:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeAdd, 500);
                break;
            case R.id.btn_running1_slope_minus:
                longHandler = new Handler();
                longHandler.postDelayed(Run_SlopeSub, 500);
                break;
        }
        return false;
    }

}
