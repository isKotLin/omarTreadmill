package com.vigorchip.omatreadmill.serialport;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.SerialToHome;
import com.vigorchip.omatreadmill.eventbus.setBMIHeart;
import com.vigorchip.omatreadmill.eventbus.setHeartHRC;
import com.vigorchip.omatreadmill.eventbus.setMainClickVisibility;
import com.vigorchip.omatreadmill.eventbus.setSerialRun;
import com.vigorchip.omatreadmill.eventbus.setSerialStop;
import com.vigorchip.omatreadmill.fragment.Fragment_Train_BMI;
import com.vigorchip.omatreadmill.receiver.WakeLockReceiver;
import com.vigorchip.omatreadmill.server.ServerWindows;
import com.vigorchip.omatreadmill.utils.CreatDialog;
import com.vigorchip.omatreadmill.utils.DoubleUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import android_serialport_api.SerialPort;

/**
 * Created by wr-app1 on 2018/2/26.
 */

public class newSerial {
    private SerialPort serialPort;
    private Context context;
    private InputStream inputStream;
    private OutputStream outputStream;

    //获取软件需要发送给下控实时的状态、速度、坡度的信息 需要赋值
    private int getStatus;
    private double getSpeed;
    private int getSlopes;
    public static int heart = 0;//心率值
    private long mySteps;//获取实时步数
    private boolean isFlag = true;//判断100s发送的读写流标志位
    public static int TREADMILL_STATUS = 0;//跑步机运行状态

    private AudioManager audioManager;  // 系统声音管理类

    public static int MACHINE_BATCH;//机器批次
    public static int MACHINE_STEP;//有无计步
    public static int MACHINE_BUZZER;//蜂鸣器

    private boolean ph_flag = false;//第一次进入app获取ph

    public void CreateSerial(Context context) {
        this.context = context;
        try {
            serialPort = new SerialPort(new File("/dev/ttyS2"), 9600);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            handler.postDelayed(runnable1, 100);
            audioManager = (AudioManager) isApplication.getInstance().getApplicationContext().getSystemService(Context.AUDIO_SERVICE); // 实例化系统声音管理类
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void set() {
//        Log.e("查看当前在哪个模式", String.valueOf(SportData.getStatus()));
        if (SportData.isRunning() && ServerWindows.isPause == false
                && WakeLockReceiver.isSCREEN == true) {
            getStatus = 1;
//            Log.e("串口状态监听1", "getStatus = 1" + "    ServerWindows.isPause = false");
        } else if (SportData.getStatus() == SportData.ERROR_STATUS
                && WakeLockReceiver.isSCREEN == true) {
            getStatus = 2;
//            Log.e("串口状态监听2", "getStatus = 2" + "    ERROR_STATUS");
        } else if (!SportData.isRunning() && SportData.getStatus() != SportData.ERROR_STATUS
                && ServerWindows.isPause == false && WakeLockReceiver.isSCREEN == true) {
            getStatus = 0;
//            Log.e("串口状态监听3", "getStatus = 0" + "    ServerWindows.isPause = false");
        } else if (SportData.isRunning() && ServerWindows.isPause == true
                && WakeLockReceiver.isSCREEN == true) {
            getStatus = 0;
//            Log.e("串口状态监听4", "getStatus = 0" + "    ServerWindows.isPause = true");
        } else if (WakeLockReceiver.isSCREEN == false) {
            getStatus = 3;
//            Log.e("串口状态监听5", "getStatus = 3-----------屏幕休眠");
        }
        if (isApplication.ph_selector == 2) {//如果是mph模式，發送給下控的數值需要轉成公制速度
            getSpeed = new BigDecimal((SportData.getSpeed() / 0.62)).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
//            Log.e("查看转换英制后的速度", String.valueOf(getSpeed));
        } else {
            getSpeed = SportData.getSpeed();
        }

        if (!ph_flag) {//第一次進入app確保獲取到最大速度
            if (isApplication.ph_selector == 1) {
                SportData.MINSPEED = 1.0;
                if (SportData.MAXSPEED == 8.1) {
                    SportData.MAXSPEED = 13.0;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 9.9) {
                    SportData.MAXSPEED = 16.0;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 11.2) {
                    SportData.MAXSPEED = 18.0;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 12.4) {
                    SportData.MAXSPEED = 20.0;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 13.6) {
                    SportData.MAXSPEED = 22.0;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 15.5) {
                    SportData.MAXSPEED = 25.0;
                    ph_flag = true;
                }
            } else if (isApplication.ph_selector == 2) {
                SportData.MINSPEED = 0.6;
                if (SportData.MAXSPEED == 13.0) {
                    SportData.MAXSPEED = 8.1;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 16.0) {
                    SportData.MAXSPEED = 9.9;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 18.0) {
                    SportData.MAXSPEED = 11.2;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 20.0) {
                    SportData.MAXSPEED = 12.4;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 22.0) {
                    SportData.MAXSPEED = 13.6;
                    ph_flag = true;
                } else if (SportData.MAXSPEED == 25.0) {
                    SportData.MAXSPEED = 15.5;
                    ph_flag = true;
                }
            }
        }
//        Log.e("查看最大速度", String.valueOf(SportData.MAXSPEED));
        getSlopes = SportData.getSlope();
    }

    Handler handler = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            if (isFlag) {
                write();
                isFlag = false;
            } else {
                getMsg(read());
                isFlag = true;
            }
            handler.postDelayed(runnable1, 60);
        }
    };

