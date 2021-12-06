package com.demon.remotecontrol.bean;

/**
 * socket 具体消息类型
 */
public class MsgTypeBean {

    public static final int MSG_TYPE_INSTALL = 110;             // 安装应用
    public static final int MSG_TYPE_INSTALL_ANSWER = 111;      // 安装应用答复

    public static final int MSG_TYPE_DELETE = 120;              // 删除文件
    public static final int MSG_TYPE_DELETE_ANSWER = 121;       // 删除文件答复

    public static final int MSG_TYPE_UNINSTALL = 130;           // 卸载应用
    public static final int MSG_TYPE_UNINSTALL_ANSWER = 131;    // 删除应用答复

    public static final int MSG_TYPE_UPLOAD_FILE = 140;                // 文件传输：上传
    public static final int MSG_TYPE_UPLOAD_FILE_ANSWER = 141;         // 文件传输答复，文件内容
    public static final int MSG_TYPE_UPLOAD_FILE_NOT_FIND = 142;       // 文件未找到
    public static final int MSG_TYPE_UPLOAD_FILE_EXCEPTION = 143;      // 文件上传异常
    public static final int MSG_TYPE_UPLOAD_FILE_COMPLETE = 144;       // 文件上传完成

    public static final int MSG_TYPE_ISSUE_FILE = 150;                  // 下发文件
    public static final int MSG_TYPE_ISSUE_FILE_ANSWER = 151;           // 下发文件,开始接收文件
    public static final int MSG_TYPE_ISSUE_FILE_COMPLETE = 152;         // 下发文件，接收文件完成
    public static final int MSG_TYPE_ISSUE_FILE_RECEIVER_PROGRESS = 153;// 下发文件，接收进度

    public static final int MSG_TYPE_ALL_FILE = 160;                    // 查询文件内容
    public static final int MSG_TYPE_ALL_FILE_ANSWER = 161;             // 查询文件内容答复

    public static final int MSG_TYPE_QUERY_ALL_APP = 170;            // 查询所有安装的应用
    public static final int MSG_TYPE_QUERY_ALL_APP_ANSWER = 171;     // 查询所有安装的应用答复

    public int type;

    public String data;

    public MsgTypeBean() {
    }

    public MsgTypeBean(int type, String data) {
        this.type = type;
        this.data = data;
    }
}
