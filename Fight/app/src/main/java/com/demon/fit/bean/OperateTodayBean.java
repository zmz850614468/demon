package com.demon.fit.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 当天操作结果类
 */
@DatabaseTable(tableName = "operate_today_table")
public class OperateTodayBean {

    public OperateTodayBean() {
        inType = "买入";
        price = 10;
        isFollow = false;
        isBadOperate = false;
    }

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "create_time")
    public long createTime;

    /**
     * 买入
     * 卖入
     */
    @DatabaseField(columnName = "in_type")
    public String inType;

    @DatabaseField(columnName = "is_follow")
    public boolean isFollow;    // 是否是跟随单

    @DatabaseField(columnName = "name")
    public String name;

    /**
     * 参数类型
     * 5
     * 10   默认参数
     * 15
     * 20
     */
    @DatabaseField(columnName = "price")
    public int price;   // 单价

    @DatabaseField(columnName = "is_bad_operate")
    public boolean isBadOperate;    // 是否是糟糕的操作

    @DatabaseField(columnName = "in_price")
    public int inPrice;

    @DatabaseField(columnName = "out_price")
    public int outPrice;

    @DatabaseField(columnName = "result")
    public int result;

}
