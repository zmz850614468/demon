package com.lilanz.wificonnect.beans;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
    public String deviceType;   // 设备类型

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
}
