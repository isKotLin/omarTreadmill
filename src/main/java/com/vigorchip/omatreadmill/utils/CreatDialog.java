package com.vigorchip.omatreadmill.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.fragment.Fragment_Running2;
import com.vigorchip.omatreadmill.interfaci.setPlayerSpeed;
import com.vigorchip.omatreadmill.serialport.newSerial;

import java.io.File;

/**
 * Created by wr-app1 on 2018/3/26.
 */

public class CreatDialog implements View.OnClickListener, View.OnTouchListener {
    public static Dialog dialog;
    public static Dialog RunningDialog;
    public static Dialog ErrorDialog;
    private File spfile;

    public static void showDialog(Context context, int v, boolean can) {
        dialog = new Dialog(context, R.style.DefaultDialog);
        dialog.setCancelable(can);
        dialog.setContentView(v);
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialog.show();
    }

    public static void showRunningDialog(Context context, String mode) {
        RunningDialog = new Dialog(context, R.style.DefaultDialog);
        RunningDialog.setCancelable(true);
        RunningDialog.setContentView(R.layout.dialog_running);
        TextView toast_contant = RunningDialog.findViewById(R.id.toast_contant);
        TextView btn_null = RunningDialog.findViewById(R.id.btn_null);
        RunningDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        RunningDialog.show();
        toast_contant.setText(mode + context.getString(R.string.Isrunning));
        btn_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunningDialog.dismiss();
            }
        });
    }

    public static void ErrorDialog(Context context, String error) {
        ErrorDialog = new Dialog(context, R.style.CommentStyle);
        ErrorDialog.setCancelable(false);
        ErrorDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return false; //默认返回 false
                }
            }
        });
        ErrorDialog.show();
        Window window = ErrorDialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.dialog_errors);
        final TextView tv_error = window.findViewById(R.id.tv_error);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        window.setAttributes(lp);
        tv_error.setText(error);
    }

    private SharedPreferences sp_loadPH;
    private String PH;
    private TextView speedDialog_title;
    private LinearLayout speedDialog;
    private Button bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9, bt_10,
            bt_11, bt_12, bt_13, bt_14, bt_15, bt_16, bt_17, bt_18, bt_19, bt_20,
            bt_21, bt_22, bt_23, bt_24, bt_25;
    public static Dialog AdjSpeedDialog;

    public static void setPlayerSpeed(setPlayerSpeed setPlayerSpeed) {
        PlayerSpeed = setPlayerSpeed;
    }

    Handler speed_handler = new Handler();

    public void AdjSpeedDialog(Context context, boolean can) {
        AdjSpeedDialog = new Dialog(context, R.style.DefaultDialog);
        AdjSpeedDialog.setCancelable(can);
        AdjSpeedDialog.setContentView(R.layout.dialog_speed_adjust);
        speedDialog_title = AdjSpeedDialog.findViewById(R.id.speedDialog_title);
        speedDialog = AdjSpeedDialog.findViewById(R.id.speedDialog);
        bt_1 = AdjSpeedDialog.findViewById(R.id.bt_1);
        bt_2 = AdjSpeedDialog.findViewById(R.id.bt_2);
        bt_3 = AdjSpeedDialog.findViewById(R.id.bt_3);
        bt_4 = AdjSpeedDialog.findViewById(R.id.bt_4);
        bt_5 = AdjSpeedDialog.findViewById(R.id.bt_5);
        bt_6 = AdjSpeedDialog.findViewById(R.id.bt_6);
        bt_7 = AdjSpeedDialog.findViewById(R.id.bt_7);
        bt_8 = AdjSpeedDialog.findViewById(R.id.bt_8);
        bt_9 = AdjSpeedDialog.findViewById(R.id.bt_9);
        bt_10 = AdjSpeedDialog.findViewById(R.id.bt_10);
        bt_11 = AdjSpeedDialog.findViewById(R.id.bt_11);
        bt_12 = AdjSpeedDialog.findViewById(R.id.bt_12);
        bt_13 = AdjSpeedDialog.findViewById(R.id.bt_13);
        bt_14 = AdjSpeedDialog.findViewById(R.id.bt_14);
        bt_15 = AdjSpeedDialog.findViewById(R.id.bt_15);
        bt_16 = AdjSpeedDialog.findViewById(R.id.bt_16);
        bt_17 = AdjSpeedDialog.findViewById(R.id.bt_17);
        bt_18 = AdjSpeedDialog.findViewById(R.id.bt_18);
        bt_19 = AdjSpeedDialog.findViewById(R.id.bt_19);
        bt_20 = AdjSpeedDialog.findViewById(R.id.bt_20);
        bt_21 = AdjSpeedDialog.findViewById(R.id.bt_21);
        bt_22 = AdjSpeedDialog.findViewById(R.id.bt_22);
        bt_23 = AdjSpeedDialog.findViewById(R.id.bt_23);
        bt_24 = AdjSpeedDialog.findViewById(R.id.bt_24);
        bt_25 = AdjSpeedDialog.findViewById(R.id.bt_25);
        if (isApplication.ph_selector == 1) {
            speedDialog_title.setText(context.getString(R.string.server_speed) + "km/h");
        } else if (isApplication.ph_selector == 2) {
            speedDialog_title.setText(context.getString(R.string.server_speed) + "MPH");
        }
        sp_loadPH = context.getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        spfile = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/sp_PH.xml");
        if (spfile.exists()) {
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
                } else if (SportData.MAXSPEED == 25.0) {
                    SportData.MAXSPEED = 15.5;
                }
            }
        }
        ph_speed();
        AdjSpeedDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        AdjSpeedDialog.show();
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        bt_6.setOnClickListener(this);
        bt_7.setOnClickListener(this);
        bt_8.setOnClickListener(this);
        bt_9.setOnClickListener(this);
        bt_10.setOnClickListener(this);
        bt_11.setOnClickListener(this);
        bt_12.setOnClickListener(this);
        bt_13.setOnClickListener(this);
        bt_14.setOnClickListener(this);
        bt_15.setOnClickListener(this);
        bt_16.setOnClickListener(this);
        bt_17.setOnClickListener(this);
        bt_18.setOnClickListener(this);
        bt_19.setOnClickListener(this);
        bt_20.setOnClickListener(this);
        bt_21.setOnClickListener(this);
        bt_22.setOnClickListener(this);
        bt_23.setOnClickListener(this);
        bt_24.setOnClickListener(this);
        bt_25.setOnClickListener(this);

        bt_1.setOnTouchListener(this);
        bt_2.setOnTouchListener(this);
        bt_3.setOnTouchListener(this);
        bt_4.setOnTouchListener(this);
        bt_5.setOnTouchListener(this);
        bt_6.setOnTouchListener(this);
        bt_7.setOnTouchListener(this);
        bt_8.setOnTouchListener(this);
        bt_9.setOnTouchListener(this);
        bt_10.setOnTouchListener(this);
        bt_11.setOnTouchListener(this);
        bt_12.setOnTouchListener(this);
        bt_13.setOnTouchListener(this);
        bt_14.setOnTouchListener(this);
        bt_15.setOnTouchListener(this);
        bt_16.setOnTouchListener(this);
        bt_17.setOnTouchListener(this);
        bt_18.setOnTouchListener(this);
        bt_19.setOnTouchListener(this);
        bt_20.setOnTouchListener(this);
        bt_21.setOnTouchListener(this);
        bt_22.setOnTouchListener(this);
        bt_23.setOnTouchListener(this);
        bt_24.setOnTouchListener(this);
        bt_25.setOnTouchListener(this);
        speed_handler.postDelayed(new Runnable() {
            public void run() {
                CreatDialog.AdjSpeedDialog.dismiss();
                CreatDialog.AdjSpeedDialog = null;
                speed_handler.removeCallbacksAndMessages(null);
            }
        }, 3000);
    }

    private Button bt_slope_1, bt_slope_2, bt_slope_3, bt_slope_4, bt_slope_5, bt_slope_6,
            bt_slope_7, bt_slope_8, bt_slope_9, bt_slope_10, bt_slope_11, bt_slope_12, bt_slope_13,
            bt_slope_14, bt_slope_15, bt_slope_16;
    public static Dialog AdjSlopeDialog;
    public static float currentPlaySpeed = 0.6f;
    //标志位，由于在主页面选择速度播放倍速的时候回ANR，所以需要判断有无设置过速度，有的话在实景fragment打开的时候再将速度设置进去
    public static boolean flag_playSpeed = false;
    public static setPlayerSpeed PlayerSpeed;

    Handler slope_handler = new Handler();

    public void AdjSlopeDialog(Context context, boolean can) {
        AdjSlopeDialog = new Dialog(context, R.style.DefaultDialog);
        AdjSlopeDialog.setCancelable(can);
        AdjSlopeDialog.setContentView(R.layout.dialog_slope_adjust);
        bt_slope_1 = AdjSlopeDialog.findViewById(R.id.bt_slope_1);
        bt_slope_2 = AdjSlopeDialog.findViewById(R.id.bt_slope_2);
        bt_slope_3 = AdjSlopeDialog.findViewById(R.id.bt_slope_3);
        bt_slope_4 = AdjSlopeDialog.findViewById(R.id.bt_slope_4);
        bt_slope_5 = AdjSlopeDialog.findViewById(R.id.bt_slope_5);
        bt_slope_6 = AdjSlopeDialog.findViewById(R.id.bt_slope_6);
        bt_slope_7 = AdjSlopeDialog.findViewById(R.id.bt_slope_7);
        bt_slope_8 = AdjSlopeDialog.findViewById(R.id.bt_slope_8);
        bt_slope_9 = AdjSlopeDialog.findViewById(R.id.bt_slope_9);
        bt_slope_10 = AdjSlopeDialog.findViewById(R.id.bt_slope_10);
        bt_slope_11 = AdjSlopeDialog.findViewById(R.id.bt_slope_11);
        bt_slope_12 = AdjSlopeDialog.findViewById(R.id.bt_slope_12);
        bt_slope_13 = AdjSlopeDialog.findViewById(R.id.bt_slope_13);
        bt_slope_14 = AdjSlopeDialog.findViewById(R.id.bt_slope_14);
        bt_slope_15 = AdjSlopeDialog.findViewById(R.id.bt_slope_15);
        bt_slope_16 = AdjSlopeDialog.findViewById(R.id.bt_slope_16);
        AdjSlopeDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        AdjSlopeDialog.show();
        bt_slope_1.setOnClickListener(this);
        bt_slope_2.setOnClickListener(this);
        bt_slope_3.setOnClickListener(this);
        bt_slope_4.setOnClickListener(this);
        bt_slope_5.setOnClickListener(this);
        bt_slope_6.setOnClickListener(this);
        bt_slope_7.setOnClickListener(this);
        bt_slope_8.setOnClickListener(this);
        bt_slope_9.setOnClickListener(this);
        bt_slope_10.setOnClickListener(this);
        bt_slope_11.setOnClickListener(this);
        bt_slope_12.setOnClickListener(this);
        bt_slope_13.setOnClickListener(this);
        bt_slope_14.setOnClickListener(this);
        bt_slope_15.setOnClickListener(this);
        bt_slope_16.setOnClickListener(this);

        bt_slope_1.setOnTouchListener(this);
        bt_slope_2.setOnTouchListener(this);
        bt_slope_3.setOnTouchListener(this);
        bt_slope_4.setOnTouchListener(this);
        bt_slope_5.setOnTouchListener(this);
        bt_slope_6.setOnTouchListener(this);
        bt_slope_7.setOnTouchListener(this);
        bt_slope_8.setOnTouchListener(this);
        bt_slope_9.setOnTouchListener(this);
        bt_slope_10.setOnTouchListener(this);
        bt_slope_11.setOnTouchListener(this);
        bt_slope_12.setOnTouchListener(this);
        bt_slope_13.setOnTouchListener(this);
        bt_slope_14.setOnTouchListener(this);
        bt_slope_15.setOnTouchListener(this);
        bt_slope_16.setOnTouchListener(this);

        sp_loadPH = context.getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        spfile = new File("/data/data/com.vigorchip.omatreadmill/shared_prefs/sp_PH.xml");
        if (spfile.exists()) {
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
            } else if (SportData.MAXSPEED == 15.5) {
                SportData.MAXSPEED = 25.0;
            }
        }

        if (SportData.MAXSLOPES == 12) {
            bt_slope_1.setBackgroundResource(R.color.colorGreen);
            bt_slope_2.setBackgroundResource(R.color.colorGreen);
            bt_slope_3.setBackgroundResource(R.color.colorGreen);
            bt_slope_4.setBackgroundResource(R.color.colorGreen);
            bt_slope_5.setBackgroundResource(R.color.colorGreen);
            bt_slope_6.setBackgroundResource(R.color.colorGreen);
            bt_slope_7.setBackgroundResource(R.color.colorGreen);
            bt_slope_8.setBackgroundResource(R.color.colorGreen);
            bt_slope_9.setBackgroundResource(R.color.colorGreen);
            bt_slope_10.setBackgroundResource(R.color.colorGreen);
            bt_slope_11.setBackgroundResource(R.color.colorGreen);
            bt_slope_12.setBackgroundResource(R.color.colorGreen);
            bt_slope_13.setBackgroundResource(R.color.colorGray2);
            bt_slope_14.setBackgroundResource(R.color.colorGray2);
            bt_slope_15.setBackgroundResource(R.color.colorGray2);
            bt_slope_16.setBackgroundResource(R.color.colorGray2);
        } else if (SportData.MAXSLOPES == 15) {
            bt_slope_1.setBackgroundResource(R.color.colorGreen);
            bt_slope_2.setBackgroundResource(R.color.colorGreen);
            bt_slope_3.setBackgroundResource(R.color.colorGreen);
            bt_slope_4.setBackgroundResource(R.color.colorGreen);
            bt_slope_5.setBackgroundResource(R.color.colorGreen);
            bt_slope_6.setBackgroundResource(R.color.colorGreen);
            bt_slope_7.setBackgroundResource(R.color.colorGreen);
            bt_slope_8.setBackgroundResource(R.color.colorGreen);
            bt_slope_9.setBackgroundResource(R.color.colorGreen);
            bt_slope_10.setBackgroundResource(R.color.colorGreen);
            bt_slope_11.setBackgroundResource(R.color.colorGreen);
            bt_slope_12.setBackgroundResource(R.color.colorGreen);
            bt_slope_13.setBackgroundResource(R.color.colorGreen);
            bt_slope_14.setBackgroundResource(R.color.colorGreen);
            bt_slope_15.setBackgroundResource(R.color.colorGreen);
            bt_slope_16.setBackgroundResource(R.color.colorGray2);
        }
        slope_handler.postDelayed(new Runnable() {
            public void run() {
                CreatDialog.AdjSlopeDialog.dismiss();
                CreatDialog.AdjSlopeDialog = null;
                slope_handler.removeCallbacksAndMessages(null);
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {
        AdjSpeedOnClick(v);
        AdjSlopeOnClick(v);
    }

    private void AdjSpeedOnClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(1.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 1.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        currentPlaySpeed = 0.5f;
                        PlayerSpeed.OnPlayerSpeed(0.5f);
                        SportData.setSpeed(1.0);
                    } else {
                        SportData.setSpeed(1.0);
                    }
                }
                break;
            case R.id.bt_2:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(2.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 2.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            currentPlaySpeed = 0.6f;
                            PlayerSpeed.OnPlayerSpeed(0.6f);
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 8.1) {
                                currentPlaySpeed = 0.8f;
                                PlayerSpeed.OnPlayerSpeed(0.8f);
                            } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2
                                    || SportData.MAXSPEED == 12.4 || SportData.MAXSPEED == 13.6
                                    || SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 0.6f;
                                PlayerSpeed.OnPlayerSpeed(0.6f);
                            }
                        }
                        SportData.setSpeed(2.0);
                    } else {
                        SportData.setSpeed(2.0);
                    }
                }
                break;
            case R.id.bt_3:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(3.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 3.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            currentPlaySpeed = 0.7f;
                            PlayerSpeed.OnPlayerSpeed(0.7f);
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 8.1) {
                                currentPlaySpeed = 1.0f;
                                PlayerSpeed.OnPlayerSpeed(1.0f);
                            } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2
                                    || SportData.MAXSPEED == 12.4 || SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 0.8f;
                                PlayerSpeed.OnPlayerSpeed(0.8f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 0.7f;
                                PlayerSpeed.OnPlayerSpeed(0.7f);
                            }
                        }
                        SportData.setSpeed(3.0);
                    } else {
                        SportData.setSpeed(3.0);
                    }
                }
                break;
            case R.id.bt_4:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(4.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 4.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            currentPlaySpeed = 0.8f;
                            PlayerSpeed.OnPlayerSpeed(0.8f);
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 8.1) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2
                                    || SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.0f;
                                PlayerSpeed.OnPlayerSpeed(1.0f);
                            } else if (SportData.MAXSPEED == 12.4) {
                                currentPlaySpeed = 0.9f;
                                PlayerSpeed.OnPlayerSpeed(0.9f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 0.8f;
                                PlayerSpeed.OnPlayerSpeed(0.8f);
                            }
                        }
                        SportData.setSpeed(4.0);
                    } else {
                        SportData.setSpeed(4.0);
                    }
                }
                break;
            case R.id.bt_5:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(5.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 5.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            currentPlaySpeed = 0.9f;
                            PlayerSpeed.OnPlayerSpeed(0.9f);
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 8.1) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            } else if (SportData.MAXSPEED == 12.4) {
                                currentPlaySpeed = 1.0f;
                                PlayerSpeed.OnPlayerSpeed(1.0f);
                            } else if (SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.1f;
                                PlayerSpeed.OnPlayerSpeed(1.1f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 0.9f;
                                PlayerSpeed.OnPlayerSpeed(0.9f);
                            }
                        }
                        SportData.setSpeed(5.0);
                    } else {
                        SportData.setSpeed(5.0);
                    }
                }
                break;
            case R.id.bt_6:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(6.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 6.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            currentPlaySpeed = 1.0f;
                            PlayerSpeed.OnPlayerSpeed(1.0f);
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 8.1) {
                                currentPlaySpeed = 1.6f;
                                PlayerSpeed.OnPlayerSpeed(1.6f);
                            } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 12.4 || SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 1.0f;
                                PlayerSpeed.OnPlayerSpeed(1.0f);
                            }
                        }
                        SportData.setSpeed(6.0);
                    } else {
                        SportData.setSpeed(6.0);
                    }
                }
                break;
            case R.id.bt_7:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(7.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 7.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            currentPlaySpeed = 1.1f;
                            PlayerSpeed.OnPlayerSpeed(1.1f);
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 8.1) {
                                currentPlaySpeed = 1.8f;
                                PlayerSpeed.OnPlayerSpeed(1.8f);
                            } else if (SportData.MAXSPEED == 9.9 || SportData.MAXSPEED == 11.2) {
                                currentPlaySpeed = 1.6f;
                                PlayerSpeed.OnPlayerSpeed(1.6f);
                            } else if (SportData.MAXSPEED == 12.4) {
                                currentPlaySpeed = 1.5f;
                                PlayerSpeed.OnPlayerSpeed(1.5f);
                            } else if (SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 1.1f;
                                PlayerSpeed.OnPlayerSpeed(1.1f);
                            }
                        }
                        SportData.setSpeed(7.0);
                    } else {
                        SportData.setSpeed(7.0);
                    }
                }
                break;
            case R.id.bt_8:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(8.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 8.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            currentPlaySpeed = 1.2f;
                            PlayerSpeed.OnPlayerSpeed(1.2f);
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 8.1) {
                                currentPlaySpeed = 2.0f;
                                PlayerSpeed.OnPlayerSpeed(2.0f);
                            } else if (SportData.MAXSPEED == 9.9) {
                                currentPlaySpeed = 1.8f;
                                PlayerSpeed.OnPlayerSpeed(1.8f);
                            } else if (SportData.MAXSPEED == 11.2) {
                                currentPlaySpeed = 1.7f;
                                PlayerSpeed.OnPlayerSpeed(1.7f);
                            } else if (SportData.MAXSPEED == 12.4) {
                                currentPlaySpeed = 1.6f;
                                PlayerSpeed.OnPlayerSpeed(1.6f);
                            } else if (SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.5f;
                                PlayerSpeed.OnPlayerSpeed(1.5f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            }
                        }
                        SportData.setSpeed(8.0);
                    } else {
                        SportData.setSpeed(8.0);
                    }
                }
                break;
            case R.id.bt_9:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(9.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 9.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            if (SportData.MAXSPEED == 13) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 16) {
                                currentPlaySpeed = 1.3f;
                                PlayerSpeed.OnPlayerSpeed(1.3f);
                            } else if (SportData.MAXSPEED == 18) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            } else if (SportData.MAXSPEED == 20) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            } else if (SportData.MAXSPEED == 22) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            } else if (SportData.MAXSPEED == 25) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            }
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 9.9) {
                                currentPlaySpeed = 2.0f;
                                PlayerSpeed.OnPlayerSpeed(2.0f);
                            } else if (SportData.MAXSPEED == 11.2) {
                                currentPlaySpeed = 1.8f;
                                PlayerSpeed.OnPlayerSpeed(1.8f);
                            } else if (SportData.MAXSPEED == 12.4) {
                                currentPlaySpeed = 1.7f;
                                PlayerSpeed.OnPlayerSpeed(1.7f);
                            } else if (SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.6f;
                                PlayerSpeed.OnPlayerSpeed(1.6f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 1.3f;
                                PlayerSpeed.OnPlayerSpeed(1.3f);
                            }
                        }
                        SportData.setSpeed(9.0);
                    } else {
                        SportData.setSpeed(9.0);
                    }
                }
                break;
            case R.id.bt_10:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(10.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 10.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            if (SportData.MAXSPEED == 13) {
                                currentPlaySpeed = 1.7f;
                                PlayerSpeed.OnPlayerSpeed(1.7f);
                            } else if (SportData.MAXSPEED == 16) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 18) {
                                currentPlaySpeed = 1.3f;
                                PlayerSpeed.OnPlayerSpeed(1.3f);
                            } else if (SportData.MAXSPEED == 20) {
                                currentPlaySpeed = 1.3f;
                                PlayerSpeed.OnPlayerSpeed(1.3f);
                            } else if (SportData.MAXSPEED == 22) {
                                currentPlaySpeed = 1.3f;
                                PlayerSpeed.OnPlayerSpeed(1.3f);
                            } else if (SportData.MAXSPEED == 25) {
                                currentPlaySpeed = 1.2f;
                                PlayerSpeed.OnPlayerSpeed(1.2f);
                            }
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 11.2) {
                                currentPlaySpeed = 1.9f;
                                PlayerSpeed.OnPlayerSpeed(1.9f);
                            } else if (SportData.MAXSPEED == 12.4) {
                                currentPlaySpeed = 1.8f;
                                PlayerSpeed.OnPlayerSpeed(1.8f);
                            } else if (SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.7f;
                                PlayerSpeed.OnPlayerSpeed(1.7f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            }
                        }
                        SportData.setSpeed(10.0);
                    } else {
                        SportData.setSpeed(10.0);
                    }
                }
                break;
            case R.id.bt_11:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSpeed(11.0);
                        Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 11.0;
                    } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                        if (isApplication.ph_selector == 1) {
                            if (SportData.MAXSPEED == 13) {
                                currentPlaySpeed = 1.8f;
                                PlayerSpeed.OnPlayerSpeed(1.8f);
                            } else if (SportData.MAXSPEED == 16) {
                                currentPlaySpeed = 1.5f;
                                PlayerSpeed.OnPlayerSpeed(1.5f);
                            } else if (SportData.MAXSPEED == 18) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 20) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 22) {
                                currentPlaySpeed = 1.4f;
                                PlayerSpeed.OnPlayerSpeed(1.4f);
                            } else if (SportData.MAXSPEED == 25) {
                                currentPlaySpeed = 1.3f;
                                PlayerSpeed.OnPlayerSpeed(1.3f);
                            }
                        } else if (isApplication.ph_selector == 2) {
                            if (SportData.MAXSPEED == 11.2) {
                                currentPlaySpeed = 2.0f;
                                PlayerSpeed.OnPlayerSpeed(2.0f);
                            } else if (SportData.MAXSPEED == 12.4) {
                                currentPlaySpeed = 1.9f;
                                PlayerSpeed.OnPlayerSpeed(1.9f);
                            } else if (SportData.MAXSPEED == 13.6) {
                                currentPlaySpeed = 1.8f;
                                PlayerSpeed.OnPlayerSpeed(1.8f);
                            } else if (SportData.MAXSPEED == 15.5) {
                                currentPlaySpeed = 1.5f;
                                PlayerSpeed.OnPlayerSpeed(1.5f);
                            }
                        }
                        SportData.setSpeed(11.0);
                    } else {
                        SportData.setSpeed(11.0);
                    }
                }
                break;
            case R.id.bt_12:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 16.0 || SportData.MAXSPEED == 18.0
                            || SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0
                            || SportData.MAXSPEED == 25.0 || SportData.MAXSPEED == 15.5) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(12.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 12.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 13) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                } else if (SportData.MAXSPEED == 16) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                } else if (SportData.MAXSPEED == 18) {
                                    currentPlaySpeed = 1.4f;
                                    PlayerSpeed.OnPlayerSpeed(1.4f);
                                } else if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.4f;
                                    PlayerSpeed.OnPlayerSpeed(1.4f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.5f;
                                    PlayerSpeed.OnPlayerSpeed(1.5f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.4f;
                                    PlayerSpeed.OnPlayerSpeed(1.4f);
                                }
                            } else if (isApplication.ph_selector == 2) {
                                if (SportData.MAXSPEED == 12.4) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 13.6) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                } else if (SportData.MAXSPEED == 15.5) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                }
                            }
                            SportData.setSpeed(12.0);
                        } else {
                            SportData.setSpeed(12.0);
                        }
                    }
                }
                break;
            case R.id.bt_13:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 16.0 || SportData.MAXSPEED == 18.0
                            || SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0
                            || SportData.MAXSPEED == 25.0 || SportData.MAXSPEED == 15.5) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(13.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 13.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 13) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 16) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                } else if (SportData.MAXSPEED == 18) {
                                    currentPlaySpeed = 1.5f;
                                    PlayerSpeed.OnPlayerSpeed(1.5f);
                                } else if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.5f;
                                    PlayerSpeed.OnPlayerSpeed(1.5f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.5f;
                                    PlayerSpeed.OnPlayerSpeed(1.5f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.4f;
                                    PlayerSpeed.OnPlayerSpeed(1.4f);
                                }
                            } else if (isApplication.ph_selector == 2) {
                                if (SportData.MAXSPEED == 13.6) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 15.5) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                }
                            }
                            SportData.setSpeed(13.0);
                        } else {
                            SportData.setSpeed(13.0);
                        }
                    }
                }
                break;
            case R.id.bt_14:
                if (newSerial.TREADMILL_STATUS == 1) {
                    Log.e("查看最大14", String.valueOf(SportData.MAXSPEED == 16.0 || SportData.MAXSPEED == 18.0
                            || SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0));
                    if (SportData.MAXSPEED == 16.0 || SportData.MAXSPEED == 18.0
                            || SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0
                            || SportData.MAXSPEED == 25.0 || SportData.MAXSPEED == 15.5) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(14.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 14.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 16) {
                                    currentPlaySpeed = 1.8f;
                                    PlayerSpeed.OnPlayerSpeed(1.8f);
                                } else if (SportData.MAXSPEED == 18) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                } else if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.4f;
                                    PlayerSpeed.OnPlayerSpeed(1.4f);
                                }
                            } else if (isApplication.ph_selector == 2) {
                                if (SportData.MAXSPEED == 15.5) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                }
                            }
                            SportData.setSpeed(14.0);
                        } else {
                            SportData.setSpeed(14.0);
                        }
                    }
                }
                break;
            case R.id.bt_15:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 16.0 || SportData.MAXSPEED == 18.0
                            || SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0
                            || SportData.MAXSPEED == 25.0 || SportData.MAXSPEED == 15.5) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(15.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 15.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 16) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                } else if (SportData.MAXSPEED == 18) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                } else if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.5f;
                                    PlayerSpeed.OnPlayerSpeed(1.5f);
                                }
                            } else if (isApplication.ph_selector == 2) {
                                if (SportData.MAXSPEED == 15.5) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                }
                            }
                            SportData.setSpeed(15.0);
                        } else {
                            SportData.setSpeed(15.0);
                        }
                    }
                }
                break;
            case R.id.bt_16:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 16.0 || SportData.MAXSPEED == 18.0
                            || SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(16.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 16.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 16) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 18) {
                                    currentPlaySpeed = 1.8f;
                                    PlayerSpeed.OnPlayerSpeed(1.8f);
                                } else if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.5f;
                                    PlayerSpeed.OnPlayerSpeed(1.5f);
                                }
                            }
                            SportData.setSpeed(16.0);
                        } else {
                            SportData.setSpeed(16.0);
                        }
                    }
                }
                break;
            case R.id.bt_17:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                            || SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(17.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 17.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 18) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                } else if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.6f;
                                    PlayerSpeed.OnPlayerSpeed(1.6f);
                                }
                            }
                            SportData.setSpeed(17.0);
                        } else {
                            SportData.setSpeed(17.0);
                        }
                    }
                }
                break;
            case R.id.bt_18:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 20.0
                            || SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(18.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 18.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 18) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.8f;
                                    PlayerSpeed.OnPlayerSpeed(1.8f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.8f;
                                    PlayerSpeed.OnPlayerSpeed(1.8f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.7f;
                                    PlayerSpeed.OnPlayerSpeed(1.7f);
                                }
                            }
                            SportData.setSpeed(18.0);
                        } else {
                            SportData.setSpeed(18.0);
                        }
                    }
                }
                break;
            case R.id.bt_19:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(19.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 19.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.8f;
                                    PlayerSpeed.OnPlayerSpeed(1.8f);
                                }
                            }
                            SportData.setSpeed(19.0);
                        } else {
                            SportData.setSpeed(19.0);
                        }
                    }
                }
                break;
            case R.id.bt_20:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(20.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 20.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 20) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.8f;
                                    PlayerSpeed.OnPlayerSpeed(1.8f);
                                }
                            }
                            SportData.setSpeed(20.0);
                        } else {
                            SportData.setSpeed(20.0);
                        }
                    }
                }
                break;
            case R.id.bt_21:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(21.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 21.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                }
                            }
                            SportData.setSpeed(21.0);
                        } else {
                            SportData.setSpeed(21.0);
                        }
                    }
                }
                break;
            case R.id.bt_22:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 22.0 || SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(22.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 22.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 22) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                } else if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 1.9f;
                                    PlayerSpeed.OnPlayerSpeed(1.9f);
                                }
                            }
                            SportData.setSpeed(22.0);
                        } else {
                            SportData.setSpeed(22.0);
                        }
                    }
                }
                break;
            case R.id.bt_23:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(23.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 23.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                }
                            }
                            SportData.setSpeed(23.0);
                        } else {
                            SportData.setSpeed(23.0);
                        }
                    }
                }
                break;
            case R.id.bt_24:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(24.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 24.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                }
                            }
                            SportData.setSpeed(24.0);
                        } else {
                            SportData.setSpeed(24.0);
                        }
                    }
                }
                break;
            case R.id.bt_25:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSPEED == 25.0) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSpeed(25.0);
                            Fragment_Running2.Arr_speed[Fragment_Running2.racIndex] = 25.0;
                        } else if (SportData.getStatus() == SportData.RUNNING_MODE_LIVE) {
                            if (isApplication.ph_selector == 1) {
                                if (SportData.MAXSPEED == 25) {
                                    currentPlaySpeed = 2.0f;
                                    PlayerSpeed.OnPlayerSpeed(2.0f);
                                }
                            }
                            SportData.setSpeed(25.0);
                        } else {
                            SportData.setSpeed(25.0);
                        }
                    }
                }
                break;
        }
    }

    private void AdjSlopeOnClick(View v) {
        switch (v.getId()) {
            case R.id.bt_slope_1:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(1);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 1;
                    } else {
                        SportData.setSlope(1);
                    }
                }
                break;
            case R.id.bt_slope_2:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(2);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 2;
                    } else {
                        SportData.setSlope(2);
                    }
                }
                break;
            case R.id.bt_slope_3:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(3);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 3;
                    } else {
                        SportData.setSlope(3);
                    }
                }
                break;
            case R.id.bt_slope_4:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(4);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 4;
                    } else {
                        SportData.setSlope(4);
                    }
                }
                break;
            case R.id.bt_slope_5:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(5);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 5;
                    } else {
                        SportData.setSlope(5);
                    }
                }
                break;
            case R.id.bt_slope_6:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(6);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 6;
                    } else {
                        SportData.setSlope(6);
                    }
                }
                break;
            case R.id.bt_slope_7:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(7);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 7;
                    } else {
                        SportData.setSlope(7);
                    }
                }
                break;
            case R.id.bt_slope_8:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(8);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 8;
                    } else {
                        SportData.setSlope(8);
                    }
                }
                break;
            case R.id.bt_slope_9:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(9);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 9;
                    } else {
                        SportData.setSlope(9);
                    }
                }
                break;
            case R.id.bt_slope_10:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(10);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 10;
                    } else {
                        SportData.setSlope(10);
                    }
                }
                break;
            case R.id.bt_slope_11:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(11);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 11;
                    } else {
                        SportData.setSlope(11);
                    }
                }
                break;
            case R.id.bt_slope_12:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.isTrainRunning()) {
                        SportData.setSlope(12);
                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 12;
                    } else {
                        SportData.setSlope(12);
                    }
                }
                break;
            case R.id.bt_slope_13:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSLOPES == 15) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSlope(13);
                            Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 13;
                        } else {
                            SportData.setSlope(13);
                        }
                    }
                }
                break;
            case R.id.bt_slope_14:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSLOPES == 15) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSlope(14);
                            Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 14;
                        } else {
                            SportData.setSlope(14);
                        }
                    }
                }
                break;
            case R.id.bt_slope_15:
                if (newSerial.TREADMILL_STATUS == 1) {
                    if (SportData.MAXSLOPES == 15) {
                        if (SportData.isTrainRunning()) {
                            SportData.setSlope(15);
                            Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 15;
                        } else {
                            SportData.setSlope(15);
                        }
                    }
                }
                break;
            case R.id.bt_slope_16:
