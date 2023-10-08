package com.demo.fightdata.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// 名称,代码,日期,开盘,收盘,最高,最低,成交量,成交额
// 名称,代码,日期,开盘,收盘,最高,最低,成交量,成交额,振幅,涨跌幅,涨跌额,换手率
@DatabaseTable(tableName = "type_bean_table")
public class TypeBean {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;
    @DatabaseField(columnName = "date")
    public String date;
    @DatabaseField(columnName = "name")
    public String name;
    @DatabaseField(columnName = "dm")
    public String dm;
    @DatabaseField(columnName = "start")
    public int start;
    @DatabaseField(columnName = "end")
    public int end;
    @DatabaseField(columnName = "high")
    public int high;
    @DatabaseField(columnName = "low")
    public int low;
    @DatabaseField(columnName = "number")
    public int number;  // 交易量
    @DatabaseField(columnName = "amplitude")
    public float amplitude; // 振幅
    @DatabaseField(columnName = "amount")
    public float amount;  // 涨跌额
    @DatabaseField(columnName = "ma5")
    public int ma5;  // 均线
    @DatabaseField(columnName = "ma10")
    public int ma10; // 均线
    @DatabaseField(columnName = "ma30")
    public int ma30; // 均线

    public int dir;
    public boolean isCondition_1;   // 是否满足条件1
    public boolean isCondition_2;   // 是否满足条件2
    public String memo;


//    @DatabaseField(columnName = "macd")
//    public float macd;  // (diff - dea)*2
//    @DatabaseField(columnName = "diff")
//    public float diff;  // 12日均线 - 26日均线
//    @DatabaseField(columnName = "dea")
//    public float dea;   // dif的9日均线值
//    public float ema12;
//    public float ema26;

    /**
     * 菜油309,OI309,2023-04-03,8573,8628,8768,8558,173861,15052885504,2.46,0.99,85.0,0.0
     *
     * @param data
     * @return
     */
    public boolean setData(String data) {
//        while (data.contains("  ")) {
//            data.replace("  ", " ");
//        }

        String[] strArr = data.split("\t");
        if (strArr.length < 7) {
            return false;
        }
//        name = strArr[0];
//        dm = strArr[1];
        date = strArr[0] + "-" + strArr[1];
        if (strArr[2].contains(".")) {
            start = (int) (Float.parseFloat(strArr[2]) * 10);
            end = (int) (Float.parseFloat(strArr[5]) * 10);
            high = (int) (Float.parseFloat(strArr[3]) * 10);
            low = (int) (Float.parseFloat(strArr[4]) * 10);
        } else {
            start = Integer.parseInt(strArr[2]);
            end = Integer.parseInt(strArr[5]);
            high = Integer.parseInt(strArr[3]);
            low = Integer.parseInt(strArr[4]);
        }
        number = Integer.parseInt(strArr[6]);
//        amplitude = Float.parseFloat(strArr[7]);
//        amount = Float.parseFloat(strArr[10]);
        amount = end - start;
        return true;
    }

    /**
     * 相关数据相加
     *
     * @param bean
     */
    public void add(TypeBean bean) {
        if (start == 0) {
            start = bean.start;
            name = bean.name;
            dm = bean.dm;
            low = bean.low;
        }

        date = bean.date;
        end = bean.end;
        high = Math.max(high, bean.high);
        low = Math.min(low, bean.low);
        number += bean.number;
        amount = end - start;
    }
}


//12 26 9 ma























