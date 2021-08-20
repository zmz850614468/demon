package com.demon.agv.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.demon.agv.bean.MapBean;

import java.util.ArrayList;
import java.util.List;

public class PointView extends View {

    //    private List<Point> pointList = new ArrayList<>();
    private List<MapBean> mapBeanList = new ArrayList<>();
    private Paint myPaint;
    private Paint pointPaint;

    private int centenX;                // 坐标轴中心X
    private int centerY;                // 坐标轴中心Y

    public PointView(Context context) {
        super(context);
        initData();
    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

//    public synchronized void addPoint(List<Point> list) {
//        if (pointList.size() > 30000) {
//            pointList.clear();
//        }
//
//        pointList.addAll(list);
//    }

    public synchronized void resetPoint() {
//        pointList.clear();
        mapBeanList.clear();
    }

    public synchronized void addPoint(List<MapBean> list) {
        mapBeanList.clear();
        mapBeanList.addAll(list);
//        if (pointList.size() > 30000) {
//            pointList.clear();
//        }
//
//        pointList.addAll(list);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
        //如果是确切大小，直接赋值
        if (widthMode == MeasureSpec.EXACTLY) {
            centenX = widthSize / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            centerY = heightSize / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        synchronized (this) {
            for (MapBean bean : mapBeanList) {
                canvas.drawPoint(bean.disX + centenX, bean.disY + centerY, myPaint);
            }
        }
//        synchronized (this) {
//            for (Point point : pointList) {
//                if (point != null) {
//                    canvas.drawPoint(centenX + point.x, centerY + point.y, myPaint);
//                }
//            }
//        }

        canvas.drawPoint(centenX, centerY, pointPaint);
    }

    private void initData() {
        myPaint = new Paint();
        myPaint.setColor(Color.RED);
        myPaint.setStrokeWidth(5f);

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLACK);
        pointPaint.setStrokeWidth(3f);
    }
}
