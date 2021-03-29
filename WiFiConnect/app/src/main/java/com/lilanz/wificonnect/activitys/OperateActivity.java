package com.lilanz.wificonnect.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.adapters.ItemBeanAdapter;
import com.lilanz.wificonnect.beans.Bean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.DeviceControlBean;
import com.lilanz.wificonnect.beans.ItemBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.controls.PermissionControl;
import com.lilanz.wificonnect.controls.SoundControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.threads.FifoThread;
import com.lilanz.wificonnect.threads.WifiService;
import com.lilanz.wificonnect.utils.WiFiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperateActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;

    //    private WifiService wifiService;
    private PermissionControl permissionControl;

    @BindView(R.id.rv_function)
    protected RecyclerView recycler;
    private ItemBeanAdapter adapter;

    private List<ItemBean> beanList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        ButterKnife.bind(this);

        permissionControl = new PermissionControl(this);
        permissionControl.storagePermission();

        initUI();
        initData();
        initAdapter();
        initService();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("断开状态".equals(tvStatus.getText().toString())) {
            AppDataControl.reconnectWifiService(this);
        }
    }

    @OnClick(R.id.iv_setting)
    public void onSettingClicked() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_refresh)
    public void onRefreshClicked() {
        showToast("重连服务器!");
        ivRefresh.setEnabled(false);
        AppDataControl.reconnectWifiService(this);
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
                                case 1:
                                    tvStatus.setText("连接状态");
                                    tvStatus.setTextColor(getResources().getColor(R.color.black));
                                    break;
                                case 2:
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

    private void initAdapter() {
        adapter = new ItemBeanAdapter(this, beanList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new ItemBeanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemBean bean) {
//                if ("断开状态".equals(tvStatus.getText().toString())) {
//                    showToast("请先连接服务器！");
//                    return;
//                }

                switch (bean.name) {
                    case "逗逗你":
                        Intent intent = new Intent(OperateActivity.this, PlayVoiceActivity.class);
                        startActivity(intent);
                        break;
                    case "来点音乐":
                        intent = new Intent(OperateActivity.this, MusicActivity.class);
                        startActivity(intent);
                        break;
                    case "设备控制":
                        intent = new Intent(OperateActivity.this, DeviceManagerActivity.class);
                        startActivity(intent);
                        break;
                    case "文件传输":
                        intent = new Intent(OperateActivity.this, FileActivity.class);
                        startActivity(intent);
                        break;
                    case "WIFI设置":
                        intent = new Intent(OperateActivity.this, WifiSettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initData() {
        beanList = new ArrayList<>();
        beanList.add(new ItemBean(R.mipmap.ding_dong, "逗逗你"));
        beanList.add(new ItemBean(R.mipmap.play_music, "来点音乐"));
        beanList.add(new ItemBean(R.mipmap.device, "设备控制"));
        beanList.add(new ItemBean(R.mipmap.wifi_setting, "WIFI设置"));
//        beanList.add(new ItemBean(R.mipmap.play_music, "文件传输"));
    }

    private void initUI() {
        tvAddress.setText("ip：" + WiFiUtil.getIpAddr(this));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:     // 打开讯飞语音监听
//                    initXunFei();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (AppDataControl.wifiService != null) {
            unbindService(connection);
            AppDataControl.wifiService = null;
        }
        super.onDestroy();
    }

    private void showMsg(String msg) {
        if (tvMsg.length() > 150) {
            tvMsg.setText(msg + "\n");
        } else {
            tvMsg.append(msg + "\n");
        }
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("xunfei", msg);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    // ===================== test =========================
    private FifoThread fifoThread;

    private void initTest() {

    }

    @OnClick({R.id.tv_address, R.id.tv_status})
    public void onClicked(View v) {
        showToast("点击事件");
        if (fifoThread == null) {
            fifoThread = new FifoThread(this);
            fifoThread.start();
        }
        switch (v.getId()) {
            case R.id.tv_address:
                fifoThread.add(new Bean());
                break;
            case R.id.tv_status:
                fifoThread.close();
                fifoThread = null;
                break;
        }

    }

}
