package com.demon.remotecontrol.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.activity_ui.RemoteFileViewerUi;
import com.demon.remotecontrol.bean.FileInfoBean;
import com.demon.remotecontrol.bean.MsgTypeBean;
import com.demon.remotecontrol.fileclone.FileDataBean;
import com.demon.remotecontrol.fileclone.FileReadThread;
import com.demon.remotecontrol.interfaces.OnFileDeliverCallback;
import com.demon.remotecontrol.socketcontrol.SocketBean;
import com.demon.remotecontrol.socketcontrol.SocketMsgControl;
import com.google.gson.Gson;

import butterknife.ButterKnife;

public class RemoteFileViewActivity extends AppCompatActivity {

    public static final String ROOT_PATH = "root";  // 根目录

    private RemoteFileViewerUi remoteFileViewerUi;

    private SocketMsgControl socketMsgControl;
    private String rootPath;
    private String selectedPath;
    private boolean isRequesting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);
        ButterKnife.bind(this);

        remoteFileViewerUi = new RemoteFileViewerUi(this);

        initSocket();
    }

    @Override
    public void onBackPressed() {
        if (rootPath != null && !rootPath.equals(selectedPath)) {
            int index = selectedPath.lastIndexOf("/");
            selectedPath = selectedPath.substring(0, index + 1);
            queryPath(selectedPath);
            return;
        }

        super.onBackPressed();
    }

    private void initSocket() {
        socketMsgControl = SocketMsgControl.getInstance(this);
        socketMsgControl.setOnFileListCallback(list -> {
            // 查询文件列表回调
            isRequesting = false;
            if (rootPath == null && !list.isEmpty()) {
                rootPath = list.get(0).fileDir;
                int index = rootPath.lastIndexOf("/");
                rootPath = rootPath.substring(0, index + 1);
                selectedPath = rootPath;
            }
            remoteFileViewerUi.updateUI(selectedPath, list);
        });
        // 安装应用回调
        socketMsgControl.setOnInstallCallback(this::showToast);
        socketMsgControl.setOnDeleteCallback(msg -> {
            // 删除文件回调
            showToast(msg);
            queryPath(selectedPath);
        });
        socketMsgControl.setOnFileDeliverCallback(new OnFileDeliverCallback() {
            @Override
            public void onFileNotFind(String msg) {
                showToast(msg);
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }

            @Override
            public void onComplete(String msg) {
                showToast(msg);
            }

            @Override
            public void onStartReceiver(String data) {
                showToast("开始接收上传的文件" + data);
            }
        });

        queryPath(ROOT_PATH);
    }

    /**
     * 请求对应路径的所有文件信息
     *
     * @param path
     */
    public void queryPath(String path) {
        if (isRequesting) {
            showToast("在请求网络数据，请稍后重试");
        }

        isRequesting = true;
        if (rootPath != null) {
            selectedPath = path;
        }

        MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_ALL_FILE, path);

//        SocketBean socketBean = new SocketBean();
//        socketBean.from = App.deviceId;
//        socketBean.to = App.selectedDevice;
//        socketBean.command = SocketBean.SOCKET_TYPE_RETRANSMISSION;
//        socketBean.msg = new Gson().toJson(bean);
//
//        socketMsgControl.sendBean(socketBean);
        sendMsg(SocketBean.SOCKET_TYPE_RETRANSMISSION, new Gson().toJson(bean));
    }

    /**
     * 安装应用请求
     *
     * @param fileInfoBean
     */
    public void installApp(FileInfoBean fileInfoBean) {
        MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_INSTALL, fileInfoBean.fileDir);
        sendMsg(SocketBean.SOCKET_TYPE_RETRANSMISSION, new Gson().toJson(bean));
    }

    public void deleteFile(FileInfoBean fileInfoBean) {
        MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_DELETE, fileInfoBean.fileDir);
        sendMsg(SocketBean.SOCKET_TYPE_RETRANSMISSION, new Gson().toJson(bean));
    }

    /**
     * 传输文件请求
     *
     * @param fileInfoBean
     */
    public void deliverFile(FileInfoBean fileInfoBean) {
        MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_UPLOAD_FILE, fileInfoBean.fileDir);
        sendMsg(SocketBean.SOCKET_TYPE_RETRANSMISSION, new Gson().toJson(bean));
    }

    private void sendMsg(int command, String msg) {
        SocketBean socketBean = new SocketBean();
        socketBean.from = App.deviceId;
        socketBean.to = App.selectedDevice;
        socketBean.command = command;
        socketBean.msg = msg;

        socketMsgControl.sendBean(socketBean);
    }

    /**
     * 发送文件
     *
     * @param bean
     */
    public void sendFile(FileInfoBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("注意:")
                .setMessage("确定要传输文件：" + bean.fileName)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FileReadThread fileSendThread = new FileReadThread(RemoteFileViewActivity.this, bean.fileDir);
                        fileSendThread.setOnSendFileListener(new FileReadThread.OnSendFileListener() {
                            @Override
                            public void onFileNoFind(String filePath) {
                                runOnUiThread(() -> {
                                    showToast("文件没有找到：" + filePath);
                                });
                            }

                            @Override
                            public void onError(int code, String msg) {
                                runOnUiThread(() -> {
                                    showToast(code + " - " + msg);
                                });
                            }

                            @Override
                            public void onSendData(FileDataBean bean) {
//                                String temp = new Gson().toJson(bean);
//                                FileDataBean tepBean = new Gson().fromJson(temp, FileDataBean.class);
//                                writeFile(tepBean);

//                                writeFile(bean);
                                // todo
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("这里需要通过socket实现 ");
                                    }
                                });

                                showLog("文件传输：" + bean.fileName + " - " + bean.index + " - " + bean.size + " - " + bean.isLast);
                            }

                            @Override
                            public void onSendFileFinish(String filePath) {
                                runOnUiThread(() -> {
                                    showToast("文件传输完成：" + filePath);
                                });
                            }
                        });
                        fileSendThread.start();
                    }
                });
        builder.create().show();
    }

    public void showInstallDialog(FileInfoBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("应用安装")
                .setMessage("是否要安装应用：" + bean.fileName)
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    installApp(bean);
//                    AppUtil.install(this, new File(bean.fileDir));
                });

        builder.create().show();
    }

    public void showDeleteDialog(FileInfoBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("文件删除")
                .setMessage("是否要删除文件：" + bean.fileName)
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    deleteFile(bean);
//                    AppUtil.install(this, new File(bean.fileDir));
                });

        builder.create().show();
    }

    public void showFileDeliverDialog(FileInfoBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("文件传输")
                .setMessage("是否要传输文件：" + bean.fileName)
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    deliverFile(bean);
//                    AppUtil.install(this, new File(bean.fileDir));
                });

        builder.create().show();
    }


//    private FileWriteThread fileReceiverThread;
//
//    /**
//     * 写入文件
//     *
//     * @param bean
//     */
//    public void writeFile(FileDataBean bean) {
//        if (bean.index == 1) {
//            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
//            path += File.separator + bean.fileName;
//            fileReceiverThread = new FileWriteThread(this, path);
//            fileReceiverThread.setOnFileWriteListener(new FileWriteThread.OnFileWriteListener() {
//                @Override
//                public void onError(int code, String msg) {
//                    showLog(code + " - " + msg);
//                }
//
//                @Override
//                public void onWriteComplete(String filePath) {
//                    showLog("文件写入完成");
//                    showLog("文件写入完成：" + filePath);
//                }
//            });
//            fileReceiverThread.start();
//        }
//
//        fileReceiverThread.addBean(bean);
//    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FileViewActivity", msg);
    }
}
