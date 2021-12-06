package com.demon.remotecontrol.activity_ui;

import android.app.AlertDialog;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.activity.RemoteFileViewActivity;
import com.demon.remotecontrol.adapter.FileViewerAdapter;
import com.demon.remotecontrol.bean.FileInfoBean;
import com.demon.remotecontrol.dialog.ProgressDialog;
import com.demon.remotecontrol.util.AppUtil;
import com.demon.remotecontrol.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemoteFileViewerUi {

    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.tv_file_dir)
    TextView tvFileDir;

    @BindView(R.id.rv_file_viewer)
    protected RecyclerView fileViewerRecycler;
    private FileViewerAdapter fileViewerAdapter;

    private List<FileInfoBean> fileViewerList;
    private RemoteFileViewActivity activity;

    private String baseFileDir;
    private String currentFileDir;

    private ProgressDialog progressDialog;

    public RemoteFileViewerUi(RemoteFileViewActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initDialog();
        initUI();
        initAdapter();
    }

    @OnClick(R.id.bt_progress)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_progress:
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                } else {
                    progressDialog.show();
                }
                break;
        }
    }

    public void updateUI(String parentPath, List<FileInfoBean> list) {
        tvFileDir.setText(parentPath);

        fileViewerList.clear();
        fileViewerList.addAll(list);
        fileViewerAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        fileViewerList = new ArrayList<>();

        fileViewerAdapter = new FileViewerAdapter(activity, fileViewerList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        fileViewerRecycler.setLayoutManager(manager);
        fileViewerRecycler.setAdapter(fileViewerAdapter);

        fileViewerAdapter.setListener(new FileViewerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FileInfoBean bean) {
                if (!bean.isFile) { // 文件夹
                    activity.queryPath(bean.fileDir);
                } else {            // 文件
                    if (bean.fileDir.endsWith(".apk")) {
                        activity.showInstallDialog(bean);
                    } else {
//                        activity.sendFile(bean);
                        activity.showFileDeliverDialog(bean);
                    }
                }
            }

            @Override
            public void onDeleteCLick(FileInfoBean bean) {
                if (!bean.isFile) { // 文件夹
                    activity.queryPath(bean.fileDir);
                } else {            // 文件
                    activity.showDeleteDialog(bean);
                }
            }
        });
    }

    private void initUI() {
        tvDevice.setText("远程设备");
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(activity, R.style.DialogStyleOne);
        progressDialog.show();
        progressDialog.dismiss();
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FileViewerUi", msg);
    }

}
