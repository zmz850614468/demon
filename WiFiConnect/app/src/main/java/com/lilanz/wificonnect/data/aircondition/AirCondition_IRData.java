package com.lilanz.wificonnect.data.aircondition;

/**
 * 空调接口类
 */
public abstract class AirCondition_IRData {

    public abstract String getCloseData();

    public abstract String getOpenData();

    public abstract String getWindType();

    public abstract String getTiming();

    public abstract String getTemperature(int temperature);
}