//                if (SportData.MAXSLOPES == 15) {
//                    if (SportData.isTrainRunning()) {
//                        SportData.setSlope(16);
//                        Fragment_Running2.Arr_slope[Fragment_Running2.racIndex] = 16;
//                        AdjSlopeDialog.dismiss();
//                    } else {
//                        SportData.setSlope(16);
//                        AdjSlopeDialog.dismiss();
//                    }
//                }
                break;
        }
    }

    private void ph_speed() {
        if (SportData.MAXSPEED == 13.0 || SportData.MAXSPEED == 8.1) {
            if (isApplication.ph_selector == 1) {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGreen);
                bt_13.setBackgroundResource(R.color.colorGreen);
                bt_14.setBackgroundResource(R.color.colorGray2);
                bt_15.setBackgroundResource(R.color.colorGray2);
                bt_16.setBackgroundResource(R.color.colorGray2);
                bt_17.setBackgroundResource(R.color.colorGray2);
                bt_18.setBackgroundResource(R.color.colorGray2);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            } else {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGray2);
                bt_10.setBackgroundResource(R.color.colorGray2);
                bt_11.setBackgroundResource(R.color.colorGray2);
                bt_12.setBackgroundResource(R.color.colorGray2);
                bt_13.setBackgroundResource(R.color.colorGray2);
                bt_14.setBackgroundResource(R.color.colorGray2);
                bt_15.setBackgroundResource(R.color.colorGray2);
                bt_16.setBackgroundResource(R.color.colorGray2);
                bt_17.setBackgroundResource(R.color.colorGray2);
                bt_18.setBackgroundResource(R.color.colorGray2);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            }
        } else if (SportData.MAXSPEED == 16.0 || SportData.MAXSPEED == 9.9) {
            if (isApplication.ph_selector == 1) {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGreen);
                bt_13.setBackgroundResource(R.color.colorGreen);
                bt_14.setBackgroundResource(R.color.colorGreen);
                bt_15.setBackgroundResource(R.color.colorGreen);
                bt_16.setBackgroundResource(R.color.colorGreen);
                bt_17.setBackgroundResource(R.color.colorGray2);
                bt_18.setBackgroundResource(R.color.colorGray2);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            } else {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGray2);
                bt_12.setBackgroundResource(R.color.colorGray2);
                bt_13.setBackgroundResource(R.color.colorGray2);
                bt_14.setBackgroundResource(R.color.colorGray2);
                bt_15.setBackgroundResource(R.color.colorGray2);
                bt_16.setBackgroundResource(R.color.colorGray2);
                bt_17.setBackgroundResource(R.color.colorGray2);
                bt_18.setBackgroundResource(R.color.colorGray2);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            }
        } else if (SportData.MAXSPEED == 18.0 || SportData.MAXSPEED == 11.2) {
            if (isApplication.ph_selector == 1) {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGreen);
                bt_13.setBackgroundResource(R.color.colorGreen);
                bt_14.setBackgroundResource(R.color.colorGreen);
                bt_15.setBackgroundResource(R.color.colorGreen);
                bt_16.setBackgroundResource(R.color.colorGreen);
                bt_17.setBackgroundResource(R.color.colorGreen);
                bt_18.setBackgroundResource(R.color.colorGreen);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            } else {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGray2);
                bt_13.setBackgroundResource(R.color.colorGray2);
                bt_14.setBackgroundResource(R.color.colorGray2);
                bt_15.setBackgroundResource(R.color.colorGray2);
                bt_16.setBackgroundResource(R.color.colorGray2);
                bt_17.setBackgroundResource(R.color.colorGray2);
                bt_18.setBackgroundResource(R.color.colorGray2);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            }
        } else if (SportData.MAXSPEED == 20.0 || SportData.MAXSPEED == 12.4) {
            if (isApplication.ph_selector == 1) {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGreen);
                bt_13.setBackgroundResource(R.color.colorGreen);
                bt_14.setBackgroundResource(R.color.colorGreen);
                bt_15.setBackgroundResource(R.color.colorGreen);
                bt_16.setBackgroundResource(R.color.colorGreen);
                bt_17.setBackgroundResource(R.color.colorGreen);
                bt_18.setBackgroundResource(R.color.colorGreen);
                bt_19.setBackgroundResource(R.color.colorGreen);
                bt_20.setBackgroundResource(R.color.colorGreen);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            } else {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGreen);
                bt_13.setBackgroundResource(R.color.colorGray2);
                bt_14.setBackgroundResource(R.color.colorGray2);
                bt_15.setBackgroundResource(R.color.colorGray2);
                bt_16.setBackgroundResource(R.color.colorGray2);
                bt_17.setBackgroundResource(R.color.colorGray2);
                bt_18.setBackgroundResource(R.color.colorGray2);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            }
        } else if (SportData.MAXSPEED == 25.0 || SportData.MAXSPEED == 15.5) {
            if (isApplication.ph_selector == 1) {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGreen);
                bt_13.setBackgroundResource(R.color.colorGreen);
                bt_14.setBackgroundResource(R.color.colorGreen);
                bt_15.setBackgroundResource(R.color.colorGreen);
                bt_16.setBackgroundResource(R.color.colorGreen);
                bt_17.setBackgroundResource(R.color.colorGreen);
                bt_18.setBackgroundResource(R.color.colorGreen);
                bt_19.setBackgroundResource(R.color.colorGreen);
                bt_20.setBackgroundResource(R.color.colorGreen);
                bt_21.setBackgroundResource(R.color.colorGreen);
                bt_22.setBackgroundResource(R.color.colorGreen);
                bt_23.setBackgroundResource(R.color.colorGreen);
                bt_24.setBackgroundResource(R.color.colorGreen);
                bt_25.setBackgroundResource(R.color.colorGreen);
            } else {
                bt_1.setBackgroundResource(R.color.colorGreen);
                bt_2.setBackgroundResource(R.color.colorGreen);
                bt_3.setBackgroundResource(R.color.colorGreen);
                bt_4.setBackgroundResource(R.color.colorGreen);
                bt_5.setBackgroundResource(R.color.colorGreen);
                bt_6.setBackgroundResource(R.color.colorGreen);
                bt_7.setBackgroundResource(R.color.colorGreen);
                bt_8.setBackgroundResource(R.color.colorGreen);
                bt_9.setBackgroundResource(R.color.colorGreen);
                bt_10.setBackgroundResource(R.color.colorGreen);
                bt_11.setBackgroundResource(R.color.colorGreen);
                bt_12.setBackgroundResource(R.color.colorGreen);
                bt_13.setBackgroundResource(R.color.colorGreen);
                bt_14.setBackgroundResource(R.color.colorGreen);
                bt_15.setBackgroundResource(R.color.colorGreen);
                bt_16.setBackgroundResource(R.color.colorGray2);
                bt_17.setBackgroundResource(R.color.colorGray2);
                bt_18.setBackgroundResource(R.color.colorGray2);
                bt_19.setBackgroundResource(R.color.colorGray2);
                bt_20.setBackgroundResource(R.color.colorGray2);
                bt_21.setBackgroundResource(R.color.colorGray2);
                bt_22.setBackgroundResource(R.color.colorGray2);
                bt_23.setBackgroundResource(R.color.colorGray2);
                bt_24.setBackgroundResource(R.color.colorGray2);
                bt_25.setBackgroundResource(R.color.colorGray2);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        SpeedTouch(v, motionEvent);
        SlopeTouch(v, motionEvent);
        return false;
    }

    public void SpeedTouch(View v, MotionEvent m) {
        switch (v.getId()) {
            case R.id.bt_1:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_2:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_3:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_4:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_5:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_6:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_7:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_8:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_9:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_10:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_11:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_12:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_13:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_14:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_15:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_16:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_17:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_18:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_19:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_20:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_21:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_22:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_23:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_24:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_25:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speed_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSpeedDialog.dismiss();
                                CreatDialog.AdjSpeedDialog = null;
                                speed_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speed_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
        }
    }

    public void SlopeTouch(View v, MotionEvent m) {//坡度弹窗触摸事件
        switch (v.getId()) {
            case R.id.bt_slope_1:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_2:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_3:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_4:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_5:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_6:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_7:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_8:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_9:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_10:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_11:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_12:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_13:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_14:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_15:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
            case R.id.bt_slope_16:
                switch (m.getAction()) {
                    case MotionEvent.ACTION_UP:
                        slope_handler.postDelayed(new Runnable() {
                            public void run() {
                                CreatDialog.AdjSlopeDialog.dismiss();
                                CreatDialog.AdjSlopeDialog = null;
                                slope_handler.removeCallbacksAndMessages(null);
                            }
                        }, 3000);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        slope_handler.removeCallbacksAndMessages(null);
                        break;
                }
                break;
        }
    }
}
