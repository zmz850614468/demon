package com.lilanz.wificonnect.data.electricfan;

import com.lilanz.wificonnect.data.myenum.BrandType;
import com.lilanz.wificonnect.data.myenum.DeviceType;

/**
 * 风扇接口类
 */
public abstract class ElectricFan_IRData {

    public abstract String getCloseData();

    public abstract String getOpenOrExchange();

    public abstract String getWindType();

    public abstract String getShake();

    public abstract String getTiming();

    public static ElectricFan_IRData getInstance(BrandType brandType) {
        ElectricFan_IRData data = null;

        switch (brandType) {
            case GREE:
                data = new GREE_ElectricFan_IRData();
                break;
            case CAMEL:
                data = new CAMEL_ElectricFan_IRData();
                break;
        }

        return data;
    }
}
