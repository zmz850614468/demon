package com.demon.remotecontrol.fileclone;

import java.io.Serializable;

/**
 * 文件对象类
 */
public class FileDataBean {

    public String fileName; // 文件名
    public int index;       // 数据包序号
    public boolean isLast;  // 是否是尾包
    public byte[] data;     // 数据包内容
    public int size;        // 数据包大小
    public int packageSize; // 数据包个数

    public void setData(int index, byte[] data, int size) {
        this.index = index;
        this.data = data;
        this.size = size;
    }

}
