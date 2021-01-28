package com.demon.dream_realizer_car.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;

import com.demon.dream_realizer_car.bean.PointBean;
import com.demon.dream_realizer_car.bean.TravelBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravelView extends View {

    private Paint xyPaint;              // x,y轴画笔
    private Paint routePaint;           // 已保存的轨迹笔
    private Paint travelPaint;          // 轨迹画笔
    private Paint directionPaint;       // 方向画笔
    private int centenX;                // 坐标轴中心X
    private int centerY;                // 坐标轴中心Y

    private float direction;            // 当前方向（0~360）
    private float currentX;             // 当前位置X
    private float currentY;             // 当前位置Y

    private List<TravelBean> travelList;    // 轨迹路线数据
    private List<TravelBean> routeList;     // 已保存的轨迹路线图

    public TravelView(Context context) {
        super(context);
    }

    public TravelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        travelList = new ArrayList<>();
        routeList = new ArrayList<>();
        direction = 90;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    private void initPaint() {
        //初始化虚线圆圈
        xyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xyPaint.setColor(Color.BLACK);
        xyPaint.setStrokeWidth(3);
        xyPaint.setStyle(Paint.Style.STROKE);
        xyPaint.setStrokeCap(Paint.Cap.ROUND);

        travelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        travelPaint.setColor(Color.GREEN);
        travelPaint.setStrokeWidth(3);
        travelPaint.setStyle(Paint.Style.STROKE);
        travelPaint.setStrokeCap(Paint.Cap.ROUND);

        directionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        directionPaint.setColor(Color.RED);
        directionPaint.setStrokeWidth(3);
        directionPaint.setStyle(Paint.Style.STROKE);
        directionPaint.setStrokeCap(Paint.Cap.ROUND);

        routePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        routePaint.setColor(Color.BLUE);
        routePaint.setStrokeWidth(3);
        routePaint.setStyle(Paint.Style.STROKE);
        routePaint.setStrokeCap(Paint.Cap.ROUND);
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

        // 绘制x，y轴
        canvas.drawLine(0, centerY + dirY, centenX * 2, centerY + dirY, xyPaint);
        canvas.drawLine(centenX + dirX, 0, centenX + dirX, centerY * 2, xyPaint);

        drawTravel(canvas, routePaint, routeList);
        synchronized (this) {
            drawTravel(canvas, travelPaint, travelList);
        }
//        currentX = centenX + dirX;
//        currentY = centerY + dirY;
//        direction = 90;
//
//        // 绘制已保存的轨迹图
//        for (TravelBean bean : routeList) {
//            if (bean.upOrDown == 0 && bean.leftOrRight != 0) {      // 直线运动
//                direction += bean.getChangeDirection();
//                direction = direction % 360;
//                if (direction < 0) {
//                    direction += 360;
//                }
//            } else if (bean.upOrDown != 0 && bean.leftOrRight == 0) {   // 原地转动
//                drawLine(canvas, bean, routePaint);
//            } else if (bean.upOrDown != 0 && bean.leftOrRight != 0) {  // 弧线运动
//                drawArc(canvas, bean, routePaint);
//            }
//        }

//        currentX = centenX + dirX;
//        currentY = centerY + dirY;
//        direction = 90;
//
//        // 绘制当前轨迹图
//        for (TravelBean bean : travelList) {
//            if (bean.upOrDown == 0 && bean.leftOrRight != 0) {      // 直线运动
//                direction += bean.getChangeDirection();
//                direction = direction % 360;
//                if (direction < 0) {
//                    direction += 360;
//                }
//            } else if (bean.upOrDown != 0 && bean.leftOrRight == 0) {   // 原地转动
//                drawLine(canvas, bean, travelPaint);
//            } else if (bean.upOrDown != 0 && bean.leftOrRight != 0) {  // 弧线运动
//                drawArc(canvas, bean, travelPaint);
//            }
//        }

        drawDirection(canvas);

//        canvas.drawArc(currentX, currentY, currentX + 300, currentY + 300,
//                45, 90, true, travelPaint);
    }

    private void drawTravel(Canvas canvas, Paint paint, List<TravelBean> list) {
        currentX = centenX + dirX;
        currentY = centerY + dirY;
        direction = 90;

        // 绘制已保存的轨迹图
        for (TravelBean bean : list) {
            if (bean.upOrDown == 0 && bean.leftOrRight != 0) {         // 原地转动
                direction += bean.getChangeDirection();
                direction = direction % 360;
                if (direction < 0) {
                    direction += 360;
                }
            } else if (bean.upOrDown != 0 && bean.leftOrRight == 0) {  // 直线运动
                drawLine(canvas, bean, paint);
            } else if (bean.upOrDown != 0 && bean.leftOrRight != 0) {  // 弧线运动
                drawArc(canvas, bean, paint);
            }
        }
    }

    /**
     * 绘制当前方向
     *
     * @param canvas
     */
    private void drawDirection(Canvas canvas) {
        double a = Math.toRadians(direction);
        float desY = (float) (20 * Math.sin(a));
        float desX = (float) (20 * Math.cos(a));
        desY *= -1;

        canvas.drawLine(currentX, currentY, currentX + desX, currentY + desY, directionPaint);
    }

    /**
     * 绘制直线
     */
    private void drawLine(Canvas canvas, TravelBean bean, Paint paint) {
        float distance = bean.getDistance();
        double a = Math.toRadians(direction);
        int desX = (int) (distance * Math.cos(a)) * bean.upOrDown;
        int desY = (int) -(distance * Math.sin(a)) * bean.upOrDown;

        canvas.drawLine(currentX, currentY, currentX + desX, currentY + desY, paint);
        currentX += desX;
        currentY += desY;
    }

    /**
     * 绘制弧线
     */
    private void drawArc(Canvas canvas, TravelBean bean, Paint paint) {
        bean.startDirection = direction;
        float curDirection = direction;
        if (bean.leftOrRight == 1) {
            curDirection += 90;
        } else {
            curDirection -= 90;
        }

        curDirection = curDirection % 360;
        if (curDirection < 0) {
            curDirection += 360;
        }

        float distance = TravelBean.getRadius();
        double a = Math.toRadians(curDirection);
        float desX = (float) (distance * Math.cos(a));
        float desY = (float) -(distance * Math.sin(a));

        // 圆心
        float circleX = currentX + desX;
        float circleY = currentY + desY;
        float disAngle = bean.getCircleAngle();

        if (bean.upOrDown == 1 && bean.leftOrRight == -1) { // 右前转
            float startAngle = 360 - direction - 90;
            canvas.drawArc(circleX - distance, circleY - distance, circleX + distance, circleY + distance,
                    startAngle, disAngle, false, paint);
            direction -= disAngle;

            float disDir = direction + 90;
            PointBean pointBean = getDis(circleX, circleY, disDir, distance);
            currentX = pointBean.x;
            currentY = pointBean.y;
        } else if (bean.upOrDown == 1 && bean.leftOrRight == 1) {   // 左前转
            float startAngle = 360 - direction + 90;
            canvas.drawArc(circleX - distance, circleY - distance, circleX + distance, circleY + distance,
                    startAngle - disAngle, disAngle, false, paint);

            direction += disAngle;

            float disDir = direction - 90;
            PointBean pointBean = getDis(circleX, circleY, disDir, distance);
            currentX = pointBean.x;
            currentY = pointBean.y;
        } else if (bean.upOrDown == -1 && bean.leftOrRight == -1) {     // 右后转
            float startAngle = 360 - direction - 90;
            canvas.drawArc(circleX - distance, circleY - distance, circleX + distance, circleY + distance,
                    startAngle - disAngle, disAngle, false, paint);

            direction += disAngle;
            float disDir = direction + 90;
            PointBean pointBean = getDis(circleX, circleY, disDir, distance);
            currentX = pointBean.x;
            currentY = pointBean.y;
        } else if (bean.upOrDown == -1 && bean.leftOrRight == 1) {      // 左后转
            float startAngle = 360 - direction + 90;
            canvas.drawArc(circleX - distance, circleY - distance, circleX + distance, circleY + distance,
                    startAngle, disAngle, false, paint);

            direction -= disAngle;
            float disDir = direction - 90;
            PointBean pointBean = getDis(circleX, circleY, disDir, distance);
            currentX = pointBean.x;
            currentY = pointBean.y;
        }
    }

    /**
     * 返回目的坐标
     *
     * @param startX
     * @param startY
     * @param direction
     * @param distance
     * @return
     */
    private PointBean getDis(float startX, float startY, float direction, float distance) {
        double a = Math.toRadians(direction);
        float desX = (float) (distance * Math.cos(a));
        float desY = (float) -(distance * Math.sin(a));

        return new PointBean(startX + desX, startY + desY);
    }

    /**
     * 重新绘制
     */
    public void reset() {
        routeList.clear();
        travelList.clear();
        dirX = 0;
        dirY = 0;
        currentX = 0;
        currentY = 0;
        invalidate();
    }

    /**
     * 添加轨迹数据
     *
     * @param bean
     * @param isUpdate // 更新用的数据，不是最终数据(之后要被删除的)
     */
    public synchronized void addTravel(TravelBean bean, boolean isUpdate) {
        if (!travelList.isEmpty()) {
            int size = travelList.size();
            TravelBean tempBean = travelList.get(size - 1);
            if (tempBean.isUpdate) {
                travelList.remove(tempBean);
            }
        }
        travelList.add(bean);
        invalidate();
    }

    int lastX, lastY;
    int dirX, dirY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                // 图形随手指滑动
                if (Math.abs(offsetX) > 30 || Math.abs(offsetY) > 30) {
                    dirX += offsetX;
                    dirY += offsetY;
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                dirX += x - lastX;
                dirY += y - lastY;
                invalidate();
                break;
        }

        return true;
    }

    public void setRouteList(List<TravelBean> routeList) {
        reset();
        this.routeList.addAll(routeList);
    }

    public List<TravelBean> getRouteList() {
        return routeList;
    }

    public List<TravelBean> getTravelList() {
        return travelList;
    }
}
