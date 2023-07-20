package com.demo.fightdata.strategy;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略：锤子线 上吊线 操作
 * 适用：时K线，日K线
 * 买点：锤子线 上吊线
 * 卖点：
 * 结果优异的例子：
 */
public class Strategy_test_1 {

//    private static final int AMOUNT = 5;        // 锤子大小
//    private static final int AMOUNT_LENGTH = 5; // 锤柄长短

    /**
     * 买点：锤子线 上吊线
     * 1.上涨趋势的 锤子线
     * 2.下跌趋势的 上吊线
     * <p>
     * 卖点：
     *
     * @param list
     */
    public static List<TypeBean> getResult(List<TypeBean> list) {
        BeanUtil.calAvg(list);

        List<TypeBean> resultList = new ArrayList<>();
        boolean hasBuy = false;
        TypeBean buyBean = null;

        int size = list.size();
        TypeBean bean;

        for (int i = 29; i < size; i++) {
            bean = list.get(i);
            if (!hasBuy) {  // 未买入，找买点
//                if (bean.date.equals("2023-05-05")) {
//                    bean.dir = 0;
//                }

                // 1.判断是 锤子线 或 上吊线
//                if (Math.abs(bean.amount) <= AMOUNT && bean.high - bean.low >= 15) {
                if (Math.abs(bean.amount) * 5 <= bean.high - bean.low) {
                    if (bean.amount >= 0) {  // 底部，锤子线
                        if ((bean.high - bean.start) * 3 <= bean.high - bean.low) {
                            bean.dir = 1;
                        }
                    }
                    if (bean.amount <= 0) {  // 顶部，上吊线
                        if ((bean.start - bean.low) * 3 <= bean.high - bean.low) {
                            bean.dir = -1;
                        }
                    }
                }

                // 2.判断 趋势是否满足
                if (bean.dir == 1) { // 底部，锤子线, 下跌趋势
//                    if (bean.ma5 < bean.ma10 && bean.ma10 < bean.ma30 && bean.end < bean.ma5) {
                    if (trendNumber(list, -1, i) > 5 && bean.end < bean.ma5) {
                        buyBean = bean;
                        hasBuy = true;
                    }
                } else if (bean.dir == -1) {  // 顶部，上吊线，上涨趋势
//                        if (bean.ma5 > bean.ma10 && bean.ma10 > bean.ma30 && bean.end > bean.ma5) {
                    if (trendNumber(list, 1, i) > 5 && bean.end > bean.ma5) {
                        buyBean = bean;
                        hasBuy = true;
                    }
                }

            } else {

                // 测试卖点1：反趋势K线
                if (buyBean.dir > 0) {  //  上吊线,买上涨
                    if (bean.amount < 0) {
                        hasBuy = false;
                    }
                } else {     // 锤子线,买下跌
                    if (bean.amount > 0) {
                        hasBuy = false;
                    }
                }

                if (!hasBuy) {
                    i--;
                    resultList.add(buyBean);
                    resultList.add(bean);
                }
            }
        }

        print(resultList);
        return resultList;
    }

    /**
     * 趋势数量
     *
     * @param list
     * @param dir   趋势方向
     * @param index 当前位置
     * @return
     */
    private static int trendNumber(List<TypeBean> list, int dir, int index) {
        int number = 0;
        TypeBean bean;
        for (int i = index; i >= 0; i--) {
            bean = list.get(i);
            if (dir > 0) {  // 上升趋势
                if (bean.ma5 > bean.ma10 && bean.ma10 > bean.ma30) {
                    number++;
                } else {
                    break;
                }
            } else {         // 下跌趋势
                if (bean.ma5 < bean.ma10 && bean.ma10 < bean.ma30) {
                    number++;
                } else {
                    break;
                }
            }
        }

        return number;
    }

    /**
     * 打印数据
     *
     * @param resultList
     */
    private static void print(List<TypeBean> resultList) {
        TypeBean b;
        TypeBean s;
        int count = 0;
        for (int i = 1; i < resultList.size(); i += 2) {
            b = resultList.get(i - 1);
            s = resultList.get(i);
            System.out.println(String.format("%s -- %s  %2d -- %5d --%5d %4d", b.date, s.date, b.dir, b.end, s.end, (s.end - b.end) * b.dir));
            count += (s.end - b.end) * b.dir;
        }
        System.out.println(resultList.size() / 2 + " -- " + count);
    }
}

