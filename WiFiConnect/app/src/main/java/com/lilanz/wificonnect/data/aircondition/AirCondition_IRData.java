package com.lilanz.wificonnect.data.aircondition;

import com.lilanz.wificonnect.data.myenum.BrandType;

import java.util.List;

/**
 * 空调接口类
 */
public abstract class AirCondition_IRData {

    public abstract String getCloseData();

    public abstract String getOpenData();

    public abstract String getWindType();

    public abstract String getTiming();

    public abstract String getTemperature(int temperature);

    public abstract List<String> getTemperatureList();

    public abstract List<String> getModeList();

    public abstract List<String> getWindSpeedList();

    public static AirCondition_IRData getInstance(BrandType brandType) {
        AirCondition_IRData airCondition_irData = null;
        switch (brandType) {
            case MEDIA:
                airCondition_irData = new Media_AirCondition_IRData();
                break;
        }
        return airCondition_irData;
    }
}
