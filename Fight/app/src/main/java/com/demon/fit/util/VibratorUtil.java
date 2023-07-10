package com.demon.fit.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * 手机振动功能
 */
public class VibratorUtil {


    public static void vibrator(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        long[] patter = {100, 800, 300, 800};
        vibrator.vibrate(patter, -1);
    }

    public static void vibratorLong(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        long[] patter = {100, 800, 300, 800, 300, 800};
        vibrator.vibrate(patter, -1);
    }
}
