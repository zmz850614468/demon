package com.lilanz.tooldemo;

import com.lilanz.tooldemo.utils.CRC16Util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SerialPortTest {

    @Test
    public void crc16Calculate() {
        List<String> list = new ArrayList<>();
        list.add("FE 10 00 03 00 02 04 00 04");
//        list.add("FE 10 00 03 00 02 04 00 05");

        for (String s : list) {
            String crc = CRC16Util.calcCrc16(s);
            System.out.println(s + crc);
        }
    }
}