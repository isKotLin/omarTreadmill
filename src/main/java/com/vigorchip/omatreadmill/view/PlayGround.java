package com.vigorchip.omatreadmill.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vigorchip.omatreadmill.R;

/**
 * Created by wr-app1 on 2018/4/17.
 */

public class PlayGround extends View {

    private Context context;
    private int width;//view的宽
    private int height;//view的高
    private int maxProgress = 800;//seekbar总值
    private int progress;//当前的进度值
    private int start = 180;//thumb起始位置
    private int angle = 0;//移动后的角度
    private Bitmap thumb;//thumb点

    private float left;//矩形图层左边边距
    private float right;//矩形图层右边边距
    private float top;//矩形图层顶端边距
    private float bottom;//矩形图层底边边距
    private float rx;//生成圆角矩形的x轴半径
    private float ry;//生成圆角矩形的y轴半径

    private float thumbX;//thumb X坐标
    private float thumbY;//thumb Y坐标
    private float index_x;
    private float index_y;//T标记的seekbar当前位置的Y坐标，预设置值为12点位置

    private Paint paint;
    private RectF rectf;//绘制范围

    /** seekbar变化监听回调 */
//    private OnSeekChangeListener mListener;

    public PlayGround(Context context) {
        super(context);
        this.context = context;
        initDrawable();
    }

    public PlayGround(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initDrawable();
        setBackgroundResource(R.drawable.playground);
    }

    public PlayGround(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
       // initDrawable();
    }

    private void initDrawable() {
        thumb = BitmapFactory.decodeResource(this.context.getResources(),
                R.drawable.thumb);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        width = getMeasuredWidth();
//        height = getMeasuredHeight();
        Log.i("getTop", String.valueOf(getTop()));
        Log.i("getLeft", String.valueOf(getLeft()));
        Log.i("getRight", String.valueOf(getRight()));
        Log.i("getBottom", String.valueOf(getBottom()));
        Log.i("getHeight", String.valueOf(width));
        Log.i("getWeight", String.valueOf(height));

//        left = getLeft()+30;
//        top = getTop()+30;
//        right = getRight()-30;
//        bottom = getBottom()-30;
//        rx = getLeft()+160f;//生成圆角矩形的x轴半径
//        ry = getTop()+405f;//生成圆角矩形的y轴半径

//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setStrokeWidth(2);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.BLACK);
//
//        rectf = new RectF(left,top,right,bottom);//矩形


    }


    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawRoundRect(rectf,rx,ry,paint);
        super.onDraw(canvas);

    }


//    public void setSeekBarChangeListener(OnSeekChangeListener listener) {
//        mListener = listener;
//    }
//
//    public void setProgress(int progress) {
//        this.progress = progress;
//        mListener.onProgressChange(this, this.getProgress());
//    }

    public int getProgress() {
        return progress;
    }
}
