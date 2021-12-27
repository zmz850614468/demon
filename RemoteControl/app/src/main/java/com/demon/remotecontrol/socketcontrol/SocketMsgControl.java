package com.demon.remotecontrol.socketcontrol;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.demon.remotecontrol.activity.App;
import com.demon.remotecontrol.activity.RemoteFileViewActivity;
import com.demon.remotecontrol.bean.AppInfoBean;
import com.demon.remotecontrol.bean.FileInfoBean;
import com.demon.remotecontrol.bean.IssueProgressBean;
import com.demon.remotecontrol.bean.MsgTypeBean;
import com.demon.remotecontrol.bean.ProgressBean;
import com.demon.remotecontrol.fileclone.FileDataBean;
import com.demon.remotecontrol.fileclone.FileReadThread;
import com.demon.remotecontrol.fileclone.FileWriteControl;
import com.demon.remotecontrol.interfaces.OnAllDeviceNoCallback;
import com.demon.remotecontrol.interfaces.OnAppListCallback;
import com.demon.remotecontrol.interfaces.OnDeleteCallback;
import com.demon.remotecontrol.interfaces.OnDeviceNoCallback;
import com.demon.remotecontrol.interfaces.OnFileDeliverCallback;
import com.demon.remotecontrol.interfaces.OnFileIssueCallback;
import com.demon.remotecontrol.interfaces.OnFileListCallback;
import com.demon.remotecontrol.interfaces.OnInstallCallback;
import com.demon.remotecontrol.interfaces.OnSocketStatusListener;
import com.demon.remotecontrol.interfaces.OnUninstallCallback;
import com.demon.remotecontrol.util.AppUtil;
import com.demon.remotecontrol.util.FileUtil;
import com.demon.remotecontrol.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Socket 消息管理类
 */
public class SocketMsgControl extends Thread {

    private static SocketMsgControl instance;

