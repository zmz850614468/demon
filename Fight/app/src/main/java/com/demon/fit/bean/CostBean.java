package com.demon.fit.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "cost_table")
public class CostBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "index")
    public int index;       // 用于排序用

    @DatabaseField(columnName = "name")
    public String name;     // 名称

    @DatabaseField(columnName = "cost")
    public int cost;    // 费用

    public float cost2;// 费用

    public CostBean() {
    }

    public CostBean(String name, float cost2) {
        this.name = name;
        this.cost2 = cost2;
    }
}
