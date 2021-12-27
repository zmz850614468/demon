package com.demon.remotecontrol.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.activity.App;
import com.demon.remotecontrol.bean.FileInfoBean;
import com.demon.remotecontrol.bean.MsgTypeBean;
import com.demon.remotecontrol.bean.ProgressBean;
import com.demon.remotecontrol.fileclone.FileWriteControl;
import com.demon.remotecontrol.socketcontrol.SocketBean;
import com.demon.remotecontrol.socketcontrol.SocketMsgControl;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgressDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.rv_ble_device)
    protected RecyclerView recyclerView;
    private ProgressAdapter progressAdapter;

    private Context context;
    private List<ProgressBean> progressList;

    private Timer updateTimer;

    public ProgressDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initUI();
    }

    @Override
    public void show() {
        super.show();
        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {

            private boolean isFirst;

            @Override
            public void run() {

                progressList.clear();
                progressList.addAll(FileWriteControl.getInstance(context).getProgressList());
                progressList.addAll(SocketMsgControl.getInstance(context).getProgressList());
                Collections.sort(progressList, (o1, o2) -> (int) (o2.createTime - o1.createTime));
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> progressAdapter.notifyDataSetChanged());
                }

                boolean isAllFinish = true;
                for (ProgressBean progressBean : progressList) {
                    if (!progressBean.isFinish) {
                        isAllFinish = false;
                        break;
                    }
                }

                if (isAllFinish) {
                    if (!isFirst) {
                        isFirst = true;
                    } else {
                        updateTimer.cancel();
                        updateTimer = null;
                    }
                }
            }
        }, 0, 300);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initUI() {
        progressList = new ArrayList<>();
        progressAdapter = new ProgressAdapter(context, progressList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(progressAdapter);

        progressAdapter.setListener(bean -> {
            if (bean.fileName.endsWith(".apk") && bean.isIssue
                    && bean.issueProgress.equals(bean.receiverProgress)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("注意:")
                        .setMessage("确定要安装应用：" + bean.fileName)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                                path += File.separator + bean.fileName;
                                MsgTypeBean msgBean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_INSTALL, path);
                                sendMsg(SocketBean.SOCKET_TYPE_RETRANSMISSION, new Gson().toJson(msgBean));
                            }
                        });
                builder.create().show();

            }
        });
    }

    private void sendMsg(int command, String msg) {
        SocketBean socketBean = new SocketBean();
        socketBean.from = App.deviceId;
        socketBean.to = App.selectedDevice;
        socketBean.command = command;
        socketBean.msg = msg;

        SocketMsgControl.getInstance(context).sendBean(socketBean);
    }

//    private AlertDialog alertDialog;
//
//    private void initDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                .setTitle("注意:")
//                .setNegativeButton("取消", null)
//                .setPositiveButton("确定", null);
//        alertDialog = builder.create();
//        alertDialog.show();
//        alertDialog.dismiss();
//    }

    //  ====================== 时间监听 ===========================


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
