package com.vigorchip.omatreadmill.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.setUserData;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.utils.TimeUtils;
import com.vigorchip.omatreadmill.view.GradientView;
import com.vigorchip.omatreadmill.view.SpeedView;

import org.greenrobot.eventbus.EventBus;

import static com.vigorchip.omatreadmill.R.id.Gradientview;

/**
 * Created by wr-app1 on 2018/4/23.
 */

public class Fragment_Train_User extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout btn_U1, btn_U2, btn_U3, btn_U4, btn_U5, btn_U6;
    private TextView user_title_speed, user_title_slope;
    private Button btn_user_Start;
    private int flag_mode = 1;//模式判断的标志位

    public static SpeedView Speedview;
    private GradientView Slopeview;
    private int titleSelector = 1;//1：代表速度  2：代表坡度
    private SharedPreferences sp1;
    private SharedPreferences.Editor editor1;
    private SharedPreferences sp2;
    private SharedPreferences.Editor editor2;
    private SharedPreferences sp3;
    private SharedPreferences.Editor editor3;
    private SharedPreferences sp4;
    private SharedPreferences.Editor editor4;
    private SharedPreferences sp5;
    private SharedPreferences.Editor editor5;
    private SharedPreferences sp6;
    private SharedPreferences.Editor editor6;
    private int[] mSpeed = new int[10];//速度的数组
    private int[] mSlope = new int[10];//坡度的数组

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_user, container, false);
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
        if (!hidden) {
            defult();
            Speedview.setMaxSpeend((int) SportData.MAXSPEED);
            for (int i = 0; i < mSpeed.length; i++) {
                if (mSpeed[i] > ((int) SportData.MAXSPEED)) {
                    mSpeed[i] = ((int) SportData.MAXSPEED);
                }
            }
        } else {
            ServerWindows.tv_RunningTime.setText(String.valueOf("00:00:00"));
        }
    }

    private void init() {
        Speedview = view.findViewById(R.id.Speedview);
        Slopeview = view.findViewById(Gradientview);
        user_title_speed = view.findViewById(R.id.user_title_speed);
        user_title_slope = view.findViewById(R.id.user_title_slope);
        btn_U1 = view.findViewById(R.id.btn_U1);
        btn_U2 = view.findViewById(R.id.btn_U2);
        btn_U3 = view.findViewById(R.id.btn_U3);
        btn_U4 = view.findViewById(R.id.btn_U4);
        btn_U5 = view.findViewById(R.id.btn_U5);
        btn_U6 = view.findViewById(R.id.btn_U6);
        btn_user_Start = view.findViewById(R.id.btn_user_Start);
        user_title_speed.setOnClickListener(this);
        user_title_slope.setOnClickListener(this);
        btn_U1.setOnClickListener(this);
        btn_U2.setOnClickListener(this);
        btn_U3.setOnClickListener(this);
        btn_U4.setOnClickListener(this);
        btn_U5.setOnClickListener(this);
        btn_U6.setOnClickListener(this);
        btn_user_Start.setOnClickListener(this);
        defult();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_title_speed:
                SpeedAndSlope(1, user_title_speed, user_title_slope);
                break;
            case R.id.user_title_slope:
                SpeedAndSlope(2, user_title_slope, user_title_speed);
                break;
            case R.id.btn_U1:
                SaveData(flag_mode);//保存上一个用户自定义数据
                ModeSelector(1, btn_U1, btn_U2, btn_U3, btn_U4, btn_U5, btn_U6);
                LoadData(flag_mode);
                break;
            case R.id.btn_U2:
                SaveData(flag_mode);//保存上一个用户自定义数据
                ModeSelector(2, btn_U2, btn_U1, btn_U3, btn_U4, btn_U5, btn_U6);
                LoadData(flag_mode);
                break;
            case R.id.btn_U3:
                SaveData(flag_mode);//保存上一个用户自定义数据
                ModeSelector(3, btn_U3, btn_U2, btn_U1, btn_U4, btn_U5, btn_U6);
                LoadData(flag_mode);
                break;
            case R.id.btn_U4:
                SaveData(flag_mode);//保存上一个用户自定义数据
                ModeSelector(4, btn_U4, btn_U3, btn_U2, btn_U1, btn_U5, btn_U6);
                LoadData(flag_mode);
                break;
            case R.id.btn_U5:
                SaveData(flag_mode);//保存上一个用户自定义数据
                ModeSelector(5, btn_U5, btn_U4, btn_U3, btn_U2, btn_U1, btn_U6);
                LoadData(flag_mode);
                break;
            case R.id.btn_U6:
                SaveData(flag_mode);//保存上一个用户自定义数据
                ModeSelector(6, btn_U6, btn_U5, btn_U4, btn_U3, btn_U2, btn_U1);
                LoadData(flag_mode);
                break;
            case R.id.btn_user_Start:
                SaveData(flag_mode);//保存用户自定义数据
                EventBus.getDefault().postSticky(new setUserData(mSpeed,mSlope));//发送速度 坡度到选择时间页面
                ((MainActivity) getActivity()).showFragment(18,24);
                break;
        }
    }


    private void SaveData(int flag) {//保存当前自定义模式的速度、坡度到数据库
        switch (flag) {
            case 1:
                saveU1();
                break;
            case 2:
                saveU2();
                break;
            case 3:
                saveU3();
                break;
            case 4:
                saveU4();
                break;
            case 5:
                saveU5();
                break;
            case 6:
                saveU6();
                break;
        }
    }

    private void LoadData(int flag) {//读取当前自定义模式的速度、坡度到数据库
        switch (flag) {
            case 1:
                loadU1();
                break;
            case 2:
                loadU2();
                break;
            case 3:
                loadU3();
                break;
            case 4:
                loadU4();
                break;
            case 5:
                loadU5();
                break;
            case 6:
                loadU6();
                break;
        }
    }

    private void defult() {//进来页面的默认设置
        SpeedAndSlope(1, user_title_speed, user_title_slope);
        ModeSelector(1, btn_U1, btn_U2, btn_U3, btn_U4, btn_U5, btn_U6);
        sp1 = getContext().getSharedPreferences("U1", Context.MODE_PRIVATE);
        editor1 = sp1.edit();
        sp2 = getContext().getSharedPreferences("U2", Context.MODE_PRIVATE);
        editor2 = sp2.edit();
        sp3 = getContext().getSharedPreferences("U3", Context.MODE_PRIVATE);
        editor3 = sp3.edit();
        sp4 = getContext().getSharedPreferences("U4", Context.MODE_PRIVATE);
        editor4 = sp4.edit();
        sp5 = getContext().getSharedPreferences("U5", Context.MODE_PRIVATE);
        editor5 = sp5.edit();
        sp6 = getContext().getSharedPreferences("U6", Context.MODE_PRIVATE);
        editor6 = sp6.edit();
        LoadData(flag_mode);
        Speedview.setMaxSpeend((int) SportData.MAXSPEED);
        for (int i = 0; i < mSpeed.length; i++) {
            if (mSpeed[i] > ((int) SportData.MAXSPEED)) {
                mSpeed[i] = ((int) SportData.MAXSPEED);
            }
        }
        ServerWindows.tv_RunningTime.setText(TimeUtils.TimeFomat(TimeUtils.TimeManager(String.valueOf("0030"))));
    }

    private void saveU1() {
        if (Speedview.getdata() != null) {
            editor1.putString("Uspeed_0", String.valueOf(Speedview.getdata()[0]));
            editor1.putString("Uspeed_1", String.valueOf(Speedview.getdata()[1]));
            editor1.putString("Uspeed_2", String.valueOf(Speedview.getdata()[2]));
            editor1.putString("Uspeed_3", String.valueOf(Speedview.getdata()[3]));
            editor1.putString("Uspeed_4", String.valueOf(Speedview.getdata()[4]));
            editor1.putString("Uspeed_5", String.valueOf(Speedview.getdata()[5]));
            editor1.putString("Uspeed_6", String.valueOf(Speedview.getdata()[6]));
            editor1.putString("Uspeed_7", String.valueOf(Speedview.getdata()[7]));
            editor1.putString("Uspeed_8", String.valueOf(Speedview.getdata()[8]));
            editor1.putString("Uspeed_9", String.valueOf(Speedview.getdata()[9]));
        }

        if (Slopeview.getdata() != null) {
            editor1.putString("Uslope_0", String.valueOf(Slopeview.getdata()[0]));
            editor1.putString("Uslope_1", String.valueOf(Slopeview.getdata()[1]));
            editor1.putString("Uslope_2", String.valueOf(Slopeview.getdata()[2]));
            editor1.putString("Uslope_3", String.valueOf(Slopeview.getdata()[3]));
            editor1.putString("Uslope_4", String.valueOf(Slopeview.getdata()[4]));
            editor1.putString("Uslope_5", String.valueOf(Slopeview.getdata()[5]));
            editor1.putString("Uslope_6", String.valueOf(Slopeview.getdata()[6]));
            editor1.putString("Uslope_7", String.valueOf(Slopeview.getdata()[7]));
            editor1.putString("Uslope_8", String.valueOf(Slopeview.getdata()[8]));
            editor1.putString("Uslope_9", String.valueOf(Slopeview.getdata()[9]));
        }
        editor1.commit();
    }

    private void loadU1() {
        for (int i = 0; i < 10; i++) {
            if (!sp1.getString("Uspeed_" + i, "").equals("") &&
                    sp1.getString("Uspeed_" + i, "") != null) {
                mSpeed[i] = Integer.parseInt(sp1.getString("Uspeed_" + i, ""));
            } else {
                mSpeed = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (!sp1.getString("Uslope_" + i, "").equals("") &&
                    sp1.getString("Uslope_" + i, "") != null) {
                mSlope[i] = Integer.parseInt(sp1.getString("Uslope_" + i, ""));
            } else {
                mSlope = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                break;
            }
        }
        if (mSpeed.length != 0 && mSpeed.length == 10) {
            Speedview.setDaata(mSpeed);
        }
        if (mSlope.length != 0 && mSlope.length == 10) {
            Slopeview.setDaata(mSlope);
        }
    }

    private void saveU2() {
        if (Speedview.getdata() != null) {
            editor2.putString("Uspeed_0", String.valueOf(Speedview.getdata()[0]));
            editor2.putString("Uspeed_1", String.valueOf(Speedview.getdata()[1]));
            editor2.putString("Uspeed_2", String.valueOf(Speedview.getdata()[2]));
            editor2.putString("Uspeed_3", String.valueOf(Speedview.getdata()[3]));
            editor2.putString("Uspeed_4", String.valueOf(Speedview.getdata()[4]));
            editor2.putString("Uspeed_5", String.valueOf(Speedview.getdata()[5]));
            editor2.putString("Uspeed_6", String.valueOf(Speedview.getdata()[6]));
            editor2.putString("Uspeed_7", String.valueOf(Speedview.getdata()[7]));
            editor2.putString("Uspeed_8", String.valueOf(Speedview.getdata()[8]));
            editor2.putString("Uspeed_9", String.valueOf(Speedview.getdata()[9]));
        }

        if (Slopeview.getdata() != null) {
            editor2.putString("Uslope_0", String.valueOf(Slopeview.getdata()[0]));
            editor2.putString("Uslope_1", String.valueOf(Slopeview.getdata()[1]));
            editor2.putString("Uslope_2", String.valueOf(Slopeview.getdata()[2]));
            editor2.putString("Uslope_3", String.valueOf(Slopeview.getdata()[3]));
            editor2.putString("Uslope_4", String.valueOf(Slopeview.getdata()[4]));
            editor2.putString("Uslope_5", String.valueOf(Slopeview.getdata()[5]));
            editor2.putString("Uslope_6", String.valueOf(Slopeview.getdata()[6]));
            editor2.putString("Uslope_7", String.valueOf(Slopeview.getdata()[7]));
            editor2.putString("Uslope_8", String.valueOf(Slopeview.getdata()[8]));
            editor2.putString("Uslope_9", String.valueOf(Slopeview.getdata()[9]));
        }
        editor2.commit();
    }

    private void loadU2() {
        for (int i = 0; i < 10; i++) {
            if (!sp2.getString("Uspeed_" + i, "").equals("") &&
                    sp2.getString("Uspeed_" + i, "") != null) {
                mSpeed[i] = Integer.parseInt(sp2.getString("Uspeed_" + i, ""));
            } else {
                mSpeed = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (!sp2.getString("Uslope_" + i, "").equals("") &&
                    sp2.getString("Uslope_" + i, "") != null) {
                mSlope[i] = Integer.parseInt(sp2.getString("Uslope_" + i, ""));
            } else {
                mSlope = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                break;
            }
        }
        if (mSpeed.length != 0 && mSpeed.length == 10) {
            Speedview.setDaata(mSpeed);
        }
        if (mSlope.length != 0 && mSlope.length == 10) {
            Slopeview.setDaata(mSlope);
        }
    }

    private void saveU3() {
        if (Speedview.getdata() != null) {
            editor3.putString("Uspeed_0", String.valueOf(Speedview.getdata()[0]));
            editor3.putString("Uspeed_1", String.valueOf(Speedview.getdata()[1]));
            editor3.putString("Uspeed_2", String.valueOf(Speedview.getdata()[2]));
            editor3.putString("Uspeed_3", String.valueOf(Speedview.getdata()[3]));
            editor3.putString("Uspeed_4", String.valueOf(Speedview.getdata()[4]));
            editor3.putString("Uspeed_5", String.valueOf(Speedview.getdata()[5]));
            editor3.putString("Uspeed_6", String.valueOf(Speedview.getdata()[6]));
            editor3.putString("Uspeed_7", String.valueOf(Speedview.getdata()[7]));
            editor3.putString("Uspeed_8", String.valueOf(Speedview.getdata()[8]));
            editor3.putString("Uspeed_9", String.valueOf(Speedview.getdata()[9]));
        }

        if (Slopeview.getdata() != null) {
            editor3.putString("Uslope_0", String.valueOf(Slopeview.getdata()[0]));
            editor3.putString("Uslope_1", String.valueOf(Slopeview.getdata()[1]));
            editor3.putString("Uslope_2", String.valueOf(Slopeview.getdata()[2]));
            editor3.putString("Uslope_3", String.valueOf(Slopeview.getdata()[3]));
            editor3.putString("Uslope_4", String.valueOf(Slopeview.getdata()[4]));
            editor3.putString("Uslope_5", String.valueOf(Slopeview.getdata()[5]));
            editor3.putString("Uslope_6", String.valueOf(Slopeview.getdata()[6]));
            editor3.putString("Uslope_7", String.valueOf(Slopeview.getdata()[7]));
            editor3.putString("Uslope_8", String.valueOf(Slopeview.getdata()[8]));
            editor3.putString("Uslope_9", String.valueOf(Slopeview.getdata()[9]));
        }
        editor3.commit();
    }

    private void loadU3() {
        for (int i = 0; i < 10; i++) {
            if (!sp3.getString("Uspeed_" + i, "").equals("") &&
                    sp3.getString("Uspeed_" + i, "") != null) {
                mSpeed[i] = Integer.parseInt(sp3.getString("Uspeed_" + i, ""));
            } else {
                mSpeed = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (!sp3.getString("Uslope_" + i, "").equals("") &&
                    sp3.getString("Uslope_" + i, "") != null) {
                mSlope[i] = Integer.parseInt(sp3.getString("Uslope_" + i, ""));
            } else {
                mSlope = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                break;
            }
        }
        if (mSpeed.length != 0 && mSpeed.length == 10) {
            Speedview.setDaata(mSpeed);
        }
        if (mSlope.length != 0 && mSlope.length == 10) {
            Slopeview.setDaata(mSlope);
        }
    }

    private void saveU4() {
        if (Speedview.getdata() != null) {
            editor4.putString("Uspeed_0", String.valueOf(Speedview.getdata()[0]));
            editor4.putString("Uspeed_1", String.valueOf(Speedview.getdata()[1]));
            editor4.putString("Uspeed_2", String.valueOf(Speedview.getdata()[2]));
            editor4.putString("Uspeed_3", String.valueOf(Speedview.getdata()[3]));
            editor4.putString("Uspeed_4", String.valueOf(Speedview.getdata()[4]));
            editor4.putString("Uspeed_5", String.valueOf(Speedview.getdata()[5]));
            editor4.putString("Uspeed_6", String.valueOf(Speedview.getdata()[6]));
            editor4.putString("Uspeed_7", String.valueOf(Speedview.getdata()[7]));
            editor4.putString("Uspeed_8", String.valueOf(Speedview.getdata()[8]));
            editor4.putString("Uspeed_9", String.valueOf(Speedview.getdata()[9]));
        }

        if (Slopeview.getdata() != null) {
            editor4.putString("Uslope_0", String.valueOf(Slopeview.getdata()[0]));
            editor4.putString("Uslope_1", String.valueOf(Slopeview.getdata()[1]));
            editor4.putString("Uslope_2", String.valueOf(Slopeview.getdata()[2]));
            editor4.putString("Uslope_3", String.valueOf(Slopeview.getdata()[3]));
            editor4.putString("Uslope_4", String.valueOf(Slopeview.getdata()[4]));
            editor4.putString("Uslope_5", String.valueOf(Slopeview.getdata()[5]));
            editor4.putString("Uslope_6", String.valueOf(Slopeview.getdata()[6]));
            editor4.putString("Uslope_7", String.valueOf(Slopeview.getdata()[7]));
            editor4.putString("Uslope_8", String.valueOf(Slopeview.getdata()[8]));
            editor4.putString("Uslope_9", String.valueOf(Slopeview.getdata()[9]));
        }
        editor4.commit();
    }

    private void loadU4() {
        for (int i = 0; i < 10; i++) {
            if (!sp4.getString("Uspeed_" + i, "").equals("") &&
                    sp4.getString("Uspeed_" + i, "") != null) {
                mSpeed[i] = Integer.parseInt(sp4.getString("Uspeed_" + i, ""));
            } else {
                mSpeed = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (!sp4.getString("Uslope_" + i, "").equals("") &&
                    sp4.getString("Uslope_" + i, "") != null) {
                mSlope[i] = Integer.parseInt(sp4.getString("Uslope_" + i, ""));
            } else {
                mSlope = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                break;
            }
        }
        if (mSpeed.length != 0 && mSpeed.length == 10) {
            Speedview.setDaata(mSpeed);
        }
        if (mSlope.length != 0 && mSlope.length == 10) {
            Slopeview.setDaata(mSlope);
        }
    }

    private void saveU5() {
        if (Speedview.getdata() != null) {
            editor5.putString("Uspeed_0", String.valueOf(Speedview.getdata()[0]));
            editor5.putString("Uspeed_1", String.valueOf(Speedview.getdata()[1]));
            editor5.putString("Uspeed_2", String.valueOf(Speedview.getdata()[2]));
            editor5.putString("Uspeed_3", String.valueOf(Speedview.getdata()[3]));
            editor5.putString("Uspeed_4", String.valueOf(Speedview.getdata()[4]));
            editor5.putString("Uspeed_5", String.valueOf(Speedview.getdata()[5]));
            editor5.putString("Uspeed_6", String.valueOf(Speedview.getdata()[6]));
            editor5.putString("Uspeed_7", String.valueOf(Speedview.getdata()[7]));
            editor5.putString("Uspeed_8", String.valueOf(Speedview.getdata()[8]));
            editor5.putString("Uspeed_9", String.valueOf(Speedview.getdata()[9]));
        }

        if (Slopeview.getdata() != null) {
            editor5.putString("Uslope_0", String.valueOf(Slopeview.getdata()[0]));
            editor5.putString("Uslope_1", String.valueOf(Slopeview.getdata()[1]));
            editor5.putString("Uslope_2", String.valueOf(Slopeview.getdata()[2]));
            editor5.putString("Uslope_3", String.valueOf(Slopeview.getdata()[3]));
            editor5.putString("Uslope_4", String.valueOf(Slopeview.getdata()[4]));
            editor5.putString("Uslope_5", String.valueOf(Slopeview.getdata()[5]));
            editor5.putString("Uslope_6", String.valueOf(Slopeview.getdata()[6]));
            editor5.putString("Uslope_7", String.valueOf(Slopeview.getdata()[7]));
            editor5.putString("Uslope_8", String.valueOf(Slopeview.getdata()[8]));
            editor5.putString("Uslope_9", String.valueOf(Slopeview.getdata()[9]));
        }
        editor5.commit();
    }

    private void loadU5() {
        for (int i = 0; i < 10; i++) {
            if (!sp5.getString("Uspeed_" + i, "").equals("") &&
                    sp5.getString("Uspeed_" + i, "") != null) {
                mSpeed[i] = Integer.parseInt(sp5.getString("Uspeed_" + i, ""));
            } else {
                mSpeed = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (!sp5.getString("Uslope_" + i, "").equals("") &&
                    sp5.getString("Uslope_" + i, "") != null) {
                mSlope[i] = Integer.parseInt(sp5.getString("Uslope_" + i, ""));
            } else {
                mSlope = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                break;
            }
        }
        if (mSpeed.length != 0 && mSpeed.length == 10) {
            Speedview.setDaata(mSpeed);
        }
        if (mSlope.length != 0 && mSlope.length == 10) {
            Slopeview.setDaata(mSlope);
        }
    }

    private void saveU6() {
        if (Speedview.getdata() != null) {
            editor6.putString("Uspeed_0", String.valueOf(Speedview.getdata()[0]));
            editor6.putString("Uspeed_1", String.valueOf(Speedview.getdata()[1]));
            editor6.putString("Uspeed_2", String.valueOf(Speedview.getdata()[2]));
            editor6.putString("Uspeed_3", String.valueOf(Speedview.getdata()[3]));
            editor6.putString("Uspeed_4", String.valueOf(Speedview.getdata()[4]));
            editor6.putString("Uspeed_5", String.valueOf(Speedview.getdata()[5]));
            editor6.putString("Uspeed_6", String.valueOf(Speedview.getdata()[6]));
            editor6.putString("Uspeed_7", String.valueOf(Speedview.getdata()[7]));
            editor6.putString("Uspeed_8", String.valueOf(Speedview.getdata()[8]));
            editor6.putString("Uspeed_9", String.valueOf(Speedview.getdata()[9]));
        }

        if (Slopeview.getdata() != null) {
            editor6.putString("Uslope_0", String.valueOf(Slopeview.getdata()[0]));
            editor6.putString("Uslope_1", String.valueOf(Slopeview.getdata()[1]));
            editor6.putString("Uslope_2", String.valueOf(Slopeview.getdata()[2]));
            editor6.putString("Uslope_3", String.valueOf(Slopeview.getdata()[3]));
            editor6.putString("Uslope_4", String.valueOf(Slopeview.getdata()[4]));
            editor6.putString("Uslope_5", String.valueOf(Slopeview.getdata()[5]));
            editor6.putString("Uslope_6", String.valueOf(Slopeview.getdata()[6]));
            editor6.putString("Uslope_7", String.valueOf(Slopeview.getdata()[7]));
            editor6.putString("Uslope_8", String.valueOf(Slopeview.getdata()[8]));
            editor6.putString("Uslope_9", String.valueOf(Slopeview.getdata()[9]));
        }
        editor6.commit();
    }

    private void loadU6() {
        for (int i = 0; i < 10; i++) {
            if (!sp6.getString("Uspeed_" + i, "").equals("") &&
                    sp6.getString("Uspeed_" + i, "") != null) {
                mSpeed[i] = Integer.parseInt(sp6.getString("Uspeed_" + i, ""));
            } else {
                mSpeed = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (!sp6.getString("Uslope_" + i, "").equals("") &&
                    sp6.getString("Uslope_" + i, "") != null) {
                mSlope[i] = Integer.parseInt(sp6.getString("Uslope_" + i, ""));
            } else {
                mSlope = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                break;
            }
        }
        if (mSpeed.length != 0 && mSpeed.length == 10) {
            Speedview.setDaata(mSpeed);
        }
        if (mSlope.length != 0 && mSlope.length == 10) {
            Slopeview.setDaata(mSlope);
        }
    }

    private void ModeSelector(int flag, View view1, View view2, View view3,
                              View view4, View view5, View view6) {
        flag_mode = flag;
        view1.setBackgroundResource(R.color.colorBlack);
        view2.setBackgroundResource(R.color.colorBlack3);
        view3.setBackgroundResource(R.color.colorBlack3);
        view4.setBackgroundResource(R.color.colorBlack3);
        view5.setBackgroundResource(R.color.colorBlack3);
        view6.setBackgroundResource(R.color.colorBlack3);
    }

    private void SpeedAndSlope(int flag, View view1, View view2) {
        titleSelector = flag;
        if (titleSelector == 1) {
            Speedview.setVisibility(View.VISIBLE);
            Slopeview.setVisibility(View.GONE);
        } else if (titleSelector == 2) {
            Slopeview.setVisibility(View.VISIBLE);
            Speedview.setVisibility(View.GONE);
        }
        view1.setBackgroundResource(R.color.colorBlack);
        view2.setBackgroundResource(R.color.colorBlack3);
    }

}
