package com.demon.remotecontrol.bean;

/**
 * 下发文件进度对象
 */
public class IssueProgressBean {

    public String fileName;

    public int totalSize;

    public int issueProgress;       // 下发进度

    public int receiverProgress;    // 接收进度

    public long createTime;

    public IssueProgressBean() {
        createTime = System.currentTimeMillis();
    }
}