    public static SocketMsgControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new SocketMsgControl(context);
                    instance.start();
                }
            }
        }
        return instance;
    }

    private Context context;

    private SocketIoControl socketIoControl;
    private String host;
    private String deviceNo;

    private List<IssueProgressBean> issueProgressList;

    private SocketMsgControl(Context context) {
        this.context = context;
        issueProgressList = new ArrayList<>();
        isContinue = true;
        sendMsgList = new ArrayList<>();
    }

    private boolean isContinue;
    private List<String> sendMsgList;

    private synchronized void addMsg(String msg) {
        sendMsgList.add(msg);
        if (isSleeping) {
            interrupt();
        }
    }

    private synchronized String getMsg() {
        if (!sendMsgList.isEmpty()) {
            return sendMsgList.remove(0);
        }
        return "";
    }

    private boolean isSleeping;

    @Override
    public void run() {
        super.run();

        while (isContinue) {

            String msg = getMsg();
            while (!StringUtil.isEmpty(msg)) {

                if (!socketIoControl.isConnected()) {
                    showToast("socket 处于断开状态");

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                socketIoControl.sendMsg(msg);

                try {
                    sleep(9);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msg = getMsg();
            }

            try {
                isSleeping = true;
                sleep(3600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isSleeping = false;
        }
    }

    /**
     * 更新下发进度
     *
     * @param fileName
     * @param issueProgress
     * @param totalSize
     */
    private synchronized void updateIssueProgress(String fileName, int issueProgress, int totalSize) {
        if (totalSize > 0) {
            IssueProgressBean bean = new IssueProgressBean();
            bean.fileName = fileName;
            bean.issueProgress = issueProgress;
            bean.receiverProgress = 0;
            bean.totalSize = totalSize;
            issueProgressList.add(0, bean);
        } else {
            for (IssueProgressBean bean : issueProgressList) {
                if (fileName.equals(bean.fileName)) {
                    bean.issueProgress = issueProgress;
//                    showLog("下发 接收进度：" + fileName + " - " + bean.issueProgress + "/" + bean.totalSize + " ||| " + bean.receiverProgress + "/" + bean.totalSize);
                    break;
                }
            }
        }
    }

    /**
     * 跟新 远程设备的 接收进度
     *
     * @param fileName
     * @param receiverProgress
     */
    private synchronized void updateReceiverProgress(String fileName, int receiverProgress) {
        for (IssueProgressBean bean : issueProgressList) {
            if (fileName.equals(bean.fileName)) {
                bean.receiverProgress = receiverProgress;
//                showLog("下发 接收进度：" + fileName + " - " + bean.issueProgress + "/" + bean.totalSize + " ||| " + receiverProgress + "/" + bean.totalSize);
                break;
            }
        }
    }

    /**
     * 向服务器发送信息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (StringUtil.isEmpty(msg)) {
            showToast("数据不能为空");
            return;
        }

        addMsg(msg);

//        if (!socketIoControl.isConnected()) {
//            showToast("socket 处于断开状态");
//            return;
//        }
//        socketIoControl.sendMsg(msg);
    }

    public void sendBean(SocketBean bean) {
        if (bean == null) {
            showToast("数据不能为空");
            return;
        }

        addMsg(new Gson().toJson(bean));

//        if (!socketIoControl.isConnected()) {
//            showToast("socket 处于断开状态");
//            return;
//        }
//        socketIoControl.sendMsg(new Gson().toJson(bean));
    }

    /**
     * 连接服务器socket
     *
     * @param host
     * @param deviceNo
     */
    public void autoConnectSocket(String host, String deviceNo) {
        this.host = host;
        this.deviceNo = deviceNo;
        initSocketIo(this.host, this.deviceNo);
    }

    private void initSocketIo(String host, String deviceNo) {
        if (socketIoControl != null) {
            socketIoControl.close();
            socketIoControl = null;
        }

        socketIoControl = new SocketIoControl();
        socketIoControl.setOnSocketIoListener(new SocketIoControl.OnSocketIoListener() {
            @Override
            public void onConnected() {
                showLog("socket已连接");
                if (onSocketStatusListener != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onSocketStatusListener.onConnected();
                    });
                }
            }

            @Override
            public void onDisconnected() {
                showLog("socket断开连接");
                if (onSocketStatusListener != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onSocketStatusListener.onDisconnected();
                    });
                }
            }

            @Override
            public void onError(String msg) {
                showLog("socket出现错误:" + msg);
                if (onSocketStatusListener != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onSocketStatusListener.onError("socket出现错误");
                    });
                }
            }

            @Override
            public void onReceiver(String data) {
//                showLog("接收到服务器数据:" + data);
                dealSocketMsg(data);
            }
        });

        if (StringUtil.isEmpty(deviceNo)) {
            socketIoControl.connect(host);
        } else {
            socketIoControl.connect(host + "?mac=" + deviceNo);
        }
    }

    /**
     * {"msg":"[\"CSCBB19917200490\"]","from":"CSCBB19917200490","to":"server","status":3}
     * 处理服务器发过来的数据
     *
     * @param data
     */
    private void dealSocketMsg(String data) {
        SocketBean socketBean;
        try {
            socketBean = new Gson().fromJson(data, SocketBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return;
        }

        switch (socketBean.status) {
            case SocketBean.SOCKET_TYPE_NOT_FIND_DEVICE:   // 没有对应编号
//                if (context instanceof Activity) {
//                    ((Activity) context).runOnUiThread(() -> {
                showToast("没有对应的编号设备");
//                    });
//                }
                break;

//            case SocketBean.SOCKET_TYPE_DEVICE_ID:         // 服务器分配设备编号
//                deviceNo = socketBean.to;
//                SharePreferencesUtil.saveDeviceNo(context, deviceNo);
//                if (onDeviceNoCallback != null && context instanceof Activity) {
//                    ((Activity) context).runOnUiThread(() -> {
//                        onDeviceNoCallback.onResult(deviceNo);
//                    });
//                }
//                break;

            case SocketBean.SOCKET_TYPE_ALL_DEVICE:        // 获取所有设备编号
                ArrayList<String> deviceNoList = new Gson().fromJson(socketBean.msg,
                        new TypeToken<ArrayList<String>>() {
                        }.getType());
                if (onAllDeviceNoCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onAllDeviceNoCallback.onResult(deviceNoList);
                    });
                }
                break;

            case SocketBean.SOCKET_TYPE_REPEAT:         // 转发的消息
                dealDetailMsg(socketBean.from, socketBean.msg);
                break;
        }

    }

    /**
     * 处理详细的数据信息
     *
     * @param data
     */
    private void dealDetailMsg(String from, String data) {
        if (StringUtil.isEmpty(data)) {
            return;
        }

        MsgTypeBean msgTypeBean = new Gson().fromJson(data, MsgTypeBean.class);
        switch (msgTypeBean.type) {
            case MsgTypeBean.MSG_TYPE_INSTALL:          // 安装应用
                AppUtil.install(context, new File(msgTypeBean.data));
                installCallback(from, "开始安装应用");
                break;
            case MsgTypeBean.MSG_TYPE_UNINSTALL:        // 卸载应用
                AppUtil.uninstall(context, msgTypeBean.data);
                uninstallCallback(from, "开始卸载应用");
                break;
            case MsgTypeBean.MSG_TYPE_DELETE:           // 删除文件
                FileUtil.deleteFile(new File(msgTypeBean.data));
                deleteCallback(from, "删除文件完成");
                break;
            case MsgTypeBean.MSG_TYPE_UPLOAD_FILE:      // 上传文件命令
                uploadFile(from, msgTypeBean.data);
                break;
            case MsgTypeBean.MSG_TYPE_ISSUE_FILE:       // 接收下发的文件
                receiverIssueFile(from, msgTypeBean.data);
                break;
            case MsgTypeBean.MSG_TYPE_ALL_FILE:   // 查询文件夹下数据
                String path = msgTypeBean.data;
                if (RemoteFileViewActivity.ROOT_PATH.equals(path)) {
                    path = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                sendFileList(from, FileUtil.searchAllFile(path));
                break;
            case MsgTypeBean.MSG_TYPE_QUERY_ALL_APP:
                List<AppInfoBean> appInfoList = AppUtil.getInstallApp(context, false);
                MsgTypeBean msgBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_QUERY_ALL_APP_ANSWER, new Gson().toJson(appInfoList));
                retransmissionMsg(from, new Gson().toJson(msgBean));
                break;


            // -------------------------------   以下是控制端接收到的信息    ---------------------------------
            case MsgTypeBean.MSG_TYPE_ALL_FILE_ANSWER:        // 返回文件夹下的所有数据
                List<FileInfoBean> list = new Gson().fromJson(msgTypeBean.data, new TypeToken<ArrayList<FileInfoBean>>() {
                }.getType());
                if (onFileListCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        if (list == null) {
                            onFileListCallback.onResult(new ArrayList<>());
                        } else {
                            onFileListCallback.onResult(list);
                        }
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_INSTALL_ANSWER:
                if (onInstallCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onInstallCallback.onResult(msgTypeBean.data);
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_UNINSTALL_ANSWER:
                if (onUninstallCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onUninstallCallback.onResult(msgTypeBean.data);
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_DELETE_ANSWER:
                if (onDeleteCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onDeleteCallback.onResult(msgTypeBean.data);
                    });
                }
                break;

            case MsgTypeBean.MSG_TYPE_UPLOAD_FILE_ANSWER:
                FileDataBean fileDataBean = new Gson().fromJson(msgTypeBean.data, FileDataBean.class);
                FileWriteControl.getInstance(context).writeFile(fileDataBean);
//                showLog("接收到上传的文件：" + fileDataBean.fileName + " - " + fileDataBean.index + " - complete: " + fileDataBean.isLast);
                if (fileDataBean.index == 1 && onFileDeliverCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onFileDeliverCallback.onStartReceiver(fileDataBean.fileName);
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_UPLOAD_FILE_NOT_FIND:
                if (onFileDeliverCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onFileDeliverCallback.onFileNotFind(msgTypeBean.data);
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_UPLOAD_FILE_EXCEPTION:
                if (onFileDeliverCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onFileDeliverCallback.onError(msgTypeBean.data);
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_UPLOAD_FILE_COMPLETE:
                if (onFileDeliverCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onFileDeliverCallback.onComplete(msgTypeBean.data);
                    });
                }
                break;

            case MsgTypeBean.MSG_TYPE_ISSUE_FILE_ANSWER:
                if (onFileIssueCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onFileIssueCallback.onBeginReceiver(msgTypeBean.data);
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_ISSUE_FILE_COMPLETE:
                if (onFileIssueCallback != null && context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> {
                        onFileIssueCallback.onReceiverComplete(msgTypeBean.data);
                    });
                }
                break;
            case MsgTypeBean.MSG_TYPE_ISSUE_FILE_RECEIVER_PROGRESS:
                try {
                    int index = msgTypeBean.data.lastIndexOf("-");
                    if (index > 0) {
                        String fileName = msgTypeBean.data.substring(0, index);
                        int receiverProgress = Integer.parseInt(msgTypeBean.data.substring(index + 1));
                        updateReceiverProgress(fileName, receiverProgress);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;

            case MsgTypeBean.MSG_TYPE_QUERY_ALL_APP_ANSWER:
                if (onAppListCallback != null && context instanceof Activity) {
                    List<AppInfoBean> appInfoList2 = new Gson().fromJson(msgTypeBean.data, new TypeToken<ArrayList<AppInfoBean>>() {
                    }.getType());
                    ((Activity) context).runOnUiThread(() -> onAppListCallback.onResult(appInfoList2));
                }
                break;
        }

    }

    /**
     * 上传文件
     *
     * @param from
     * @param dir
     */
    private void uploadFile(String from, String dir) {
        FileReadThread fileSendThread = new FileReadThread(context, dir);
        fileSendThread.setOnSendFileListener(new FileReadThread.OnSendFileListener() {
            @Override
            public void onFileNoFind(String filePath) {
//                runOnUiThread(() -> {
//                    showToast("文件没有找到：" + filePath);
//                });
                MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_UPLOAD_FILE_NOT_FIND, "没找到要上传文件:" + dir);
                retransmissionMsg(from, new Gson().toJson(bean));
            }

            @Override
            public void onError(int code, String msg) {
                MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_UPLOAD_FILE_EXCEPTION, "上传文件异常:" + code + " - " + msg);
                retransmissionMsg(from, new Gson().toJson(bean));
            }

            @Override
            public void onSendData(FileDataBean bean) {
                MsgTypeBean msgBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_UPLOAD_FILE_ANSWER, new Gson().toJson(bean));
                retransmissionMsg(from, new Gson().toJson(msgBean));
//                                String temp = new Gson().toJson(bean);
//                                FileDataBean tepBean = new Gson().fromJson(temp, FileDataBean.class);
//                                writeFile(tepBean);

//                                writeFile(bean);
//                showLog("文件传输：" + bean.fileName + " - " + bean.index + " - " + bean.size + " - " + bean.isLast);
            }

            @Override
            public void onSendFileFinish(String filePath) {
                MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_UPLOAD_FILE_COMPLETE, "上传文件完成:" + dir);
                retransmissionMsg(from, new Gson().toJson(bean));
            }
        });
        fileSendThread.start();
    }

    /**
     * @param from
     * @param data 文件对象
     */
    private void receiverIssueFile(String from, String data) {
        FileDataBean bean = new Gson().fromJson(data, FileDataBean.class);
        FileWriteControl.getInstance(context).writeFile(bean);
        if (bean.index == 1) {
            MsgTypeBean msgTypeBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_ISSUE_FILE_ANSWER, "开始接收下发的文件：" + bean.fileName);
            retransmissionMsg(from, new Gson().toJson(msgTypeBean));
        }
        if (bean.isLast) {
            MsgTypeBean msgTypeBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_ISSUE_FILE_COMPLETE, "完成下发文件的接收：" + bean.fileName);
            retransmissionMsg(from, new Gson().toJson(msgTypeBean));

            MsgTypeBean msgTypeBean2 = new MsgTypeBean(MsgTypeBean.MSG_TYPE_ISSUE_FILE_RECEIVER_PROGRESS, bean.fileName + "-" + bean.index);
            retransmissionMsg(from, new Gson().toJson(msgTypeBean2));
        } else if (bean.index % 100 == 0) {
            MsgTypeBean msgTypeBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_ISSUE_FILE_RECEIVER_PROGRESS, bean.fileName + "-" + bean.index);
            retransmissionMsg(from, new Gson().toJson(msgTypeBean));
        }
