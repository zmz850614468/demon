package com.demon.remotecontrol.fileclone;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件发送线程
 */
public class FileReadThread extends Thread {

    private Context context;
    private String filePath;

    public FileReadThread(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        super.run();

        File file = new File(filePath);
        if (!file.exists()) {
            showLog("文件不存在");
            if (onSendFileListener != null) {
                onSendFileListener.onFileNoFind(filePath);
            }
            return;
        }

        InputStream inputStream = null;
        try {
            int readSize = 3 * 1024;
            byte[] inputByte = new byte[readSize];
            byte[] resultByte;
            int readCount;
            int index = 0;
            FileDataBean bean;
            inputStream = new FileInputStream(file);
            while (inputStream.available() > 0) {
                bean = new FileDataBean();
                bean.fileName = file.getName();
                index++;
                if (index == 1) {
                    int packageSize = inputStream.available() / (readSize);
                    if (inputStream.available() % (readSize) > 0) {
                        packageSize++;
                    }
                    bean.packageSize = packageSize;
                }

                readCount = inputStream.read(inputByte);

                if (inputStream.available() <= 0) {  // 是否是最后一个数据包
                    bean.isLast = true;
                }

                resultByte = new byte[readCount];
                System.arraycopy(inputByte, 0, resultByte, 0, readCount);

                bean.setData(index, resultByte, readCount);

                if (onSendFileListener != null) {
                    onSendFileListener.onSendData(bean);
                }
                try {
                    Thread.sleep(10);
//                    Thread.sleep(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (onSendFileListener != null) {
                onSendFileListener.onFileNoFind(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (onSendFileListener != null) {
                onSendFileListener.onError(-1, "文件读写异常：" + e.getMessage());
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (onSendFileListener != null) {
            onSendFileListener.onSendFileFinish(filePath);
        }
    }

    private OnSendFileListener onSendFileListener;

    public void setOnSendFileListener(OnSendFileListener onSendFileListener) {
        this.onSendFileListener = onSendFileListener;
    }

    public interface OnSendFileListener {
        void onFileNoFind(String filePath);

        void onError(int code, String msg);

        void onSendData(FileDataBean bean);

        void onSendFileFinish(String filePath);
    }

    private void showLog(String msg) {
        Log.e("FileSendThread", msg);
    }
}
