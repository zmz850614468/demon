package com.lilanz.tooldemo.beans;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 疵点类
 * 要与服务器数据进行对接:进行json解析
 */
@DatabaseTable(tableName = "flawtable")
public class FlawBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;      // 表主键
    @DatabaseField(columnName = "flawpos")
    public String cdwz; // 疵点位置
    @DatabaseField(columnName = "flawname")
    public String cdmc; // 疵点名称
    @DatabaseField(columnName = "flawgoal")
    public String cdfs; // 疵点分数
    @DatabaseField(columnName = "flawpicture")
    public String cdtp; // 疵点图片地址

    // 只用于数据库存储，关联主表的主键
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "juan_id")
    public PerRollBean perRollBean;

   
}
