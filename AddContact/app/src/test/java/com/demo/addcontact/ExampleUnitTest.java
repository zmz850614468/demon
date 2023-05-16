package com.demo.addcontact;

import android.util.Log;

import com.demo.addcontact.util.PhoneUitl;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            int op = random.nextInt(3);//随机运营商标志位
            String phone = PhoneUitl.createMobile(op);
            System.out.println(i + " -- " + phone);
        }

//        assertEquals(4, 2 + 2);
    }

    private void showLog(String msg) {
        Log.e("ExampleUnitTest", msg);
    }
}