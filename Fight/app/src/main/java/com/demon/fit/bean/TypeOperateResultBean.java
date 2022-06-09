//package com.demon.fit.bean;
//
//import com.demon.fit.util.StringUtil;
//import com.j256.ormlite.field.DatabaseField;
//import com.j256.ormlite.table.DatabaseTable;
//
///**
// * 针对品种，记录操作结果
// */
//@DatabaseTable(tableName = "type_operate_result_table")
//public class TypeOperateResultBean {
//
//    @DatabaseField(columnName = "id", generatedId = true)
//    public int id;
//
//    @DatabaseField(columnName = "type_name")
//    public String typeName;
//
//    /**
//     * 买入
//     * 卖入
//     */
//    @DatabaseField(columnName = "in_type")
//    public String inType;
//
//    @DatabaseField(columnName = "in_price")
//    public int inPrice;
//
//    @DatabaseField(columnName = "out_price")
//    public int outPrice;
//
//    @DatabaseField(columnName = "operate_result")
//    public int operateResult;
//
//}
