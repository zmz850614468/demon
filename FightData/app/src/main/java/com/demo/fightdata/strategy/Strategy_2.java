package com.demo.fightdata.strategy;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.util.BeanUtil;
import com.demo.fightdata.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 策略：中线操作
 * 适用：10分钟K线 ...
 * 买点：在趋势方向上 + 交易量增加
 * 卖点：ma5均线反向穿过ma30 且 k线涨跌与趋势反向
 * 结果优异的例子：
 * 棕榈油10分组K线
 */
public class Strategy_2 {

    /**
     * 买点：在趋势方向上 + 交易量增加
     * 1.去除前面四个中最大的一个交易量
     * 2.当前交易量大于前面的另外三个 且 大于这三个的均值300以上
     * 3.涨-ma5大于ma30      跌-ma5小于ma30
     * <p>
     * 卖点：ma5均线反向穿过ma30 且 k线涨跌与趋势反向
     *
     * @param list
     */
    public static List<TypeBean> getResult(List<TypeBean> list) {
        BeanUtil.calAvg(list);

        List<TypeBean> resultList = new ArrayList<>();
        boolean hasBuy = false;
        TypeBean buyBean = null;

        int size = list.size();
        List<Integer> numberList = new ArrayList<>();
        TypeBean bean;
        for (int i = 29; i < size; i++) {
            bean = list.get(i);

            if (bean.date.equals("2023-06-27 21:10")) {
                bean.dir = 0;
            }
            /**
             * 买点
             * 1.交易量增加 -
             * 2.趋势方向相同 ma5、ma30 与交易量同向
             */
            if (!hasBuy) {  // 未买入，找买点
                numberList.clear();
                for (int j = 4; j > 0; j--) {
                    numberList.add(list.get(i - j).number);
                }

                // 1.两个以上交易量大于当日交易量，则丢弃
                int count = 0;
                int numberTotal = 0;
                for (Integer integer : numberList) {
                    if (bean.number < integer) {
                        count++;
                    }
                    numberTotal += integer;
                }
                if (count >= 2) {
//                System.out.println("1111");
                    continue;
                }

                // 2.三个最小交易量均值要小于当日交易量 500多
                int avg = (numberTotal - Collections.max(numberList)) / 3;
                if (bean.number - avg < 500) {
//                System.out.println("2222");
                    continue;
                }

                if (bean.amount >= 0 && bean.ma5 >= bean.ma30) {
                    bean.dir = 1;
                    hasBuy = true;
                    buyBean = bean;
                }
                if (bean.amount <= 0 && bean.ma5 <= bean.ma30) {
                    bean.dir = -1;
                    hasBuy = true;
                    buyBean = bean;
                }

            } else {// 已买入，找卖点
                /**
                 * 卖点
                 * 1.反向趋势后卖出  ma5、ma30 形成反向交点
                 */
                if (buyBean.dir > 0) { // 买涨
                    if (bean.ma5 <= bean.ma30 && bean.amount < 0) {
                        hasBuy = false;
                    }
                } else {    // 买跌
                    if (bean.ma5 >= bean.ma30 && bean.amount > 0) {
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

        BeanUtil.print(resultList);
        return resultList;
    }


}

