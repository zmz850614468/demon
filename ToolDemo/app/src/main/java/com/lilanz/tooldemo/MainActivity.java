package com.lilanz.tooldemo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import demon.CopyUseActivity;

import com.lilanz.tooldemo.multiplex.activitys.ReuseActivity;
import com.lilanz.tooldemo.prints.PrintsActivity;
import com.lilanz.tooldemo.utils.internetcheck.InternetCheckUtil;

import butterknife.BindView;
import butterknife.OnClick;
import demon.controls.PermissionControl;

public class MainActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new PermissionControl(this).cameraPermission();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.e("longjiang", "wifi是否可用: " + wifiManager.isWifiEnabled());
//        initMenu();
//        String base = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        base += "/file11.pdf";
//        DocumentViewerUtil.openDocumentViewer(this, base);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.bt_internal_check, R.id.bt_reuse, R.id.bt_prints, R.id.bt_copy_use, R.id.bt_scan_wifi})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_copy_use:
                Intent intent = new Intent(this, CopyUseActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_prints:
                intent = new Intent(this, PrintsActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_reuse:
                intent = new Intent(this, ReuseActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_internal_check:
                InternetCheckUtil.internetCheck(this, "webt.lilang.com");//"webt.lilang.com" ; "www.baidu.com"
                break;
            case R.id.bt_scan_wifi:
                scanWifi();
                break;
        }
    }

    WifiManager wifiManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void scanWifi() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            if (!hasPermissionsGranted(permission)) {
            }
        }
        ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        boolean b = wifiManager.startScan();
        Log.e("longjiang", "wifiManager.startScan " + b);
        registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            for (ScanResult scanResult : wifiManager.getScanResults()) {
                Log.e("longjiang", scanResult.SSID + " - " + scanResult.BSSID + " - " + scanResult.level);
            }
        }
    };

    public boolean hasPermissionsGranted(@NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
                //此时没有授权，返回false
            }
        }
        return true;
        //已经授权过，返回true
    }

    String Tag = "MainActivity";
    final int PERMISSION_REQUEST_CODE = 1;
    String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//        Log.d(Tag,"-------onRequestPermissionsResult--------");
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!hasPermissionsGranted(permissions)) {//返回false代表申请失败
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
                this.finish();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


//    private void initMenu() {
//        SlidingMenu menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.layout_menu);
//    }

    private void initUSB() {
        UsbManager manager = (UsbManager) getSystemService(USB_SERVICE);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }

}


