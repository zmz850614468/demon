package com.lilanz.wificonnect.activity_new;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.adapters.SearchDeviceAdapter;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.data.myenum.DeviceType;
import com.lilanz.wificonnect.utils.LocalNetworkUtil;
import com.lilanz.wificonnect.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查找wifi设备
 */
public class SearchDeviceActivity extends AppCompatActivity {

    public static boolean needUpdate = false;

    @BindView(R.id.layout_progress)
    RelativeLayout layoutProgress;

    @BindView(R.id.rv_device)
    protected RecyclerView recycler;
    private SearchDeviceAdapter adapter;

    private List<DeviceBean> newDeviceList;
    private List<DeviceBean> oldDeviceList;
    private boolean canRefresh = false;

    private DeviceBean selectedDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);
        ButterKnife.bind(this);

        refreshIp();
        initUI();
        initData();
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needUpdate) {
            oldDeviceList = DBControl.quaryAll(this, DeviceBean.class);
            needUpdate = false;
            newDeviceList.remove(selectedDevice);
            adapter.notifyDataSetChanged();
        }
    }

    private Handler msgHandle = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                Map<String, String> map = LocalNetworkUtil.readArp();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Esp8266ControlBean bean = new Esp8266ControlBean();
                    bean.ip = entry.getKey();
                    bean.port = 81;
                    bean.control = "deviceType~";
                    DeviceOkSocketControl.getInstance(SearchDeviceActivity.this).sendControlMsg(bean);
                }
            } else if (msg.what == 10) {
                layoutProgress.setVisibility(View.GONE);
                canRefresh = true;
                if (newDeviceList.isEmpty()) {
                    showToast("没有找到新的设备");
                }
            }
            return false;
        }
    });

    @OnClick(R.id.iv_refresh)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
                if (canRefresh) {
                    newDeviceList.clear();
                    adapter.notifyDataSetChanged();
                    canRefresh = false;
                    refreshIp();
                } else {
                    showToast("已经在努力搜索中...");
                }
                break;
        }
    }

    private void refreshIp() {
        layoutProgress.setVisibility(View.VISIBLE);
        msgHandle.sendEmptyMessageDelayed(10, 4000);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    LocalNetworkUtil.sendUdpMsg();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                msgHandle.sendEmptyMessage(1);
            }
        }.start();
    }

    private void initAdapter() {
        newDeviceList = new ArrayList<>();

        adapter = new SearchDeviceAdapter(this, newDeviceList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new SearchDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeviceBean bean) {
                selectedDevice = bean;
                Intent intent = new Intent(SearchDeviceActivity.this, AddDeviceActivity.class);
                intent.putExtra("action", AddDeviceActivity.ACTION_ADD_NEW);
                intent.putExtra("device", bean.toString());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        oldDeviceList = DBControl.quaryAll(this, DeviceBean.class);
    }

    private void initUI() {
        DeviceOkSocketControl.getInstance(this).addListener(onStatusListener);
    }

    private DeviceOkSocketControl.OnSocketListener onStatusListener = new DeviceOkSocketControl.OnSocketListener() {
        @Override
        public void onMsgCallback(String ip, String msg) {
            showLog(ip + " : " + msg);
            if (!StringUtil.isEmpty(ip) && !StringUtil.isEmpty(ip)) {
                for (DeviceBean bean : oldDeviceList) {
                    if (ip.equals(bean.ip)) {
                        return;
                    }
                }

                for (DeviceBean bean : newDeviceList) {
                    if (ip.equals(bean.ip)) {
                        return;
                    }
                }

                DeviceBean bean = new DeviceBean();
                bean.ip = ip;
                bean.port = 81;
                switch (msg) {
                    case "lamp":
                        bean.deviceType = DeviceType.LAMP;
                        break;
                    case "heatWater":
                        bean.deviceType = DeviceType.WATER_HEATER;
                        break;
                    case "electricFan":
                        bean.deviceType = DeviceType.ELECTRIC_FAN;
                        break;
                }
                newDeviceList.add(bean);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onDisconnect(String ip) {
        }
    };

    @Override
    protected void onDestroy() {
        DeviceOkSocketControl.getInstance(this).removeListener(onStatusListener);
        super.onDestroy();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("ip", msg);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
