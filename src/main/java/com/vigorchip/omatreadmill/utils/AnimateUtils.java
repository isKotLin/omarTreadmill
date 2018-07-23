package com.vigorchip.omatreadmill.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.vigorchip.omatreadmill.R;

/**
 * Created by wr-app1 on 2018/3/27.
 */

public class AnimateUtils {

    public static void setClickAnimation(final Context context, View v) {
        v.clearAnimation();
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.custom_button_down));
                        break;
                    case MotionEvent.ACTION_UP:
                        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.custom_button_up));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
