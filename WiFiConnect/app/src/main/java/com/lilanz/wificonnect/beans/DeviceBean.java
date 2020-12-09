package com.lilanz.wificonnect.beans;

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

    @DatabaseField(columnName = "device_type")
    public String deviceType;   // 设备类型

    @DatabaseField(columnName = "control_type")
    public String controlType;  // 控制方式

    @DatabaseField(columnName = "open_setting")
    public String openSetting;  // 打开设置,不同电器需要不同的操作才能打开设备
}
