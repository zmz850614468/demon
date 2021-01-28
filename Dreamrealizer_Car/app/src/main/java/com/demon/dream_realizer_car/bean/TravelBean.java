package com.demon.dream_realizer_car.bean;

import com.demon.dream_realizer_car.view.TravelView;

/**
 * 轨迹对象
 */
public class TravelBean {

    public static final int PAI_COUNT = 4096;                       // 一圈需要的拍数
//    public static final float CAR_WIDTH = 9.65f;                     // 两个车轮的距离(cm)
    public static final float CAR_WIDTH = 13.4f;                     // 两个车轮的距离(cm) 13.7
    public static final float CAR_TYRE_PERIMETER = 3.14f * 6.75f;   // 轮胎周长(cm)
    public static final float CAR_TYRE_CIRCLE = CAR_WIDTH * 3.14f;  // 小车自转周长(cm)
    private static final float R_DISTANCE = 1.5f * CAR_WIDTH;        // 弧线运动的半径

    public static int scale = 10;          // 缩放
    public int upOrDown;
    public int leftOrRight;
    public long paiCount;
    public boolean isUpdate;

    public float startDirection;            // 开始方向

    /**
     * bean.upOrDown == 0 && bean.leftOrRight != 0
     * 原地拐弯：获取改变的角度大小
     *
     * @return
     */
    public float getChangeDirection() {
        // 单个轮胎走过的距离
        float distance = getDistance() / scale;
        float dir = distance / CAR_TYRE_CIRCLE * 360;

        return dir * leftOrRight;
    }

    public static float getRadius() {
        return R_DISTANCE * scale;
    }

    /**
     * 行进时拐弯：获取改变的角度大小
     *
     * @return
     */
    public float getCircleAngle() {
        return getDistance2() / (6.28f * getRadius()) * 360;
    }

    public String getTravelData() {
        return upOrDown + ":" + leftOrRight + ":" + paiCount + ":travel\n\r";
    }

    /**
     * 获取运行的长度
     * 设置缩放倍数
     *
     * @return
     */
    public float getDistance() {
//        return 0.75f * paiCount * 1.0f / PAI_COUNT * CAR_TYRE_PERIMETER * scale;
        return paiCount * 1.0f / PAI_COUNT * CAR_TYRE_PERIMETER * scale;
    }

    /**
     * 获取运行的长度
     * 设置缩放倍数
     *
     * @return
     */
    public float getDistance2() {
        return 0.75f * paiCount * 1.0f / PAI_COUNT * CAR_TYRE_PERIMETER * scale;
//        return paiCount * 1.0f / PAI_COUNT * CAR_TYRE_PERIMETER * scale;
    }

}
