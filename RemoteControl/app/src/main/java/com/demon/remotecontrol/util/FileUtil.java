package com.demon.remotecontrol.util;

import com.demon.remotecontrol.bean.FileInfoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    /**
     * 查询文件
     *
     * @param path
     * @return
     */
    public static List<FileInfoBean> searchAllFile(String path) {
        List<FileInfoBean> fileInfoList = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null) {
            FileInfoBean bean;
            for (File file1 : files) {
                bean = new FileInfoBean();
                bean.fileName = file1.getName();
                if (bean.fileName.startsWith(".")) {
                    continue;
                }
                bean.fileDir = file1.getAbsolutePath();
                bean.isFile = file1.isFile();

                fileInfoList.add(bean);
            }
        }

        Collections.sort(fileInfoList, (o1, o2) -> {
            if (o1.isFile == o2.isFile) {
                return o1.fileName.compareTo(o2.fileName);
            }

            if (!o1.isFile) {
                return -1;
            }

            return 1;
        });

        return fileInfoList;
    }

    /**
     * 删除指定文件夹下的所有数据
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
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
}