    private void write() {
        try {
            if (outputStream != null) {
                set();
                outputStream.write(setNewbyte((byte) getStatus, (byte) ((int) (getSpeed * 10) & 0xFF), (byte) getSlopes));
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] read() {
        try {
            if (inputStream != null) {
                int len = inputStream.available();
                byte[] buffer = new byte[len];
                if ((inputStream.read(buffer)) != -1) {
                    return buffer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //private int fla = 1;
    private int ERROR_COMM = 0;//通訊故障
    private int ERROR_NODRIVEN = 0;//電機未插
    private int ERROR_DRIVENNULL = 0;//电机过流
    private int ERROR_NOSPEED = 0;//無速度
    private int ERROR_LOCK = 0;//安全鎖
    private boolean flag_batch = true;//只获取一次机器批次
    private boolean flag_year = true;//只获取一次年份
    private boolean flag_buzzer = true;//只获取一次蜂鸣器
    private boolean flag_speed = true;//只获取一次速度

    public boolean getMsg(byte[] read) {
        if (read == null) {
            return false;
        }
        byte[] mbyte = read;

        if (mbyte == null && mbyte.length == 0) {
            return false;
        }

        if (mbyte != null && mbyte.length != 0 && mbyte.length == 20
                && mbyte[0] == (byte) 0x02 && mbyte[19] == (byte) 0x03) {
            byte[] new_mbyte = new byte[mbyte.length - 2];
            for (int i = 0; i < new_mbyte.length; i++) {
                //掐头去尾后new_mbyte为有效数据
                new_mbyte[i] = mbyte[i + 1];
            }

            if (flag_year) {
                Log.i("机器年份： ", new_mbyte[0] + "");
                flag_year = false;
            }

            if (new_mbyte[1] >= 0 && new_mbyte[1] <= 255 && flag_batch) {
                MACHINE_BATCH = getDecimalism(new_mbyte[1]);
                flag_batch = false;
                Log.i("机器批次： ", String.valueOf(MACHINE_BATCH));
            }

            if (calHeightBack(new_mbyte[5]) == (byte) 1) {
                if (flag_speed) {
                    flag_speed = false;
                    SportData.MAXSPEED = 13.0;
                }
//                Log.i("当前速度范围是： ", "1-13KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte) 2) {
                if (flag_speed) {
                    flag_speed = false;
                    SportData.MAXSPEED = 16.0;
                }
//                Log.i("当前速度范围是： ", "1-16KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte) 3) {
                if (flag_speed) {
                    flag_speed = false;
                    SportData.MAXSPEED = 18.0;
                }
//                Log.i("当前速度范围是： ", "1-18KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte) 4) {
                if (flag_speed) {
                    flag_speed = false;
                    SportData.MAXSPEED = 20.0;
                }
//                Log.i("当前速度范围是： ", "1-20KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte) 5) {
                if (flag_speed) {
                    flag_speed = false;
                    SportData.MAXSPEED = 22.0;
                }
//                Log.i("当前速度范围是： ", "1-22KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte) 6) {
                if (flag_speed) {
                    flag_speed = false;
                    SportData.MAXSPEED = 25.0;
                }
//                Log.i("当前速度范围是： ", "1-25KM");
            }

            if (calLowerBack(new_mbyte[5]) == (byte) 1) {
                if (SportData.MAXSLOPES != 12) {
                    SportData.MAXSLOPES = 12;
                }
//                Log.i("当前坡度范围是： ", "0-12KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte) 2) {
                if (SportData.MAXSLOPES != 15) {
                    SportData.MAXSLOPES = 15;
                }
//                Log.i("当前坡度范围是： ", "0-15KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte) 3) {
//                Log.i("当前坡度范围是： ", "0-20KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte) 4) {
//                Log.i("当前坡度范围是： ", "0-35KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte) 5) {
//                Log.i("当前坡度范围是： ", "-1-35KM");
            }

            if (calHeightBack(new_mbyte[6]) == (byte) 0) {
//                Log.i("当前为中文", "");
            } else if (calHeightBack(new_mbyte[6]) == (byte) 1) {
//                Log.i("当前为英文", "");
            } else if (calHeightBack(new_mbyte[6]) == (byte) 3) {
//                Log.i("当前为日文", "");
            } else if (calHeightBack(new_mbyte[6]) == (byte) 4) {
//                Log.i("当前为西班牙文", "");
            } else if (calHeightBack(new_mbyte[6]) == (byte) 5) {
//                Log.i("当前为俄罗斯文", "");
            }
            //将下标6转低四位再转成字节myBit数组 获取每个bit
            byte[] myBit = getBitArray(calLowerBack(new_mbyte[6]));
            if (myBit[0] == (byte) 1) {
//                if (SportData.RADIX != 2) {
//                    SportData.RADIX = 2;
//                }
//                Log.i("Bit-0为: " + myBit[0], "====英制");
            } else if (myBit[0] == (byte) 0) {
//                if (SportData.RADIX != 1) {
//                    SportData.RADIX = 1;
//                }
//                Log.i("Bit-0为: " + myBit[0], "====公制");
            }
            if (myBit[1] == (byte) 1) {
//                Log.i("Bit-1为: " + myBit[1], "====无HRC");
            } else if (myBit[1] == 0) {
//                Log.i("Bit-1为: " + myBit[1], "====有HRC");
            }
            if (myBit[2] == (byte) 1 && MACHINE_STEP != myBit[2]) {
                MACHINE_STEP = myBit[2];
                if (ServerWindows.service_steps != null) {
                    ServerWindows.service_steps.setVisibility(View.VISIBLE);
                }
//                Log.i("Bit-2为: " + myBit[2], "====带计步");
            } else if (myBit[2] == (byte) 0 && MACHINE_STEP != myBit[2]) {
                MACHINE_STEP = myBit[2];
                if (ServerWindows.service_steps != null) {
                    ServerWindows.service_steps.setVisibility(View.GONE);
                }
//                Log.i("Bit-2为: " + myBit[2], "====无计步");
            }

//            Log.i("查看new_mbyte[7]高四位", String.valueOf(calHeightBack(new_mbyte[7])));
            if (calHeightBack(new_mbyte[7]) == (byte) 0) {
                ERROR_COMM = 0;//通訊故障
                ERROR_NODRIVEN = 0;//電機未插
                ERROR_DRIVENNULL = 0;//电机过流
                ERROR_NOSPEED = 0;//無速度
                ERROR_LOCK = 0;//安全鎖
                if (CreatDialog.ErrorDialog != null) {
                    if (CreatDialog.ErrorDialog.isShowing()) {
                        if (SportData.getStatus() == SportData.ERROR_STATUS) {
                            EventBus.getDefault().postSticky(new SerialToHome(true));//发送true返回主頁面
                            SportData.setStatus(SportData.MAIN_ACTIVITY);
                        }
                        CreatDialog.ErrorDialog.dismiss();
                    }
//                    Log.e("跑步机无故障运行状态： ", "无故障");
                }
            } else if (calHeightBack(new_mbyte[7]) == (byte) 1) {
                if (ERROR_COMM == 0) {
                    ERROR_COMM = 1;
                    if (SportData.getStatus() == SportData.ERROR_STATUS) {
                        if (CreatDialog.ErrorDialog != null) {
                            if (CreatDialog.ErrorDialog.isShowing()) {
                                CreatDialog.ErrorDialog.dismiss();
                            }
                        }
                    }
                    CreatDialog.ErrorDialog(context, String.valueOf(context.getString(R.string.E01)));
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.error_alert);
                    if (SportData.isRunning()) {
                        ServerWindows.RunningEnd();
                        EventBus.getDefault().postSticky(new setSerialStop(true, true));//发送true true 表示发生故障停止跑步
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给主页面回初始状态
                    }
                }
                SportData.setStatus(SportData.ERROR_STATUS);
                Log.e("跑步机运行状态： ", "通讯故障");
            } else if (calHeightBack(new_mbyte[7]) == (byte) 2) {
                if (ERROR_NODRIVEN == 0) {
                    ERROR_NODRIVEN = 1;
                    if (SportData.getStatus() == SportData.ERROR_STATUS) {
                        if (CreatDialog.ErrorDialog != null) {
                            if (CreatDialog.ErrorDialog.isShowing()) {
                                CreatDialog.ErrorDialog.dismiss();
                            }
                        }
                    }
                    CreatDialog.ErrorDialog(context, String.valueOf(context.getString(R.string.E02)));
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.error_alert);
                    if (SportData.isRunning()) {
                        ServerWindows.RunningEnd();
                        EventBus.getDefault().postSticky(new setSerialStop(true, true));//发送true true 表示发生故障停止跑步
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给主页面回初始状态
                    }
                }
                SportData.setStatus(SportData.ERROR_STATUS);
                Log.e("跑步机运行状态： ", "电机尾插");
            } else if (calHeightBack(new_mbyte[7]) == (byte) 3) {
                if (ERROR_NOSPEED == 0) {
                    ERROR_NOSPEED = 1;
                    if (SportData.getStatus() == SportData.ERROR_STATUS) {
                        if (CreatDialog.ErrorDialog != null) {
                            if (CreatDialog.ErrorDialog.isShowing()) {
                                CreatDialog.ErrorDialog.dismiss();
                            }
                        }
                    }
                    CreatDialog.ErrorDialog(context, String.valueOf(context.getString(R.string.E03)));
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.error_alert);
                    if (SportData.isRunning()) {
                        ServerWindows.RunningEnd();
                        EventBus.getDefault().postSticky(new setSerialStop(true, true));//发送true true 表示发生故障停止跑步
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给主页面回初始状态
                    }
                }
                SportData.setStatus(SportData.ERROR_STATUS);
                Log.e("跑步机运行状态： ", "无速度信号");
            } else if (calHeightBack(new_mbyte[7]) == (byte) 5) {
                if (ERROR_DRIVENNULL == 0) {
                    ERROR_DRIVENNULL = 1;
                    if (SportData.getStatus() == SportData.ERROR_STATUS) {
                        if (CreatDialog.ErrorDialog != null) {
                            if (CreatDialog.ErrorDialog.isShowing()) {
                                CreatDialog.ErrorDialog.dismiss();
                            }
                        }
                    }
                    CreatDialog.ErrorDialog(context, String.valueOf(context.getString(R.string.E05)));
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.error_alert);
                    if (SportData.isRunning()) {
                        ServerWindows.RunningEnd();
                        EventBus.getDefault().postSticky(new setSerialStop(true, true));//发送true true 表示发生故障停止跑步
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给主页面回初始状态
                    }
                }
                SportData.setStatus(SportData.ERROR_STATUS);
                Log.e("跑步机运行状态： ", "电机过流");
            } else if (calHeightBack(new_mbyte[7]) == (byte) 7) {
                if (ERROR_LOCK == 0) {
                    ERROR_LOCK = 1;
                    if (SportData.getStatus() == SportData.ERROR_STATUS) {
                        if (CreatDialog.ErrorDialog != null) {
                            if (CreatDialog.ErrorDialog.isShowing()) {
                                CreatDialog.ErrorDialog.dismiss();
                            }
                        }
                    }
                    CreatDialog.ErrorDialog(context, String.valueOf(context.getString(R.string.E07)));
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.error_alert);
                    Log.e("跑步机运行状态ssssss： ", String.valueOf(SportData.getStatus()));
                    if (SportData.isRunning()) {
                        ServerWindows.RunningEnd();
                        EventBus.getDefault().postSticky(new setSerialStop(true, true));//发送true true 表示发生故障停止跑步
                        EventBus.getDefault().postSticky(new setMainClickVisibility(0));//发送给主页面回初始状态
                        Log.e("跑步机运行状态ssssss： ", "安锁脱落");
                    }
                }
                SportData.setStatus(SportData.ERROR_STATUS);
                Log.e("跑步机运行状态： ", "安锁脱落");
            }
//            fla = calHeightBack(new_mbyte[7]);
//            Log.i("查看new_mbyte[7]低四位", String.valueOf(calLowerBack(new_mbyte[7])));
            if (calLowerBack(new_mbyte[7]) == (byte) 0) {
//                Log.e("跑步机运行状态： ", "待机状态");
                TREADMILL_STATUS = 0;
            } else if (calLowerBack(new_mbyte[7]) == (byte) 1) {
//                Log.e("跑步机运行状态： ", "运行状态");
                TREADMILL_STATUS = 1;
            } else if (calLowerBack(new_mbyte[7]) == (byte) 2) {
//                Log.e("跑步机运行状态： ", "故障状态");
                TREADMILL_STATUS = 2;
            } else if (calLowerBack(new_mbyte[7]) == (byte) 3) {
//                Log.e("跑步机运行状态： ", "正在减速停机状态");
                TREADMILL_STATUS = 3;
            } else if (calLowerBack(new_mbyte[7]) == (byte) 4) {
                Log.e("跑步机运行状态： ", "休眠状态");
                TREADMILL_STATUS = 4;
            }

            if (new_mbyte[8] == (byte) 1) {
                if (!SportData.isRunning()) {
                    EventBus.getDefault().postSticky(new setSerialRun(true));//发送true  启动
                }
                Log.i("键值： ", "1启动");
            } else if (new_mbyte[8] == (byte) 2) {
                if (SportData.isRunning()) {
                    EventBus.getDefault().postSticky(new setSerialStop(true, false));//发送true false 表示未发生故障，停止键触发停止跑步
                } else if (!SportData.isRunning() && SportData.getStatus() != SportData.ERROR_STATUS
                        && SportData.getStatus() != SportData.START_TREADMILL_ANIM
                        && SportData.getStatus() != SportData.STOPPING_TREADMILL) {//如果不在这几个模式下，点击物理暂停键需要返回到首页
                    EventBus.getDefault().postSticky(new SerialToHome(true));//发送true返回主頁面
                }
                Log.i("键值： ", "2停止");
            } else if (new_mbyte[8] == (byte) 3) {
                Log.i("键值： ", "3确认");
            } else if (new_mbyte[8] == (byte) 4) {
                Log.i("键值： ", "4风扇");
            } else if (new_mbyte[8] == (byte) 5) {
                if (SportData.isRunning()) {
                    if (SportData.getSlope() != 15) {
                        SportData.setSlope(SportData.getSlope() + 1);
                    }
                }
                Log.i("键值： ", "5扬升加1");
            } else if (new_mbyte[8] == (byte) 6) {
                if (SportData.isRunning()) {
                    if (SportData.getSlope() != 0) {
                        SportData.setSlope(SportData.getSlope() - 1);
                    }
                }
                Log.i("键值： ", "6扬升减1");
            } else if (new_mbyte[8] == (byte) 7) {
                if (SportData.isRunning()) {
                    if (SportData.getSpeed() != 16.0) {
                        SportData.setSpeed(DoubleUtils.add(SportData.getSpeed(), 0.1));
                    }
                }
                Log.i("键值：  " + new_mbyte[8], " 速度加" + getSpeed);
            } else if (new_mbyte[8] == (byte) 8) {
                if (SportData.isRunning()) {
                    if (SportData.getSpeed() != 16.0) {
                        SportData.setSpeed(DoubleUtils.sub(SportData.getSpeed(), 0.1));
                    }
                }
                Log.i("键值： ", "8    速度减" + getSpeed);
            } else if (new_mbyte[8] == (byte) 9) {
                muteRelieve();
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.i("键值： ", "9    音量+" + "    当前音量： " + currentVolume);
            } else if (new_mbyte[8] == (byte) 10) {
                muteRelieve();
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.i("键值： ", "10    音量-" + "    当前音量： " + currentVolume);
            } else if (new_mbyte[8] == (byte) 11) {
                muteRelieve();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                Log.i("键值： ", "11静音");
            } else if (new_mbyte[8] == (byte) 12) {
                Log.i("键值： ", "12关机");
            } else if (new_mbyte[8] == (byte) 13) {
                Log.i("键值： ", "13待机休眠");
            } else if (new_mbyte[8] == (byte) 14) {
                Log.i("键值： ", "14程式");
            } else if (new_mbyte[8] == (byte) 15) {
                Log.i("键值： ", "15模式");
            } else if (new_mbyte[8] == (byte) 16) {
                Log.i("键值： ", "16测脂");
            }

            if (new_mbyte[9] >= (byte) 0 && new_mbyte[9] <= (byte) 255) {
                //获取速度控制数值
                int speedCount = getDecimalism(new_mbyte[9]);
                Log.i("获取速度控制数值： ", String.valueOf(speedCount));
            }

            if (new_mbyte[10] >= (byte) 0 && new_mbyte[10] <= (byte) 255) {
                //获取坡度控制数值
                int gradeCount = getDecimalism(new_mbyte[10]);
                Log.i("获取坡度控制数值： ", String.valueOf(gradeCount));
            }

            if (heart != new_mbyte[11]) {
                if (new_mbyte[11] >= 40) {
                    heart = new_mbyte[11];
                    ServerWindows.service_hrc.setText(String.valueOf(heart));
                }
                Log.i("获取心率值： ", String.valueOf(heart));
            }
            //获取心率值
            if (SportData.getStatus() == SportData.RUNNING_MODE_TRAINING_HEART) {
                if (heart != 0) {
                    EventBus.getDefault().postSticky(new setHeartHRC(true));
                } else {
                    EventBus.getDefault().postSticky(new setHeartHRC(false));
                }
            }

            if (Fragment_Train_BMI.hide == false) {//如果bmi界面显示状态
                if (Fragment_Train_BMI.pg_bmi != null &&
                        Fragment_Train_BMI.pg_bmi.getVisibility() == View.VISIBLE) {
                    if (heart != 0) {
                        EventBus.getDefault().postSticky(new setBMIHeart(true));
                    }
                }
            }

            //计算步数的变量mySteps
            if (new_mbyte[12] == (byte) 1) {
                ServerWindows.steps_layout.setVisibility(View.VISIBLE);
                mySteps = mySteps + 1;
                if (ServerWindows.service_steps != null) {
                    ServerWindows.service_steps.setText(String.valueOf(mySteps));
                }
                Log.i("当前步数为： ", String.valueOf(mySteps));
            } else if (new_mbyte[12] == (byte) 0) {
                if (ServerWindows.service_steps != null) {
                    ServerWindows.steps_layout.setVisibility(View.GONE);
                }
            }

            if (flag_buzzer) {
                MACHINE_BUZZER = new_mbyte[13];
                flag_buzzer = false;
                Log.i("蜂鸣器控制位： ", String.valueOf(MACHINE_BUZZER));
            }

            if (mbyte[18] == XorCheckOut(new_mbyte, new_mbyte.length)) {
//                Log.v("校验成功", "(*^▽^*)");
                return true;
            }
        }
//        Log.v("校验失败","￣へ￣");
        return false;
    }

    public void muteRelieve() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
        audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        audioManager.setStreamMute(AudioManager.STREAM_DTMF, false);
    }

