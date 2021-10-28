package com.demon.tool.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.demon.tool.R;
import com.demon.tool.controls.PermissionControl;
import com.demon.tool.controls.ScanKeyManager;
import com.demon.tool.documentviewer.DocumentViewerActivity;
import com.demon.tool.download.DownloadActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    private ScanKeyManager scanKeyManager;

    // 去除底部导航栏
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPermission();
        initScanCode();

//        String base = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        base += "/file11.pdf";
//        base += "/file11.docx";
//        base += "/file11.xlsx";
//        showLog("path:" + base);
//        openDocumentViewer(base);
    }

    @OnClick({R.id.bt_camera, R.id.bt_document_viewer, R.id.bt_download, R.id.bt_data_save})
    public void onClicked(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_document_viewer:
                intent = new Intent(this, DocumentViewerActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_download:
                intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_data_save:
                intent = new Intent(this, SaveDataActivity.class);
                startActivity(intent);
                break;
        }
//        startActivity(intent);
    }

    /**
     * 处理设备输入问题
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // deviceId>0 的是设备 ； ==-1的是平板输入
        showLog("device:" + event.getDeviceId() + " ; key:" + event.getKeyCode());
        if (event.getDeviceId() > 0 && event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            scanKeyManager.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showLog("down device:" + event.getDeviceId() + " ; key:" + event.getKeyCode());
        return super.onKeyDown(keyCode, event);
    }

    private void initScanCode() {
        //拦截扫码器回调,获取扫码内容
        scanKeyManager = new ScanKeyManager(new ScanKeyManager.OnScanValueListener() {
            @Override
            public void onScanValue(String value) {
                showLog("code:" + value);
            }
        });
    }

    /**
     * 打开文档浏览器
     *
     * @param path
     */
    private void openDocumentViewer(@NonNull String path) {

//        Uri uri = Uri.parse(path);
//        showLog("path:" + path);
//        Uri uri = Uri.fromFile(new File(path));
//        if (uri != null) {
//            Intent intent = new Intent(this, DocumentActivity.class);
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.setData(uri);
//            startActivity(intent);
//        }

    }

    private void initPermission() {
        PermissionControl control = new PermissionControl(this);
        control.requestPermissions(new String[]{PermissionControl.STORAGE, PermissionControl.CAMERA});
//        control.storagePermission();
//        control.cameraPermission();
    }

    private void showLog(String msg) {
        Log.e("MainActivity", msg);
    }
}
