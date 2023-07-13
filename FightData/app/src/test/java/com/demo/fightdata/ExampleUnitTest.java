package com.demo.fightdata;

import com.demo.fightdata.bean.TypeBean;
import com.demo.fightdata.strategy.Strategy_1;
import com.demo.fightdata.strategy.Strategy_2;
import com.demo.fightdata.strategy.Strategy_test_1;
import com.demo.fightdata.util.BeanUtil;
import com.demo.fightdata.util.FileUtil;

import org.junit.Test;

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

    @Test
    public void addition_isCorrect() {

        try {

            List<TypeBean> k5List = FileUtil.parseTxt("E:\\demon\\FightData\\app\\src\\main\\assets\\cai_5k.txt");
//            List<TypeBean> k5List = FileUtil.parseTxt("E:\\demon\\FightData\\app\\src\\main\\assets\\zong_5k.txt");
            System.out.println("5K 数据条数：" + k5List.size());
            Strategy_1.getResult(k5List);

//            k5List = BeanUtil.k5To(k5List, 10);
//            Strategy_2.getResult(k5List);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}

//
//
//14.40 1 1 9728
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
