package com.demo.fightdata.util;

import com.demo.fightdata.bean.TypeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class BeanUtil {

    /**
     * 解析数据对象
     *
     * @param str
     * @return
     */
    public static List<TypeBean> parse(String str) {
        List<TypeBean> list = new ArrayList<>();
        String[] strArr = str.split("\n");
        for (String s : strArr) {
            TypeBean cai5k23Bean = new TypeBean();
            if (cai5k23Bean.setData(s)) {
                list.add(cai5k23Bean);
            }
        }
        calAvg(list);
        return list;
    }

    /**
     * 计算均值数据
     * ma5 ma10 ma30
     *
     * @param list
     */
    public static void calAvg(List<TypeBean> list) {
        int size = list.size();
        TypeBean bean;
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

    /**
     * 处理特殊时间点   amount数据
     * 9:05
     * 10:35
     * 13:35
     * 21:05
     *
     * @param list
     */
    public static void dealSpecialTime(List<TypeBean> list) {
        int size = list.size();
        TypeBean bean;
        for (int i = 1; i < size; i++) {
            bean = list.get(i);
            if (bean.date.contains("9:05") || bean.date.contains("10:35")
                    || bean.date.contains("13:35") || bean.date.contains("21:05")) {
                int am = bean.end - list.get(i - 1).end;
                if (bean.amount >= 0 && am >= 0) {
                    bean.amount = Math.min(bean.amount, am);
                } else if (bean.amount <= 0 && am <= 0) {
                    bean.amount = Math.max(bean.amount, am);
                }
            }
        }
    }

//    /**
//     * Y=［2*X+(N-1)*Y’］/(N+1)
//     * Calculate EMA,
//     *
//     * @param list :Price list to calculate，the first at head, the last at tail.
//     * @return
//     */
//    public static float getEMA(List<Float> list, int number) {
//
//        float ema = list.get(0);
//        for (int i = 1; i < list.size(); i++) {
//            ema = (2 * list.get(i) + (number - 1) * ema) / (number + 1);
////            ema = (2 * list.get(i) + i * ema) / (i + 2);
////            System.out.println(i + " -- " + ema);
//        }
//
//        return ema;
//    }


//    /**
//     * 计算 diff、dea、macd 值
//     *
//     * @param list
//     */
//    public static void calMacd(List<TypeBean> list) {
//        List<Float> end12List = new ArrayList<>();
//        List<Float> end26List = new ArrayList<>();
//    }

//    /**
//     * @param list
//     * @param n
//     */
//    public static float calEma(List<Float> list, int total, int n) {
//        int index = list.size() - total;
//        if (n == 1) {
//            return list.get(index);
//        } else {
//            return (2 * list.get(index + n - 1) + (n - 1) * calEma(list, total, n - 1)) / (n + 1);
//        }
//    }

    /**
     * 5K线 转其他时间的K线数据
     * 5
     * 10
     * 60
     *
     * @param list
     * @param kMin
     * @return
     */
    public static List<TypeBean> k5To(List<TypeBean> list, int kMin) {
        int type = kMin / 5;
        List<TypeBean> newList = new ArrayList<>();
        TypeBean bean = null;

        int i = 0;
        for (TypeBean typeBean : list) {
            if (i % type == 0 || typeBean.date.contains("2105") || typeBean.date.contains("0905")) {
                i = 0;
                bean = new TypeBean();
                newList.add(bean);
            }
            bean.add(typeBean);
            i++;
        }

        calAvg(newList);
        return newList;
    }

    /**
     * 5K线 转日K线数据
     *
     * @param list
     * @return
     */
    public static List<TypeBean> K5ToDay(List<TypeBean> list) {
        List<TypeBean> newList = new ArrayList<>();

        TypeBean bean = null;
        boolean needNewNext = true;
        for (TypeBean typeBean : list) {
            if (needNewNext) {
                needNewNext = false;
                bean = new TypeBean();
                newList.add(bean);
            }

            if (typeBean.date.contains("15:00")) {
                needNewNext = true;
            }

            bean.add(typeBean);
        }

        calAvg(newList);
        return newList;
    }


    /**
     * 打印数据
     *
     * @param resultList
     */
    public static void print(List<TypeBean> resultList) {
        TypeBean b;
        TypeBean s;
        int count = 0;
        int monthCount = 0;
        int index = 0;
        int printCount = 0;
        int temCount = 0;
        StringBuffer buffer = new StringBuffer();
        String monthStr = resultList.get(0).date.substring(0, 7);
        for (int i = 1; i < resultList.size(); i += 2) {
            b = resultList.get(i - 1);
            s = resultList.get(i);

            // todo 测试用
//            if ((s.end - b.end) * b.dir >= 180 || printCount > 0) {
//                if (printCount == 0) {
//                    printCount = 6;
//                    temCount = 0;
//                }
//                System.out.println(String.format("%s -- %s  %2d -- %5d --%5d %4d", b.date, s.date, b.dir, b.end, s.end, (s.end - b.end) * b.dir));
//                printCount--;
//                if (printCount < 5) {
//                    temCount += (s.end - b.end) * b.dir;
//                }
//                if (printCount == 0) {
//                    System.out.println("小计：" + temCount);
//                }
//            }

            if (!b.date.startsWith(monthStr)) {
                buffer.append(String.format("月份：%s -- 次数：%d -- 结果：%d", monthStr, index, monthCount)).append("\n");

                index = 0;
                count += monthCount;
                monthCount = 0;
                monthStr = b.date.substring(0, 7);
            }
            monthCount += (s.end - b.end) * b.dir;
            index++;
        }
        count += monthCount;
        
        buffer.append(String.format("月份：%s -- 次数：%d -- 结果：%d", monthStr, index, monthCount)).append("\n");
        System.out.println(String.format("总次数：%d -- 总结果：%d", resultList.size() / 2, count));
        System.out.println(buffer.toString());
    }

    /**
     * 打印数据
     *
     * @param resultList
     */
    public static void printLimit(List<TypeBean> list, List<TypeBean> resultList, int limit) {
        TypeBean b;
        TypeBean s;
        int count = 0;
        int limCount = 0;
        for (int i = 1; i < resultList.size(); i += 2) {
            b = resultList.get(i - 1);
            b.low = b.end;
            b.high = b.end;
            s = resultList.get(i);

//            boolean isStart = false;
//            int lim = (s.end - b.end) * b.dir;
//            for (TypeBean bean : list) {
//
//                if (isStart) {
//                    b.low = Math.min(b.low, bean.low);
//                    b.high = Math.max(bean.high, bean.high);
//                    if (b.dir > 0 && b.high - bean.end > limit) {
//                        lim = bean.end - b.end;
//                        break;
//                    } else if (b.dir < 0 && bean.end - b.low > limit) {
//                        lim = (bean.end - b.end) * b.dir;
//                        break;
//                    }
//                }
//                if (bean == s) {
//                    break;
//                }
//                if (bean == b) {
//                    isStart = true;
//                }
//            }

//            int res = (s.end - b.end) * b.dir;
//            int lim = 0;
//            if (b.dir > 0) {
//                lim = b.high - b.end > limit ? limit : res;
//            } else {
//                lim = b.end - b.low > limit ? limit : res;
//            }
//            count += (s.end - b.end) * b.dir;
//            limCount += lim;

//            System.out.println(String.format("%s -- %s  %2d -- %5d --%5d  %4d  %4d", b.date, s.date, b.dir, b.end, s.end, (s.end - b.end) * b.dir, lim));
        }
        System.out.println(resultList.size() / 2 + " -- " + count + " -- " + limCount);
    }

}
