package com.lilanz.wificonnect.data.myenum;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备类型
 */
public enum DeviceType {
    LAMP("灯"),                 // 灯
    ELECTRIC_FAN("电风扇"),     // 电风扇
//    HEAT_WATER("热水器"),            // 热水器
//    ELECTRIC_POT("电饭锅"),         // 电饭锅
    ;
    public String name;

    DeviceType(String name) {
        this.name = name;
    }

    /**
     * 获取所有设备名
     *
     * @return
     */
    public static List<String> getDeviceName() {
        List<String> list = new ArrayList<>();
        for (DeviceType value : DeviceType.values()) {
            list.add(value.name);
        }

        return list;
    }

    /**
     * 通过设备名称获取设备类型
     *
     * @param deviceName
     * @return
     */
    public static DeviceType getDeviceType(@NonNull String deviceName) {
        for (DeviceType value : DeviceType.values()) {
            if (deviceName.equals(value.name)) {
                return value;
            }
        }
        return null;
    }
}
