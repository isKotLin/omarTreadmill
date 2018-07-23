package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.Mode_Time_Info;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.setUserData;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.start_treadmill_anim.Start_Treadmill;
import com.vigorchip.omatreadmill.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wr-app1 on 2018/5/8.
 */

public class Fragment_UserTime extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private CheckedTextView tv_mm1, tv_mm2, tv_ss1, tv_ss2;
    private Button UserTime_Start_Run;
    private RelativeLayout btn_TimeAdd, btn_TimeSub;
    private int isSelector = 0;
    private Mode_Time_Info mode_time_info;

    private double[] mSpeed;//速度的数组
    private double[] mSlope;//坡度的数组

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_usertime, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mode_time_info = new Mode_Time_Info();
        init();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然接收不到
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            defult();
        } else {
            ServerWindows.tv_RunningTime.setText(String.valueOf("00:00:00"));
        }
    }

    private void init() {
        tv_mm1 = view.findViewById(R.id.tv_mm1);
        tv_mm2 = view.findViewById(R.id.tv_mm2);
        tv_ss1 = view.findViewById(R.id.tv_ss1);
        tv_ss2 = view.findViewById(R.id.tv_ss2);
        btn_TimeAdd = view.findViewById(R.id.btn_TimeAdd);
        btn_TimeSub = view.findViewById(R.id.btn_TimeSub);
        UserTime_Start_Run = view.findViewById(R.id.UserTime_Start_Run);
        tv_mm1.setOnClickListener(this);
        tv_mm2.setOnClickListener(this);
        tv_ss1.setOnClickListener(this);
        tv_ss2.setOnClickListener(this);
        btn_TimeAdd.setOnClickListener(this);
        btn_TimeSub.setOnClickListener(this);
        btn_TimeAdd.setOnTouchListener(this);
        btn_TimeSub.setOnTouchListener(this);
        UserTime_Start_Run.setOnClickListener(this);
        defult();
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onUserData(setUserData data) {
        mSpeed = new double[data.getSpeed().length];
        mSlope = new double[data.getSlope().length];
        for (int i = 0; i < data.getSpeed().length; i++) {
            mSpeed[i] = data.getSpeed()[i];
        }
        for (int i = 0; i < data.getSlope().length; i++) {
            mSlope[i] = data.getSlope()[i];
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mm1:
                setTextViewAction(tv_mm1, tv_mm2, tv_ss1, tv_ss2);
                isSelector = 1;
                break;
            case R.id.tv_mm2:
                setTextViewAction(tv_mm2, tv_mm1, tv_ss1, tv_ss2);
                isSelector = 2;
                break;
            case R.id.tv_ss1:
                setTextViewAction(tv_ss1, tv_mm1, tv_mm2, tv_ss2);
                isSelector = 3;
                break;
            case R.id.tv_ss2:
                setTextViewAction(tv_ss2, tv_mm1, tv_mm2, tv_ss1);
                isSelector = 4;
                break;
            case R.id.btn_TimeAdd:
                if (isSelector == 1) {
                    if (mode_time_info.getMM1() != 9) {
                        mode_time_info.setMM1(mode_time_info.getMM1() + 1);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                }
                if (isSelector == 2) {
                    if (mode_time_info.getMM2() != 9) {
                        mode_time_info.setMM2(mode_time_info.getMM2() + 1);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                    } else if (mode_time_info.getMM2() == 9 && mode_time_info.getMM1() != 9) {
                        mode_time_info.setMM2(0);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setMM1(mode_time_info.getMM1() + 1);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                }
                if (isSelector == 3) {
                    if (mode_time_info.getSS1() != 9) {
                        mode_time_info.setSS1(mode_time_info.getSS1() + 1);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                    } else if (mode_time_info.getSS1() == 9 && mode_time_info.getMM2() != 9) {
                        mode_time_info.setSS1(0);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setMM2(mode_time_info.getMM2() + 1);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                    } else if (mode_time_info.getSS1() == 9 && mode_time_info.getMM2() == 9 && mode_time_info.getMM1() != 9) {
                        mode_time_info.setSS1(0);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setMM2(0);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setMM1(mode_time_info.getMM1() + 1);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                }
                if (isSelector == 4) {
                    if (mode_time_info.getSS2() != 9) {
                        mode_time_info.setSS2(mode_time_info.getSS2() + 1);
                        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                    } else if (mode_time_info.getSS2() == 9 && mode_time_info.getSS1() != 9) {
                        mode_time_info.setSS2(0);
                        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                        mode_time_info.setSS1(mode_time_info.getSS1() + 1);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                    } else if (mode_time_info.getSS2() == 9 && mode_time_info.getSS1() == 9 && mode_time_info.getMM2() != 9) {
                        mode_time_info.setSS2(0);
                        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                        mode_time_info.setSS1(0);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setMM2(mode_time_info.getMM2() + 1);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                    } else if (mode_time_info.getSS2() == 9 && mode_time_info.getSS1() == 9 && mode_time_info.getMM2() == 9 && mode_time_info.getMM1() != 9) {
                        mode_time_info.setSS2(0);
                        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                        mode_time_info.setSS1(0);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setMM2(0);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setMM1(mode_time_info.getMM1() + 1);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                }
                ServerWindows.tv_RunningTime.setText(
                        TimeUtils.TimeFomat(
                                TimeUtils.TimeManager(String.valueOf(mode_time_info.getMM1()) +
                                        String.valueOf(mode_time_info.getMM2()) +
                                        String.valueOf(mode_time_info.getSS1()) +
                                        String.valueOf(mode_time_info.getSS2()))));
                break;
            case R.id.btn_TimeSub:
                if (isSelector == 1) {
                    if (mode_time_info.getMM1() != 0) {
                        mode_time_info.setMM1(mode_time_info.getMM1() - 1);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                    if (mode_time_info.getMM1() == 0 && mode_time_info.getMM2() == 0
                            && mode_time_info.getSS1() == 0 & mode_time_info.getSS2() == 0) {
                        mode_time_info.setMM1(0);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                        mode_time_info.setMM2(0);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setSS1(0);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setSS2(5);
                        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                    }
                }

                if (isSelector == 2) {
                    if (mode_time_info.getMM2() != 0) {
                        mode_time_info.setMM2(mode_time_info.getMM2() - 1);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                    } else if (mode_time_info.getMM2() == 0 && mode_time_info.getMM1() != 0) {
                        mode_time_info.setMM2(9);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setMM1(mode_time_info.getMM1() - 1);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                    if (mode_time_info.getMM1() == 0 && mode_time_info.getMM2() == 0
                            && mode_time_info.getSS1() == 0 & mode_time_info.getSS2() == 0) {
                        mode_time_info.setMM1(0);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                        mode_time_info.setMM2(0);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setSS1(0);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setSS2(5);
                        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                    }
                }

                if (isSelector == 3) {
                    if (mode_time_info.getSS1() != 0) {
                        mode_time_info.setSS1(mode_time_info.getSS1() - 1);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                    } else if (mode_time_info.getSS1() == 0 && mode_time_info.getMM2() != 0) {
                        mode_time_info.setSS1(9);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setMM2(mode_time_info.getMM2() - 1);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                    } else if (mode_time_info.getSS1() == 0 && mode_time_info.getMM2() == 0 && mode_time_info.getMM1() != 0) {
                        mode_time_info.setSS1(9);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setMM2(9);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setMM1(mode_time_info.getMM1() - 1);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                    if (mode_time_info.getSS1() == 0 && mode_time_info.getMM1() == 0
                            && mode_time_info.getMM2() == 0) {//当时间前两位为0 第三位减到零时 给最小值
                        mode_time_info.setSS2(5);
                        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                        mode_time_info.setSS1(0);
                        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        mode_time_info.setMM2(0);
                        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        mode_time_info.setMM1(0);
                        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                    }
                }
                if (isSelector == 4) {
                    if (MinFomat(mode_time_info.getMM1(), mode_time_info.getMM2(), mode_time_info.getSS1(), mode_time_info.getSS2()) > 5) {
                        if (mode_time_info.getSS2() != 0) {
                            mode_time_info.setSS2(mode_time_info.getSS2() - 1);
                            tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                        } else if (mode_time_info.getSS2() == 0 && mode_time_info.getSS1() != 0) {
                            mode_time_info.setSS2(9);
                            tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                            mode_time_info.setSS1(mode_time_info.getSS1() - 1);
                            tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                        } else if (mode_time_info.getSS2() == 0 && mode_time_info.getSS1() == 0 && mode_time_info.getMM2() != 0) {
                            mode_time_info.setSS2(9);
                            tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                            mode_time_info.setSS1(9);
                            tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                            mode_time_info.setMM2(mode_time_info.getMM2() - 1);
                            tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                        } else if (mode_time_info.getSS2() == 0 && mode_time_info.getSS1() == 0 && mode_time_info.getMM2() == 0 && mode_time_info.getMM1() != 0) {
                            mode_time_info.setSS2(9);
                            tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
                            mode_time_info.setSS1(9);
                            tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
                            mode_time_info.setMM2(9);
                            tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
                            mode_time_info.setMM1(mode_time_info.getMM1() - 1);
                            tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
                        }
                    }
                }
                ServerWindows.tv_RunningTime.setText(
                        TimeUtils.TimeFomat(
                                TimeUtils.TimeManager(String.valueOf(mode_time_info.getMM1()) +
                                        String.valueOf(mode_time_info.getMM2()) +
                                        String.valueOf(mode_time_info.getSS1()) +
                                        String.valueOf(mode_time_info.getSS2()))));
                break;
            case R.id.UserTime_Start_Run:
                Start_Treadmill.AnimDialog2(getContext(), Fragment_UserTime.this, 24, 17,
                        getContext().getString(R.string.Customization), SportData.RUNNING_MODE_TRAINING_USER,
                        mode_time_info.getOverTime(), mSpeed, mSlope);
                break;
        }
    }


    private int MinFomat(int min1, int min2, int ss1, int ss2) {//格式化时间
        String getMin = String.valueOf(min1) + String.valueOf(min2) + String.valueOf(ss1) + String.valueOf(ss2);
        int min = Integer.parseInt(getMin);
        return min;
    }

    private void setTextViewAction(CheckedTextView v1, CheckedTextView v2, CheckedTextView v3, CheckedTextView v4) {
        v1.setChecked(true);
        v1.setBackgroundResource(R.color.colorRed2);

        v2.setChecked(false);
        v2.setBackgroundResource(R.color.colorBlack);

        v3.setChecked(false);
        v3.setBackgroundResource(R.color.colorBlack);

        v4.setChecked(false);
        v4.setBackgroundResource(R.color.colorBlack);
    }

    public void defult() {
        setTextViewAction(tv_ss2, tv_mm1, tv_mm2, tv_ss1);
        isSelector = 4;
        tv_ss2.setChecked(true);
        mode_time_info.setMM1(0);
        mode_time_info.setMM2(0);
        mode_time_info.setSS1(0);
        mode_time_info.setSS2(5);
        tv_mm1.setText(String.valueOf(mode_time_info.getMM1()));
        tv_mm2.setText(String.valueOf(mode_time_info.getMM2()));
        tv_ss1.setText(String.valueOf(mode_time_info.getSS1()));
        tv_ss2.setText(String.valueOf(mode_time_info.getSS2()));
        ServerWindows.tv_RunningTime.setText(
                TimeUtils.TimeFomat(
                        TimeUtils.TimeManager(String.valueOf(mode_time_info.getMM1()) +
                                String.valueOf(mode_time_info.getMM2()) +
                                String.valueOf(mode_time_info.getSS1()) +
                                String.valueOf(mode_time_info.getSS2()))));
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_TimeAdd:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_TimeAdd.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_TimeAdd.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
            case R.id.btn_TimeSub:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_TimeSub.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_TimeSub.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
        }
        return false;
    }

}