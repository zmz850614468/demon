package com.lilanz.wificonnect.activity_new;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activitys.App;
import com.lilanz.wificonnect.adapters.ItemBeanAdapter;
import com.lilanz.wificonnect.beans.ItemBean;
import com.lilanz.wificonnect.controls.PermissionControl;
import com.lilanz.wificonnect.controls.XunFeiVoiceControl;
import com.lilanz.wificonnect.utils.WiFiUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 直接控制设备，不通过服务端
 */
public class ControlDeviceActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    private PermissionControl permissionControl;

    @BindView(R.id.rv_function)
    protected RecyclerView recycler;
    private ItemBeanAdapter adapter;

    private List<ItemBean> beanList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_device);
        ButterKnife.bind(this);

        permissionControl = new PermissionControl(this);
        permissionControl.storagePermission();

        initUI();
        initData();
        initAdapter();
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.iv_setting)
    public void onSettingClicked() {
        Intent intent = new Intent(this, AppSettingActivity.class);
        startActivity(intent);
    }

    private void initAdapter() {
        adapter = new ItemBeanAdapter(this, beanList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new ItemBeanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemBean bean) {
                switch (bean.name) {
                    case "设备控制":
                        Intent intent = new Intent(ControlDeviceActivity.this, HomeDeviceActivity.class);
                        startActivity(intent);
                        break;
                    case "WIFI设置":
                        intent = new Intent(ControlDeviceActivity.this, Esp8266SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void initData() {
        beanList = new ArrayList<>();
        beanList.add(new ItemBean(R.mipmap.device, "设备控制"));
        beanList.add(new ItemBean(R.mipmap.wifi_setting, "WIFI设置"));
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
                    initXunFei();
                    break;
            }
        }
    };

    private void initXunFei() {
        XunFeiVoiceControl.getInstance(ControlDeviceActivity.this).setOnOneShotResult(new XunFeiVoiceControl.OnOneShotResult() {
            @Override
            public void onResult(boolean status, String result) {
                if ("灯|热水器".contains(result)) {
                    // TODO
                }

                handler.sendEmptyMessageDelayed(1, 300);
            }
        });
        XunFeiVoiceControl.getInstance(ControlDeviceActivity.this).oneShot();
    }

    @Override
    protected void onDestroy() {
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

}
