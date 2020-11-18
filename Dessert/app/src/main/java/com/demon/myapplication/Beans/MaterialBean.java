package com.demon.myapplication.Beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "material_table")
public class MaterialBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "type")
    public String type;     // 对应的配方名称

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "number")
    public double number;

    @DatabaseField(columnName = "unit")
    public String unit;     // 单位

    @DatabaseField(columnName = "order_id")
    public int orderId;

    public String group;    // 对应的组

}