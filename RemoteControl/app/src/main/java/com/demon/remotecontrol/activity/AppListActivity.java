package com.demon.remotecontrol.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.activity_ui.AppListUi;
import com.demon.remotecontrol.bean.MsgTypeBean;
import com.demon.remotecontrol.interfaces.OnUninstallCallback;
import com.demon.remotecontrol.socketcontrol.SocketMsgControl;
import com.demon.remotecontrol.util.StringUtil;
import com.google.gson.Gson;

import butterknife.ButterKnife;

public class AppListActivity extends AppCompatActivity {

    private AppListUi appListUi;

    private SocketMsgControl socketMsgControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ButterKnife.bind(this);

        appListUi = new AppListUi(this);
        initSocket();
    }

    private void initSocket() {
        socketMsgControl = SocketMsgControl.getInstance(this);
        socketMsgControl.setOnAppListCallback(list -> appListUi.updateUI(list));
        socketMsgControl.setOnUninstallCallback(msg -> {
            showToast(msg);
//            queryAllApp();
        });

        queryAllApp();
    }

    public void queryAllApp() {
        MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_QUERY_ALL_APP, "");
        socketMsgControl.retransmissionMsg(App.selectedDevice, new Gson().toJson(bean));
    }

    public void showUninstallDialog(String packageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("注意:")
                .setMessage("确定要删除应用：" + packageName)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MsgTypeBean bean = new MsgTypeBean(MsgTypeBean.MSG_TYPE_UNINSTALL, packageName);
                        socketMsgControl.retransmissionMsg(App.selectedDevice, new Gson().toJson(bean));
                    }
                });
        builder.create().show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}


