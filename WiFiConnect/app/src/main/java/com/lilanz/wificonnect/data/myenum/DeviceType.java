package com.lilanz.wificonnect.data.myenum;


import androidx.annotation.NonNull;

import com.lilanz.wificonnect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备类型
 */
public enum DeviceType {
    LAMP("灯"),                 // 灯
    ELECTRIC_FAN("电风扇"),     // 电风扇
    WATER_HEATER("热水器"),     // 热水器
    AIR_CONDITION("空调"),      // 空调
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

    public static int getImgResoure(DeviceType deviceType) {
        int resouce = 0;
        switch (deviceType) {
            case LAMP:
                resouce = R.mipmap.lamp;
                break;
            case ELECTRIC_FAN:
                resouce = R.mipmap.electric_fans;
                break;
            case WATER_HEATER:
                resouce = R.mipmap.water_heater;
                break;
            case AIR_CONDITION:
                resouce = R.mipmap.air_condition;
                break;
//            case "电饭锅":
//                holder.ivPic.setBackgroundResource(R.mipmap.electric_pot);
//                break;
        }
        return resouce;
    }
}
