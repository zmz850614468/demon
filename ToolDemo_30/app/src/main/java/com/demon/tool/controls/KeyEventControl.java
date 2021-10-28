package com.demon.tool.controls;

import android.app.Instrumentation;
import android.view.KeyEvent;

import com.demon.tool.util.StringUtil;

/**
 * 出发软键盘输入
 */
public class KeyEventControl {

    private static KeyEventControl instance;

    public static KeyEventControl getInstance() {
        if (instance == null) {
            instance = new KeyEventControl();
        }
        return instance;
    }

    /**
     * 把字符结果 模拟成输入数据
     *
     * @param keys
     */
    public void keyAction(final String keys) {
        if (StringUtil.isEmpty(keys)) {
            return;
        }

        new Thread() {
            public void run() {
                try {
                    String tempKeys = keys.toUpperCase();
                    Instrumentation inst = new Instrumentation();
                    for (int i = 0; i < tempKeys.length(); i++) {
                        inst.sendKeyDownUpSync(getKeyCode(tempKeys.charAt(i)));
                    }
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private int getKeyCode(char ch) {
        int keyCode = 0;
        switch (ch) {
            case '0':
                keyCode = KeyEvent.KEYCODE_0;
                break;
            case '1':
                keyCode = KeyEvent.KEYCODE_1;
                break;
            case '2':
                keyCode = KeyEvent.KEYCODE_2;
                break;
            case '3':
                keyCode = KeyEvent.KEYCODE_3;
                break;
            case '4':
                keyCode = KeyEvent.KEYCODE_4;
                break;
            case '5':
                keyCode = KeyEvent.KEYCODE_5;
                break;
            case '6':
                keyCode = KeyEvent.KEYCODE_6;
                break;
            case '7':
                keyCode = KeyEvent.KEYCODE_7;
                break;
            case '8':
                keyCode = KeyEvent.KEYCODE_8;
                break;
            case '9':
                keyCode = KeyEvent.KEYCODE_9;
                break;
            case 'A':
                keyCode = KeyEvent.KEYCODE_A;
                break;
            case 'B':
                keyCode = KeyEvent.KEYCODE_B;
                break;
            case 'C':
                keyCode = KeyEvent.KEYCODE_C;
                break;
            case 'D':
                keyCode = KeyEvent.KEYCODE_D;
                break;
            case 'E':
                keyCode = KeyEvent.KEYCODE_E;
                break;
            case 'F':
                keyCode = KeyEvent.KEYCODE_F;
                break;
            case 'G':
                keyCode = KeyEvent.KEYCODE_G;
                break;
            case 'H':
                keyCode = KeyEvent.KEYCODE_H;
                break;
            case 'I':
                keyCode = KeyEvent.KEYCODE_I;
                break;
            case 'J':
                keyCode = KeyEvent.KEYCODE_J;
                break;
            case 'K':
                keyCode = KeyEvent.KEYCODE_K;
                break;
            case 'L':
                keyCode = KeyEvent.KEYCODE_L;
                break;
            case 'M':
                keyCode = KeyEvent.KEYCODE_M;
                break;
            case 'N':
                keyCode = KeyEvent.KEYCODE_N;
                break;
            case 'O':
                keyCode = KeyEvent.KEYCODE_O;
                break;
            case 'P':
                keyCode = KeyEvent.KEYCODE_P;
                break;
            case 'Q':
                keyCode = KeyEvent.KEYCODE_Q;
                break;
            case 'R':
                keyCode = KeyEvent.KEYCODE_R;
                break;
            case 'S':
                keyCode = KeyEvent.KEYCODE_S;
                break;
            case 'T':
                keyCode = KeyEvent.KEYCODE_T;
                break;
            case 'U':
                keyCode = KeyEvent.KEYCODE_U;
                break;
            case 'V':
                keyCode = KeyEvent.KEYCODE_V;
                break;
            case 'W':
                keyCode = KeyEvent.KEYCODE_W;
                break;
            case 'X':
                keyCode = KeyEvent.KEYCODE_X;
                break;
            case 'Y':
                keyCode = KeyEvent.KEYCODE_Y;
                break;
            case 'Z':
                keyCode = KeyEvent.KEYCODE_Z;
                break;
            case '-':
                keyCode = KeyEvent.KEYCODE_MINUS;
                break;
        }

        return keyCode;
    }

    ;
}
