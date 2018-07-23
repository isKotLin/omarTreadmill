package com.vigorchip.omatreadmill.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.Mode_Distance_Info;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.start_treadmill_anim.Start_Treadmill;

/**
 * Created by wr-app1 on 2018/3/28.
 */

public class Fragment_Mode_Distance extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private Mode_Distance_Info mode_distance_info;
    private CheckedTextView tv_KM, tv_HM, tv_M;
    private RelativeLayout btn_Add, btn_Sub;
    private Button Distance_Start_Run;
    private int isSelector = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mode_distance, container, false);
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
            defult();
        }
    }

    private void init() {
        mode_distance_info = new Mode_Distance_Info();
        tv_KM = view.findViewById(R.id.tv_KM);
        tv_HM = view.findViewById(R.id.tv_HM);
        tv_M = view.findViewById(R.id.tv_M);
        btn_Add = view.findViewById(R.id.btn_Add);
        btn_Sub = view.findViewById(R.id.btn_Sub);
        Distance_Start_Run = view.findViewById(R.id.Distance_Start_Run);
        tv_KM.setOnClickListener(this);
        tv_HM.setOnClickListener(this);
        tv_M.setOnClickListener(this);
        btn_Add.setOnClickListener(this);
        btn_Sub.setOnClickListener(this);
        btn_Add.setOnTouchListener(this);
        btn_Sub.setOnTouchListener(this);
        Distance_Start_Run.setOnClickListener(this);
        defult();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_KM:
                setTextViewAction(tv_KM, tv_HM, tv_M);
                isSelector = 1;
                break;
            case R.id.tv_HM:
                setTextViewAction(tv_HM, tv_KM, tv_M);
                isSelector = 2;
                break;
            case R.id.tv_M:
                setTextViewAction(tv_M, tv_KM, tv_HM);
                isSelector = 3;
                break;
            case R.id.btn_Add:
                if (DistanceFomat(mode_distance_info.getKM(), mode_distance_info.getHM(),
                        mode_distance_info.getM()) < 990) {
                    if (isSelector == 1) {
                        if (mode_distance_info.getKM() < 9) {
                            mode_distance_info.setKM(mode_distance_info.getKM() + 1);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                            Log.i(">>>>>>>>查看公里值1：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getKM() == 9) {
                            mode_distance_info.setKM(9);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                            mode_distance_info.setHM(9);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            mode_distance_info.setM(0);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                        }
                    }
                    if (isSelector == 2) {
                        if (mode_distance_info.getHM() < 9 && mode_distance_info.getKM() == 9) {
                            mode_distance_info.setHM(mode_distance_info.getHM() + 1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            Log.i(">>>>>>>>查看公里值2：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getHM() < 9 && mode_distance_info.getKM() < 9) {
                            mode_distance_info.setHM(mode_distance_info.getHM() + 1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            Log.i(">>>>>>>>查看公里值3：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getHM() == 9 && mode_distance_info.getKM() < 9) {
                            mode_distance_info.setHM(0);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            mode_distance_info.setKM(mode_distance_info.getKM() + 1);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                            Log.i(">>>>>>>>查看公里值4：", mode_distance_info.getOverDistance());
                        }
                    }
                    if (isSelector == 3) {
                        if (mode_distance_info.getM() < 9) {
                            mode_distance_info.setM(mode_distance_info.getM() + 1);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            Log.i(">>>>>>>>查看公里值5：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getM() == 9 && mode_distance_info.getHM() < 9 &&
                                mode_distance_info.getKM() < 9) {
                            mode_distance_info.setM(0);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            mode_distance_info.setHM(mode_distance_info.getHM() + 1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            Log.i(">>>>>>>>查看公里值6：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getHM() == 9 && mode_distance_info.getM() == 9 &&
                                mode_distance_info.getKM() < 9) {
                            mode_distance_info.setM(0);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            mode_distance_info.setHM(0);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            mode_distance_info.setKM(mode_distance_info.getKM() + 1);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                            Log.i(">>>>>>>>查看公里值7：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getHM() < 9 && mode_distance_info.getM() == 9 &&
                                mode_distance_info.getKM() == 9) {
                            mode_distance_info.setM(0);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            mode_distance_info.setHM(mode_distance_info.getHM() + 1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            Log.i(">>>>>>>>查看公里值8：", mode_distance_info.getOverDistance());
                        }
                    }
                }
                if (DistanceFomat(mode_distance_info.getKM(), mode_distance_info.getHM(),
                        mode_distance_info.getM()) > 990) {
                    mode_distance_info.setKM(9);
                    tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                    mode_distance_info.setHM(9);
                    tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                    mode_distance_info.setM(0);
                    tv_M.setText(String.valueOf(mode_distance_info.getM()));
                }
                break;
            case R.id.btn_Sub:
                if (mode_distance_info.getHM() == 1 && mode_distance_info.getKM() == 0
                        && mode_distance_info.getM() == 0) {
                    //距离模式最小值01.0
                    mode_distance_info.setKM(0);
                    tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                    mode_distance_info.setHM(1);
                    tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                    mode_distance_info.setM(0);
                    tv_M.setText(String.valueOf(mode_distance_info.getM()));
                    Log.i(">>>>>>>>查看公里值：", mode_distance_info.getOverDistance());
                } else {
                    if (isSelector == 1) {
                        if (mode_distance_info.getKM() > 0) {
                            mode_distance_info.setKM(mode_distance_info.getKM() - 1);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                            Log.i(">>>>>>>>查看公里值：", mode_distance_info.getOverDistance());
                        }
                        if (mode_distance_info.getHM() == 0 && mode_distance_info.getKM() == 0) {
                            mode_distance_info.setM(0);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            mode_distance_info.setHM(1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            mode_distance_info.setKM(0);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                        }
                    }
                    if (isSelector == 2) {
                        if (mode_distance_info.getHM() > 0) {
                            mode_distance_info.setHM(mode_distance_info.getHM() - 1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            Log.i(">>>>>>>>查看公里值：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getHM() == 0 && mode_distance_info.getKM() > 0) {
                            mode_distance_info.setHM(9);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            mode_distance_info.setKM(mode_distance_info.getKM() - 1);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                            Log.i(">>>>>>>>查看公里值：", mode_distance_info.getOverDistance());
                        }

                        if (mode_distance_info.getHM() == 0 && mode_distance_info.getKM() == 0) {
                            mode_distance_info.setM(0);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            mode_distance_info.setHM(1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            mode_distance_info.setKM(0);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                        }
                    }
                    if (isSelector == 3) {
                        if (mode_distance_info.getM() > 0) {
                            mode_distance_info.setM(mode_distance_info.getM() - 1);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            Log.i(">>>>>>>>查看公里值：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getM() == 0 && mode_distance_info.getHM() > 0) {
                            mode_distance_info.setM(9);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            mode_distance_info.setHM(mode_distance_info.getHM() - 1);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            Log.i(">>>>>>>>查看公里值：", mode_distance_info.getOverDistance());
                        } else if (mode_distance_info.getM() == 0 && mode_distance_info.getHM() == 0
                                && mode_distance_info.getKM() > 0) {
                            mode_distance_info.setM(9);
                            tv_M.setText(String.valueOf(mode_distance_info.getM()));
                            mode_distance_info.setHM(9);
                            tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
                            mode_distance_info.setKM(mode_distance_info.getKM() - 1);
                            tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
                            Log.i(">>>>>>>>查看公里值：", mode_distance_info.getOverDistance());
                        }
                    }
                }
                break;
            case R.id.Distance_Start_Run:
                Start_Treadmill.AnimDialog(getContext(), Fragment_Mode_Distance.this, 2, 16,
                        getContext().getString(R.string.main_mode_distance), SportData.RUNNING_MODE_DISTANCE, 1,
                        "0", mode_distance_info.getOverDistance(), "0");
                break;
        }
    }

    private int DistanceFomat(int dis1, int dis2, int dis3) {//格式化距离
        String getDis = String.valueOf(dis1) + String.valueOf(dis2) + String.valueOf(dis3);
        int dis = Integer.parseInt(getDis);
        return dis;
    }

    private void setTextViewAction(CheckedTextView v1, CheckedTextView v2, CheckedTextView v3) {
        v1.setChecked(true);
        v1.setBackgroundResource(R.color.colorRed2);

        v2.setChecked(false);
        v2.setBackgroundResource(R.color.colorBlack);

        v3.setChecked(false);
        v3.setBackgroundResource(R.color.colorBlack);
    }

    public void defult() {//设置默认值
        setTextViewAction(tv_HM, tv_KM, tv_M);
        isSelector = 2;
        mode_distance_info.setKM(0);
        mode_distance_info.setHM(1);
        mode_distance_info.setM(0);
        tv_HM.setChecked(true);
        tv_KM.setText(String.valueOf(mode_distance_info.getKM()));
        tv_HM.setText(String.valueOf(mode_distance_info.getHM()));
        tv_M.setText(String.valueOf(mode_distance_info.getM()));
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_Add:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_Add.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_Add.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
            case R.id.btn_Sub:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_Sub.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_Sub.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
        }
        return false;
    }
}
