package com.demon.remotecontrol.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.activity_ui.LocalFileViewerUi;
import com.demon.remotecontrol.bean.FileInfoBean;
import com.demon.remotecontrol.fileclone.FileDataBean;
import com.demon.remotecontrol.fileclone.FileWriteThread;
import com.demon.remotecontrol.fileclone.FileReadThread;
import com.demon.remotecontrol.interfaces.OnFileIssueCallback;
import com.demon.remotecontrol.socketcontrol.SocketMsgControl;
import com.demon.remotecontrol.util.AppUtil;

import java.io.File;

import butterknife.ButterKnife;

/**
 * 本地文件浏览，只能
 */
public class LocalFileViewActivity extends AppCompatActivity {

    private LocalFileViewerUi fileViewerUi;

    private SocketMsgControl socketMsgControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_viewer);
        ButterKnife.bind(this);

        fileViewerUi = new LocalFileViewerUi(this);
        initSocket();
    }

    @Override
    public void onBackPressed() {
        if (fileViewerUi.goBack()) {
            return;
        }

        super.onBackPressed();
    }

    /**
     * 发送文件
     *
     * @param bean
     */
    public void showIssueFileDialog(FileInfoBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("注意:")
                .setMessage("确定要传输文件：" + bean.fileName)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SocketMsgControl.getInstance(LocalFileViewActivity.this).issueFile(App.selectedDevice, bean.fileDir);
                    }
                });
        builder.create().show();
    }

    private void initSocket() {
        socketMsgControl = SocketMsgControl.getInstance(this);
        socketMsgControl.setOnFileIssueCallback(new OnFileIssueCallback() {
            @Override
            public void onBeginReceiver(String msg) {
                showToast(msg);
            }

            @Override
            public void onReceiverComplete(String msg) {
                showToast(msg);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FileViewActivity", msg);
    }
}
