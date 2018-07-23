package com.vigorchip.omatreadmill.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vigorchip.omatreadmill.bean.SportData;

import java.text.DecimalFormat;

public class HistoggramView extends View {
    /**
     * 声明画笔
     */
    private Paint mPaint_X;//X坐标轴画笔
    private Paint mPaint_Y;//Y坐标轴画笔
    private Paint mPaint_InsideLine;//内部虚线P
    private Paint mPaint_Text;//字体画笔
    private Paint mPaint_Rec;//矩形画笔
    private Paint mPaint_Line;
    private Paint mPaint_back;
    private OnChartClickListener listener;
    DecimalFormat format = new DecimalFormat("#0.0");
    DecimalFormat formats = new DecimalFormat("#0");
    private double[] dataRec = new double[16];//矩形数据
    private int width;//视图的宽高
    private int height;
    private String[] mText_Y = new String[]{"16", "15", "14", "13", "12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0"}; //坐标轴数据
    private String[] mText_X = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    int index;

    public HistoggramView(Context context) {
        super(context);
        init();
    }

    public HistoggramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 更新数据
     */
    public void upDataRec(double[] data, boolean type) {
        for (int i = 0; i < mText_X.length; i++) {
            this.dataRec[i] = data[i];
        }
        isSpeed = type;
        mPaint_Rec.setColor(Color.parseColor("#9ACD32"));
        this.invalidate();//更新视图
    }

    boolean isSpeed;

    /**
     * 更新数据
     */
    public void upDataRec(int[] data, boolean type) {
        for (int i = 0; i < mText_X.length; i++) {
            this.dataRec[i] = data[i];
        }
        isSpeed = type;
        mPaint_Rec.setColor(Color.YELLOW);
        this.invalidate();//更新视图
    }

    /**
     * 初始化画笔
     */
    private void init() {
        mPaint_X = new Paint();
        mPaint_InsideLine = new Paint();
        mPaint_Text = new Paint();
        mPaint_Rec = new Paint();
        mPaint_Y = new Paint();
        mPaint_Line = new Paint();
        mPaint_back = new Paint();
        mPaint_X.setColor(Color.DKGRAY);
        mPaint_X.setStrokeWidth(3);
        mPaint_X.setAntiAlias(true);
        mPaint_X.setDither(true);

        mPaint_Y.setColor(Color.GRAY);
        mPaint_Y.setStrokeWidth(3);
        mPaint_Y.setAntiAlias(true);
        mPaint_Y.setDither(true);

        mPaint_InsideLine.setColor(Color.LTGRAY);
        mPaint_InsideLine.setAntiAlias(true);
        mPaint_InsideLine.setDither(true);

        mPaint_Text.setTextSize(14);
        mPaint_Text.setTextAlign(Paint.Align.CENTER);
        mPaint_Text.setColor(Color.parseColor("#000000"));
        mPaint_Text.setDither(true);
        mPaint_Text.setAntiAlias(true);

        mPaint_Rec.setColor(Color.YELLOW);
        mPaint_Rec.setDither(true);
        mPaint_Rec.setAntiAlias(true);

        mPaint_Line.setColor(Color.RED);
        mPaint_Line.setStrokeWidth(3);
        mPaint_Line.setDither(true);
        mPaint_Line.setAntiAlias(true);

        mPaint_back.setColor(Color.GRAY);
        mPaint_back.setAlpha(40);

    }

    int leftHeight_Every;
    int downWeight_Every;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        leftHeight_Every = height / (int) (isSpeed ? SportData.MAXSPEED + 1.5 : SportData.MAXSLOPES + 1); //Y轴每个数据的间距
        downWeight_Every = (width / mText_X.length - 2);//X轴每个数据的间距
        if (dataRec != null && dataRec.length > 0) {
            for (int i = 1; i < dataRec.length + 1; i++) {//画矩形
                RectF rectBack = new RectF();
                rectBack.left = downWeight_Every * i;
                rectBack.right = 35 + downWeight_Every * i;
                rectBack.top = (float) (height - ((isSpeed ? SportData.MAXSPEED : SportData.MAXSLOPES) * leftHeight_Every));
                rectBack.bottom = height;
                canvas.drawRoundRect(rectBack, 0, 0, mPaint_back);

                RectF rect = new RectF();
                rect.left = downWeight_Every * i;
                rect.right = 35 + downWeight_Every * i;
                rect.top = (float) (height - (dataRec[i - 1] * leftHeight_Every));
                rect.bottom = height;
                canvas.drawRoundRect(rect, 0, 0, mPaint_Rec);

                if (isSpeed)
                    canvas.drawText(format.format(dataRec[i - 1]), 20 + downWeight_Every * i, (height - (int) (dataRec[i - 1] * leftHeight_Every)) - 1, mPaint_Text);
                else
                    canvas.drawText(formats.format(dataRec[i - 1]), 20 + downWeight_Every * i, (height - (int) (dataRec[i - 1] * leftHeight_Every)) - 1, mPaint_Text);
            }
        }
    }

    boolean isClick;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float leftx;
        float rightx;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 1; i < mText_X.length + 1; i++) {
                    leftx = 5 + downWeight_Every * i;
                    rightx = 35 + downWeight_Every * i;
                    if (x < leftx)
                        continue;
                    if (leftx <= x && x <= rightx) {
                        float top = 0;
                        float bottom = height;
                        if (y >= top && y <= bottom) {
                            if (listener != null) { //判断是否设置监听,将点击的第几条柱子,点击柱子顶部的坐值
                                top = (float) ((height - y) * (isSpeed ? SportData.MAXSPEED : SportData.MAXSLOPES) * 10 / height);
                                if (i <= 16 && i >= 1) {
                                    isClick = true;
                                    listener.onClick(i - 1, top / 9);
                                }
                                index = i - 1;
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isClick) {
                    float top = 0;
                    float bottom = height ;
                    if (y >= top && y <= bottom) {
                        if (listener != null) { //判断是否设置监听,将点击的第几条柱子,点击柱子顶部的坐值
                            Log.e("TAGS", top + "                       " + y);
                            top = (float) ((height - y) * (isSpeed ? SportData.MAXSPEED : SportData.MAXSLOPES) * 10 / height);
                            if (index <= 15 && index >= 0)
                                listener.onClick(index, top / 9);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isClick = false;
                index = -1;
                break;
        }
        return true;
    }

    /**
     * 柱子点击时的监听接口
     */
    public interface OnChartClickListener {
        void onClick(int num, float y);
    }

    /**
     * 设置柱子点击监听的方法
     *
     * @param listener
     */
    public void setOnChartClickListener(OnChartClickListener listener) {
        this.listener = listener;
    }
}