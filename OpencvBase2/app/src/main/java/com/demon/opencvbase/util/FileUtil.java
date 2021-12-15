package com.demon.opencvbase.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FileUtil {

    /**
     * 保存数据到文件中
     *
     * @param context
     * @param filePath
     * @param list
     */
    public static void save2File(Context context, final String filePath, final List list) {
        new Thread() {
            @Override
            public void run() {
                super.run();

                FileWriter writer = null;
                try {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    file = new File(file, "peiFangDB.txt");
                    file.createNewFile();

                    writer = new FileWriter(file);
                    writer.write(new Gson().toJson(list));
                    writer.flush();
                    writer.close();
                    Log.e("fileUtil", "保存数据到文件 完成");
                } catch (Exception e) {
                    e.printStackTrace();
                    if (writer != null) {
                        try {
                            writer.flush();
                            writer.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 更新文件信息到数据库中去
     *
     * @param context
     * @param path
     */
    public static void updateDBFromFile(Context context, String path) {
        File file = new File(path);
        if (file.exists() && file.canRead()) {
            try {
                char[] chArr = new char[2048];
                StringBuffer buffer = new StringBuffer();
                FileReader reader = new FileReader(file);
                while (true) {
                    int length = reader.read(chArr);
                    if (length > 0) {
                        buffer.append(chArr, 0, length);
                    } else {
                        break;
                    }
                }
                if (buffer.length() > 0) {
                    // TODO
//                    List<> list = new Gson().fromJson(buffer.toString(), new TypeToken<List<>>() {
//                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param dir  文件夹地址
     * @param name 文件名，包含后缀
     * @return null：文件不存在或创建失败
     */
    public static File createIfNotExit(String dir, String name) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (file.canWrite()) {
            file = new File(file, name);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return file;
        }

        return null;
    }

    /**
     * 向文件中写入数据
     *
     * @param file    文件地址
     * @param content 要写入的内容
     * @return true:写入成功；否则 false
     */
    public static boolean writeFile(File file, String content) {
        return writeFile(file, content, false);
    }

    /**
     * 向文件尾部添加新的数据
     *
     * @param file    文件地址
     * @param content 要写入的内容
     * @return true:写入成功；否则 false
     */
    public static boolean appendFile(File file, String content) {
        return writeFile(file, content, true);
    }

    /**
     * 向文件写入内容
     *
     * @param file     文件地址
     * @param content  要写入的内容
     * @param isAppend
     * @return true:写入成功；否则 false
     */
    private static boolean writeFile(File file, String content, boolean isAppend) {
        if (!file.exists() || !file.canWrite()) {
            return false;
        }

        try {
            FileWriter fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 获取当前文件夹大小，不递归子文件夹
     *
     * @param file
     * @return
     */
    public static long getCurrentFolderSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    //跳过子文件夹

                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定文件夹下的所有数据
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    /**
     * @param path   网络图片地址
     * @param target 本地目标地址：文件夹必须存在
     * @return
     * @throws Exception
     */
    public static String downFile(String path, File target) throws Exception {
        // 从网络上获取图片
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        if (conn.getResponseCode() == 200) {

            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            return target.getAbsolutePath();
        }
        return null;
    }

}
