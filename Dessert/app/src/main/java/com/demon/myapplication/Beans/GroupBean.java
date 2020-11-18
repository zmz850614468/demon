package com.demon.myapplication.Beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "group_table")
public class GroupBean {


    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "group_name")
    public String groupName;

    @DatabaseField(columnName = "pei_fang_name")
    public String peiFangName;

    @DatabaseField(columnName = "order_id")
    public int orderId;

}
