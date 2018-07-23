package com.vigorchip.omatreadmill.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.setPH;
import com.vigorchip.omatreadmill.utils.AnimateUtils;
import com.vigorchip.omatreadmill.utils.BrightnessUtils;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.WakeLockUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wr-app1 on 2018/3/27.
 */

public class Fragment_Settings extends Fragment implements View.OnClickListener {
    private RelativeLayout settingsID_language, settingsID_Tones, settingsID_wifi,
            settingsID_Sleep, settingsID_Timeset, settingsID_skin, settingsID_bright;
    private TextView settingsID_tv_Tones, tv_kph, tv_mph;
    private View view;
    private AudioManager audioManager;
    private ImageView icon_Tones;
    private SeekBar Seekbar_Brightness;
    private RadioGroup Sleep_rg;
    private RadioButton sleep_rb1, sleep_rb2, sleep_rb3, sleep_rb4, sleep_rb5;

    private boolean TonesFlag = true;//判断提示音的标志位
    private int wakelock_selector = 0;//判断休眠的标志位

    /**
     * 公英制SharedPreferences
     */
    private SharedPreferences sp_savePH;
    private SharedPreferences sp_loadPH;
    private SharedPreferences.Editor PH_editor;
    private String PH;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        getTone();
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
                tv_kph.setBackgroundResource(R.color.colorOrange);
                tv_mph.setBackgroundResource(R.color.colorGreen);
                EventBus.getDefault().postSticky(new setPH("km", "km/h"));
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
                tv_mph.setBackgroundResource(R.color.colorOrange);
                tv_kph.setBackgroundResource(R.color.colorGreen);
                EventBus.getDefault().postSticky(new setPH("mile", "MPH"));
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
            tv_kph.setBackgroundResource(R.color.colorOrange);
            tv_mph.setBackgroundResource(R.color.colorGreen);
            EventBus.getDefault().postSticky(new setPH("km", "km/h"));
        }
    }


    public void initView() {
        /**公英制SharedPreferences*/
        sp_savePH = getContext().getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        sp_loadPH = getContext().getSharedPreferences("sp_PH", Context.MODE_PRIVATE);
        PH_editor = sp_savePH.edit();

        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        tv_kph = view.findViewById(R.id.tv_kph);
        tv_mph = view.findViewById(R.id.tv_mph);
        settingsID_Tones = view.findViewById(R.id.settingsID_Tones);
        icon_Tones = view.findViewById(R.id.icon_Tones);
        settingsID_tv_Tones = view.findViewById(R.id.settingsID_tv_Tones);
        settingsID_language = view.findViewById(R.id.settingsID_language);
        settingsID_wifi = view.findViewById(R.id.settingsID_wifi);
        settingsID_Sleep = view.findViewById(R.id.settingsID_Sleep);
        settingsID_Timeset = view.findViewById(R.id.settingsID_Timeset);
        settingsID_skin = view.findViewById(R.id.settingsID_skin);
        settingsID_bright = view.findViewById(R.id.settingsID_bright);
        tv_kph.setOnClickListener(this);
        tv_mph.setOnClickListener(this);
        settingsID_Tones.setOnClickListener(this);
        settingsID_language.setOnClickListener(this);
        settingsID_wifi.setOnClickListener(this);
        settingsID_Sleep.setOnClickListener(this);
        settingsID_Timeset.setOnClickListener(this);
        settingsID_skin.setOnClickListener(this);
        settingsID_bright.setOnClickListener(this);
        AnimateUtils.setClickAnimation(getContext(), settingsID_language);
        AnimateUtils.setClickAnimation(getContext(), settingsID_Tones);
        AnimateUtils.setClickAnimation(getContext(), settingsID_wifi);
        AnimateUtils.setClickAnimation(getContext(), settingsID_Sleep);
        AnimateUtils.setClickAnimation(getContext(), settingsID_Timeset);
        AnimateUtils.setClickAnimation(getContext(), settingsID_skin);
        AnimateUtils.setClickAnimation(getContext(), settingsID_bright);
        tv_kph.setBackgroundResource(R.color.colorOrange);
        tv_mph.setBackgroundResource(R.color.colorGreen);
    }


    Handler br_handler = new Handler();//判斷用戶松手后重記時間的綫程
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingsID_language:
                ((MainActivity) getActivity()).showFragment(7, 10);
                break;
            case R.id.settingsID_Tones:
                if (TonesFlag) {
                    Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 1);
                    settingsID_tv_Tones.setText(getContext().getString(R.string.tone_on));
                    icon_Tones.setImageResource(R.drawable.volume_on);
                    settingsID_Tones.setBackgroundResource(R.color.colorOrange);
                    TonesFlag = false;
                } else {
                    Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
                    settingsID_tv_Tones.setText(getContext().getString(R.string.tone_off));
                    icon_Tones.setImageResource(R.drawable.volume_off);
                    settingsID_Tones.setBackgroundResource(R.color.colorGreen);
                    TonesFlag = true;
                }
                break;
            case R.id.settingsID_wifi:
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.settingsID_Sleep:
                if (!SportData.isRunning()) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_sleep, true);
                    Sleep_rg = CreatDialog.dialog.findViewById(R.id.Sleep_rg);
                    sleep_rb1 = CreatDialog.dialog.findViewById(R.id.sleep_rb1);
                    sleep_rb2 = CreatDialog.dialog.findViewById(R.id.sleep_rb2);
                    sleep_rb3 = CreatDialog.dialog.findViewById(R.id.sleep_rb3);
                    sleep_rb4 = CreatDialog.dialog.findViewById(R.id.sleep_rb4);
                    sleep_rb5 = CreatDialog.dialog.findViewById(R.id.sleep_rb5);
                    WackLockFlag(WakeLockUtils.getScreenOffTime(getContext()));
                    Sleep_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int rb) {
                            RadioButton radioButton = Sleep_rg.findViewById(rb);
                            String result = radioButton.getText().toString();
                            switch (result) {
                                case "1分钟":
                                    wakelock_selector = 1;
                                    WakeLockUtils.setWakeLock(getContext(), 60 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "4.5分钟":
                                    wakelock_selector = 2;
                                    WakeLockUtils.setWakeLock(getContext(), 270 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "10分钟":
                                    wakelock_selector = 3;
                                    WakeLockUtils.setWakeLock(getContext(), 600 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "30分钟":
                                    wakelock_selector = 4;
                                    WakeLockUtils.setWakeLock(getContext(), 1800 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "永不":
                                    wakelock_selector = 5;
                                    WakeLockUtils.setWakeLock(getContext(), Integer.MAX_VALUE);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "1Min":
                                    wakelock_selector = 1;
                                    WakeLockUtils.setWakeLock(getContext(), 60 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "4.5Min":
                                    wakelock_selector = 2;
                                    WakeLockUtils.setWakeLock(getContext(), 270 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "10Min":
                                    wakelock_selector = 3;
                                    WakeLockUtils.setWakeLock(getContext(), 600 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "30Min":
                                    wakelock_selector = 4;
                                    WakeLockUtils.setWakeLock(getContext(), 1800 * 1000);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "Never":
                                    wakelock_selector = 5;
                                    WakeLockUtils.setWakeLock(getContext(), Integer.MAX_VALUE);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                                case "Jamais":
                                    wakelock_selector = 5;
                                    WakeLockUtils.setWakeLock(getContext(), Integer.MAX_VALUE);
                                    radioButton.setChecked(true);
                                    CreatDialog.dialog.dismiss();
                                    break;
                            }
                        }
                    });
                } else if (SportData.isRunning()) {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_sleep_toast, true);
                    CreatDialog.dialog.findViewById(R.id.sleep_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CreatDialog.dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.settingsID_Timeset:
                startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                break;
            case R.id.settingsID_skin:
                ((MainActivity) getActivity()).showFragment(7, 13);
                break;
            case R.id.settingsID_bright:
                CreatDialog.showDialog(getContext(), R.layout.dialog_brightness, true);
                Seekbar_Brightness = CreatDialog.dialog.findViewById(R.id.Seekbar_Brightness);
                lightness();
                Seekbar_Brightness.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_UP:
                                br_handler.postDelayed(new Runnable() {
                                    public void run() {
                                        CreatDialog.dialog.dismiss();
                                    }
                                }, 3000);
                                break;
                            case MotionEvent.ACTION_DOWN:
                                br_handler.removeCallbacksAndMessages(null);
                                break;
                        }
                        return false;
                    }
                });
                br_handler.postDelayed(new Runnable() {
                    public void run() {
                        CreatDialog.dialog.dismiss();
                    }
                }, 3000);
                break;
            case R.id.tv_kph:
                if (!SportData.isRunning()) {
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
                    tv_kph.setBackgroundResource(R.color.colorOrange);
                    tv_mph.setBackgroundResource(R.color.colorGreen);
                    EventBus.getDefault().postSticky(new setPH("km", "km/h"));
                    PH_editor.putString("PH", "KPH");
                    PH_editor.commit();
                } else {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_ph_toast, true);
                    CreatDialog.dialog.findViewById(R.id.ph_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.tv_mph:
                if (!SportData.isRunning()) {
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
                    tv_mph.setBackgroundResource(R.color.colorOrange);
                    tv_kph.setBackgroundResource(R.color.colorGreen);
                    EventBus.getDefault().postSticky(new setPH("mile", "MPH"));
                    PH_editor.putString("PH", "MPH");
                    PH_editor.commit();
                } else {
                    CreatDialog.showDialog(getContext(), R.layout.dialog_ph_toast, true);
                    CreatDialog.dialog.findViewById(R.id.ph_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CreatDialog.dialog.dismiss();
                                }
                            });
                }
                break;
        }
    }


    private void getTone() {//获取系统提示音
        if (Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0) == 0) {
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
            settingsID_tv_Tones.setText(getContext().getString(R.string.tone_off));
            icon_Tones.setImageResource(R.drawable.volume_off);
            settingsID_Tones.setBackgroundResource(R.color.colorGreen);
            TonesFlag = true;
        } else {
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 1);
            settingsID_tv_Tones.setText(getContext().getString(R.string.tone_on));
            icon_Tones.setImageResource(R.drawable.volume_on);
            settingsID_Tones.setBackgroundResource(R.color.colorOrange);
            TonesFlag = false;
        }
    }

    private void lightness() {//设置亮度
        Seekbar_Brightness.setProgress(BrightnessUtils.getScreenBrightness(getContext()));
        BrightnessUtils.setScreenMode(getContext(), 0);
        Seekbar_Brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                BrightnessUtils.saveScreenBrightness(getContext(), i);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void WackLockFlag(int time) {
        switch (time) {
            case 60000:
                wakelock_selector = 1;
                sleep_rb1.setChecked(true);
                sleep_rb2.setChecked(false);
                sleep_rb3.setChecked(false);
                sleep_rb4.setChecked(false);
                sleep_rb5.setChecked(false);
                break;
            case 270000:
                wakelock_selector = 2;
                sleep_rb1.setChecked(false);
                sleep_rb2.setChecked(true);
                sleep_rb3.setChecked(false);
                sleep_rb4.setChecked(false);
                sleep_rb5.setChecked(false);
                break;
            case 600000:
                wakelock_selector = 3;
                sleep_rb1.setChecked(false);
                sleep_rb2.setChecked(false);
                sleep_rb3.setChecked(true);
                sleep_rb4.setChecked(false);
                sleep_rb5.setChecked(false);
                break;
            case 1800000:
                wakelock_selector = 4;
                sleep_rb1.setChecked(false);
                sleep_rb2.setChecked(false);
                sleep_rb3.setChecked(false);
                sleep_rb4.setChecked(true);
                sleep_rb5.setChecked(false);
                break;
            case 2147483647:
                wakelock_selector = 5;
                sleep_rb1.setChecked(false);
                sleep_rb2.setChecked(false);
                sleep_rb3.setChecked(false);
                sleep_rb4.setChecked(false);
                sleep_rb5.setChecked(true);
                break;
        }
    }

}
