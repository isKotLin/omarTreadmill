package com.vigorchip.omatreadmill.start_treadmill_anim;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.vigorchip.omatreadmill.R;
import com.vigorchip.omatreadmill.activity.MainActivity;
import com.vigorchip.omatreadmill.application.isApplication;
import com.vigorchip.omatreadmill.bean.SportData;
import com.vigorchip.omatreadmill.eventbus.setMainClickVisibility;
import com.vigorchip.omatreadmill.eventbus.setTitle;
import com.vigorchip.omatreadmill.eventbus.setTitle_Run2;
import com.vigorchip.omatreadmill.eventbus.set_TitleRun4;
import com.vigorchip.omatreadmill.server.ServerWindows;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wr-app1 on 2018/4/20.
 * 运动开始动画，并设置跳转到运动界面
 */

public class Start_Treadmill {
    private static Animation animation1;
    private static Animation animation2;
    private static Animation animation3;
    private static Animation animation4;
    private static Animation animation5;
    private static Animation animation6;
    public static Dialog dialog;

    /**
     * 参数说明：
     * context：上下文
     * fragment：当前调用此方法的碎片
     * oldIndex：当前页面下标
     * newIndex：需要跳转到的页面下标
     * title：运动模式的标题
     * mode：设置当前的运动模式
     * flag：设置当前是跳转到哪种运动界面
     * time: 时间模式设置的时间
     * distance: 距离模式设置的时间
     * cal: 热量模式设置的时间
     */
    public static void AnimDialog(final Context context, final Fragment fragment,
                                  final int oldIndex, final int newIndex, final String title,
                                  final int mode, final int flag, final String time,
                                  final String distance, final String cal) {
        if (SportData.getStatus() != SportData.START_TREADMILL_ANIM) {
            dialog = new Dialog(context, R.style.CommentStyle);
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false; //默认返回 false
                    }
                }
            });
            SportData.setStatus(SportData.START_TREADMILL_ANIM);
            dialog.show();
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.start_animation);
            final ImageView Treadmill_StartRun = window.findViewById(R.id.Treadmill_StartRun);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.FILL_PARENT;
            window.setAttributes(lp);
            animation1 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation2 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation3 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation4 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation5 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation6 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            Treadmill_StartRun.startAnimation(animation1);
            isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_4);
                    Treadmill_StartRun.startAnimation(animation2);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_3);
                    Treadmill_StartRun.startAnimation(animation3);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_2);
                    Treadmill_StartRun.startAnimation(animation4);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation4.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_1);
                    Treadmill_StartRun.startAnimation(animation5);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation5.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_go);
                    Treadmill_StartRun.startAnimation(animation6);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation6.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (SportData.getStatus() != SportData.ERROR_STATUS) {
                        ((MainActivity) fragment.getActivity()).showFragment(oldIndex, newIndex);
                        //1 分开主页面启动键  0显示主页面启动键
                        EventBus.getDefault().postSticky(new setMainClickVisibility(1));
                        if (flag == 1) {//发送当前的模式标题和当前模式到运动界面1
                            EventBus.getDefault().postSticky(new setTitle(title, mode, time, distance, cal));
                        }
                        isApplication.Start_SystemTime = System.currentTimeMillis();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    //训练模式开始动画
    public static void AnimDialog2(final Context context, final Fragment fragment,
                                   final int oldIndex, final int newIndex, final String title,
                                   final int mode, final String time, final double[] speed,
                                   final double[] slope) {
        if (SportData.getStatus() != SportData.START_TREADMILL_ANIM) {
            dialog = new Dialog(context, R.style.CommentStyle);
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false; //默认返回 false
                    }
                }
            });
            SportData.setStatus(SportData.START_TREADMILL_ANIM);
            dialog.show();
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.start_animation);
            final ImageView Treadmill_StartRun = window.findViewById(R.id.Treadmill_StartRun);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.FILL_PARENT;
            window.setAttributes(lp);
            animation1 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation2 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation3 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation4 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation5 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation6 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            Treadmill_StartRun.startAnimation(animation1);
            isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_4);
                    Treadmill_StartRun.startAnimation(animation2);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_3);
                    Treadmill_StartRun.startAnimation(animation3);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_2);
                    Treadmill_StartRun.startAnimation(animation4);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation4.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_1);
                    Treadmill_StartRun.startAnimation(animation5);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation5.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_go);
                    Treadmill_StartRun.startAnimation(animation6);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation6.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (SportData.getStatus() != SportData.ERROR_STATUS) {
                        ((MainActivity) fragment.getActivity()).showFragment(oldIndex, newIndex);
                        //1 分开主页面启动键  0显示主页面启动键
                        EventBus.getDefault().postSticky(new setMainClickVisibility(1));
                        EventBus.getDefault().postSticky(new setTitle_Run2(title, mode, time, speed, slope));
                        isApplication.Start_SystemTime = System.currentTimeMillis();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }


    //心率模式开始动画
    public static void AnimDialog4(final Context context, final Fragment fragment,
                                   final String age, final String hrc, final String min) {
        if (SportData.getStatus() != SportData.START_TREADMILL_ANIM) {
            dialog = new Dialog(context, R.style.CommentStyle);
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false; //默认返回 false
                    }
                }
            });
            SportData.setStatus(SportData.START_TREADMILL_ANIM);
            dialog.show();
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setGravity(Gravity.CENTER);
            window.setContentView(R.layout.start_animation);
            final ImageView Treadmill_StartRun = window.findViewById(R.id.Treadmill_StartRun);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.FILL_PARENT;
            lp.height = WindowManager.LayoutParams.FILL_PARENT;
            window.setAttributes(lp);
            animation1 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation2 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation3 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation4 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation5 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            animation6 = AnimationUtils.loadAnimation(context, R.anim.treadmill_start);
            Treadmill_StartRun.startAnimation(animation1);
            isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_4);
                    Treadmill_StartRun.startAnimation(animation2);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_3);
                    Treadmill_StartRun.startAnimation(animation3);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_2);
                    Treadmill_StartRun.startAnimation(animation4);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation4.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_1);
                    Treadmill_StartRun.startAnimation(animation5);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation5.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Treadmill_StartRun.setVisibility(View.GONE);
                    Treadmill_StartRun.setImageResource(R.drawable.count_go);
                    Treadmill_StartRun.startAnimation(animation6);
                    isApplication.setMediaPlayer(isApplication.getInstance().getApplicationContext(),R.raw.alert);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation6.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (SportData.getStatus() != SportData.ERROR_STATUS) {
                        ((MainActivity) fragment.getActivity()).showFragment(20, 22);
//                        ServerWindows.CHECK_HRC6 = 0;
                        //1 分开主页面启动键  0显示主页面启动键
                        EventBus.getDefault().postSticky(new setMainClickVisibility(1));
                        EventBus.getDefault().postSticky(new set_TitleRun4(age, hrc, min));
                        isApplication.Start_SystemTime = System.currentTimeMillis();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
}
