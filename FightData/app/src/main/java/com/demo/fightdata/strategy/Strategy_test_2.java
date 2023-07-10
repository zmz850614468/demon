package com.demo.fightdata.strategy;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略：在长时间趋势后，反向穿过ma30线，并回踩ma30线
 * 适用：
 * 买点：回踩后，是反向K线，且收盘价处于回踩ma30线方向上
 * 卖点：收盘价穿过ma30线
 * 结果优异的例子：
 */
public class Strategy_test_2 {

    /**
     * 买点：
     * 1.寻找趋势线最高点或最低点（10跟K线以上）
     * 2.寻找闯过ma30线后的最高或最低点（1.2两条K线在30跟K线内）
     * 3.寻找回踩ma30线的回踩点
     * <p>
     * 卖点：收盘价穿过ma30线
     *
     * @param list
     */
    public static List<TypeBean> getResult(List<TypeBean> list) {
        BeanUtil.calAvg(list);

        List<TypeBean> resultList = new ArrayList<>();
        boolean hasBuy = false;
        TypeBean buyBean = null;
        TypeBean point1Bean = null; // 最低点或最高点
        TypeBean point2Bean = null; // 反向最低点或最高点
        int point1Index = 0;
        int point2Index = 0;

        int size = list.size();
        TypeBean bean;
        for (int i = 29; i < size; i++) {
            bean = list.get(i);

            if (!hasBuy) {// 未买入，找买点
                // 1.寻找趋势线最高点或最低点（10跟K线以上）
                if (bean.ma5 > bean.ma30 && bean.ma10 > bean.ma30 && bean.ma5 > bean.ma10) {
                    bean.dir = 1;
                    if (point1Bean == null) {
                        point1Bean = bean;
                    } else if (bean.high > point1Bean.high) {
                        point1Bean = bean;
                    }
                } else if (bean.ma5 < bean.ma30 && bean.ma10 < bean.ma30 && bean.ma5 < bean.ma10) {
                    bean.dir = -1;
                    if (point1Bean == null) {
                        point1Bean = bean;
                    } else if (bean.low < point1Bean.low) {
                        point1Bean = bean;
                    }
                }

                // 有新的高低点后，判断是否（10跟K线以上）
                if (point1Bean == bean) {
                    TypeBean tempBean;
                    point1Bean.isCondition_1 = true;
                    point1Index = i;
                    for (int j = i - 1; j > i - 10; j--) {
                        tempBean = list.get(j);
                        if (!((point1Bean.dir > 0 && tempBean.ma5 > tempBean.ma30 && tempBean.ma10 > tempBean.ma30)
                                || (point1Bean.dir < 0 && tempBean.ma5 < tempBean.ma30 && tempBean.ma10 < tempBean.ma30))) {
                            point1Bean.isCondition_1 = false;
                            break;
                        }
                    }
                    continue;
                }

                // 2.寻找穿过ma30线后的最高或最低点（1.2两条K线在30跟K线内）
                if (point1Bean != null && point1Bean.isCondition_1) {
                    if (point1Bean.dir > 0 && bean.ma5 < bean.ma10 && bean.ma10 < bean.ma30 && bean.end < bean.ma5) {
                        if (point2Bean == null) {
                            point2Bean = bean;
                        } else if (bean.low < point2Bean.low) {
                            point2Bean = bean;
                        }
                    } else if (point1Bean.dir < 0 && bean.ma5 > bean.ma10 && bean.ma10 > bean.ma30 && bean.end > bean.ma5) {
                        if (point2Bean == null) {
                            point2Bean = bean;
                        } else if (bean.high > point2Bean.high) {
                            point2Bean = bean;
                        }
                    } else {
                        point1Bean.isCondition_1 = false;
                        point1Bean = null;
                        point2Bean = null;
                        continue;
                    }
                }

                // 判断 （1.2两条K线在30跟K线内）
                if (point2Bean == bean) {
                    point2Index = i;
                    if (point2Index - point1Index <= 30) {
                        point2Bean.isCondition_2 = true;
                    } else {
                        point1Bean.isCondition_1 = false;
                        point1Bean = null;
                        point2Bean = null;
                    }
                    continue;
                }

                // 3.寻找回踩ma30线的回踩点
                if (point2Bean != null && point2Bean.isCondition_2) {
                    if (point1Bean.dir > 0) {
                        if (bean.amount < 0 && bean.high > bean.ma30 && bean.end < bean.ma30) {
                            hasBuy = true;
                            buyBean = bean;
                        } else if (bean.end > bean.ma30) {
                            point1Bean.isCondition_1 = false;
                            point2Bean.isCondition_2 = false;
                            point1Bean = null;
                            point2Bean = null;
                            continue;
                        }
                    } else {
                        if (bean.amount > 0 && bean.low < bean.ma30 && bean.end > bean.ma30) {
                            hasBuy = true;
                            buyBean = bean;
                        } else if (bean.end < bean.ma30) {
                            point1Bean.isCondition_1 = false;
                            point2Bean.isCondition_2 = false;
                            point1Bean = null;
                            point2Bean = null;
                            continue;
                        }
                    }
                }

            } else {
                if (point1Bean.dir > 0) {
                    if (bean.amount > 0 && bean.end > bean.ma30) {
                        hasBuy = false;
                    }
                } else {
                    if (bean.amount < 0 && bean.amount < 0 && bean.end < bean.ma30) {
                        hasBuy = false;
                    }
                }
                if (!hasBuy) {
                    resultList.add(buyBean);
                    resultList.add(bean);
                }
            }
        }

        BeanUtil.print(resultList);
        return resultList;
    }

}

