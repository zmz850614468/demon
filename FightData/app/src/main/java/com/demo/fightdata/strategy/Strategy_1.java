package com.demo.fightdata.strategy;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.util.BeanUtil;
import com.demo.fightdata.util.StringUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 策略：短线操作
 * 适用：5分钟K线
 * 买点：在趋势方向上 + 交易量增加
 * 卖点：两根或三个反趋势k线
 * <p>
 * 结果优异的例子：
 * 菜油-OI2309
 * 棕榈油-P2309
 * 豆油-Y2309
 * 豆粕-RM2309
 */
public class Strategy_1 {

    /**
     * 买点：在趋势方向上 + 交易量增加
     * 1.去除前面四个中最大的一个交易量
     * 2.当前交易量大于前面的另外三个 且 大于这三个的均值300以上
     * 3.涨-ma5大于ma30      跌-ma5小于ma30
     * <p>
     * 1).21:00~21:15不能成为买点
     * 2).节假日回来15分钟不能成为买点
     *
     * <p>
     * 卖点：
     * 1.两根或三个反趋势k线
     * 2.周末 或 节假日前夕
     */
    public static void getResult(List<TypeBean> list, String beginTime) {
        BeanUtil.calAvg(list);
        BeanUtil.dealSpecialTime(list);

        List<TypeBean> resultList = new ArrayList<>();
        boolean hasBuy = false;
        TypeBean buyBean = null;

        int size = list.size();
        List<Integer> numberList = new ArrayList<>();
        TypeBean bean;

        int i = 0;
        for (; i < size; i++) {
            if (list.get(i).date.startsWith(beginTime)) {
                break;
            }
        }

        for (; i < size; i++) {

            bean = list.get(i);
            if (!hasBuy) {  // 未买入，找买点
                numberList.clear();
                for (int j = 4; j > 0; j--) {
                    numberList.add(list.get(i - j).number);
                }

                // 1.两个以上交易量大于 当日交易量-400 以上，则丢弃。（明显大于，肉眼可以分辨）
                int count = 0;
                int numberTotal = 0;
                for (Integer integer : numberList) {
                    if (bean.number < integer + 400) {
                        count++;
                    }
                    numberTotal += integer;
                }
                if (count >= 2) {
                    continue;
                }

                // 2.三个最小交易量均值要小于当日交易量 500多
//                int avg = (numberTotal - Collections.max(numberList)) / 3;
//                if (bean.number - avg < 500) {
//                    continue;
//                }

                // 3.5K、30K 均线方向 和 收盘价方向一致
                if (!((bean.ma5 >= bean.ma30 && bean.amount >= 0 && bean.end > Math.max(bean.ma5, bean.ma10))
                        || (bean.ma5 <= bean.ma30 && bean.amount <= 0 && bean.end < Math.min(bean.ma5, bean.ma10)))) {
                    continue;
                }

                /**
                 * 1.21:00~21:15不能成为买点
                 * 2.节假日回来15分钟不能成为买点
                 * todo
                 */
                if (bean.date.contains("21:05")
                        || (StringUtil.getTime(bean.date) - StringUtil.getTime(list.get(i - 1).date)) >= 3 * 24 * 3600 * 1000) {
                    i += 2;
                    continue;
                }

                hasBuy = true;
                buyBean = bean;
//                System.out.println(new Gson().toJson(bean));
            } else {    // 已买入，找卖点

                // 1.两根或三个反趋势k线
                float amount1 = list.get(i - 2).amount;
                float amount2 = list.get(i - 1).amount;
                float amount3 = bean.amount;
                if (buyBean.ma5 >= buyBean.ma30 && buyBean.amount >= 0) { // 阳线，上涨
                    buyBean.dir = 1;
                    if ((amount2 < 0 && amount3 < 0 && amount3 <= -10)
                            || amount1 <= 0 && amount2 <= 0 && amount3 <= 0) {
                        hasBuy = false;
                    }
                } else {    // 阴线，下跌
                    buyBean.dir = -1;
                    if ((amount2 > 0 && amount3 > 0 && amount3 >= 10)
                            || amount1 >= 0 && amount2 >= 0 && amount3 >= 0) {
                        hasBuy = false;
                    }
                }

                // 2.周末 或 节假日前夕,优先卖
                if (hasBuy && i + 1 < size) {
                    if (StringUtil.getTime(list.get(i + 1).date) - StringUtil.getTime(bean.date) > 48 * 3600 * 1000) {
                        hasBuy = true;
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
//        BeanUtil.printLimit(list, resultList, 40);
    }


}

