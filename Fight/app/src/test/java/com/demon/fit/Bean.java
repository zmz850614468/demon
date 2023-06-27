package com.demon.fit;

// 名称,代码,日期,开盘,收盘,最高,最低,成交量,成交额
public class Bean {

    public String name;
    public String dm;
    public String date;
    public int start;
    public int end;
    public int high;
    public int low;
    public int number;
    public int ma5;  // 均线
    public int ma10; // 均线
    public int ma30; // 均线

    /**
     * 菜油309,OI309,2023-04-03,8573,8628,8768,8558,173861,15052885504,2.46,0.99,85.0,0.0
     *
     * @param data
     * @return
     */
    public boolean setData(String data) {
        String[] strArr = data.split(",");
        if (strArr.length < 8) {
            return false;
        }
        name = strArr[0];
        dm = strArr[1];
        date = strArr[2];
        start = Integer.parseInt(strArr[3]);
        end = Integer.parseInt(strArr[4]);
        high = Integer.parseInt(strArr[5]);
        low = Integer.parseInt(strArr[6]);
        number = Integer.parseInt(strArr[7]);

        return true;
    }

}


























