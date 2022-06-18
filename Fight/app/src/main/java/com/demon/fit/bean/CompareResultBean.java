package com.demon.fit.bean;

public class CompareResultBean {
    public CompareResultBean() {
    }

    public CompareResultBean(String name) {
        this.name = name;
    }

    public String name;
    public int posCount;
    public int negCount;
    public int totalCount;
    public int badCount;
    public int result;

    public float badPercent;    // 糟糕占比
    public float percent;       // 胜率

    public void calculatePercent(){
        badPercent = badCount * 100.0f / totalCount;
        percent = posCount * 100.0f / totalCount;
    }
}
