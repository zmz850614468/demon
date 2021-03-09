package com.lilanz.tooldemo.multiplex.download;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 文件下载线程
 */
public class DownloadThread extends Thread {

    public static final int MSG_UPDATE = 1;     // 更新进度
    public static final int MSG_COMPLETE = 2;   // 完成文件
    public static final int MSG_FAILURE = 3;    // 下载失败

    // 文件保存的根地址
    private static final String BASE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator
            + "IM" + File.separator;
    private static final int MAX_DOWNLOAD = 5;      // 同时最大下载个数
    private static final int SLEET_TIME = 2000;    // 睡眠时间

    private Context context;
    private DownloadListener downloadListener;
//    private DownloadControl downloadControl;

    private List<String> urlList;
    private int downloadCount;  // 正在下载的个数
    private boolean isContinue = true;
    private Handler msgHandle;

    public DownloadThread(Context context, @NonNull List<String> urlList, @NonNull Handler msgHandle) {
        this.context = context;
        this.urlList = urlList;
        this.msgHandle = msgHandle;
        initDownload();
    }

    @Override
    public void run() {
        super.run();

        while (isContinue) {
            // 1.判断是否已经最大个数
            if (downloadCount < MAX_DOWNLOAD) {
                // 2.下载文件
                if (urlList.isEmpty()) {
                    break;
                }
                updateCount(1);
                String url = urlList.remove(0);
                String fileName = getFileName(url);
                File file = new File(fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                new DownloadControl().getDownRequest(url, new File(fileName), downloadListener);
            } else {
                // 3.睡眠等待返回
                try {
                    Thread.sleep(SLEET_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新下载个数
     *
     * @param count
     */
    private synchronized void updateCount(int count) {
        downloadCount += count;
    }

    private void initDownload() {
//        DownloadControl downloadControl = new DownloadControl();
        downloadListener = new DownloadListener() {
            @Override
            public void onResponse(Call call, Response response, String downUrl, long totalLength, long alreadyDownLength) {
                Message msg = new Message();
                msg.what = MSG_UPDATE;
                Map<String, String> map = new HashMap<>();
                map.put("url", downUrl);
                if (totalLength <= 0) {
                    map.put("progress", "0");
                    map.put("maxProgress", "1");
                } else {
                    map.put("progress", alreadyDownLength + "");
                    map.put("maxProgress", totalLength + "");
                }
                msg.obj = map;
                msgHandle.sendMessage(msg);
            }

            @Override
            public void onFailure(Call call, Exception e, String downUrl) {
                updateCount(-1);
                interrupted();
                Message msg = new Message();
                msg.what = MSG_FAILURE;
                msg.obj = downUrl;
                msgHandle.sendMessage(msg);
            }

            @Override
            public void onComplete(String downUrl, String savePath) {
                updateCount(-1);
                interrupted();
                Message msg = new Message();
                msg.what = MSG_COMPLETE;
                Map<String, String> map = new HashMap<>();
                map.put("url", downUrl);
                map.put("savePath", savePath);
                msg.obj = map;
                msgHandle.sendMessage(msg);
            }
        };

        File file = new File(BASE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void close() {
        isContinue = false;
        Thread.interrupted();
    }

    /**
     * 获取文件名字
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(BASE_PATH);
        int random = (int) (Math.random() * 10000);
        buffer.append("im_").append(System.currentTimeMillis()).append("_").append(random);

        try {
            int index = url.lastIndexOf("/");
            String str = url.substring(index + 1);
            if (index > 0) {
                index = str.lastIndexOf(".");
                if (index > 0) {
                    buffer.append(str.substring(index));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

}
