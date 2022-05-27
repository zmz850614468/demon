package com.demon.fit.bean;

/**
 * 当天操作结果类
 */
public class OperateTodayBean {

    public OperateTodayBean() {
        inType= "买入";
    }

    /**
     * 买入
     * 卖入
     */
    public String inType;

    public String name;

    public int inPrice;

    public int outPrice;

    public int result;

}
