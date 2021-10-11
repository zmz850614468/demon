package com.lilanz.foodie.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 */
@DatabaseTable(tableName = "food_table")
public class FoodBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "food_type")
    public String foodType;

    @DatabaseField(columnName = "order")
    public int order;   // 排序

    public int count;   // 总共有几个

}
