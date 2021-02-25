package com.lilanz.wificonnect;

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
        assertEquals(4, 2 + 2);
    }

    @Test
    public void parseIRData() {
        String data = "         +4250,-4400     + 500,-1650     + 500,- 600     + 450,-1700\n" +
                "     + 450,-1700     + 450,- 600     + 450,- 600     + 500,-1650\n" +
                "     + 500,- 550     + 500,- 600     + 450,-1700     + 450,- 600\n" +
                "     + 450,- 600     + 500,-1650     + 500,-1650     + 500,- 550\n" +
                "     + 500,-1650     + 500,-1700     + 450,- 600     + 450,-1700\n" +
                "     + 450,-1700     + 450,-1700     + 450,-1700     + 450,-1700\n" +
                "     + 450,-1700     + 450,- 600     + 500,-1650     + 500,- 550\n" +
                "     + 500,- 600     + 450,- 600     + 500,- 550     + 500,- 550\n" +
                "     + 500,- 600     + 450,-1700     + 450,- 600     + 500,-1650\n" +
                "     + 500,- 550     + 500,- 600     + 450,- 600     + 450,- 600\n" +
                "     + 500,- 550     + 500,- 600     + 450,-1700     + 450,- 600\n" +
                "     + 450,-1700     + 450,-1700     + 450,-1700     + 500,-1650\n" +
                "     + 500,-1650     + 500\n";

        data = data.replace(" ", "");
        data = data.replace("\n", "");
        data = data.replace("-", "");
        data = data.replace("+", ",");

        String[] array = data.split(",");
        StringBuffer buffer = new StringBuffer();
        for (int i = 4; i < array.length; i = i + 2) {
            if (Integer.parseInt(array[i]) > 1000) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }
        }

        int length = buffer.length();
        StringBuffer result = new StringBuffer();
        int count = 0;
        for (int i = 1; i <= buffer.length(); i++) {
            count *= 2;
            if (buffer.charAt(i - 1) == '1') {
                count += 1;
            }
            if (i % 4 == 0) {
                result.append(int2Hex2(count));
                count = 0;
            }
        }

        result.toString();
    }

    /**
     * @param number 屏幕端显示的10进制数据
     * @return 屏幕端显示的16进制数据
     */
    public static String int2Hex2(int number) {
        String result = null;
        switch (number) {
            case 10:
                result = "A";
                break;
            case 11:
                result = "B";
                break;
            case 12:
                result = "C";
                break;
            case 13:
                result = "D";
                break;
            case 14:
                result = "E";
                break;
            case 15:
                result = "F";
                break;
            default:
                result = number + "";
                break;
        }

        return result;
    }
}