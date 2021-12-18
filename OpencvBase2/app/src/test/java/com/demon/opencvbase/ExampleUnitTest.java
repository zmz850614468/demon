package com.demon.opencvbase;

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
//        assertEquals(4, 2 + 2);

        String result = String.format("%08x", 258);
        result = String.format("%08x", -1);
        result = String.format("%x", -257);

    }

    private String intTo4Byte(int x) {
        int x1 = x % 256;
        x /= 256;
        int x2 = x % 256;
        x /= 256;
        int x3 = x % 256;
        x /= 256;
        return String.format(" %02x 02x 02x 02x", x, x3, x2, x1);
    }
}