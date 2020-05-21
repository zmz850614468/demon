package com.lilanz.tooldemo.beans;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 每卷布匹的数据
 * 要与服务器数据进行对接:进行json解析
 */
@DatabaseTable(tableName = "teacher_table")
public class TeacherBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;      // 表主键，只用于数据库存储

    @DatabaseField(columnName = "name")
    public String name;

    @DatabaseField(columnName = "age")
    public String age;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<StudentBean> studentList;

}
