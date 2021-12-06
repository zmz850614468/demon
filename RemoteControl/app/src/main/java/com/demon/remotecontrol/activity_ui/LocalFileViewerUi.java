package com.demon.remotecontrol.activity_ui;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.activity.LocalFileViewActivity;
import com.demon.remotecontrol.adapter.FileViewerAdapter;
import com.demon.remotecontrol.bean.FileInfoBean;
import com.demon.remotecontrol.dialog.ProgressDialog;
import com.demon.remotecontrol.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalFileViewerUi {

    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.tv_file_dir)
    TextView tvFileDir;

    @BindView(R.id.rv_file_viewer)
    protected RecyclerView fileViewerRecycler;
    private FileViewerAdapter fileViewerAdapter;

    private List<FileInfoBean> fileViewerList;
    private LocalFileViewActivity activity;

    private String baseFileDir;
    private String currentFileDir;

    private ProgressDialog progressDialog;

    public LocalFileViewerUi(LocalFileViewActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initDialog();
        initUI();
        initAdapter();

        baseFileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        showFile(baseFileDir);
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

    private void showFile(String path) {
        currentFileDir = path;
        tvFileDir.setText("路径：" + currentFileDir);
        File file = new File(path);
        File[] files = file.listFiles();
        fileViewerList.clear();
        if (files != null) {
            FileInfoBean bean;
            for (File file1 : files) {
                bean = new FileInfoBean();
                bean.fileName = file1.getName();
                if (bean.fileName.startsWith(".")) {
                    continue;
                }
                bean.fileDir = file1.getAbsolutePath();
                bean.isFile = file1.isFile();
                fileViewerList.add(bean);
            }
        }

        Collections.sort(fileViewerList, (o1, o2) -> {
            if (o1.isFile == o2.isFile) {
                return o1.fileName.compareTo(o2.fileName);
            }

            if (!o1.isFile) {
                return -1;
            }

            return 1;
        });

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
                    showFile(bean.fileDir);
                } else {            // 文件
                    activity.showIssueFileDialog(bean);
                }
            }

            @Override
            public void onDeleteCLick(FileInfoBean bean) {
                if (!bean.isFile) { // 文件夹
                    showFile(bean.fileDir);
                } else {            // 文件
                    activity.showIssueFileDialog(bean);
                }
            }
        });
    }

    private void initUI() {
        tvDevice.setText("本地设备");
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(activity, R.style.DialogStyleOne);
        progressDialog.show();
        progressDialog.dismiss();
    }

    public boolean goBack() {
        File file = new File(currentFileDir);
        String parentPath = file.getParent();
        if (!StringUtil.isEmpty(parentPath) && !baseFileDir.equals(currentFileDir)) {
            showFile(parentPath);
            return true;
        }

        return false;
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FileViewerUi", msg);
    }

}
