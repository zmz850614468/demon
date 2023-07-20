package com.demo.fightdata;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.strategy.Strategy_1;
import com.demo.fightdata.strategy.Strategy_2;
import com.demo.fightdata.strategy.Strategy_test_1;
import com.demo.fightdata.util.BeanUtil;
import com.demo.fightdata.util.FileUtil;

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


//    OI2309
//    5k -- 399 -- 1866
//    月份：2023/02 -- 次数：22 -- 结果：-36
//    月份：2023/03 -- 次数：68 -- 结果：712
//    月份：2023/04 -- 次数：86 -- 结果：247
//    月份：2023/05 -- 次数：80 -- 结果：173
//    月份：2023/06 -- 次数：82 -- 结果：770
//    月份：2023/07 -- 次数：61 -- 结果：261
//    10k -- 117 -- 2106
//    月份：2023/01 -- 次数：3 -- 结果：-116
//    月份：2023/02 -- 次数：23 -- 结果：-482
//    月份：2023/03 -- 次数：16 -- 结果：1588
//    月份：2023/04 -- 次数：22 -- 结果：90
//    月份：2023/05 -- 次数：18 -- 结果：224
//    月份：2023/06 -- 次数：22 -- 结果：802
//    月份：2023/07 -- 次数：13 -- 结果：-62


    @Test
    public void addition_isCorrect() {
        List<String> fileNameList = new ArrayList<>();
        fileNameList.add("OI2309.txt");
        try {
            for (String s : fileNameList) {

                List<TypeBean> k5List = FileUtil.parseTxt("E:\\demon\\FightData\\app\\src\\main\\assets\\" + s);
                System.out.println(s + " -- 5K 数据条数：" + k5List.size());
                System.out.println(s + " -- 5k策略结果：");
                Strategy_1.getResult(k5List);


                System.out.println(s + " -- 10k策略结果：");
                List<TypeBean> k10List = BeanUtil.k5To(k5List, 10);
                Strategy_2.getResult(k10List);

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}

//
//14.45 -1 -1 9638
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//