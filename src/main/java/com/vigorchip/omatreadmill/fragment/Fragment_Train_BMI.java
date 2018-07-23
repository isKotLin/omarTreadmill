package com.vigorchip.omatreadmill.fragment;

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
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.bean.Mode_Bmi_Info;
import com.vigorchip.omatreadmill.eventbus.setBMIHeart;
import com.vigorchip.omatreadmill.serialport.newSerial;
import com.vigorchip.omatreadmill.utils.CreatDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

/**
 * Created by wr-app1 on 2018/4/24.
 */

public class Fragment_Train_BMI extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private TextView bmi_toast;
    private Button bt_scaleBMI;
    public static CircularProgressView pg_bmi;
    private ImageView img_showHeart;
    private TextView tv_keep, tv_bmiResult;
    private CheckedTextView tv1_age, tv2_age, tv1_height, tv2_height, tv3_height, tv1_weight, tv2_weight, tv3_weight;
    private ImageView img_male, img_female;
    private RelativeLayout btn_BmiAdd, btn_BmiSub;
    private Mode_Bmi_Info modeBmiInfo;

    private int Checktv_flag = 0;//CheckTextView标志位
    public static int check = 0;//1 未檢測到心率 2 檢測到心率
    private int Sex_flag = 1;//性别标志位：1表示男  2表示女
    public static boolean hide = true;//判断fragment有无显示 true无显示 false有显示

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_bmi, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);//必须在最后初始化eventbus，不然接收不到
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        hide = hidden;
    }

    private void init() {
        hide = false;
        modeBmiInfo = new Mode_Bmi_Info();
        bmi_toast = view.findViewById(R.id.bmi_toast);
        bt_scaleBMI = view.findViewById(R.id.bt_scaleBMI);
        bt_scaleBMI.setOnClickListener(this);
        bmi_toast.setText(getContext().getString(R.string.BmiRange));

        tv1_age = view.findViewById(R.id.tv1_age);
        tv2_age = view.findViewById(R.id.tv2_age);
        tv1_height = view.findViewById(R.id.tv1_height);
        tv2_height = view.findViewById(R.id.tv2_height);
        tv3_height = view.findViewById(R.id.tv3_height);
        tv1_weight = view.findViewById(R.id.tv1_weight);
        tv2_weight = view.findViewById(R.id.tv2_weight);
        tv3_weight = view.findViewById(R.id.tv3_weight);
        img_male = view.findViewById(R.id.img_male);
        img_female = view.findViewById(R.id.img_female);
        btn_BmiAdd = view.findViewById(R.id.btn_BmiAdd);
        btn_BmiSub = view.findViewById(R.id.btn_BmiSub);
        tv1_age.setOnClickListener(this);
        tv2_age.setOnClickListener(this);
        tv1_height.setOnClickListener(this);
        tv2_height.setOnClickListener(this);
        tv3_height.setOnClickListener(this);
        tv1_weight.setOnClickListener(this);
        tv2_weight.setOnClickListener(this);
        tv3_weight.setOnClickListener(this);
        img_male.setOnClickListener(this);
        img_female.setOnClickListener(this);
        btn_BmiAdd.setOnClickListener(this);
        btn_BmiSub.setOnClickListener(this);
        btn_BmiAdd.setOnTouchListener(this);
        btn_BmiSub.setOnTouchListener(this);
        DefultInfo();
    }

    private Handler CheckHRC_Handler = new Handler();//計算心率的綫程
    private int count_flag = 0;//计时3秒后算出bmi
    private boolean flag_hrc = true;//让 已经检测到心率，正在检测BMI值 这个画面只出现一次
    private double height = 0.0;
    private double weight = 0.0;
    private double BMI = 0.0;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count_flag != 30) {
                count_flag = count_flag + 1;
                if (flag_hrc) {
                    flag_hrc = false;
                    pg_bmi.setVisibility(View.GONE);
                    img_showHeart.setVisibility(View.VISIBLE);
                    tv_bmiResult.setVisibility(View.GONE);
                    tv_keep.setVisibility(View.VISIBLE);
                    tv_keep.setText(getContext().getString(R.string.CalculatingBMI));
                    height = HeightFomat(modeBmiInfo.getHeight1(), modeBmiInfo.getHeight2(), modeBmiInfo.getHeight3());
                    height = height / 100;
                    weight = WeightFomat(modeBmiInfo.getWeight1(), modeBmiInfo.getWeight2(), modeBmiInfo.getWeight3());
                    BMI = new BigDecimal(weight / (height * height)).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                }
                CheckHRC_Handler.postDelayed(this, 100);
            }
            if (count_flag == 30) {
                pg_bmi.setVisibility(View.GONE);
                img_showHeart.setVisibility(View.GONE);
                tv_bmiResult.setVisibility(View.VISIBLE);
                tv_keep.setVisibility(View.VISIBLE);
                if (BMI <= 18.0) {//偏瘦
                    tv_bmiResult.setText(String.valueOf(BMI));
                    tv_keep.setText(getContext().getString(R.string.BmiUnderweight));
                    check = 0;
                } else if (BMI > 18.0 && BMI <= 28.0) {//正常
                    tv_bmiResult.setText(String.valueOf(BMI));
                    tv_keep.setText(getContext().getString(R.string.BmiNormal));
                    check = 0;
                } else if (BMI > 28) {//偏胖
                    tv_bmiResult.setText(String.valueOf(BMI));
                    tv_keep.setText(getContext().getString(R.string.BmiRisk));
                    check = 0;
                }
                Log.e("查看dialog", String.valueOf(CreatDialog.dialog != null)
                        + CreatDialog.dialog.isShowing());
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onHeartBMI(setBMIHeart BMIHeart) {
        if (BMIHeart.isBMI()) {
            check = 2;//检测到心率
            if (CreatDialog.dialog == null) {
                CreatDialog.showDialog(getContext(), R.layout.dialog_bmi, true);
                pg_bmi = CreatDialog.dialog.findViewById(R.id.pg_bmi);
                img_showHeart = CreatDialog.dialog.findViewById(R.id.img_showHeart);
                tv_bmiResult = CreatDialog.dialog.findViewById(R.id.tv_bmiResult);
                tv_keep = CreatDialog.dialog.findViewById(R.id.tv_keep);
            }
            CheckHandler();
        }
    }

    private void CheckHandler() {//計算心率的綫程
        CheckHRC_Handler.postDelayed(runnable, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1_age:
                CheckTextSelector(1, tv1_age, tv2_age, tv1_height, tv2_height, tv3_height, tv1_weight, tv2_weight, tv3_weight);
                break;
            case R.id.tv2_age:
                CheckTextSelector(2, tv2_age, tv1_age, tv1_height, tv2_height, tv3_height, tv1_weight, tv2_weight, tv3_weight);
                break;
            case R.id.tv1_height:
                CheckTextSelector(3, tv1_height, tv2_age, tv1_age, tv2_height, tv3_height, tv1_weight, tv2_weight, tv3_weight);
                break;
            case R.id.tv2_height:
                CheckTextSelector(4, tv2_height, tv1_height, tv2_age, tv1_age, tv3_height, tv1_weight, tv2_weight, tv3_weight);
                break;
            case R.id.tv3_height:
                CheckTextSelector(5, tv3_height, tv2_height, tv1_height, tv2_age, tv1_age, tv1_weight, tv2_weight, tv3_weight);
                break;
            case R.id.tv1_weight:
                CheckTextSelector(6, tv1_weight, tv3_height, tv2_height, tv1_height, tv2_age, tv1_age, tv2_weight, tv3_weight);
                break;
            case R.id.tv2_weight:
                CheckTextSelector(7, tv2_weight, tv1_weight, tv3_height, tv2_height, tv1_height, tv2_age, tv1_age, tv3_weight);
                break;
            case R.id.tv3_weight:
                CheckTextSelector(8, tv3_weight, tv2_weight, tv1_weight, tv3_height, tv2_height, tv1_height, tv2_age, tv1_age);
                break;
            case R.id.img_male:
                SexSelector(1, img_male, img_female);
                break;
            case R.id.img_female:
                SexSelector(2, img_female, img_male);
                break;
            case R.id.btn_BmiAdd:
                addAgeJudge();
                addHeightJudge();
                addWeightJudge();
                break;
            case R.id.btn_BmiSub:
                minusAgeJudge();
                minusHeightJudge();
                minusWeightJudge();
                break;
            case R.id.bt_scaleBMI:
                flag_hrc = true;
                count_flag = 0;
                height = 0.0;
                weight = 0.0;
                BMI = 0.0;
                if (newSerial.heart == 0) {
                    check = 1;//未检测到心率
                    CreatDialog.showDialog(getContext(), R.layout.dialog_bmi, true);
                    pg_bmi = CreatDialog.dialog.findViewById(R.id.pg_bmi);
                    img_showHeart = CreatDialog.dialog.findViewById(R.id.img_showHeart);
                    tv_bmiResult = CreatDialog.dialog.findViewById(R.id.tv_bmiResult);
                    tv_keep = CreatDialog.dialog.findViewById(R.id.tv_keep);
                    pg_bmi.setVisibility(View.VISIBLE);
                    pg_bmi.startAnimation();
                    img_showHeart.setVisibility(View.GONE);
                    tv_bmiResult.setVisibility(View.GONE);
                    tv_keep.setVisibility(View.VISIBLE);
                    tv_keep.setText(getContext().getString(R.string.HoldHandrail));
                } else {
                    check = 2;//检测到心率
                    CreatDialog.showDialog(getContext(), R.layout.dialog_bmi, true);
                    pg_bmi = CreatDialog.dialog.findViewById(R.id.pg_bmi);
                    img_showHeart = CreatDialog.dialog.findViewById(R.id.img_showHeart);
                    tv_bmiResult = CreatDialog.dialog.findViewById(R.id.tv_bmiResult);
                    tv_keep = CreatDialog.dialog.findViewById(R.id.tv_keep);
                    CheckHandler();
                }
                break;
        }
    }

    private void addAgeJudge() {
        if (Checktv_flag == 1 || Checktv_flag == 2) {//年龄的判断开始
            if (Checktv_flag == 1) {
                if (modeBmiInfo.getAge1() != 9) {
                    modeBmiInfo.setAge1(modeBmiInfo.getAge1() + 1);
                    tv1_age.setText(String.valueOf(modeBmiInfo.getAge1()));
                }
            } else if (Checktv_flag == 2) {
                if (modeBmiInfo.getAge2() != 9) {
                    modeBmiInfo.setAge2(modeBmiInfo.getAge2() + 1);
                    tv2_age.setText(String.valueOf(modeBmiInfo.getAge2()));
                } else if (modeBmiInfo.getAge2() == 9 && modeBmiInfo.getAge1() != 9) {
                    modeBmiInfo.setAge1(modeBmiInfo.getAge1() + 1);
                    tv1_age.setText(String.valueOf(modeBmiInfo.getAge1()));
                    modeBmiInfo.setAge2(0);
                    tv2_age.setText(String.valueOf(modeBmiInfo.getAge2()));
                }
            }
        }
    }

    private void minusAgeJudge() {
        if (Checktv_flag == 1 || Checktv_flag == 2) {//年龄的判断开始
            if (AgeFomat(modeBmiInfo.getAge1(), modeBmiInfo.getAge2()) > 1) {
                if (Checktv_flag == 1) {
                    if (modeBmiInfo.getAge1() > 0) {
                        modeBmiInfo.setAge1(modeBmiInfo.getAge1() - 1);
                        tv1_age.setText(String.valueOf(modeBmiInfo.getAge1()));
                    } else if (modeBmiInfo.getAge1() == 0) {
                        modeBmiInfo.setAge1(0);
                        tv1_age.setText(String.valueOf(modeBmiInfo.getAge1()));
                        modeBmiInfo.setAge2(1);
                        tv2_age.setText(String.valueOf(modeBmiInfo.getAge2()));
                    }
                    if (modeBmiInfo.getAge1() == 0 && modeBmiInfo.getAge2() < 1) {
                        modeBmiInfo.setAge1(0);
                        tv1_age.setText(String.valueOf(modeBmiInfo.getAge1()));
                        modeBmiInfo.setAge2(1);
                        tv2_age.setText(String.valueOf(modeBmiInfo.getAge2()));
                    }
                } else if (Checktv_flag == 2) {
                    if (modeBmiInfo.getAge2() > 0) {
                        modeBmiInfo.setAge2(modeBmiInfo.getAge2() - 1);
                        tv2_age.setText(String.valueOf(modeBmiInfo.getAge2()));
                    } else if (modeBmiInfo.getAge2() == 0 && modeBmiInfo.getAge1() > 0) {
                        modeBmiInfo.setAge2(9);
                        tv2_age.setText(String.valueOf(modeBmiInfo.getAge2()));
                        modeBmiInfo.setAge1(modeBmiInfo.getAge1() - 1);
                        tv1_age.setText(String.valueOf(modeBmiInfo.getAge1()));
                    }
                }
            }
        }
    }

    private void addHeightJudge() {
        if (HeightFomat(modeBmiInfo.getHeight1(), modeBmiInfo.getHeight2(),
                modeBmiInfo.getHeight3()) < 220) {//身高判断方法开始
            if (Checktv_flag == 3 || Checktv_flag == 4 || Checktv_flag == 5) {
                if (Checktv_flag == 3) {
                    if (modeBmiInfo.getHeight1() != 2) {
                        modeBmiInfo.setHeight1(modeBmiInfo.getHeight1() + 1);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                    } else if (modeBmiInfo.getHeight1() == 2 && modeBmiInfo.getHeight2() < 2) {
                        modeBmiInfo.setHeight1(2);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(2);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(0);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    }
                    if (modeBmiInfo.getHeight1() == 2 && modeBmiInfo.getHeight2() > 2) {
                        modeBmiInfo.setHeight1(2);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(2);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(0);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    }
                } else if (Checktv_flag == 4) {
                    if (modeBmiInfo.getHeight1() == 2 && modeBmiInfo.getHeight2() == 1) {
                        modeBmiInfo.setHeight1(2);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(2);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(0);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    } else if (modeBmiInfo.getHeight2() < 9) {
                        modeBmiInfo.setHeight2(modeBmiInfo.getHeight2() + 1);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                    } else if (modeBmiInfo.getHeight2() == 9 && modeBmiInfo.getHeight1() != 2) {
                        modeBmiInfo.setHeight1(modeBmiInfo.getHeight1() + 1);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(0);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                    }
                } else if (Checktv_flag == 5) {
                    if (modeBmiInfo.getHeight1() == 2 && modeBmiInfo.getHeight2() == 1 &&
                            modeBmiInfo.getHeight3() == 9) {
                        modeBmiInfo.setHeight1(2);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(2);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(0);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    } else if (modeBmiInfo.getHeight3() != 9) {
                        modeBmiInfo.setHeight3(modeBmiInfo.getHeight3() + 1);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    } else if (modeBmiInfo.getHeight3() == 9 && modeBmiInfo.getHeight1() != 2
                            && modeBmiInfo.getHeight2() < 9) {
                        modeBmiInfo.setHeight2(modeBmiInfo.getHeight2() + 1);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(0);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    } else if (modeBmiInfo.getHeight1() == 2 && modeBmiInfo.getHeight2() == 1 &&
                            modeBmiInfo.getHeight3() != 9) {
                        modeBmiInfo.setHeight3(modeBmiInfo.getHeight3() + 1);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    } else if (modeBmiInfo.getHeight1() == 1 && modeBmiInfo.getHeight2() == 9
                            && modeBmiInfo.getHeight3() == 9) {
                        modeBmiInfo.setHeight1(2);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(0);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(0);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    }
                }
            }
        }
    }

    private void minusHeightJudge() {
        if (HeightFomat(modeBmiInfo.getHeight1(), modeBmiInfo.getHeight2(),
                modeBmiInfo.getHeight3()) > 100) {
            if (Checktv_flag == 3 || Checktv_flag == 4 || Checktv_flag == 5) {
                if (Checktv_flag == 3) {
                    if (modeBmiInfo.getHeight1() == 2) {
                        modeBmiInfo.setHeight1(modeBmiInfo.getHeight1() - 1);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                    } else if (modeBmiInfo.getHeight1() == 1) {
                        modeBmiInfo.setHeight1(1);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(0);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(0);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    }
                } else if (Checktv_flag == 4) {
                    if (modeBmiInfo.getHeight2() != 0) {
                        modeBmiInfo.setHeight2(modeBmiInfo.getHeight2() - 1);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                    } else if (modeBmiInfo.getHeight2() == 0 && modeBmiInfo.getHeight1() != 1) {
                        modeBmiInfo.setHeight1(modeBmiInfo.getHeight1() - 1);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(9);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                    }
                } else if (Checktv_flag == 5) {
                    if (modeBmiInfo.getHeight3() != 0) {
                        modeBmiInfo.setHeight3(modeBmiInfo.getHeight3() - 1);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    } else if (modeBmiInfo.getHeight3() == 0 && modeBmiInfo.getHeight2() != 0) {
                        modeBmiInfo.setHeight2(modeBmiInfo.getHeight2() - 1);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(9);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    } else if (modeBmiInfo.getHeight3() == 0 && modeBmiInfo.getHeight2() == 0
                            && modeBmiInfo.getHeight1() == 2) {
                        modeBmiInfo.setHeight1(1);
                        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
                        modeBmiInfo.setHeight2(9);
                        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
                        modeBmiInfo.setHeight3(9);
                        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
                    }
                }
            }
        }
    }

    private void addWeightJudge() {
        if (WeightFomat(modeBmiInfo.getWeight1(), modeBmiInfo.getWeight2(),
                modeBmiInfo.getWeight3()) < 150) {
            if (Checktv_flag == 6) {
                if (modeBmiInfo.getWeight1() == 0 && modeBmiInfo.getWeight2() < 5) {
                    modeBmiInfo.setWeight1(modeBmiInfo.getWeight1() + 1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                } else if (modeBmiInfo.getWeight1() == 0 && modeBmiInfo.getWeight2() >= 5) {
                    modeBmiInfo.setWeight1(1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(5);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight1() == 1) {
                    modeBmiInfo.setWeight1(1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(5);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));

                }
            } else if (Checktv_flag == 7) {
                if (modeBmiInfo.getWeight2() != 9 && modeBmiInfo.getWeight1() != 1) {
                    modeBmiInfo.setWeight2(modeBmiInfo.getWeight2() + 1);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                } else if (modeBmiInfo.getWeight2() == 9 && modeBmiInfo.getWeight1() != 1) {
                    modeBmiInfo.setWeight1(1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(0);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                } else if (modeBmiInfo.getWeight2() == 4 && modeBmiInfo.getWeight1() == 1) {
                    modeBmiInfo.setWeight1(1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(5);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight2() != 5 && modeBmiInfo.getWeight1() == 1) {
                    modeBmiInfo.setWeight2(modeBmiInfo.getWeight2() + 1);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                }
            } else if (Checktv_flag == 8) {
                if (modeBmiInfo.getWeight3() != 9) {
                    modeBmiInfo.setWeight3(modeBmiInfo.getWeight3() + 1);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight3() == 9 && modeBmiInfo.getWeight2() != 9 && modeBmiInfo.getWeight1() != 1) {
                    modeBmiInfo.setWeight2(modeBmiInfo.getWeight2() + 1);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight3() == 9 && modeBmiInfo.getWeight2() == 9 && modeBmiInfo.getWeight1() != 1) {
                    modeBmiInfo.setWeight1(1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(0);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight3() == 9 && modeBmiInfo.getWeight2() != 4 && modeBmiInfo.getWeight1() == 1) {
                    modeBmiInfo.setWeight1(1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(modeBmiInfo.getWeight2() + 1);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight3() == 9 && modeBmiInfo.getWeight2() == 4 && modeBmiInfo.getWeight1() == 1) {
                    modeBmiInfo.setWeight1(1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(5);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                }
            }
        }
    }

    private void minusWeightJudge() {
        if (WeightFomat(modeBmiInfo.getWeight1(), modeBmiInfo.getWeight2(),
                modeBmiInfo.getWeight3()) > 20) {
            if (Checktv_flag == 6) {
                if (modeBmiInfo.getWeight1() == 1) {
                    modeBmiInfo.setWeight1(modeBmiInfo.getWeight1() - 1);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                }
                if (modeBmiInfo.getWeight1() == 0) {
                    modeBmiInfo.setWeight1(0);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(2);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                }
            } else if (Checktv_flag == 7) {
                if (modeBmiInfo.getWeight2() > 0) {
                    modeBmiInfo.setWeight2(modeBmiInfo.getWeight2() - 1);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                } else if (modeBmiInfo.getWeight2() == 0 && modeBmiInfo.getWeight1() == 1) {
                    modeBmiInfo.setWeight1(0);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(9);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                } else if (modeBmiInfo.getWeight1() == 0 && modeBmiInfo.getWeight2() == 3
                        && modeBmiInfo.getWeight3() > 0) {
                    modeBmiInfo.setWeight1(0);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(2);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                }
                if (modeBmiInfo.getWeight1() == 0 && modeBmiInfo.getWeight2() == 1) {
                    modeBmiInfo.setWeight1(0);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(2);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(0);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                }
            } else if (Checktv_flag == 8) {
                if (modeBmiInfo.getWeight3() > 0) {
                    modeBmiInfo.setWeight3(modeBmiInfo.getWeight3() - 1);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight3() == 0 && modeBmiInfo.getWeight2() != 0
                        && modeBmiInfo.getWeight1() > 0) {
                    modeBmiInfo.setWeight2(modeBmiInfo.getWeight2() - 1);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(9);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight3() == 0 && modeBmiInfo.getWeight2() == 0
                        && modeBmiInfo.getWeight1() != 0) {
                    modeBmiInfo.setWeight1(0);
                    tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
                    modeBmiInfo.setWeight2(9);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(9);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                } else if (modeBmiInfo.getWeight3() == 0 && modeBmiInfo.getWeight2() != 0
                        && modeBmiInfo.getWeight1() == 0) {
                    modeBmiInfo.setWeight2(modeBmiInfo.getWeight2() - 1);
                    tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
                    modeBmiInfo.setWeight3(9);
                    tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
                }
            }
        }
    }

    private int AgeFomat(int age1, int age2) {//格式化年龄
        String getAge = String.valueOf(age1) + String.valueOf(age2);
        int age = Integer.parseInt(getAge);
        return age;
    }

    private int HeightFomat(int height1, int height2, int height3) {//格式化身高
        String getHrc = String.valueOf(height1) + String.valueOf(height2) + String.valueOf(height3);
        int hrc = Integer.parseInt(getHrc);
        return hrc;
    }

    private int WeightFomat(int weight1, int weight2, int weight3) {//格式化体重
        String getMin = String.valueOf(weight1) + String.valueOf(weight2) + String.valueOf(weight3);
        int min = Integer.parseInt(getMin);
        return min;
    }


    private void DefultInfo() {//第一次进来设置默认值
        modeBmiInfo.setAge1(2);
        modeBmiInfo.setAge2(5);
        modeBmiInfo.setHeight1(1);
        modeBmiInfo.setHeight2(7);
        modeBmiInfo.setHeight3(0);
        modeBmiInfo.setWeight1(0);
        modeBmiInfo.setWeight2(7);
        modeBmiInfo.setWeight3(0);
        tv1_age.setText(String.valueOf(modeBmiInfo.getAge1()));
        tv2_age.setText(String.valueOf(modeBmiInfo.getAge2()));
        tv1_height.setText(String.valueOf(modeBmiInfo.getHeight1()));
        tv2_height.setText(String.valueOf(modeBmiInfo.getHeight2()));
        tv3_height.setText(String.valueOf(modeBmiInfo.getHeight3()));
        tv1_weight.setText(String.valueOf(modeBmiInfo.getWeight1()));
        tv2_weight.setText(String.valueOf(modeBmiInfo.getWeight2()));
        tv3_weight.setText(String.valueOf(modeBmiInfo.getWeight3()));
        CheckTextSelector(8, tv3_weight, tv2_weight, tv1_weight, tv3_height, tv2_height, tv1_height, tv2_age, tv1_age);
        SexSelector(1, img_male, img_female);

    }

    private void CheckTextSelector(int flag, CheckedTextView v1, CheckedTextView v2, CheckedTextView v3,
                                   CheckedTextView v4, CheckedTextView v5, CheckedTextView v6,
                                   CheckedTextView v7, CheckedTextView v8) {
        Checktv_flag = flag;

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

        v8.setChecked(false);
        v8.setBackgroundResource(R.color.colorBlack);
    }

    private void SexSelector(int flag, ImageView v1, ImageView v2) {
        Sex_flag = flag;
        v1.setBackgroundResource(R.color.colorRed2);
        v2.setBackgroundResource(R.color.colorBlack);
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.btn_BmiAdd:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_BmiAdd.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_BmiAdd.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
            case R.id.btn_BmiSub:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_BmiSub.setBackgroundResource(R.color.colorRed2);
                        break;
                    case MotionEvent.ACTION_UP:
                        btn_BmiSub.setBackgroundResource(R.color.colorBlack2);
                        break;
                }
                break;
        }
        return false;
    }

}
