package com.vigorchip.omatreadmill.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.Mode_Hrc_Info;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.start_treadmill_anim.Start_Treadmill;
import com.vigorchip.omatreadmill.utils.TimeUtils;

/**
 * Created by wr-app1 on 2018/5/3.
 */

public class Fragment_Train_Heart extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private CheckedTextView tv_age1, tv_age2, tv_hrc1, tv_hrc2, tv_hrc3, tv_min1, tv_min2;
    private RelativeLayout btn_hrcModeAdd, btn_hrcModeSub;
    private Button hrc_Start_Run;
    private Mode_Hrc_Info modeHrcInfo;

    //心率开始跑步动画
    private Animation animation1;
    private Animation animation2;
    private Animation animation3;
    private Animation animation4;
    private Animation animation5;
    private Animation animation6;
    private Dialog dialog;

    private int checktv_flag = 0;//判断当前是哪个CheckText

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_heart, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            ServerWindows.tv_RunningTime.setText(String.valueOf("00:00:00"));
        } else {
            DefultInfo();
        }
    }

    private void init() {
        modeHrcInfo = new Mode_Hrc_Info();
        tv_age1 = view.findViewById(R.id.tv_age1);
        tv_age2 = view.findViewById(R.id.tv_age2);
        tv_hrc1 = view.findViewById(R.id.tv_hrc1);
        tv_hrc2 = view.findViewById(R.id.tv_hrc2);
        tv_hrc3 = view.findViewById(R.id.tv_hrc3);
        tv_min1 = view.findViewById(R.id.tv_min1);
        tv_min2 = view.findViewById(R.id.tv_min2);
        tv_age1.setOnClickListener(this);
        tv_age2.setOnClickListener(this);
        tv_hrc1.setOnClickListener(this);
        tv_hrc2.setOnClickListener(this);
        tv_hrc3.setOnClickListener(this);
        tv_min1.setOnClickListener(this);
        tv_min2.setOnClickListener(this);
        btn_hrcModeAdd = view.findViewById(R.id.btn_hrcModeAdd);
        btn_hrcModeSub = view.findViewById(R.id.btn_hrcModeSub);
        hrc_Start_Run = view.findViewById(R.id.hrc_Start_Run);
        btn_hrcModeAdd.setOnClickListener(this);
        btn_hrcModeSub.setOnClickListener(this);
        btn_hrcModeAdd.setOnTouchListener(this);
        btn_hrcModeSub.setOnTouchListener(this);
        hrc_Start_Run.setOnClickListener(this);
        DefultInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_age1:
                CheckTextSelector(1, tv_age1, tv_age2, tv_hrc1, tv_hrc2, tv_hrc3, tv_min1, tv_min2);
                break;
            case R.id.tv_age2:
                CheckTextSelector(2, tv_age2, tv_age1, tv_hrc1, tv_hrc2, tv_hrc3, tv_min1, tv_min2);
                break;
            case R.id.tv_hrc1:
                CheckTextSelector(3, tv_hrc1, tv_age2, tv_age1, tv_hrc2, tv_hrc3, tv_min1, tv_min2);
                break;
            case R.id.tv_hrc2:
                CheckTextSelector(4, tv_hrc2, tv_hrc1, tv_age2, tv_age1, tv_hrc3, tv_min1, tv_min2);
                break;
            case R.id.tv_hrc3:
                CheckTextSelector(5, tv_hrc3, tv_hrc2, tv_hrc1, tv_age2, tv_age1, tv_min1, tv_min2);
                break;
            case R.id.tv_min1:
                CheckTextSelector(6, tv_min1, tv_hrc3, tv_hrc2, tv_hrc1, tv_age2, tv_age1, tv_min2);
                break;
            case R.id.tv_min2:
                CheckTextSelector(7, tv_min2, tv_min1, tv_hrc3, tv_hrc2, tv_hrc1, tv_age2, tv_age1);
                break;
            case R.id.btn_hrcModeAdd:
                addAgeJudge();//增加年龄的判断逻辑
                addHrcJudge();//增加心率的判断逻辑
                addMinJudge();//增加时间的判断逻辑
                break;
            case R.id.btn_hrcModeSub:
                minusAgeJudge();//减少年龄的判断逻辑
                minusHrcJudge();//减少心率的判断逻辑
                minusMinJudge();//减少时间的判断逻辑
                break;
            case R.id.hrc_Start_Run:
                Start_Treadmill.AnimDialog4(getContext(), Fragment_Train_Heart.this,
                        modeHrcInfo.getOverAge(), modeHrcInfo.getOverHrc(), modeHrcInfo.getOverMin());
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_hrcModeAdd:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_hrcModeAdd.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_hrcModeAdd.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
            case R.id.btn_hrcModeSub:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_hrcModeSub.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_hrcModeSub.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
        }
        return false;
    }

    private void addAgeJudge() {
        if (checktv_flag == 1 || checktv_flag == 2) {//年龄的判断开始
            if (AgeFomat(modeHrcInfo.getAge1(), modeHrcInfo.getAge2()) < 80) {//年龄的判断逻辑
                if (checktv_flag == 1) {//年龄的第一位判断逻辑
                    if (modeHrcInfo.getAge1() != 8) {
                        modeHrcInfo.setAge1(modeHrcInfo.getAge1() + 1);
                        tv_age1.setText(String.valueOf(modeHrcInfo.getAge1()));
                    }
                    if (modeHrcInfo.getAge1() == 8) {
                        modeHrcInfo.setAge2(0);
                        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
                    }
                }
                if (checktv_flag == 2) {//年龄的第二位判断逻辑
                    if (modeHrcInfo.getAge2() != 9) {
                        modeHrcInfo.setAge2(modeHrcInfo.getAge2() + 1);
                        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
                    } else if (modeHrcInfo.getAge2() == 9 && modeHrcInfo.getAge1() != 8) {
                        modeHrcInfo.setAge1(modeHrcInfo.getAge1() + 1);
                        modeHrcInfo.setAge2(0);
                        tv_age1.setText(String.valueOf(modeHrcInfo.getAge1()));
                        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
                    }
                }
            }
        }
    }

    private void minusAgeJudge() {
        if (checktv_flag == 1 || checktv_flag == 2) {//年龄的判断开始
            if (AgeFomat(modeHrcInfo.getAge1(), modeHrcInfo.getAge2()) > 15) {//年龄的判断逻辑
                if (checktv_flag == 1) {//年龄的第一位判断逻辑
                    if (modeHrcInfo.getAge1() > 1) {
                        modeHrcInfo.setAge1(modeHrcInfo.getAge1() - 1);
                        tv_age1.setText(String.valueOf(modeHrcInfo.getAge1()));
                    } else if (modeHrcInfo.getAge1() == 1) {
                        modeHrcInfo.setAge2(5);
                        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
                    }
                    if (modeHrcInfo.getAge1() == 1 && modeHrcInfo.getAge2() < 5) {
                        modeHrcInfo.setAge2(5);
                        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
                    }
                }
                if (checktv_flag == 2) {//年龄的第二位判断逻辑
                    if (modeHrcInfo.getAge2() != 0) {
                        modeHrcInfo.setAge2(modeHrcInfo.getAge2() - 1);
                        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
                    } else if (modeHrcInfo.getAge2() == 0 && modeHrcInfo.getAge1() != 1) {
                        modeHrcInfo.setAge2(9);
                        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
                        modeHrcInfo.setAge1(modeHrcInfo.getAge1() - 1);
                        tv_age1.setText(String.valueOf(modeHrcInfo.getAge1()));
                    }
                }
            }
        }
    }

    private void addHrcJudge() {
        if (checktv_flag == 3 || checktv_flag == 4 || checktv_flag == 5) {//心率判断开始
            if (HrcFomat(modeHrcInfo.getHrc1(), modeHrcInfo.getHrc2(), modeHrcInfo.getHrc3()) < 180) {
                if (checktv_flag == 3) {//心率第三位数值
                    if (modeHrcInfo.getHrc1() != 1) {
                        modeHrcInfo.setHrc1(modeHrcInfo.getHrc1() + 1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                    } else if (modeHrcInfo.getHrc1() == 1) {
                        modeHrcInfo.setHrc1(1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        modeHrcInfo.setHrc2(8);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        modeHrcInfo.setHrc3(0);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    }
                    if (modeHrcInfo.getHrc2() >= 8 && modeHrcInfo.getHrc3() > 0) {
                        modeHrcInfo.setHrc1(1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        modeHrcInfo.setHrc2(8);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        modeHrcInfo.setHrc3(0);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    }
                } else if (checktv_flag == 4) {
                    if (modeHrcInfo.getHrc2() < 9) {
                        modeHrcInfo.setHrc2(modeHrcInfo.getHrc2() + 1);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        if (modeHrcInfo.getHrc2() == 8 && modeHrcInfo.getHrc1() == 1) {
                            modeHrcInfo.setHrc1(1);
                            tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                            modeHrcInfo.setHrc2(8);
                            tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                            modeHrcInfo.setHrc3(0);
                            tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                        }
                    } else if (modeHrcInfo.getHrc2() == 9 && modeHrcInfo.getHrc1() == 0) {
                        modeHrcInfo.setHrc1(1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        modeHrcInfo.setHrc2(0);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                    }
                } else if (checktv_flag == 5) {
                    if (modeHrcInfo.getHrc3() < 9) {
                        modeHrcInfo.setHrc3(modeHrcInfo.getHrc3() + 1);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    } else if (modeHrcInfo.getHrc3() == 9 && modeHrcInfo.getHrc2() < 9) {
                        modeHrcInfo.setHrc2(modeHrcInfo.getHrc2() + 1);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        modeHrcInfo.setHrc3(0);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    } else if (modeHrcInfo.getHrc3() == 9 && modeHrcInfo.getHrc2() == 9 && modeHrcInfo.getHrc1() != 1) {
                        modeHrcInfo.setHrc1(modeHrcInfo.getHrc1() + 1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        modeHrcInfo.setHrc2(0);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        modeHrcInfo.setHrc3(0);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    }
                }
            }
        }
    }

    private void minusHrcJudge() {
        if (checktv_flag == 3 || checktv_flag == 4 || checktv_flag == 5) {//心率判断开始
            if (HrcFomat(modeHrcInfo.getHrc1(), modeHrcInfo.getHrc2(), modeHrcInfo.getHrc3()) > 80) {
                if (checktv_flag == 3) {//心率第一位的判断逻辑
                    if (modeHrcInfo.getHrc1() > 0) {
                        modeHrcInfo.setHrc1(modeHrcInfo.getHrc1() - 1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        if (modeHrcInfo.getHrc1() == 0) {
                            modeHrcInfo.setHrc1(0);
                            tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                            modeHrcInfo.setHrc2(8);
                            tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                            modeHrcInfo.setHrc3(0);
                            tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                        }
                    }
                } else if (checktv_flag == 4) {//心率第二位的判断逻辑
                    if (modeHrcInfo.getHrc2() > 0 && modeHrcInfo.getHrc1() != 0) {
                        modeHrcInfo.setHrc2(modeHrcInfo.getHrc2() - 1);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                    } else if (modeHrcInfo.getHrc2() > 8 && modeHrcInfo.getHrc1() == 0) {
                        modeHrcInfo.setHrc2(modeHrcInfo.getHrc2() - 1);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                    } else if (modeHrcInfo.getHrc1() != 0 && modeHrcInfo.getHrc2() == 0) {
                        modeHrcInfo.setHrc1(modeHrcInfo.getHrc1() - 1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        modeHrcInfo.setHrc2(9);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                    } else if (modeHrcInfo.getHrc1() == 0 && modeHrcInfo.getHrc2() == 8) {
                        modeHrcInfo.setHrc1(0);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        modeHrcInfo.setHrc2(8);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        modeHrcInfo.setHrc3(0);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    }
                } else if (checktv_flag == 5) {//心率第三位的判断逻辑
                    if (modeHrcInfo.getHrc3() > 0) {
                        modeHrcInfo.setHrc3(modeHrcInfo.getHrc3() - 1);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    } else if (modeHrcInfo.getHrc3() == 0 && modeHrcInfo.getHrc2() != 0) {
                        modeHrcInfo.setHrc2(modeHrcInfo.getHrc2() - 1);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        modeHrcInfo.setHrc3(9);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    } else if (modeHrcInfo.getHrc3() == 0 && modeHrcInfo.getHrc2() == 0 && modeHrcInfo.getHrc1() != 0) {
                        modeHrcInfo.setHrc1(modeHrcInfo.getHrc1() - 1);
                        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
                        modeHrcInfo.setHrc2(9);
                        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
                        modeHrcInfo.setHrc3(9);
                        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
                    }
                }
            }
        }
    }

    private void addMinJudge() {
        if (checktv_flag == 6 || checktv_flag == 7) {
            if (MinFomat(modeHrcInfo.getMin1(), modeHrcInfo.getMin2()) < 99) {
                if (checktv_flag == 6) {//时间的第一位判断逻辑
                    if (modeHrcInfo.getMin1() != 9) {
                        modeHrcInfo.setMin1(modeHrcInfo.getMin1() + 1);
                        tv_min1.setText(String.valueOf(modeHrcInfo.getMin1()));
                    } else if (modeHrcInfo.getMin1() == 9) {
                        modeHrcInfo.setMin2(9);
                        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
                    }
                    if (modeHrcInfo.getMin1() == 0 && modeHrcInfo.getMin2() < 5) {
                        modeHrcInfo.setMin1(0);
                        tv_min1.setText(String.valueOf(modeHrcInfo.getMin1()));
                        modeHrcInfo.setMin2(5);
                        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
                    }
                } else if (checktv_flag == 7) {
                    if (modeHrcInfo.getMin2() != 9) {
                        modeHrcInfo.setMin2(modeHrcInfo.getMin2() + 1);
                        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
                    } else if (modeHrcInfo.getMin2() == 9 && modeHrcInfo.getMin1() != 9) {
                        modeHrcInfo.setMin1(modeHrcInfo.getMin1() + 1);
                        modeHrcInfo.setMin2(0);
                        tv_min1.setText(String.valueOf(modeHrcInfo.getMin1()));
                        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
                    }
                }
                ServerWindows.tv_RunningTime.setText(TimeUtils.TimeFomat(TimeUtils.TimeManager(
                        "00" + String.valueOf(modeHrcInfo.getMin1()) + String.valueOf(modeHrcInfo.getMin2()))));
            }
        }

    }

    private void minusMinJudge() {
        if (checktv_flag == 6 || checktv_flag == 7) {//时间的判断开始
            if (MinFomat(modeHrcInfo.getMin1(), modeHrcInfo.getMin2()) > 5) {
                if (checktv_flag == 6) {
                    if (modeHrcInfo.getMin1() > 0) {
                        modeHrcInfo.setMin1(modeHrcInfo.getMin1() - 1);
                        tv_min1.setText(String.valueOf(modeHrcInfo.getMin1()));
                    }
                    if (modeHrcInfo.getMin1() == 0) {
                        modeHrcInfo.setMin1(0);
                        tv_min1.setText(String.valueOf(modeHrcInfo.getMin1()));
                        modeHrcInfo.setMin2(5);
                        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
                    }
                } else if (checktv_flag == 7) {
                    if (modeHrcInfo.getMin2() != 0) {
                        modeHrcInfo.setMin2(modeHrcInfo.getMin2() - 1);
                        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
                    } else if (modeHrcInfo.getMin2() == 0 && modeHrcInfo.getMin1() != 0) {
                        modeHrcInfo.setMin2(9);
                        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
                        modeHrcInfo.setMin1(modeHrcInfo.getMin1() - 1);
                        tv_min1.setText(String.valueOf(modeHrcInfo.getMin1()));
                    }
                }
                ServerWindows.tv_RunningTime.setText(TimeUtils.TimeFomat(TimeUtils.TimeManager(
                        "00" + String.valueOf(modeHrcInfo.getMin1()) + String.valueOf(modeHrcInfo.getMin2()))));
            }
        }
    }

    private int AgeFomat(int age1, int age2) {//格式化年龄
        String getAge = String.valueOf(age1) + String.valueOf(age2);
        int age = Integer.parseInt(getAge);
        return age;
    }

    private int HrcFomat(int hrc1, int hrc2, int hrc3) {//格式化心率
        String getHrc = String.valueOf(hrc1) + String.valueOf(hrc2) + String.valueOf(hrc3);
        int hrc = Integer.parseInt(getHrc);
        return hrc;
    }

    private int MinFomat(int min1, int min2) {//格式化时间
        String getMin = String.valueOf(min1) + String.valueOf(min2);
        int min = Integer.parseInt(getMin);
        return min;
    }

    private void DefultInfo() {//第一次进来设置默认值
        modeHrcInfo.setAge1(2);
        modeHrcInfo.setAge2(5);
        modeHrcInfo.setHrc1(1);
        modeHrcInfo.setHrc2(1);
        modeHrcInfo.setHrc3(7);
        modeHrcInfo.setMin1(3);
        modeHrcInfo.setMin2(0);
        tv_age1.setText(String.valueOf(modeHrcInfo.getAge1()));
        tv_age2.setText(String.valueOf(modeHrcInfo.getAge2()));
        tv_hrc1.setText(String.valueOf(modeHrcInfo.getHrc1()));
        tv_hrc2.setText(String.valueOf(modeHrcInfo.getHrc2()));
        tv_hrc3.setText(String.valueOf(modeHrcInfo.getHrc3()));
        tv_min1.setText(String.valueOf(modeHrcInfo.getMin1()));
        tv_min2.setText(String.valueOf(modeHrcInfo.getMin2()));
        CheckTextSelector(6, tv_min1, tv_hrc3, tv_hrc2, tv_hrc1, tv_age2, tv_age1, tv_min2);
        ServerWindows.tv_RunningTime.setText(TimeUtils.TimeFomat(TimeUtils.TimeManager("00" + String.valueOf(modeHrcInfo.getMin1()) + String.valueOf(modeHrcInfo.getMin2()))));
    }

    private void CheckTextSelector(int flag, CheckedTextView v1, CheckedTextView v2, CheckedTextView v3,
                                   CheckedTextView v4, CheckedTextView v5, CheckedTextView v6,
                                   CheckedTextView v7) {//设置点击背景色
        checktv_flag = flag;

        v1.setChecked(true);
        v1.setBackgroundResource(R.color.colorRed2);

        v2.setChecked(false);
        v2.setBackgroundResource(R.color.colorBlack);

        v3.setChecked(false);
        v3.setBackgroundResource(R.color.colorBlack);

        v4.setChecked(false);
        v4.setBackgroundResource(R.color.colorBlack);

        v5.setChecked(false);
        v5.setBackgroundResource(R.color.colorBlack);

        v6.setChecked(false);
        v6.setBackgroundResource(R.color.colorBlack);

        v7.setChecked(false);
        v7.setBackgroundResource(R.color.colorBlack);
    }

}
