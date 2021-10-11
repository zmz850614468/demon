package com.lilanz.foodie.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 分类
 */
@DatabaseTable(tableName = "food_type_table")
public class FoodTypeBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "order")
    public int order;   // 排序

    public int count;   // 总共有几个

}