//        showLog("接收服务器下发的文件：" + bean.fileName + " - " + bean.index + " - " + bean.isLast);
    }

    /**
     * 下发文件
     *
     * @param to
     * @param fileDir
     */
    public void issueFile(String to, String fileDir) {
        FileReadThread fileSendThread = new FileReadThread(context, fileDir);
        fileSendThread.setOnSendFileListener(new FileReadThread.OnSendFileListener() {
            @Override
            public void onFileNoFind(String filePath) {
//                if (context instanceof Activity) {
//                    ((Activity) context).runOnUiThread(() -> {
                showToast("没有找到要下发的文件");
//                    });
//                }
            }

            @Override
            public void onError(int code, String msg) {
//                if (context instanceof Activity) {
//                    ((Activity) context).runOnUiThread(() -> {
                showToast("下发文件异常：" + code + " - " + msg);
//                    });
//                }
            }

            @Override
            public void onSendData(FileDataBean bean) {
                MsgTypeBean msgBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_ISSUE_FILE, new Gson().toJson(bean));
                retransmissionMsg(to, new Gson().toJson(msgBean));
                if (bean.index == 1) {
                    updateIssueProgress(bean.fileName, bean.index, bean.packageSize);
                } else {
                    updateIssueProgress(bean.fileName, bean.index, 0);
                }
            }

            @Override
            public void onSendFileFinish(String filePath) {
//                if (context instanceof Activity) {
//                    ((Activity) context).runOnUiThread(() -> {
                showToast("下发文件完成");
//                    });
//                }
            }
        });
        fileSendThread.start();
    }

    /**
     * 发送所有查询到的文件信息
     *
     * @param list
     */
    private void sendFileList(String to, List<FileInfoBean> list) {
        MsgTypeBean msgTypeBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_ALL_FILE_ANSWER, new Gson().toJson(list));
        retransmissionMsg(to, new Gson().toJson(msgTypeBean));
    }

    /**
     * 安装答复
     *
     * @param to
     * @param msg
     */
    private void installCallback(String to, String msg) {
        MsgTypeBean msgTypeBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_INSTALL_ANSWER, msg);
        retransmissionMsg(to, new Gson().toJson(msgTypeBean));
    }

    /**
     * 卸载答复
     *
     * @param to
     * @param msg
     */
    private void uninstallCallback(String to, String msg) {
        MsgTypeBean msgTypeBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_UNINSTALL_ANSWER, msg);
        retransmissionMsg(to, new Gson().toJson(msgTypeBean));
    }

    /**
     * 删除答复
     *
     * @param to
     * @param msg
     */
    private void deleteCallback(String to, String msg) {
        MsgTypeBean msgTypeBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_DELETE_ANSWER, msg);
        retransmissionMsg(to, new Gson().toJson(msgTypeBean));
    }

    /**
     * 转发信息
     *
     * @param to
     * @param msg
     */
    public void retransmissionMsg(String to, String msg) {
        SocketBean bean = new SocketBean();
        bean.from = App.deviceId;
        bean.to = to;
        bean.command = SocketBean.SOCKET_TYPE_RETRANSMISSION;
        bean.msg = msg;

        sendBean(bean);
    }

    /**
     * 获取所有进度对象
     *
     * @return
     */
    public List<ProgressBean> getProgressList() {
        List<ProgressBean> list = new ArrayList<>();
        for (IssueProgressBean bean : issueProgressList) {
            ProgressBean progressBean = new ProgressBean();
            progressBean.createTime = bean.createTime;
            progressBean.fileName = bean.fileName;
            progressBean.isIssue = true;
            progressBean.isFinish = bean.totalSize == bean.issueProgress;
            progressBean.issueProgress = bean.issueProgress + "/" + bean.totalSize;
            progressBean.receiverProgress = bean.receiverProgress + "/" + bean.totalSize;
            list.add(progressBean);
        }
        return list;
    }

    public void close() {
        isContinue = false;
        interrupt();
        if (socketIoControl != null) {
            socketIoControl.close();
            socketIoControl = null;
        }
    }

    private OnDeviceNoCallback onDeviceNoCallback;

    private OnAllDeviceNoCallback onAllDeviceNoCallback;

    private OnSocketStatusListener onSocketStatusListener;

    private OnFileListCallback onFileListCallback;

    private OnInstallCallback onInstallCallback;
    private OnUninstallCallback onUninstallCallback;
    private OnDeleteCallback onDeleteCallback;

    private OnFileDeliverCallback onFileDeliverCallback;

    private OnFileIssueCallback onFileIssueCallback;

    private OnAppListCallback onAppListCallback;

    public void setOnAppListCallback(OnAppListCallback onAppListCallback) {
        this.onAppListCallback = onAppListCallback;
    }

    public void setOnFileIssueCallback(OnFileIssueCallback onFileIssueCallback) {
        this.onFileIssueCallback = onFileIssueCallback;
    }

    public void setOnFileDeliverCallback(OnFileDeliverCallback onFileDeliverCallback) {
        this.onFileDeliverCallback = onFileDeliverCallback;
    }

    public void setOnInstallCallback(OnInstallCallback onInstallCallback) {
        this.onInstallCallback = onInstallCallback;
    }

    public void setOnUninstallCallback(OnUninstallCallback onUninstallCallback) {
        this.onUninstallCallback = onUninstallCallback;
    }

    public void setOnDeleteCallback(OnDeleteCallback onDeleteCallback) {
        this.onDeleteCallback = onDeleteCallback;
    }

    public void setOnFileListCallback(OnFileListCallback onFileListCallback) {
        this.onFileListCallback = onFileListCallback;
    }

    public void setOnDeviceNoCallback(OnDeviceNoCallback onDeviceNoCallback) {
        this.onDeviceNoCallback = onDeviceNoCallback;
    }

    public void setOnAllDeviceNoCallback(OnAllDeviceNoCallback onAllDeviceNoCallback) {
        this.onAllDeviceNoCallback = onAllDeviceNoCallback;
    }

    public void setOnSocketStatusListener(OnSocketStatusListener onSocketStatusListener) {
        this.onSocketStatusListener = onSocketStatusListener;
    }

    private void showLog(String msg) {
        Log.e("SocketMsgControl", msg);
    }

    private void showToast(String msg) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> {
//                showToast("下发文件异常：" + code + " - " + msg);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
