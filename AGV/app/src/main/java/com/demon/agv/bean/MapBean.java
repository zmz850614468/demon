package com.demon.agv.bean;

/**
 * 记录点位信息
 */
public class MapBean {

    public static final int TYPE_CAN = 1;
    public static final int TYPE_CANNOT = -1;
    public static final int TYPE_UNKNOW = 0;

    public static final int MAX_MARK = 5;   // 最大标记次数

    public int id;
    public int oriX;        // 激光发送点
    public int oriY;
    public String key;      // key: disX-disY
    public int disX;        // 激光目标点
    public int disY;
    public int markCount;   // 目标点标记次数
    public int type;

    public MapBean(String key, int oriX, int oriY, int disX, int disY) {
        this.key = key;
        this.oriX = oriX;
        this.oriY = oriY;
        this.disX = disX;
        this.disY = disY;
        markCount = 1;
    }

    /**
     * 添加标记数
     */
    public void addMarkCount() {
        markCount++;
        if (markCount >= MAX_MARK) {
            type = TYPE_CANNOT;
        }
    }

}
