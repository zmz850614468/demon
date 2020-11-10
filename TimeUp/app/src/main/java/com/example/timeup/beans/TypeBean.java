package com.example.timeup.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "type_table")
public class TypeBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "during")
    public int during;

    @DatabaseField(columnName = "order_id")
    public int orderId;

    public boolean canRing = true;     // 是否可以播放提示音

    public TypeBean() {
    }

    public TypeBean(String name, int during) {
        this.name = name;
        this.during = during;
    }

    public String getSecond() {
        int min = during / 60;
        int second = during % 60;
        if (during < 0) {
            min = Math.abs(min);
            second = Math.abs(second);
            return String.format("-%d:%02d", min, second);
        }

        return String.format("%d:%02d", min, second);
    }

    public void setCanRing(boolean canRing) {
        this.canRing = canRing;
    }
}
