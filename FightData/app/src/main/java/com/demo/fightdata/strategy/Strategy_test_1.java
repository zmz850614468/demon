package com.demo.fightdata.strategy;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.util.BeanUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略：锤子线 上吊线 操作
 * 适用：时K线，日K线
 * 买点：锤子线 上吊线
 * 卖点：
 * 结果优异的例子：
 * 棕榈油-p2309
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
    public static List<TypeBean> getResult(List<TypeBean> list, String beginTime) {
        BeanUtil.calAvg(list);

        List<TypeBean> resultList = new ArrayList<>();
        boolean hasBuy = false;
        TypeBean buyBean = null;

        int size = list.size();
        TypeBean bean;

        List<Integer> ma5List = new ArrayList<>();

        int i = 0;
        for (; i < size; i++) {
            if (list.get(i).date.startsWith(beginTime)) {
                break;
            }
        }
        for (; i < size; i++) {
            bean = list.get(i);
            if (!hasBuy) {  // 未买入，找买点

                // 1.判断是 锤子线 或 上吊线
                bean.dir = judgeTorL(bean);
                if (bean.dir == 0) {
                    continue;
                }

//                if (bean.date.equals("2023/06/14-1355")) {
//                    if (bean.date.equals("2023/06/27-2200")) {
//                    ma5List.clear();
//                    System.out.println(bean.dir + " -- " + new Gson().toJson(bean));
//                }

                // 2.判断 趋势是否满足
                ma5List.clear();
                TypeBean tempBean;
//                for (int j = i - 19; j <= i; j++) {
                for (int j = i; j >= i - 20; j--) {
                    tempBean = list.get(j);
                    if (bean.dir == 1 && tempBean.ma5 > tempBean.ma10 && tempBean.ma10 > tempBean.ma30 && tempBean.end >= tempBean.ma5) {
                        ma5List.add(0, tempBean.ma5);
                    } else if (bean.dir == -1 && tempBean.ma5 < tempBean.ma10 && tempBean.ma10 < tempBean.ma30 && tempBean.end <= tempBean.ma5) {
                        ma5List.add(0, tempBean.ma5);
                    } else {
                        break;
                    }
                }
                if (ma5List.size() < 7) {
                    continue;
                }

                List<String> result = judgeTrendV2(ma5List);
                if (bean.dir == 1 && "上涨".equals(result.get(0)) && Integer.parseInt(result.get(2)) >= 7) {
                    buyBean = bean;
                    hasBuy = true;
                    buyBean.memo = new Gson().toJson(result);
                } else if (bean.dir == -1 && "下跌".equals(result.get(0)) && Integer.parseInt(result.get(2)) >= 7) {
                    buyBean = bean;
                    hasBuy = true;
                    buyBean.memo = new Gson().toJson(result);
                }
            } else {

                // 测试卖点1：反趋势K线
                if (buyBean.dir < 0) {  //  锤子线
                    if (bean.amount < 0) {
                        hasBuy = false;
                    }
                } else {     // 上吊线
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
     * 判断是 T 还是 L
     * 1    L
     * -1   T
     * 0    都不是
     *
     * @param bean
     * @return
     */
    public static int judgeTorL(TypeBean bean) {
        if (bean.amount >= 0 && Math.max((bean.high - bean.start), 3) * 4 <= bean.high - bean.low) { // T 判断
            return -1;
        }
        if (bean.amount <= 0 && Math.max((bean.start - bean.low), 3) * 4 <= bean.high - bean.low) { // L 判断
            return 1;
        }

        return 0;
    }

    /**
     * 强趋势判断
     */
    private static List<String> judgeTrendV2(List<Integer> l) {
        if (l.size() < 3) {
            return null;
        }

        // 以最后一个为原点进行平移操作后计算斜率,并倒叙排列
        int zeroPoint = l.get(l.size() - 1);
        List<Double> list = new ArrayList<>();
        int index = 0;
        for (int i = l.size() - 1; i >= 0; i--) {
            list.add((l.get(i) - zeroPoint) / (-5.0 * index));
            index++;
        }

        // 斜率大于零上涨，小于零下跌
        int dir = list.get(1) > 0 ? 1 : -1;
        double maxK = list.get(1);
        double minK = list.get(1);
        int count = 0;
        double point;
        for (int i = 2; i < list.size(); i++) {
            point = list.get(i);
            if (dir * point < 0) {
                break;
            }
            maxK = Math.max(maxK, point);
            minK = Math.min(minK, point);
            count = i;
        }

        List<String> resultList = new ArrayList<>();
        if (dir == 1) {
            resultList.add("上涨");
        } else if (dir == -1) {
            resultList.add("下跌");
        }
        resultList.add("--");

        resultList.add((count + 1) + "");
//        System.out.println(new Gson().toJson(resultList));

        return resultList;
    }


    /**
     * 强趋势判断
     */
    private static List<String> judgeTrend(List<Integer> l) {
        if (l.size() < 3) {
            return null;
        }

        // 以最后一个为原点进行平移操作,并倒叙排列
        int zeroPoint = l.get(l.size() - 1);
        List<Integer> list = new ArrayList<>();
        for (int i = l.size() - 1; i >= 0; i--) {
            list.add(l.get(i) - zeroPoint);
        }

        // 1. 确定象限 1 第一象限，-1 第四象限
        int dir = list.get(1) > 0 ? 1 : -1;

        int size = list.size();
        int point = 0;      // 点位
        double k = 0;       // 斜率
        double lastK = 0;   // 上一个斜率

        double dk = 0;      // 斜率差值
//        double lastDk = 0;  // 上一个斜率差值
        int count = 0;      // 满足的点个数
        // 2.开始判断趋势
        for (int i = 1; i < size; i++) {
            point = list.get(i);
            k = point / (-5.0 * i);

            if (point * dir < 0) {  // 确保所有数据都在一个象限内
                break;
            }

            if (i == 1) {
                lastK = point / (-5.0 * i);
                continue;
            }

            if (dk == 0 && k - lastK != 0) {
                dk = k - lastK;
            }

            if (dk * (k - lastK) >= 0) {
                count = i;
            } else {
                break;
            }
            lastK = k;
        }

        List<String> resultList = new ArrayList<>();
        if (dir == 1 && dk > 0) {
            resultList.add("下跌");
            resultList.add("加速");
        } else if (dir == 1 && dk < 0) {
            resultList.add("下跌");
            resultList.add("减速");
        } else if (dir == -1 && dk > 0) {
            resultList.add("上涨");
            resultList.add("减速");
        } else if (dir == -1 && dk < 0) {
            resultList.add("上涨");
            resultList.add("加速");
        }

        resultList.add((count + 1) + "");
//        System.out.println(new Gson().toJson(resultList));

        return resultList;
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
            System.out.println(String.format("%s -- %s  %2d -- %5d --%5d %4d -- %s ", b.date, s.date, b.dir, b.end, s.end, (s.end - b.end) * b.dir * -1, b.memo));
            count += (s.end - b.end) * b.dir * -1;
        }
        System.out.println(resultList.size() / 2 + " -- " + count);
    }
}

// zly#P2309.txt -- 10k策略结果：
//2023/03/22-1415 -- 2023/03/22-1445  -1 --  7128 -- 7146   18 -- ["下跌","--","9"]
//2023/04/14-1010 -- 2023/04/14-1115   1 --  7176 -- 7092   84 -- ["上涨","--","10"]
//2023/06/14-1125 -- 2023/06/14-1335   1 --  6782 -- 6786   -4 -- ["上涨","--","9"]
//2023/06/14-1355 -- 2023/06/14-1500   1 --  6792 -- 6774   18 -- ["上涨","--","12"]
//2023/06/27-2200 -- 2023/06/27-2220   1 --  7198 -- 7194    4 -- ["上涨","--","12"]
//2023/07/11-0910 -- 2023/07/11-0950   1 --  7650 -- 7580   70 -- ["上涨","--","18"]
//6 -- 190
