package com.lilanz.wificonnect.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

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


}
