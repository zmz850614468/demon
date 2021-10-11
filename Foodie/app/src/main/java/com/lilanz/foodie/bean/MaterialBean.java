package com.lilanz.foodie.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 具体数据
 */
@DatabaseTable(tableName = "material_table")
public class MaterialBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "food_name")
    public String foodName;     // 对应的菜单名称

    /**
     * 描述类型:材料、步骤、备注
     */
    public static final String DES_TYPE_MATERIAL = "material";
    public static final String DES_TYPE_STEP = "step";
    public static final String DES_TYPE_MEMO = "memo";
    @DatabaseField(columnName = "des_type")
    public String desType;

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "number")
    public double number;

    @DatabaseField(columnName = "unit")
    public String unit;     // 单位

    /**
     * 用于记录 步骤、备注 信息
     */
    @DatabaseField(columnName = "des")
    public String des;

    @DatabaseField(columnName = "order_id")
    public int orderId;

    /**
     * 用于记录 材料、步骤 的排序
     */
    public int typeOrder;

}