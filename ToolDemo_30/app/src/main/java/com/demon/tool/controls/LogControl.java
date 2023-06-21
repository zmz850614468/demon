package com.demon.tool.controls;

import android.app.AlertDialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录日志信息，用于调试时展示
 */
public class LogControl {

    private static int MAX_COUNT = 40;  // 最大存放的数据条数
    private static LogControl instance;
    private static List<String> logList;
    private static Object o = new Object();

    private LogControl() {
        logList = new ArrayList<>();
    }

    public static LogControl getInstance() {
        if (instance == null) {
            synchronized (o) {
                if (instance == null) {
                    instance = new LogControl();
                }
            }
            o = null;
        }
        return instance;
    }

    /**
     * 添加日志信息
     *
     * @param log
     */
    public static void addLog(String log) {
        if (logList.size() >= MAX_COUNT) {
            logList.remove(0);
        }
        logList.add(log);
    }

    /**
     * 获取所有的日志信息,最新顺序 新--旧 排序
     *
     * @return
     */
    public static String getLog() {
        StringBuffer buffer = new StringBuffer();
        for (int i = logList.size() - 1; i >= 0; i--) {
            buffer.append(logList.get(i)).append("\n\r");
        }
        return buffer.toString();
    }

    /**
     * 显示所有日志信息
     *
     * @param context
     */
    public static void showLog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("日志信息")
                .setMessage(getLog())
                .setPositiveButton("确定", (dialog, which) -> {
                });
        builder.create().show();
    }

    public static void setMaxCount(int maxCount) {
        MAX_COUNT = maxCount;
    }
}
