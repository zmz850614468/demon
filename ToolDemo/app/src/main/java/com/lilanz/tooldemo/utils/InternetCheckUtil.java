package com.lilanz.tooldemo.utils;

import android.app.Activity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InternetCheckUtil {

    /**
     * 通过ping的方式检测网络状态和速度
     *
     * @param activity
     * @param url      需要检测的网址
     * @param times    需要ping的次数
     */
    public static void checkInternet(final Activity activity, final String url, final int times) {
        Thread checkThread = new Thread() {
            @Override
            public void run() {
                super.run();

                int receiveTimes = 0;
                int timeCount = 0;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "开始网络检测", Toast.LENGTH_SHORT).show();
                    }
                });
                Runtime runtime = Runtime.getRuntime();
                Process ipProcess = null;
                try {
                    ipProcess = runtime.exec("ping -c " + times + " " + url);

                    InputStream input = ipProcess.getInputStream();

                    BufferedReader in = new BufferedReader(new InputStreamReader(input));
                    StringBuffer stringBuffer = new StringBuffer();
                    String content = "";
                    while ((content = in.readLine()) != null) {
                        stringBuffer.append(content + "\n");
                        if (content.contains("time=")) {
                            receiveTimes++;

                            int start = content.indexOf("time=") + 5;
                            String time = content.substring(start, start + 4);
                            float count = Float.parseFloat(time);
                            timeCount += count;
                        }
                    }

                    int res = ipProcess.waitFor();
                    if (res == 0) {
                        final float averageReceive = receiveTimes * 1.0f / times;     // 收包率
                        final float averageTime = timeCount * 1.0f / receiveTimes;   // 平均收包时间
                        String msg = null;
                        if (receiveTimes == 0) {
                            msg = "网络状态差，访问速度差";
                        }

                        // 收包情况大于90%；平均时间：60ms一下；100ms一下；100ms以上
                        if (averageReceive > 0.900f) {
                            if (averageTime < 60.0f) {
                                msg = "网络状态良好，访问速度良好";
                            } else if (averageTime < 100.0f) {
                                msg = "网络状态良好，访问速度一般";
                            } else {
                                msg = "网络状态良好，访问速度较差";
                            }
                            // 收包情况大于80%
                        } else if (averageReceive > 0.800f) {
                            if (averageTime < 60.0f) {
                                msg = "网络状态可用，访问速度良好";
                            } else if (averageTime < 100.0f) {
                                msg = "网络状态可用，访问速度一般";
                            } else {
                                msg = "网络状态可用，访问速度较差";
                            }
                            // 收包情况大于60%
                        } else if (averageReceive > 0.600f) {
                            if (averageTime < 60.0f) {
                                msg = "网络状态一般，访问速度良好";
                            } else if (averageTime < 100.0f) {
                                msg = "网络状态一般，访问速度一般";
                            } else {
                                msg = "网络状态一般，访问速度较差";
                            }
                            // 收包情况小于60%
                        } else {
                            if (averageTime < 60.0f) {
                                msg = "网络状态差，访问速度良好";
                            } else if (averageTime < 100.0f) {
                                msg = "网络状态差，访问速度一般";
                            } else {
                                msg = "网络状态差，访问速度较差";
                            }
                        }
                        final String finalMsg = msg;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                showMsg("收包情况:" + averageReceive);
//                                showMsg("收包平均时间:" + averageTime);
                                Toast.makeText(activity, finalMsg, Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "网络不可用", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "网络出现异常情况", Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    if (ipProcess != null) {
                        ipProcess.destroy();
                    }
                }
            }
        };
        checkThread.start();
    }

}
