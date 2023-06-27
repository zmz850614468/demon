package com.demon.fit;

import java.util.ArrayList;
import java.util.List;

public class BeanUtil {

    /**
     * 解析数据对象
     *
     * @param str
     * @return
     */
    public static List<Bean> parse(String str) {
        List<Bean> list = new ArrayList<>();
        String[] strArr = str.split("\n");
        for (String s : strArr) {
            Bean bean = new Bean();
            if (bean.setData(s)) {
                list.add(bean);
            }
        }
        calAvg(list);
        return list;
    }

    /**
     * 计算均值数据
     *
     * @param list
     */
    private static void calAvg(List<Bean> list) {
        int size = list.size();
        Bean bean;
        int ma5 = 0;
        int ma10 = 0;
        int ma30 = 0;
        for (int i = 0; i < size; i++) {
            bean = list.get(i);
            ma5 += bean.end;
            ma10 += bean.end;
            ma30 += bean.end;
            if (i >= 4) {
                if (i - 5 >= 0) {
                    ma5 -= list.get(i - 5).end;
                }
                bean.ma5 = ma5 / 5;
            }
            if (i >= 9) {
                if (i - 10 >= 0) {
                    ma10 -= list.get(i - 10).end;
                }
                bean.ma10 = ma10 / 10;
            }
            if (i >= 29) {
                if (i - 30 >= 0) {
                    ma30 -= list.get(i - 30).end;
                }
                bean.ma30 = ma30 / 30;
            }
        }
    }

}
