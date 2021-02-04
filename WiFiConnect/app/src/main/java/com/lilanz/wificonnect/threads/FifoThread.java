package com.lilanz.wificonnect.threads;

import android.content.Context;
import android.util.Log;

import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.beans.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FifoThread extends Thread {

    private static final int SLEET_TIME = 1000;     // 睡眠时间间隔

    private Context context;
    private boolean isContinue = true;      // 是否继续循环执行任务
    private boolean canInterrupt = false;   // 是否可以打断当前的睡眠状态


    private List<Bean> list;

    public FifoThread(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    /**
     * 添加新的对象
     *
     * @param bean
     */
    public synchronized void add(Bean bean) {
        boolean needWakeUp = false;
        if (list.isEmpty() || list.get(0).tipTime > bean.tipTime) {
            needWakeUp = true;
        }

        list.add(bean);
        Collections.sort(list, new Comparator<Bean>() {
            @Override
            public int compare(Bean o1, Bean o2) {
                return (int) (o1.tipTime - o2.tipTime);
            }
        });

        showLog("添加任务：" + list.size());
        if (canInterrupt || needWakeUp) {
            showLog("准备叫醒线程");
            interrupt();
        }
    }

    /**
     * 获取对象并执行
     */
    public synchronized Bean getOne() {
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }

    @Override
    public void run() {
        super.run();

        long sleetTime = 0;
        while (isContinue) {
            Bean bean = getOne();
            if (bean != null) {
                // TODO 执行任务

                try {
                    showLog("准备进入定时睡眠:" + list.size());
                    Thread.sleep(SLEET_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            if (!isContinue) {  // 中途中断线程，则直接结束任务
                break;
            }

            try {   // 进入等待状态，等待被叫醒
                showLog("准备进入睡眠");
                canInterrupt = true;
                Thread.sleep(24 * 60 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canInterrupt = false;
            showLog("线程被叫醒");
        }
        showLog("结束线程");
    }

    public void close() {
        isContinue = false;
        if (canInterrupt) {
            interrupt();
        }
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("fifoThread", msg);
        }
    }
}
