package com.demon.remotecontrol.bean;

/**
 * 上传文件进度对象
 */
public class UploadProgressBean {

    public String fileName;

    public int totalSize;

    public int progress;

    public long createTime;

    public UploadProgressBean() {
        createTime = System.currentTimeMillis();
    }

}
