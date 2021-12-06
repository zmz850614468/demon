package com.demon.remotecontrol.fileclone;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demon.remotecontrol.bean.ProgressBean;
import com.demon.remotecontrol.bean.UploadProgressBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 写文件 控制类
 */
public class FileWriteControl {

    private static FileWriteControl instance;

    public static FileWriteControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new FileWriteControl(context);
                }
            }
        }
        return instance;
    }

    private Map<String, FileWriteThread> fileWriteMap;
    private Context context;
    private List<UploadProgressBean> uploadProgressList;

    private FileWriteControl(Context context) {
        this.context = context;
        fileWriteMap = new HashMap<>();
        uploadProgressList = new ArrayList<>();
    }

    private synchronized void put(String key, FileWriteThread fileWriteThread) {
        fileWriteMap.put(key, fileWriteThread);
    }

    private synchronized FileWriteThread get(String key) {
        return fileWriteMap.get(key);
    }

    private synchronized void remove(String key) {
        fileWriteMap.remove(key);
    }

    /**
     * 更新文件写进度信息
     *
     * @param fileName
     * @param index
     * @param totalSize
     */
    private synchronized void updateFileWriteProgress(String fileName, int index, int totalSize) {
        if (totalSize > 0) {
            UploadProgressBean bean = new UploadProgressBean();
            bean.fileName = fileName;
            bean.totalSize = totalSize;
            bean.progress = 0;
            uploadProgressList.add(0, bean);
        } else {
            for (UploadProgressBean bean : uploadProgressList) {
                if (fileName.equals(bean.fileName)) {
                    bean.progress = index;
//                    showLog("上传进度：" + bean.fileName + " - " + bean.progress + "/" + bean.totalSize);
                    break;
                }
            }
        }
    }

    /**
     * 写文件
     *
     * @param bean
     */
    public void writeFile(FileDataBean bean) {
        FileWriteThread fileWriteThread;
        if (!fileWriteMap.containsKey(bean.fileName)) {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            path += File.separator + bean.fileName;
            fileWriteThread = new FileWriteThread(context, path);
            fileWriteThread.setOnFileWriteListener(new FileWriteThread.OnFileWriteListener() {
                @Override
                public void onError(int code, String msg) {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> showToast(code + " - " + msg));
                    }
                }

                @Override
                public void onProgress(String fileName, int index) {
                    updateFileWriteProgress(fileName, index, 0);
                }

                @Override
                public void onWriteComplete(String filePath) {
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() -> showToast("文件接收完成：" + filePath));
                    }
                    remove(new File(filePath).getName());
                }
            });
            fileWriteThread.start();
            put(bean.fileName, fileWriteThread);
            updateFileWriteProgress(bean.fileName, 0, bean.packageSize);
//            fileWriteMap.put(bean.fileName, fileWriteThread);
        } else {
            fileWriteThread = get(bean.fileName);
//            fileWriteThread = fileWriteMap.get(bean.fileName);
        }

        fileWriteThread.addBean(bean);
    }

    /**
     * 获取所有进度对象
     *
     * @return
     */
    public List<ProgressBean> getProgressList() {
        List<ProgressBean> list = new ArrayList<>();
        for (UploadProgressBean bean : uploadProgressList) {
            ProgressBean progressBean = new ProgressBean();
            progressBean.createTime = bean.createTime;
            progressBean.fileName = bean.fileName;
            progressBean.isIssue = false;
            progressBean.issueProgress = "--";
            progressBean.receiverProgress = bean.progress + "/" + bean.totalSize;
            list.add(progressBean);
        }
        return list;
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FileWriteControl", msg);
    }
}
