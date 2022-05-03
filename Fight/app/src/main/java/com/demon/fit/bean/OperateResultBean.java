package com.demon.fit.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 每天的操作结果类
 */
@DatabaseTable(tableName = "operate_result_table")
public class OperateResultBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "time_stamp")
    public long timeStamp;  // 时间

    @DatabaseField(columnName = "result")
    public int result;      // 造成的结果

    @DatabaseField(columnName = "pos_count")
    public int posCount;    // 正操作数

    @DatabaseField(columnName = "neg_count")
    public int negCount;    // 负操作数

    @DatabaseField(columnName = "poundage")
    public int poundage;    // 手续费用
}
