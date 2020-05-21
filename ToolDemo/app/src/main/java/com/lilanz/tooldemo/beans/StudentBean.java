package com.lilanz.tooldemo.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 疵点类
 * 要与服务器数据进行对接:进行json解析
 */
@DatabaseTable(tableName = "flawtable")
public class StudentBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;      // 表主键

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "age")
    public String age;

    @DatabaseField(columnName = "sex")
    public String sex;

    // 只用于数据库存储，关联主表的主键
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "juan_id")
    public TeacherBean perRollBean;

   
}
