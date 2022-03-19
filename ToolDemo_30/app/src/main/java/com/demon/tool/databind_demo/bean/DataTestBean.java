package com.demon.tool.databind_demo.bean;

public class DataTestBean {

    public int id;

    public String name;

    public DataTestBean() {
    }

    public DataTestBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
