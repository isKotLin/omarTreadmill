package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.start_treadmill_anim.Start_Treadmill;
import com.vigorchip.omatreadmill.utils.TimeUtils;
import com.vigorchip.omatreadmill.view.PreinstallColumnarView;

/**
 * Created by wr-app1 on 2018/4/21.
 */

public class Fragment_Train_Default extends Fragment implements View.OnClickListener {
    private View view;
    private PreinstallColumnarView preinstallColumnarView;
    private LinearLayout btn_default_warm, btn_default_climb, btn_default_aerobic,
            btn_default_health, btn_default_loseWeight, btn_default_major;
    private Button level_0, level_1, level_2, level_3, level_4, level_5;
    private Button btn_default_Start;
    private TextView title_level;
    /**切換語言后隱藏掉這幾個textview*/
    private TextView climb_sport, oxygen_sport, health_sport, slim_sport, expert_sport;

    private static double[] Arr_speed = new double[10];
    private static double[] Arr_slope = new double[10];

    private int flag_mode = 1;//模式标志位
    private int flag_level = 0;//强度标志位

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_default, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        ModeSelector(1, getContext().getString(R.string.level20), btn_default_warm, btn_default_climb, btn_default_aerobic,
                btn_default_health, btn_default_loseWeight, btn_default_major);
        LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            ServerWindows.tv_RunningTime.setText(String.valueOf("00:00:00"));
        } else {
            switch (flag_mode) {
                case 1:
                    ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0020"))));
                    break;
                case 2:
                    ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0030"))));
                    break;
                case 3:
                    ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0040"))));
                    break;
                case 4:
                    ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0050"))));
                    break;
                case 5:
                    ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0060"))));
                    break;
                case 6:
                    ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0090"))));
                    break;
            }
        }
    }

    private void init() {
        preinstallColumnarView = view.findViewById(R.id.preinstallColumnarView);//柱状图
        title_level = view.findViewById(R.id.title_level);
        btn_default_warm = view.findViewById(R.id.btn_default_warm);//热身运动
        btn_default_climb = view.findViewById(R.id.btn_default_climb);//爬山运动
        btn_default_aerobic = view.findViewById(R.id.btn_default_aerobic);//有氧运动
        btn_default_health = view.findViewById(R.id.btn_default_health);//保健运动
        btn_default_loseWeight = view.findViewById(R.id.btn_default_loseWeight);//减肥运动
        btn_default_major = view.findViewById(R.id.btn_default_major);//专业运动

        climb_sport = view.findViewById(R.id.climb_sport);
        oxygen_sport = view.findViewById(R.id.oxygen_sport);
        health_sport = view.findViewById(R.id.health_sport);
        slim_sport = view.findViewById(R.id.slim_sport);
        expert_sport = view.findViewById(R.id.expert_sport);

        level_0 = view.findViewById(R.id.level_0);//强度0
        level_1 = view.findViewById(R.id.level_1);//强度1
        level_2 = view.findViewById(R.id.level_2);//强度2
        level_3 = view.findViewById(R.id.level_3);//强度3
        level_4 = view.findViewById(R.id.level_4);//强度4
        level_5 = view.findViewById(R.id.level_5);//强度5
        btn_default_Start = view.findViewById(R.id.btn_default_Start);//启动跑步机
        preinstallColumnarView.upDataRec(getDataColumnar(3.0, 4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 3.0));//绿色柱状条速度
        preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));//红线坡度
        ModeSelector(1, getContext().getString(R.string.level20), btn_default_warm, btn_default_climb, btn_default_aerobic,
                btn_default_health, btn_default_loseWeight, btn_default_major);
        LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);

        btn_default_warm.setOnClickListener(this);
        btn_default_climb.setOnClickListener(this);
        btn_default_aerobic.setOnClickListener(this);
        btn_default_health.setOnClickListener(this);
        btn_default_loseWeight.setOnClickListener(this);
        btn_default_major.setOnClickListener(this);
        level_0.setOnClickListener(this);
        level_1.setOnClickListener(this);
        level_2.setOnClickListener(this);
        level_3.setOnClickListener(this);
        level_4.setOnClickListener(this);
        level_5.setOnClickListener(this);
        btn_default_Start.setOnClickListener(this);

        String able = getContext().getResources().getConfiguration().locale.getLanguage();
        if (able.equals("en")){//如果是英語則隱藏這幾個textview
            climb_sport.setVisibility(View.GONE);
            oxygen_sport.setVisibility(View.GONE);
            health_sport.setVisibility(View.GONE);
            slim_sport.setVisibility(View.GONE);
            expert_sport.setVisibility(View.GONE);
        } else if (able.equals("fr")){//如果是法語則隱藏這幾個textview
            climb_sport.setVisibility(View.GONE);
            oxygen_sport.setVisibility(View.GONE);
            health_sport.setVisibility(View.GONE);
            slim_sport.setVisibility(View.GONE);
            expert_sport.setVisibility(View.GONE);
        } else {//否則中文的話，如果是隱藏則顯示
            if (climb_sport.getVisibility() == View.GONE || oxygen_sport.getVisibility() == View.GONE
                    || health_sport.getVisibility() == View.GONE || slim_sport.getVisibility() == View.GONE
                    || expert_sport.getVisibility() == View.GONE) {
                climb_sport.setVisibility(View.VISIBLE);
                oxygen_sport.setVisibility(View.VISIBLE);
                health_sport.setVisibility(View.VISIBLE);
                slim_sport.setVisibility(View.VISIBLE);
                expert_sport.setVisibility(View.VISIBLE);
            }
        }

    }

    private double[] getDataLine(double data0, double data1, double data2, double data3,
                                 double data4, double data5, double data6,
                                 double data7, double data8, double data9) {//红线坡度
        Arr_slope[0] = data0;
        Arr_slope[1] = data1;
        Arr_slope[2] = data2;
        Arr_slope[3] = data3;
        Arr_slope[4] = data4;
        Arr_slope[5] = data5;
        Arr_slope[6] = data6;
        Arr_slope[7] = data7;
        Arr_slope[8] = data8;
        Arr_slope[9] = data9;
        return Arr_slope;
    }

    private double[] getDataColumnar(double data0, double data1, double data2, double data3,
                                     double data4, double data5, double data6,
                                     double data7, double data8, double data9) {//绿色柱状条速度
        Arr_speed[0] = data0;
        Arr_speed[1] = data1;
        Arr_speed[2] = data2;
        Arr_speed[3] = data3;
        Arr_speed[4] = data4;
        Arr_speed[5] = data5;
        Arr_speed[6] = data6;
        Arr_speed[7] = data7;
        Arr_speed[8] = data8;
        Arr_speed[9] = data9;
        return Arr_speed;
    }


    @Override
    public void onClick(View v) {
        ModeClick(v);
        LevelClick(v);
    }


    private void LevelClick(View v) {
        switch (v.getId()) {
            case R.id.level_0:
                if (flag_mode == 1) {//热身运动
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(3.0, 4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_mode == 2) {//爬山运动
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(3.0, 4.0, 5.0, 4.0, 5.0, 4.0, 5.0, 4.0, 5.0, 2.0));
                } else if (flag_mode == 3) {//有氧运动
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 2.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_mode == 4) {//保健运动
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(3.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 5.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_mode == 5) {//减肥运动
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(4.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 2.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_mode == 6) {//专业运动
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(3.0, 5.0, 3.0, 5.0, 3.0, 5.0, 3.0, 5.0, 3.0, 0.0));
                }
                break;
            case R.id.level_1:
                if (flag_mode == 1) {//热身运动
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(4.0, 5.0, 6.0, 6.0, 6.0, 6.0, 6.0, 6.0, 6.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_mode == 2) {//爬山运动
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 6.0, 5.0, 6.0, 5.0, 6.0, 5.0, 6.0, 3.0));
                } else if (flag_mode == 3) {//有氧运动
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_mode == 4) {//保健运动
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(4.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 6.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_mode == 5) {//减肥运动
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_mode == 6) {//专业运动
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(9.0, 13.0, 11.0, 13.0, 11.0, 13.0, 11.0, 13.0, 11.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 6.0, 4.0, 6.0, 4.0, 6.0, 4.0, 6.0, 4.0, 1.0));
                }
                break;
            case R.id.level_2:
                if (flag_mode == 1) {//热身运动
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 6.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_mode == 2) {//爬山运动
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(5.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 4.0));
                } else if (flag_mode == 3) {//有氧运动
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 10.0, 11.0, 10.0, 11.0, 10.0, 11.0, 10.0, 11.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_mode == 4) {//保健运动
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 7.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_mode == 5) {//减肥运动
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_mode == 6) {//专业运动
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(10.0, 14.0, 12.0, 14.0, 12.0, 14.0, 12.0, 14.0, 12.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(5.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 2.0));
                }
                break;
            case R.id.level_3:
                if (flag_mode == 1) {//热身运动
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 7.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_mode == 2) {//爬山运动
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(6.0, 7.0, 8.0, 7.0, 8.0, 7.0, 8.0, 7.0, 8.0, 5.0));
                } else if (flag_mode == 3) {//有氧运动
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(9.0, 11.0, 12.0, 11.0, 12.0, 11.0, 12.0, 11.0, 12.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_mode == 4) {//保健运动
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 8.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_mode == 5) {//减肥运动
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_mode == 6) {//专业运动
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 15.0, 13.0, 15.0, 13.0, 15.0, 13.0, 15.0, 13.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 3.0));
                }
                break;
            case R.id.level_4:
                if (flag_mode == 1) {//热身运动
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 8.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_mode == 2) {//爬山运动
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(7.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 6.0));
                } else if (flag_mode == 3) {//有氧运动
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(10.0, 12.0, 13.0, 12.0, 13.0, 12.0, 13.0, 12.0, 13.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_mode == 4) {//保健运动
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 11.0, 11.0, 11.0, 11.0, 11.0, 11.0, 11.0, 9.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_mode == 5) {//减肥运动
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 11.0, 9.0, 11.0, 9.0, 11.0, 9.0, 11.0, 9.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_mode == 6) {//专业运动
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 16.0, 14.0, 16.0, 14.0, 16.0, 14.0, 16.0, 14.0, 8.0));
                    preinstallColumnarView.upDataLine(getDataLine(7.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 4.0));
                }
                break;
            case R.id.level_5:
                if (flag_mode == 1) {//热身运动
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 9.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 8.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_mode == 2) {//爬山运动
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(8.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 7.0));
                } else if (flag_mode == 3) {//有氧运动
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 13.0, 14.0, 13.0, 14.0, 13.0, 14.0, 13.0, 14.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_mode == 4) {//保健运动
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 10.0, 8.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_mode == 5) {//减肥运动
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(9.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_mode == 6) {//专业运动
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 15.0, 14.0, 15.0, 14.0, 15.0, 14.0, 15.0, 14.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(8.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 5.0));
                }
                break;
        }
    }

    private void ModeClick(View v) {
        switch (v.getId()) {
            case R.id.btn_default_warm://热身运动
                ModeSelector(1, getContext().getString(R.string.level20), btn_default_warm, btn_default_climb, btn_default_aerobic,
                        btn_default_health, btn_default_loseWeight, btn_default_major);
                if (flag_level == 0) {
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(3.0, 4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_level == 1) {
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(4.0, 5.0, 6.0, 6.0, 6.0, 6.0, 6.0, 6.0, 6.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_level == 2) {
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 6.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_level == 3) {
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 7.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_level == 4) {
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 8.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                } else if (flag_level == 5) {
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 9.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 8.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 0.0));
                }
                break;
            case R.id.btn_default_climb://爬山运动
                ModeSelector(2, getContext().getString(R.string.level30), btn_default_climb, btn_default_warm, btn_default_aerobic,
                        btn_default_health, btn_default_loseWeight, btn_default_major);
                if (flag_level == 0) {
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(3.0, 4.0, 5.0, 4.0, 5.0, 4.0, 5.0, 4.0, 5.0, 2.0));
                } else if (flag_level == 1) {
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 6.0, 5.0, 6.0, 5.0, 6.0, 5.0, 6.0, 3.0));
                } else if (flag_level == 2) {
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(5.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 4.0));
                } else if (flag_level == 3) {
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(6.0, 7.0, 8.0, 7.0, 8.0, 7.0, 8.0, 7.0, 8.0, 5.0));
                } else if (flag_level == 4) {
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(7.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 6.0));
                } else if (flag_level == 5) {
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 7.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(8.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 7.0));
                }
                break;
            case R.id.btn_default_aerobic://有氧运动
                ModeSelector(3, getContext().getString(R.string.level40), btn_default_aerobic, btn_default_climb, btn_default_warm,
                        btn_default_health, btn_default_loseWeight, btn_default_major);
                if (flag_level == 0) {
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 8.0, 9.0, 2.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_level == 1) {
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 9.0, 10.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_level == 2) {
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 10.0, 11.0, 10.0, 11.0, 10.0, 11.0, 10.0, 11.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_level == 3) {
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(9.0, 11.0, 12.0, 11.0, 12.0, 11.0, 12.0, 11.0, 12.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_level == 4) {
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(10.0, 12.0, 13.0, 12.0, 13.0, 12.0, 13.0, 12.0, 13.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                } else if (flag_level == 5) {
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 13.0, 14.0, 13.0, 14.0, 13.0, 14.0, 13.0, 14.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 0.0));
                }
                break;
            case R.id.btn_default_health://保健运动
                ModeSelector(4, getContext().getString(R.string.level50), btn_default_health, btn_default_aerobic, btn_default_climb,
                        btn_default_warm, btn_default_loseWeight, btn_default_major);
                if (flag_level == 0) {
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(3.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 7.0, 5.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_level == 1) {
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(4.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 6.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_level == 2) {
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 7.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_level == 3) {
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 8.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_level == 4) {
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 11.0, 11.0, 11.0, 11.0, 11.0, 11.0, 11.0, 9.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                } else if (flag_level == 5) {
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 10.0, 8.0));
                    preinstallColumnarView.upDataLine(getDataLine(2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0));
                }
                break;
            case R.id.btn_default_loseWeight://减肥运动
                ModeSelector(5, getContext().getString(R.string.level60), btn_default_loseWeight, btn_default_health, btn_default_aerobic,
                        btn_default_climb, btn_default_warm, btn_default_major);
                if (flag_level == 0) {
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(4.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 2.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_level == 1) {
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(5.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 3.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_level == 2) {
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(6.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_level == 3) {
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(7.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_level == 4) {
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 11.0, 9.0, 11.0, 9.0, 11.0, 9.0, 11.0, 9.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                } else if (flag_level == 5) {
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(9.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 0.0));
                }
                break;
            case R.id.btn_default_major://专业运动
                ModeSelector(6, getContext().getString(R.string.level90), btn_default_major, btn_default_loseWeight, btn_default_health,
                        btn_default_aerobic, btn_default_climb, btn_default_warm);
                if (flag_level == 0) {
                    LevelSelector(0, level_0, level_1, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(8.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 12.0, 10.0, 4.0));
                    preinstallColumnarView.upDataLine(getDataLine(3.0, 5.0, 3.0, 5.0, 3.0, 5.0, 3.0, 5.0, 3.0, 0.0));
                } else if (flag_level == 1) {
                    LevelSelector(1, level_1, level_0, level_2, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(9.0, 13.0, 11.0, 13.0, 11.0, 13.0, 11.0, 13.0, 11.0, 5.0));
                    preinstallColumnarView.upDataLine(getDataLine(4.0, 6.0, 4.0, 6.0, 4.0, 6.0, 4.0, 6.0, 4.0, 1.0));
                } else if (flag_level == 2) {
                    LevelSelector(2, level_2, level_1, level_0, level_3, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(10.0, 14.0, 12.0, 14.0, 12.0, 14.0, 12.0, 14.0, 12.0, 6.0));
                    preinstallColumnarView.upDataLine(getDataLine(5.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 7.0, 5.0, 2.0));
                } else if (flag_level == 3) {
                    LevelSelector(3, level_3, level_2, level_1, level_0, level_4, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 15.0, 13.0, 15.0, 13.0, 15.0, 13.0, 15.0, 13.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 3.0));
                } else if (flag_level == 4) {
                    LevelSelector(4, level_4, level_3, level_2, level_1, level_0, level_5);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 16.0, 14.0, 16.0, 14.0, 16.0, 14.0, 16.0, 14.0, 8.0));
                    preinstallColumnarView.upDataLine(getDataLine(7.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 9.0, 7.0, 4.0));
                } else if (flag_level == 5) {
                    LevelSelector(5, level_5, level_4, level_3, level_2, level_1, level_0);
                    preinstallColumnarView.upDataRec(getDataColumnar(11.0, 15.0, 14.0, 15.0, 14.0, 15.0, 14.0, 15.0, 14.0, 7.0));
                    preinstallColumnarView.upDataLine(getDataLine(8.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 10.0, 8.0, 5.0));
                }
                break;
            case R.id.btn_default_Start:
                switch (flag_mode) {
                    case 1:
                        Start_Treadmill.AnimDialog2(getContext(), Fragment_Train_Default.this, 15, 17,
                                getContext().getString(R.string.Warm) + getContext().getString(R.string.Up), SportData.RUNNING_MODE_WARMUP, "0020", Arr_speed, Arr_slope);
                        break;
                    case 2:
                        Start_Treadmill.AnimDialog2(getContext(), Fragment_Train_Default.this, 15, 17,
                                getContext().getString(R.string.Climb), SportData.RUNNING_MODE_CLIMB, "0030", Arr_speed, Arr_slope);
                        break;
                    case 3:
                        Start_Treadmill.AnimDialog2(getContext(), Fragment_Train_Default.this, 15, 17,
                                getContext().getString(R.string.Oxygen), SportData.RUNNING_MODE_AEROBIC, "0040", Arr_speed, Arr_slope);
                        break;
                    case 4:
                        Start_Treadmill.AnimDialog2(getContext(), Fragment_Train_Default.this, 15, 17,
                                getContext().getString(R.string.Health), SportData.RUNNING_MODE_HEALTH, "0050", Arr_speed, Arr_slope);
                        break;
                    case 5:
                        Start_Treadmill.AnimDialog2(getContext(), Fragment_Train_Default.this, 15, 17,
                                getContext().getString(R.string.Slim), SportData.RUNNING_MODE_LOSEWEIGHT, "0060", Arr_speed, Arr_slope);
                        break;
                    case 6:
                        Start_Treadmill.AnimDialog2(getContext(), Fragment_Train_Default.this, 15, 17,
                                getContext().getString(R.string.Expert), SportData.RUNNING_MODE_MAJOR, "0090", Arr_speed, Arr_slope);
                        break;
                }
                break;
        }
    }


    private void ModeSelector(int flag, String title, View view1, View view2, View view3,
                              View view4, View view5, View view6) {
        flag_mode = flag;
        switch (flag_mode) {
            case 1:
                ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0020"))));
                break;
            case 2:
                ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0030"))));
                break;
            case 3:
                ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0040"))));
                break;
            case 4:
                ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0050"))));
                break;
            case 5:
                ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0060"))));
                break;
            case 6:
                ServerWindows.tv_RunningTime.setText(String.valueOf(TimeUtils.TimeFomat(TimeUtils.TimeManager("0090"))));
                break;
        }
        title_level.setText(String.valueOf(title));
        view1.setBackgroundResource(R.color.colorBlack);
        view2.setBackgroundResource(R.color.colorBlack3);
        view3.setBackgroundResource(R.color.colorBlack3);
        view4.setBackgroundResource(R.color.colorBlack3);
        view5.setBackgroundResource(R.color.colorBlack3);
        view6.setBackgroundResource(R.color.colorBlack3);
    }

    private void LevelSelector(int flag, View view0, View view1, View view2, View view3,
                               View view4, View view5) {
        flag_level = flag;
        view0.setBackgroundResource(R.color.colorBlack);
        view1.setBackgroundResource(R.color.colorBlack3);
        view2.setBackgroundResource(R.color.colorBlack3);
        view3.setBackgroundResource(R.color.colorBlack3);
        view4.setBackgroundResource(R.color.colorBlack3);
        view5.setBackgroundResource(R.color.colorBlack3);
    }


}
