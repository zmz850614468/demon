package com.lilanz.wificonnect.data.electricfan;

/**
 * 风扇接口类
 */
public abstract class ElectricFan_IRData {

    public abstract String getCloseData();

    public abstract String getOpenOrExchange();

    public abstract String getWindType();

    public abstract String getShake();

    public abstract String getTiming();
}
