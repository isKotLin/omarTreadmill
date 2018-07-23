package com.vigorchip.omatreadmill.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

import com.vigorchip.omatreadmill.R;

/**
 * Created by wr-app1 on 2018/4/18.
 */

public class mSeekbar extends SeekBar {

    private Context context;

    private int ViewWidth;//view的宽
    private int ViewHeight;//view的高

    private Paint paint;
    private RectF rectf;//绘制范围
    private Bitmap thumb;



    public mSeekbar(Context context) {
        super(context);
        this.context = context;
    }

    public mSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setBackgroundResource(R.drawable.playground);
    }

    public mSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewWidth = getMeasuredWidth();
        ViewHeight = getMeasuredHeight();
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("getTop", String.valueOf(getTop()));
        Log.i("getLeft", String.valueOf(getLeft()));
        Log.i("getRight", String.valueOf(getRight()));
        Log.i("getBottom", String.valueOf(getBottom()));
        Log.i("getHeight", String.valueOf(ViewHeight));
        Log.i("getWeight", String.valueOf(ViewWidth));

        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        rectf = new RectF(getLeft()+35,getTop()+40,getRight()-35,getBottom()-40);
        canvas.drawRoundRect(rectf,getLeft()+160f,getTop()+405f,paint);


    }

}
