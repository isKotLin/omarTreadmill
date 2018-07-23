package com.vigorchip.omatreadmill.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.vigorchip.omatreadmill.utils.ScreenUtils;

import java.text.DecimalFormat;

/**
 * Created by wr-app2 on 2018/5/7.
 */

public class SpeedView extends View {
    private int width;
    private int heigth;
    private Context context;
    private Paint mPaint_X;//X坐标轴画笔
    private Paint mPaint_Y;//Y坐标轴画笔
    private Paint mPaint_Text;//字体画笔
    private Paint mPaint_Rec;//矩形画笔
    private int data[];//柱状图显示的数据
    private int itemWidth;//柱状图的宽度
    private int itemHeigth;//柱状图没个刻度打高度
    private boolean isClick;//判断是否按下
    private int number = 10;
    public SpeedView(Context context) {
        super(context);
        this.context=context;
        init();
    }
    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    public void setDaata(int [] a){
        this.data = a;
        this.invalidate();
    }

    public void setMaxSpeend(int a){
        Log.e("查看傳入的數字", String.valueOf(a));
        int d = 16 -a;
        if (d<0){
            this.number = 10+d;
            this.invalidate();
        }else if (d>0){
            this.number =10+d;
            this.invalidate();
        }
    }

    private void init() {
        width = ScreenUtils.getScreenWidth(context);
        heigth = ScreenUtils.getScreenHeight(context);
        mPaint_X = new Paint();
        mPaint_Y = new Paint();
        mPaint_Text=new Paint();
        mPaint_Rec = new Paint();
        mPaint_X.setColor(Color.WHITE);
        mPaint_Y.setColor(Color.WHITE);
        mPaint_Text.setColor(Color.BLACK);
        mPaint_Rec.setColor(Color.parseColor("#9ACD32"));
        mPaint_X.setStrokeWidth(1.5f);
        mPaint_X.setAntiAlias(true);
        mPaint_X.setDither(true);
        mPaint_Y.setStrokeWidth(1.5f);
        mPaint_Y.setAntiAlias(true);
        mPaint_Y.setDither(true);
        mPaint_Rec.setDither(true);
        mPaint_Rec.setAntiAlias(true);
        mPaint_Text.setDither(true);
        mPaint_Text.setAntiAlias(true);
        mPaint_Text.setTextSize(8);
    }
    //提供返回速度的方法
    public int[] getdata(){
        return this.data;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画XY
        drawLine(canvas);
        //画刻度
        drawCoordinate(canvas);
        //画柱状
        drawBar(canvas);

    }
    public void UpdataRec(int [] a ){
        for (int i = 0; i < data.length; i++) {
            this.data[i] = a[i];
        }
        this.invalidate();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getY();

        switch (event.getAction()){
            //按下的时候
            case MotionEvent.ACTION_DOWN:
                getitem(x, y);
                break;
            //移动中
            case MotionEvent.ACTION_MOVE:
                if (isClick){
                    getitem(x, y);
                }
                break;
            //手指抬起时
            case MotionEvent.ACTION_UP:
                isClick=false;
                break;
        }
        return true;
    }

    private void getitem(float x, float y) {
        float leftx;
        float rightx;
        for (int a = 1; a<11; a++){
            leftx =10*a+ width*3/8+itemWidth*(a-1);
            rightx = 10*a+width*3/8+itemWidth*a;
            if (x<leftx){
                continue;
            }
            if (leftx<=x&&x<=rightx) {
                isClick=true;
                if (y>heigth/5*4-itemHeigth||y<heigth/5+number*itemHeigth){//最大速度不能超过16
                    break;
                } else {
                    double d =(heigth/5*4-y)/itemHeigth;
                    int num = DoubleFormatInt(d);
                    data[a-1] = num;
                    UpdataRec(data);
                }
            }
        }

    }

    private void drawBar(Canvas canvas) {
        RectF re = null;
        itemWidth = (width*7/8-width*3/8-100)/10;
        int w = width*3/8+10+itemWidth;
        int s =10;
        for (int a=0;a<10;a++){
            if (data!=null) {
                re = new RectF(width*3/8 + s, heigth / 5 * 4 - data[a] * itemHeigth, w, heigth / 5 * 4);
                canvas.drawText(String.valueOf(Double.parseDouble(String.valueOf(data[a]))), width * 3/8 + s + itemWidth / 2-10, heigth / 5 * 4 - data[a] * itemHeigth - 5, mPaint_Text);
            }else {
                data = new int[10];
                for (int da =0;da<10;da++ ){
                    data[da] =1;
                }
                re = new RectF(width*3/8 + s, heigth / 5 * 4 -  itemHeigth, w, heigth / 5 * 4);
                canvas.drawText(1.0 + "", width*3/8 + s + itemWidth / 2-10, heigth / 5 * 4 -  itemHeigth - 5, mPaint_Text);
            }
            s = s+10+itemWidth;
            w =w+10+itemWidth;
            canvas.drawRect(re,mPaint_Rec);
        }
    }

    private void drawCoordinate(Canvas canvas) {
        int h =heigth/5*4;
        itemHeigth  = heigth/5*3/25;
        for (int a=0;a<26;a++){
            if (a<10) {
                canvas.drawText(a + "", width*3/8 - 12, h, mPaint_Text);
                h = h - itemHeigth;
            }else {
                canvas.drawText(a + "", width*3/8 - 15, h, mPaint_Text);
                h = h - itemHeigth;
            }
        }
    }

    private void drawLine(Canvas canvas) {
        canvas.drawLine(width*3/8,heigth/5*4,width*3/8,heigth/5,mPaint_X);
        canvas.drawLine(width*3/8,heigth/5*4,width*7/8,heigth/5*4,mPaint_Y);
    }


    private int DoubleFormatInt(Double dou){
        DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
        return Integer.parseInt(df.format(dou));
    }
}
