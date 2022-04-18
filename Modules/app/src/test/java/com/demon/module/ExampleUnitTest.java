package com.demon.module;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        String sampleNo = "L228NZ219301-90-002";
        int index = sampleNo.indexOf('-');
        int lastIndex = sampleNo.lastIndexOf('-');

        String result = sampleNo.substring(0, index) + "--1" + sampleNo.substring(lastIndex + 1);
//        String resul9 = new RfidUtil().decode("332CB3603D70D75C53B43379C2DC4000", true);
//        String result = RfidUtil.decode("E77E39C37E34CB5E32E30D7000000000", false);
//        String result1 = RfidUtil.decode("E77E36DB0C77E32E70E30D7000000000", false);
//        String result2 = RfidUtil.decode("E77E35CB0D75DB1DF4E70D7000000000", false);
//
//        String resul4 = RfidUtil.encode("E77E39C37E34CB5E32E30D7000000000", 32);
//        String resul5 = RfidUtil.encode("E77E36DB0C77E32E70E30D7000000000", 32);
//        String resul6 = RfidUtil.encode("E77E35CB0D75DB1DF4E70D7000000000", 32);
        int a = 0;
        a++;
    }
}