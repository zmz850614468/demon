package com.demon.remotecontrol.fileclone;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileWriteThread extends Thread {

    private Context context;
    private String filePath;
    private boolean isContinue;

    private List<FileDataBean> fileDataList;

    public FileWriteThread(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
        fileDataList = new ArrayList<>();
        isContinue = true;
    }

    public synchronized void addBean(FileDataBean bean) {
        fileDataList.add(bean);
    }

    public synchronized FileDataBean getBean() {
        if (fileDataList.isEmpty()) {
            return null;
        }

        return fileDataList.remove(0);
    }

    @Override
    public void run() {
        super.run();

        File file = new File(filePath);
        if (!file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                if (onFileWriteListener != null) {
                    onFileWriteListener.onError(-1, "创建文件异常：" + e.getMessage());
                }
                return;
            }
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            if (onFileWriteListener != null) {
                onFileWriteListener.onError(-1, "文件打开异常：" + e.getMessage());
            }
            return;
        }

        FileDataBean bean;
        while (isContinue) {
            bean = getBean();
            if (bean != null) {
                try {
                    outputStream.write(bean.data, 0, bean.size);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (onFileWriteListener != null) {
                        onFileWriteListener.onError(-1, "文件写入异常：" + e.getMessage());
                    }
                    break;
                }

                if (onFileWriteListener != null) {
                    onFileWriteListener.onProgress(bean.fileName, bean.index);
                }

                if (bean.isLast) {
                    break;
                }

                continue;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (onFileWriteListener != null) {
            onFileWriteListener.onWriteComplete(filePath);
        }
//        showLog("文件写入完成：" + filePath);
    }

    private OnFileWriteListener onFileWriteListener;

    public void setOnFileWriteListener(OnFileWriteListener onFileWriteListener) {
        this.onFileWriteListener = onFileWriteListener;
    }

    public interface OnFileWriteListener {
        void onError(int code, String msg);

        void onProgress(String fileName, int index);

        void onWriteComplete(String filePath);
    }

    public void close() {
        isContinue = false;
        interrupt();
    }

    private void showLog(String msg) {
        Log.e("FileReceiverThread", msg);
    }
}
