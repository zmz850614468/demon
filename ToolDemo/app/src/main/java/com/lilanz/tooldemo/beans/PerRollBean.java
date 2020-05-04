package com.lilanz.tooldemo.beans;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * 每卷布匹的数据
 * 要与服务器数据进行对接:进行json解析
 */
@DatabaseTable(tableName = "perrollbean_table")
public class PerRollBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;      // 表主键，只用于数据库存储

    @DatabaseField(columnName = "gh")
    public String gh;           // 缸号
    @DatabaseField(columnName = "ph")
    public String ph;           // 卷号


    @ForeignCollectionField(eager = true)
    public ForeignCollection<FlawBean> flawBeanList;    // 只用于数据库数据  读 取
    public List<FlawBean> fabricFaultList;  // 疵点集合


    public void addFlaw(@NonNull FlawBean bean) {
        fabricFaultList.add(bean);
    }
}
