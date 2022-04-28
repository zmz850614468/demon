package com.demon.accountmanagement.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "account_table")
public class AccountBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(columnName = "group_name")
    public String groupName;        // 分组名称

    @DatabaseField(columnName = "is_just_group")
    public boolean isJustGroup;     // 分组信息(则没有以下具体信息) 或 组内信息

    @DatabaseField(columnName = "name")
    public String name;             // 备注名称

    @DatabaseField(columnName = "account")
    public String account;          // 账号名

    @DatabaseField(columnName = "pwd")
    public String pwd;              // 密码

    @DatabaseField(columnName = "memo")
    public String memo;             // 备注 - 预留


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isJustGroup() {
        return isJustGroup;
    }

    public void setJustGroup(boolean justGroup) {
        isJustGroup = justGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