    //计算高4位
    public static byte calHeightBack(byte mbyteCount) {
        byte calHeight = (byte) ((mbyteCount >> 4) & 0xF);
        return calHeight;
    }

    //计算低4位
    public static byte calLowerBack(byte mbyteCount) {
        byte calLower = (byte) (mbyteCount & 0x0F);
        return calLower;
    }

    //计算校验码，所有的有效字节进行异或
    public byte XorCheckOut(byte[] mbyte, int length) {
        byte XorCount = 0;
        for (int i = 0; i < length; i++) {
            XorCount = (byte) (XorCount ^ mbyte[i]);
        }
        return XorCount;
    }

    //将byte字符转换成String字符串
    public static String bytesToHex(byte src) {
        StringBuilder stringBuilder = new StringBuilder("");
        // 之所以用byte和0xff相与，是因为int是32位，与0xff相与后就舍弃前面的24位，只保留后8位
        String str = Integer.toHexString(src & 0xff);
        if (str.length() < 2) { //不足两位要补0
            stringBuilder.append(0);
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    //拼接一帧完整数据，用于TFT发送给下控
    public byte[] setNewbyte(byte status, byte speed, byte grade) {
        byte[] mbyte = new byte[]{status, speed, grade, 0x00, 0x00, 0x00, 0x00};//拼接有效数据
        byte[] newbyte = new byte[]{0x0B, status, speed, grade, 0x00, 0x00, 0x00, 0x00, XorCheckOut(mbyte, mbyte.length), 0x0D};
        return newbyte;
    }

    //将十六进制转成二进制方式的数组
    public static byte[] getBitArray(byte HexCount) {
        byte[] array = new byte[4];
        for (int i = 3; i >= 0; i--) {
            array[i] = (byte) (HexCount & 1);
            HexCount = (byte) (HexCount >> 1);
        }
        return array;
    }

    //将十六进制转成十进制
    public static int getDecimalism(byte HexCount) {
        String hex = String.valueOf(HexCount);
        Integer DEC = Integer.parseInt(hex, 16);
        return DEC;
    }

}