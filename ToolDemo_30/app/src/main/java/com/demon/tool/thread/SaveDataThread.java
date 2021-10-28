package com.demon.tool.thread;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 保存数据到本地文件夹
 */
public class SaveDataThread extends Thread {

    public static final int SLEEP_TIME = 60000;                 // 固定时间间隔保存一次数据
    public static final int DELETE_TIME = 7 * 24 * 3600000;     // 只保存最近时间点以内的数据，其他数据删除
//    public static final int DELETE_TIME = 60000;              // 只保存最近时间点以内的数据，其他数据删除

    /**
     * 数据保存的根目录:根目录只有一个
     */
    private static final String BASE_FILE = Environment.DIRECTORY_DOCUMENTS;

    /**
     * 子文件夹：可以有多个子文件夹
     */
    public static final String CHILE_FILE_1 = "TestData";
    public static final String CHILE_FILE_2 = "TestData2";

    private static SaveDataThread instance;

    /**
     * 没有多线程问题，不需要加锁
     *
     * @return
     */
    public static SaveDataThread getInstance() {
        if (instance == null) {
            instance = new SaveDataThread();
        }
        return instance;
    }

    private List<DataBean> dataBeanList;
    private boolean isContinue;

    private SaveDataThread() {
        dataBeanList = new ArrayList<>();
        isContinue = true;
    }

    /**
     * 添加需要保存的数据对象
     *
     * @param bean
     */
    public synchronized void addData(DataBean bean) {
        dataBeanList.add(bean);
        showLog("添加数据");
    }

    @Override
    public void run() {
        super.run();

        deleteTimeOutData(CHILE_FILE_1);
        deleteTimeOutData(CHILE_FILE_2);

        String tempChildFile = null;
        String tempFileName = null;
        List<DataBean> tempList = new ArrayList<>();
        while (isContinue || !dataBeanList.isEmpty()) {
            if (dataBeanList.isEmpty()) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                StringBuffer buffer = new StringBuffer();
                synchronized (this) {   // 取出所有相同文件下的所有数据
                    tempChildFile = dataBeanList.get(0).childFile;
                    tempFileName = dataBeanList.get(0).fileName;
                    tempList.clear();
                    for (DataBean dataBean : dataBeanList) {
                        if (dataBean.childFile.equals(tempChildFile) && dataBean.fileName.equals(tempFileName)) {
                            buffer.append(dataBean.dataStr).append("\n");
                            tempList.add(dataBean);
                        }
                    }

                    dataBeanList.removeAll(tempList);
                }

                FileWriter fileWriter = null;
                try {
                    File file = new File(Environment.getExternalStoragePublicDirectory(BASE_FILE), tempChildFile);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    file = new File(file, tempFileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fileWriter = new FileWriter(file, true);
                    fileWriter.append(buffer.toString());
                    fileWriter.flush();
                    fileWriter.close();
                    showLog("保存数据到文件夹中");

                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (fileWriter != null) {
                        try {
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除超时的数据
     */
    private void deleteTimeOutData(String childFile) {
        File file = new File(Environment.getExternalStoragePublicDirectory(BASE_FILE), childFile);
        if (file.exists()) {
            File[] files = file.listFiles();
            long curTime = System.currentTimeMillis();
            for (File f : files) {
//                showLog(f.getName() + " : " + StringUtil.getDate(f.lastModified()));
                if (f.isFile() && (curTime - f.lastModified()) > DELETE_TIME) {
//                    showLog("删除数据：" + childFile + "/" + f.getName());
                    f.delete();
                }
            }
        }
    }

    public void close() {
        isContinue = false;
        interrupt();
    }

    public static class DataBean {
        public String childFile;    // 子文件夹名称
        public String fileName;     // 文件名
        public String dataStr;      // 需要保存的数据

        public DataBean(String childFile, String fileName, String dataStr) {
            this.childFile = childFile;
            this.fileName = fileName;
            this.dataStr = dataStr;
        }
    }

    private void showLog(String msg) {
        Log.e("SaveDataThread", msg);
    }
}
