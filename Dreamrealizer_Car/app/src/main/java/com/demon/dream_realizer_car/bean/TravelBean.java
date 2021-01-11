package com.demon.dream_realizer_car.bean;

import com.demon.dream_realizer_car.view.TravelView;

/**
 * 轨迹对象
 */
public class TravelBean {

    public static final int PAI_COUNT = 4096;                       // 一圈需要的拍数
    public static final float CAR_WIDTH = 10;                       // 两个车轮的距离(cm)
    public static final float CAR_TYRE_PERIMETER = 3.14f * 6.7f;    // 轮胎周长(cm)
    public static final float CAR_TYRE_CIRCLE = CAR_WIDTH * 3.14f;  // 小车自转周长(cm)

    public static int scale = 10;          // 缩放
    public int upOrDown;
    public int leftOrRight;
    public long paiCount;
    public boolean isUpdate;

    /**
     * bean.upOrDown == 0 && bean.leftOrRight != 0
     * 获取改变的角度大小
     *
     * @return
     */
    public float getChangeDirection() {
        // 单个轮胎走过的距离
        float distance = getDistance() / scale;
        float dir = distance / CAR_TYRE_CIRCLE * 360;

        return dir * leftOrRight;
    }

    /**
     * 获取运行的长度
     * 设置缩放倍数
     *
     * @return
     */
    public float getDistance() {
        return paiCount * 1.0f / PAI_COUNT * CAR_TYRE_PERIMETER * scale;
    }

}
