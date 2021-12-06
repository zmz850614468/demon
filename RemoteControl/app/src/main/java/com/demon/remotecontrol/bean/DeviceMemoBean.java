package com.demon.remotecontrol.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 设备备注对象
 */
@DatabaseTable(tableName = "device_memo_table")
public class DeviceMemoBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "device_id")
    public String deviceId;

    @DatabaseField(columnName = "device_memo")
    public String deviceMemo;

}
