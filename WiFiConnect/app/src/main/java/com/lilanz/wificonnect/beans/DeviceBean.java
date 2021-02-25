package com.lilanz.wificonnect.beans;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.lilanz.wificonnect.data.electricfan.ElectricFan_IRData;
import com.lilanz.wificonnect.data.lamp.Lamp_OperateData;
import com.lilanz.wificonnect.data.myenum.BrandType;
import com.lilanz.wificonnect.data.myenum.DeviceType;
import com.lilanz.wificonnect.data.waterheater.WaterHeater_OperateData;

/**
 * 设备对象
 */
@DatabaseTable(tableName = "device_table")
public class DeviceBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "ip")
    public String ip;

    @DatabaseField(columnName = "port")
    public int port;

    /**
     * 灯
     * 风扇
     * 热水器
     * 电饭锅
     */
    @DatabaseField(columnName = "device_type")
    public DeviceType deviceType;   // 设备类型

    /**
     * GREE
     */
    @DatabaseField(columnName = "brand")
    public BrandType brand;    // 牌子

    /**
     * 开关式
     * 点击式
     */
    @DatabaseField(columnName = "control_type")
    public String controlType;  // 控制方式

    @DatabaseField(columnName = "open_setting")
    public String openSetting;  // 打开设置,不同电器需要不同的操作才能打开设备

    @DatabaseField(columnName = "device_position")
    public String devicePosition;   // 设备所在位置

    public String status;       // 设备状态

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


    public static final String STATUS_OPEN = "open";
    public static final String STATUS_CLOSE = "close";
    public static final String STATUS_QUERY = "query";

    public static final String CONTENT_SHAKE = "shake";

    /**
     * TODO 获取控制数据
     *
     * @param status  操作类型：打开、关闭、查询
     * @param content 操作内容：null、摇头
     * @return
     */
    public String getControlData(String status, String content) {
        String controlData = "";

        if (deviceType != null) {
            switch (deviceType) {
                case LAMP:
                    if (STATUS_OPEN.equals(status)) {
                        controlData = Lamp_OperateData.getOpenData();
                    } else if (STATUS_CLOSE.equals(status)) {
                        controlData = Lamp_OperateData.getCloseData();
                    } else if (STATUS_QUERY.equals(status)) {
                        controlData = Lamp_OperateData.getQueryData();
                    }
                    break;
                case WATER_HEATER:
                    if (STATUS_OPEN.equals(status)) {
                        controlData = WaterHeater_OperateData.getOpenData();
                    } else if (STATUS_CLOSE.equals(status)) {
                        controlData = WaterHeater_OperateData.getCloseData();
                    } else if (STATUS_QUERY.equals(status)) {
                        controlData = WaterHeater_OperateData.getQueryData();
                    }
                    break;
                case ELECTRIC_FAN:
//                    if (CONTENT_SHAKE.equals(content)) {
//                        controlData = ElectricFan_IRData.getInstance(brand).getShake() + "~";
//                    } else
                        if (STATUS_OPEN.equals(status)) {
                        controlData = ElectricFan_IRData.getInstance(brand).getOpenOrExchange() + "~";
                    } else if (STATUS_CLOSE.equals(status)) {
                        controlData = ElectricFan_IRData.getInstance(brand).getCloseData() + "~";
                    }
                    break;
            }
        }

        return controlData;
    }

}
