package com.demon.fit.learningK;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 计算结果
 */
public class CalculateResult {

    /**
     * @param list String格式 "1 XXX XXX" 或 "-1 XXX XXX"
     */
    public static String getResult(@NonNull List<String> list) {
        int total = 0;
        int succeedCount = 0;
        float result = 0;
        for (String s : list) {
            if (s == null) {
                continue;
            }

            String[] tempArr = s.split(" ");
            if (tempArr.length == 3) {// 只处理满足条件的数据
                total++;
                try {
                    int dir = Integer.parseInt(tempArr[0]);
                    float in = Float.parseFloat(tempArr[1]);
                    float out = Float.parseFloat(tempArr[2]);
                    float tempRes = (out - in) * dir;
                    if (tempRes > 0) {
                        succeedCount++;
                    }
                    result += tempRes;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String resultInfo = "";
        resultInfo += "总数据量：" + list.size() + "\n";
        resultInfo += "有效数据量：" + total + "\n";
        resultInfo += String.format("成功率：%.2f", 1.0f * succeedCount / total * 100) + "\n";
        resultInfo += "数据结果：" + result * 10 + "\n";
        resultInfo += "预计费用：" + total * 10 + "\n";

        return resultInfo;
    }


    private static void showLog(String msg) {
        Log.e("K-Result", msg);
    }

}
