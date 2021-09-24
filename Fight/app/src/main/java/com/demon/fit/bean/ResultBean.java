package com.demon.fit.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "result_table")
public class ResultBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "time_stamp")
    public long timeStamp;

    @DatabaseField(columnName = "name")
    public String name;     // 名称

    @DatabaseField(columnName = "type")
    public String type;     // 类型：入手、出手

    @DatabaseField(columnName = "is_right")
    public boolean isRight; // 是否按要求执行

    @DatabaseField(columnName = "result")
    public int result;      // 造成的结果

}
