package com.lilanz.wificonnect.activitys;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.controls.MediaControl;
import com.lilanz.wificonnect.controls.PermissionControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.threads.WifiService;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.lilanz.wificonnect.utils.StringUtil;
import com.lilanz.wificonnect.utils.WiFiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends AppCompatActivity {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.tv_msg)
    protected TextView tvMsg;
    @BindView(R.id.et_msg)
    protected EditText etMsg;

    //    private WifiService wifiService;
    private PermissionControl permissionControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
        permissionControl = new PermissionControl(this);
        permissionControl.storagePermission();

        initService();
    }

    @OnClick(R.id.iv_setting)
    public void onSettingClicked() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_refresh)
    public void onRefreshClicked() {
        tvMsg.setText("");
        ivRefresh.setEnabled(false);
        if (AppDataControl.wifiService != null) {
            AppDataControl.wifiService.close();
            AppDataControl.wifiService.startService(SharePreferencesUtil.getServicePort(this));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivRefresh.setEnabled(true);
            }
        }, 1000);
    }

    private void initService() {
        Intent intent = new Intent(this, WifiService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppDataControl.wifiService = ((WifiService.WifiBind) service).getService();
            AppDataControl.wifiService.setListener(new WifiService.OnDataCallbackListener() {
                @Override
                public void onCallBack(int type, String msg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case 0:
                                    showMsg(msg);
                                    break;
                                case 3:
                                    tvStatus.setText("连接状态");
                                    tvStatus.setTextColor(getResources().getColor(R.color.black));
                                    break;
                                case 4:
                                    tvStatus.setText("断开状态");
                                    tvStatus.setTextColor(getResources().getColor(R.color.red));
                                    break;
                            }
                        }
                    });
                }
            });
            onRefreshClicked();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AppDataControl.wifiService = null;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showMsg(requestCode + " : " + new Gson().toJson(permissions) + " = " + new Gson().toJson(grantResults));
    }


    @OnClick(R.id.tv_send_msg)
    public void onSendMsgClicked(View v) {
        String msg = etMsg.getText().toString();
        if ("".equals(msg)) {
            showMsg("信息不能为空");
        } else {
            if (AppDataControl.wifiService != null) {
                AppDataControl.wifiService.sendMsg(new MsgBean(2, msg).toString());
            }
        }
        etMsg.setText("");
    }

    private void initUI() {
        String ipAddr = WiFiUtil.getIpAddr(this);
        tvAddress.setText("ip:" + ipAddr);
    }

    @Override
    protected void onDestroy() {
        if (AppDataControl.wifiService != null) {
            unbindService(connection);
            AppDataControl.wifiService = null;
        }
        super.onDestroy();
    }

    private void showMsg(String msg) {
        if (tvMsg.length() > 100) {
            tvMsg.setText(msg + "\n");
        } else {
            tvMsg.append(msg + "\n");
        }
    }

}
