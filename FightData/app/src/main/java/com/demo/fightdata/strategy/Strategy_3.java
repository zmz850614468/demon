package com.demo.fightdata.strategy;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.util.BeanUtil;
import com.demo.fightdata.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略：短线操作
 * 适用：10分钟K线
 * 买点：
 * 卖点：
 * <p>
 * 结果优异的例子：
 * 菜油-OI2309
 */
public class Strategy_3 {

    /**
     * 买点：
     * 1.多根交易量相较前5根交易量有显著放量
     * 2.方向相同，却有显著的涨跌幅
     * 3.之后的第一根方向K线
     *
     * <p>
     * 卖点：
     * 2.15:00前 或 节假日前夕
     */
    public static void getResult(List<TypeBean> list, String beginTime) {
        BeanUtil.calAvg(list);
//        BeanUtil.dealSpecialTime(list);

        List<TypeBean> resultList = new ArrayList<>();
        boolean hasBuy = false;
        TypeBean buyBean = null;

        int size = list.size();
//        List<Integer> numberList = new ArrayList<>();
        int avgCount = 0;   // 平均值
        int maxCount = 0;   // 最大值
        TypeBean bean;

        int i = 0;
        for (; i < size; i++) {
            if (list.get(i).date.startsWith(beginTime)) {
                break;
            }
        }

        for (; i < size; i++) {
            bean = list.get(i);

            if (bean.date.equals("2023/08/16-0910")) {
                avgCount=0;
            }
            
            if (!hasBuy) {  //
                avgCount = 0;
                maxCount = 0;
                for (int j = 5; j > 0; j--) {
                    avgCount += list.get(i - j).number;
                    maxCount = Math.max(maxCount, list.get(i - j).number);
                }
                avgCount /= 5;

                // 1.
                if (bean.number > maxCount && bean.number > 1.5 * avgCount) {
                    TypeBean tempBean = list.get(++i);
                    if (tempBean.number > maxCount && tempBean.number > 1.5 * avgCount && tempBean.amount * bean.amount > 0) {
                        while (list.get(++i).amount * bean.amount <= 0) {
                            break;
                        }
                        tempBean = list.get(i - 1);
                        if (Math.abs(tempBean.end - bean.start) >= 40) {
                            tempBean = list.get(i);
                            if (tempBean.amount >= 0 && tempBean.ma5 < tempBean.ma30) {
                                tempBean.dir = 1;
                                resultList.add(bean);
//                                hasBuy = true;
//                                buyBean = tempBean;
                            } else if (tempBean.amount <= 0 && tempBean.ma5 > tempBean.ma30) {
                                tempBean.dir = -1;
                                resultList.add(bean);
//                                hasBuy = true;
//                                buyBean = tempBean;
                            } else {
                                i++;
                            }
                        } else {
                            i++;
                        }
                    }
                }
            } else {    //

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

        for (TypeBean b : resultList) {
            System.out.println(String.format("%s  --   %5d ", b.date, b.end));
        }
//        BeanUtil.print(resultList);
    }


}

