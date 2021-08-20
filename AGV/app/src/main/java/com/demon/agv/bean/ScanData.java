package com.demon.agv.bean;


import android.util.Log;

public class ScanData {

    public final int quality;
    public final boolean startBitSet;
    public final double angle;
    public final double distance;
    public final boolean isStart;       // 是否是一圈新的开始

    public ScanData(int b0, int b1, int b2, int b3, int b4) {
        isStart = (b0 & 0x01) == 0x01 && (b0 & 0x02) == 0x00;
        quality = b0 >> 2;
        startBitSet = (b1 & 0x1) == 0x1;
        angle = ((b1 >> 1) + (b2 << 7)) / 64;
        distance = (b3 + (b4 << 8)) / 4 / 10;
    }
}

