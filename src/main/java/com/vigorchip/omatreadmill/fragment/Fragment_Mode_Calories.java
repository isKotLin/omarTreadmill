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
import com.vigorchip.omatreadmill.bean.Mode_Calories_Info;
import com.vigorchip.omatreadmill.bean.Running_Info;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.start_treadmill_anim.Start_Treadmill;

/**
 * Created by wr-app1 on 2018/3/28.
 */

public class Fragment_Mode_Calories extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private CheckedTextView tv_ca1, tv_ca2, tv_ca3, tv_ca4;
    private RelativeLayout btn_CalAdd, btn_CalSub;
    private Button Calories_Start_Run;
    Mode_Calories_Info mode_calories_info;
    private int isSelector = 0;
    private Running_Info running_info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mode_calories, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        defult();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            defult();
        }
    }

    private void init() {
        mode_calories_info = new Mode_Calories_Info();
        running_info = new Running_Info();
        tv_ca1 = view.findViewById(R.id.tv_ca1);
        tv_ca2 = view.findViewById(R.id.tv_ca2);
        tv_ca3 = view.findViewById(R.id.tv_ca3);
        tv_ca4 = view.findViewById(R.id.tv_ca4);
        btn_CalAdd = view.findViewById(R.id.btn_CalAdd);
        btn_CalSub = view.findViewById(R.id.btn_CalSub);
        Calories_Start_Run = view.findViewById(R.id.Calories_Start_Run);
        tv_ca1.setOnClickListener(this);
        tv_ca2.setOnClickListener(this);
        tv_ca3.setOnClickListener(this);
        tv_ca4.setOnClickListener(this);
        btn_CalAdd.setOnClickListener(this);
        btn_CalSub.setOnClickListener(this);
        btn_CalAdd.setOnTouchListener(this);
        btn_CalSub.setOnTouchListener(this);
        Calories_Start_Run.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ca1:
                setTextViewAction(tv_ca1, tv_ca2, tv_ca3, tv_ca4);
                isSelector = 1;
                break;
            case R.id.tv_ca2:
                setTextViewAction(tv_ca2, tv_ca1, tv_ca3, tv_ca4);
                isSelector = 2;
                break;
            case R.id.tv_ca3:
                setTextViewAction(tv_ca3, tv_ca1, tv_ca2, tv_ca4);
                isSelector = 3;
                break;
            case R.id.tv_ca4:
                setTextViewAction(tv_ca4, tv_ca1, tv_ca2, tv_ca3);
                isSelector = 4;
                break;
            case R.id.btn_CalAdd:
                if (CalFomat(mode_calories_info.getCa1(), mode_calories_info.getCa2(),
                        mode_calories_info.getCa3(), mode_calories_info.getCa4()) < 9990) {
                    if (isSelector == 1) {
                        if (mode_calories_info.getCa1() != 9) {
                            mode_calories_info.setCa1(mode_calories_info.getCa1() + 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                        } else if (mode_calories_info.getCa1() == 9) {
                            mode_calories_info.setCa1(9);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                            mode_calories_info.setCa2(9);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa3(9);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa4(0);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                        }
                    }
                    if (isSelector == 2) {
                        if (mode_calories_info.getCa2() != 9) {
                            mode_calories_info.setCa2(mode_calories_info.getCa2() + 1);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                        } else if (mode_calories_info.getCa2() == 9 && mode_calories_info.getCa1() != 9) {
                            mode_calories_info.setCa2(0);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa1(mode_calories_info.getCa1() + 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                        } else if (mode_calories_info.getCa1() == 9 && mode_calories_info.getCa2() == 9) {
                            mode_calories_info.setCa1(9);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                            mode_calories_info.setCa2(9);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa3(9);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa4(0);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                        }
                    }
                    if (isSelector == 3) {
                        if (mode_calories_info.getCa3() != 9) {
                            mode_calories_info.setCa3(mode_calories_info.getCa3() + 1);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                        } else if (mode_calories_info.getCa3() == 9 && mode_calories_info.getCa2() != 9) {
                            mode_calories_info.setCa3(0);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(mode_calories_info.getCa2() + 1);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                        } else if (mode_calories_info.getCa3() == 9 && mode_calories_info.getCa2() == 9 && mode_calories_info.getCa1() != 9) {
                            mode_calories_info.setCa3(0);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(0);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa1(mode_calories_info.getCa1() + 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1() + 1));
                        }
                    }
                    if (isSelector == 4) {
                        if (mode_calories_info.getCa4() != 9) {
                            mode_calories_info.setCa4(mode_calories_info.getCa4() + 1);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                        } else if (mode_calories_info.getCa4() == 9 && mode_calories_info.getCa3() != 9) {
                            mode_calories_info.setCa4(0);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                            mode_calories_info.setCa3(mode_calories_info.getCa3() + 1);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                        } else if (mode_calories_info.getCa4() == 9 && mode_calories_info.getCa3() == 9 && mode_calories_info.getCa2() != 9) {
                            mode_calories_info.setCa4(0);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                            mode_calories_info.setCa3(0);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(mode_calories_info.getCa2() + 1);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                        } else if (mode_calories_info.getCa4() == 9 && mode_calories_info.getCa3() == 9 && mode_calories_info.getCa2() == 9 && mode_calories_info.getCa1() != 9) {
                            mode_calories_info.setCa4(0);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                            mode_calories_info.setCa3(0);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(0);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa1(mode_calories_info.getCa1() + 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                        }
                    }
                }
                if (CalFomat(mode_calories_info.getCa1(), mode_calories_info.getCa2(),
                        mode_calories_info.getCa3(), mode_calories_info.getCa4()) > 9990) {
                    mode_calories_info.setCa1(9);
                    tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                    mode_calories_info.setCa2(9);
                    tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                    mode_calories_info.setCa3(9);
                    tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                    mode_calories_info.setCa4(0);
                    tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                }
                break;
            case R.id.btn_CalSub:
                if (CalFomat(mode_calories_info.getCa1(), mode_calories_info.getCa2(),
                        mode_calories_info.getCa3(), mode_calories_info.getCa4()) > 50) {
                    if (isSelector == 1) {
                        if (mode_calories_info.getCa1() != 0) {
                            mode_calories_info.setCa1(mode_calories_info.getCa1() - 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                        } else if (mode_calories_info.getCa1() == 0) {
                            mode_calories_info.setCa1(0);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                            mode_calories_info.setCa2(0);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa3(5);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa4(0);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                        }
                    }
                    if (isSelector == 2) {
                        if (mode_calories_info.getCa2() != 0) {
                            mode_calories_info.setCa2(mode_calories_info.getCa2() - 1);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                        } else if (mode_calories_info.getCa2() == 0 && mode_calories_info.getCa1() != 0) {
                            mode_calories_info.setCa2(9);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa1(mode_calories_info.getCa1() - 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                        } else if (mode_calories_info.getCa1() == 0 && mode_calories_info.getCa2() == 0) {
                            mode_calories_info.setCa1(0);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                            mode_calories_info.setCa2(0);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa3(5);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa4(0);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                        }
                    }
                    if (isSelector == 3) {
                        if (mode_calories_info.getCa3() != 0) {
                            mode_calories_info.setCa3(mode_calories_info.getCa3() - 1);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                        } else if (mode_calories_info.getCa3() == 0 && mode_calories_info.getCa2() != 0) {
                            mode_calories_info.setCa3(9);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(mode_calories_info.getCa2() - 1);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                        } else if (mode_calories_info.getCa3() == 0 && mode_calories_info.getCa2() == 0 && mode_calories_info.getCa1() != 0) {
                            mode_calories_info.setCa3(9);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(9);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa1(mode_calories_info.getCa1() - 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                        }
                    }
                    if (isSelector == 4) {
                        if (mode_calories_info.getCa4() != 0) {
                            mode_calories_info.setCa4(mode_calories_info.getCa4() - 1);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                        } else if (mode_calories_info.getCa4() == 0 && mode_calories_info.getCa3() != 0) {
                            mode_calories_info.setCa4(9);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                            mode_calories_info.setCa3(mode_calories_info.getCa3() - 1);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                        } else if (mode_calories_info.getCa4() == 0 && mode_calories_info.getCa3() == 0 && mode_calories_info.getCa2() != 0) {
                            mode_calories_info.setCa4(9);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                            mode_calories_info.setCa3(9);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(mode_calories_info.getCa2() - 1);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                        } else if (mode_calories_info.getCa4() == 0 && mode_calories_info.getCa3() == 0 && mode_calories_info.getCa2() == 0 && mode_calories_info.getCa1() != 0) {
                            mode_calories_info.setCa4(9);
                            tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                            mode_calories_info.setCa3(9);
                            tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                            mode_calories_info.setCa2(9);
                            tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                            mode_calories_info.setCa1(mode_calories_info.getCa1() - 1);
                            tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                        }
                    }
                }
                if (CalFomat(mode_calories_info.getCa1(), mode_calories_info.getCa2(),
                        mode_calories_info.getCa3(), mode_calories_info.getCa4()) < 50) {
                    mode_calories_info.setCa1(0);
                    tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
                    mode_calories_info.setCa2(0);
                    tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
                    mode_calories_info.setCa3(5);
                    tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
                    mode_calories_info.setCa4(0);
                    tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
                }
                break;
            case R.id.Calories_Start_Run:
                Start_Treadmill.AnimDialog(getContext(), Fragment_Mode_Calories.this, 3, 16,
                        getContext().getString(R.string.main_mode_calories), SportData.RUNNING_MODE_CALORIES, 1,
                        "0", "0", mode_calories_info.getOverCalories());
                break;
        }
    }

    private void defult() {//设置默认值
        setTextViewAction(tv_ca3, tv_ca1, tv_ca2, tv_ca4);
        isSelector = 3;
        tv_ca3.setChecked(true);
        mode_calories_info.setCa1(0);
        mode_calories_info.setCa2(0);
        mode_calories_info.setCa3(5);
        mode_calories_info.setCa4(0);
        tv_ca1.setText(String.valueOf(mode_calories_info.getCa1()));
        tv_ca2.setText(String.valueOf(mode_calories_info.getCa2()));
        tv_ca3.setText(String.valueOf(mode_calories_info.getCa3()));
        tv_ca4.setText(String.valueOf(mode_calories_info.getCa4()));
    }


    private int CalFomat(int cal1, int cal2, int cal3, int cal4) {//格式化卡路里
        String getHrc = String.valueOf(cal1) + String.valueOf(cal2) + String.valueOf(cal3) + String.valueOf(cal4);
        int hrc = Integer.parseInt(getHrc);
        return hrc;
    }

    private void setTextViewAction(CheckedTextView v1, CheckedTextView v2,
                                   CheckedTextView v3, CheckedTextView v4) {
        v1.setChecked(true);
        v1.setBackgroundResource(R.color.colorRed2);

        v2.setChecked(false);
        v2.setBackgroundResource(R.color.colorBlack);

        v3.setChecked(false);
        v3.setBackgroundResource(R.color.colorBlack);

        v4.setChecked(false);
        v4.setBackgroundResource(R.color.colorBlack);
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_CalAdd:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_CalAdd.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_CalAdd.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
            case R.id.btn_CalSub:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_CalSub.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_CalSub.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
        }
        return false;
    }

}
