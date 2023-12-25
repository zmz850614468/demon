package com.demo.fightdata;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.strategy.Strategy_1;
import com.demo.fightdata.strategy.Strategy_2;
import com.demo.fightdata.strategy.Strategy_3;
import com.demo.fightdata.strategy.Strategy_test_1;
import com.demo.fightdata.util.BeanUtil;
import com.demo.fightdata.util.FileUtil;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public float getEma(float lastEma, float close, int n) {
        return (2 * close + (n - 1) * lastEma) / (n + 1);
    }


//    OI2309.txt -- 5K 数据条数：8926
//    OI2309.txt -- 5k策略结果：
//    总次数：360 -- 总结果：2336
//    月份：2023/03 -- 次数：65 -- 结果：813
//    月份：2023/04 -- 次数：81 -- 结果：271
//    月份：2023/05 -- 次数：76 -- 结果：196
//    月份：2023/06 -- 次数：80 -- 结果：736
//    月份：2023/07 -- 次数：58 -- 结果：320
//
//    OI2309.txt -- 10k策略结果：
//    总次数：95 -- 总结果：2890
//    月份：2023/03 -- 次数：17 -- 结果：1615
//    月份：2023/04 -- 次数：23 -- 结果：199
//    月份：2023/05 -- 次数：21 -- 结果：294
//    月份：2023/06 -- 次数：21 -- 结果：801
//    月份：2023/07 -- 次数：13 -- 结果：-19
//
//    zly#P2309.txt -- 5K 数据条数：13089
//    zly#P2309.txt -- 5k策略结果：
//    总次数：375 -- 总结果：1212
//    月份：2023/03 -- 次数：74 -- 结果：-54
//    月份：2023/04 -- 次数：85 -- 结果：-380
//    月份：2023/05 -- 次数：77 -- 结果：544
//    月份：2023/06 -- 次数：82 -- 结果：640
//    月份：2023/07 -- 次数：57 -- 结果：462
//
//    zly#P2309.txt -- 10k策略结果：
//    总次数：102 -- 总结果：1272
//    月份：2023/03 -- 次数：26 -- 结果：206
//    月份：2023/04 -- 次数：19 -- 结果：482
//    月份：2023/05 -- 次数：19 -- 结果：520
//    月份：2023/06 -- 次数：22 -- 结果：380
//    月份：2023/07 -- 次数：16 -- 结果：-316
//
//    cp#RM2309.txt -- 5K 数据条数：13501
//    cp#RM2309.txt -- 5k策略结果：
//    总次数：360 -- 总结果：637
//    月份：2023/03 -- 次数：77 -- 结果：8
//    月份：2023/04 -- 次数：69 -- 结果：186
//    月份：2023/05 -- 次数：84 -- 结果：-27
//    月份：2023/06 -- 次数：76 -- 结果：203
//    月份：2023/07 -- 次数：54 -- 结果：267
//
//    cp#RM2309.txt -- 10k策略结果：
//    总次数：114 -- 总结果：593
//    月份：2023/03 -- 次数：31 -- 结果：-27
//    月份：2023/04 -- 次数：16 -- 结果：333
//    月份：2023/05 -- 次数：25 -- 结果：160
//    月份：2023/06 -- 次数：20 -- 结果：595
//    月份：2023/07 -- 次数：22 -- 结果：-468


    @Test
    public void addition_isCorrect() {
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add("28#OI2309.txt");
        fileNameList.add("29#P2309.txt");
        fileNameList.add("29#Y2309.txt");
        fileNameList.add("cp#RM2309.txt");
        fileNameList.add("29#PG2309.txt");
        fileNameList.add("29#EG2309.txt");
        fileNameList.add("29#JM2309.txt");
        fileNameList.add("29#V2309.txt");
        fileNameList.add("28#SF2309.txt");
        fileNameList.add("28#SM2309.txt");
        fileNameList.add("29#EB2309.txt");

        try {
            for (String s : fileNameList) {

                List<TypeBean> k5List = FileUtil.parseTxt("E:\\demon\\FightData\\app\\src\\main\\assets\\09\\" + s);
//                System.out.println(s + " -- 5K 数据条数：" + k5List.size());
//                System.out.println(s + " -- 5k策略结果：");
//                Strategy_1.getResult(k5List, "2023/04");


                System.out.println(s + " -- 10k策略结果：");
                List<TypeBean> k10List = BeanUtil.k5To(k5List, 10);
                Strategy_2.getResult(k10List, "2023/04");

//                System.out.println(s + " -- 10k L 和 T 策略结果：");
//                List<TypeBean> k10List = BeanUtil.k5To(k5List, 60);
//                Strategy_test_1.getResult(k10List, "2023/03");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test() {
        try {

            List<TypeBean> k5List = FileUtil.parseTxt("E:\\demon\\FightData\\app\\src\\main\\assets\\09\\28#OI2309.txt");
//            List<TypeBean> k5List = FileUtil.parseTxt("E:\\demon\\FightData\\app\\src\\main\\assets\\test.txt");
//            System.out.println(" -- 5K 数据条数：" + k5List.size());
//            System.out.println(" -- 5k策略结果：");
//            Strategy_1.getResult(k5List);

            System.out.println(" -- 10k策略结果：");
            List<TypeBean> k10List = BeanUtil.k5To(k5List, 10);
//            System.out.println(new Gson().toJson(k10List.get(0)));
//            for (TypeBean b : k10List) {
//                System.out.println(String.format("%s -- %5d", b.date, b.end));
//            }
            Strategy_3.getResult(k10List, "2023/04");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void test2() {
        List<String> strList = new ArrayList<>();
//        strList.add("3 22.00 0 1 7604 7618");
//        strList.add("12 21.30 0 -1 7598 7536");
//        strList.add("13 9.10 0 -1 7380 7398");
//        strList.add("19 9.20 0 -1 7676 7646");


        strList.add("6.30 23.00 1 7494 7626");
//        strList.add("3 22.00 0 -1 7604 7622");
        strList.add("4 13.35 -1 7618 7622");
        strList.add("4 23.00 1 7630 7594");
        strList.add("5 10.00 1 7594 7594");
        strList.add("6 10.10 1 7544 7586");
        strList.add("6 14.25 1 7586 7510");
        strList.add("6 21.40 -1 7510 7478");
        strList.add("10 14.35 1 7490 7568");
        strList.add("11 13.55 1 7524 7550");
        strList.add("12 10.35 1 7578 7510");
//        strList.add("12 21.30 0 1 7598 7536");
//        strList.add("13 9.10 0 1 7380 7504");
        strList.add("13 11.15  1 7398 7504");
        strList.add("13 22.40 1 7486 7478");
        strList.add("14 21.40 -1 7508 7582");
        strList.add("17 10.45 1 7582 7572");
        strList.add("18 21.10 -1 7578 7676");
//        strList.add("19 9.20 0 1 7676 7744");
        strList.add("19 11.15 1 7646 7744");
        strList.add("21 10.45 1 7768 7724");
        strList.add("21 11.15 -1 7724 7638");
        strList.add("24 22.10 1 7654 7680");
        strList.add("25 22.40 -1 7680 7738");
        strList.add("26 10.55 1 7738 7784");
        strList.add("27 10.00 -1 7784 7644");

        System.out.println("数据大小：" + strList.size());
        int count = 0;
        for (String s : strList) {
            String[] str = s.split(" ");
            int length = str.length;
            int dir = Integer.parseInt(str[length - 3]);
            int start = Integer.parseInt(str[length - 2]);
            int end = Integer.parseInt(str[length - 1]);
            count += (end - start) * dir;
        }

        System.out.println(String.format("数据大小：%d  -- 结果：%d ", strList.size(), count));
    }


}

//10400 豆油
//15000 液化气
//4800 菜粕
//
//
