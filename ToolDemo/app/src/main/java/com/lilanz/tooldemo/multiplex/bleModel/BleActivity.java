package com.lilanz.tooldemo.multiplex.bleModel;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.bleModel.bleCommunicate.BleSocketThread;
import com.lilanz.tooldemo.utils.SharePreferencesUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择已适配的 蓝牙地址
 */
public class BleActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.iv_back)
    protected ImageView ivBack;
    @BindView(R.id.iv_setting)
    protected ImageView ivSetting;

    @BindView(R.id.et_msg)
    protected EditText etMsg;

    @BindView(R.id.recycler_ble)
    protected RecyclerView recyclerBle;
    private BleAdapter bleAdapter;

    private List<String> bleList;

    private BluetoothAdapter bluetoothAdapter;
    private BleSocketThread bleSocketThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ble);
        ButterKnife.bind(this);
        initUI();
        initBle();
        initScanAdapter();

        // 注册广播接收器，以获取蓝牙设备搜索结果
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    @OnClick({R.id.iv_back, R.id.iv_setting, R.id.bt_wait_connect, R.id.bt_connect, R.id.bt_send,
            R.id.bt_disconnect, R.id.bt_open_ble_scan})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_setting:
                ;// TODO 
                break;
            case R.id.bt_wait_connect:      // 等待连接
                if (bluetoothAdapter == null) {
                    showToast("不支持蓝牙设备");
                    return;
                }
                bleSocketThread = new BleSocketThread(bluetoothAdapter, handler);
                bleSocketThread.start();
                break;
            case R.id.bt_connect:           // 发起连接
                String address = SharePreferencesUtil.getBleAddress(this);
                String[] strs = address.split("=");
                if (strs.length == 2) {
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strs[1]);
                    bleSocketThread = new BleSocketThread(device, handler);
                    bleSocketThread.start();
                } else {
                    showToast("没有选择要连接的蓝牙设备");
                }
                break;
            case R.id.bt_send:              // 发送
                String msg = etMsg.getText().toString();
                if (StringUtil.isEmpty(msg)) {
                    showToast("发送数据不能为空！");
                    return;
                }
                if (bleSocketThread != null) {
                    showMsg("发送:" + msg);
                    bleSocketThread.send(msg);
                }
                break;
            case R.id.bt_disconnect:
                if (bleSocketThread != null) {
                    bleSocketThread.close();
                    bleSocketThread = null;
                }
                break;
            case R.id.bt_open_ble_scan:
                layoutScan.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initBle() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            showToast("不支持蓝牙设备");
            return;
        }

        // 开启蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0) {
            showToast("没有找到已匹配的蓝牙设备");
            return;
        }

        // 添加已匹配的设备信息
        bleList = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            bleList.add(String.format("%s=%s", device.getName(), device.getAddress()));
        }

        initAdapter();
    }

    private void initAdapter() {

        bleAdapter = new BleAdapter(this, bleList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerBle.setLayoutManager(manager);
        recyclerBle.setAdapter(bleAdapter);

        bleAdapter.setListener(new BleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                showToast("选择的蓝牙：" + bean);
                SharePreferencesUtil.saveBleAddress(BleActivity.this, bean);
            }
        });
    }

    private void initUI() {
        tvTitle.setText("已匹配蓝牙设备");
        ivBack.setVisibility(View.VISIBLE);
        ivSetting.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (bleSocketThread != null) {
            bleSocketThread.close();
            bleSocketThread = null;
        }
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        private StringBuffer buffer = new StringBuffer();

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:// 显示信息
                    showMsg("提示:" + msg.obj.toString());
                    break;
                case 2:
                    if (buffer.length() == 0) {
                        tvMsg.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showMsg("接收:" + buffer.toString());
                                if (buffer.length() > 0) {
                                    buffer.delete(0, buffer.length());
                                }
                            }
                        }, 500);
                    }
                    buffer.append(msg.obj.toString());
                    break;
                case 3:
                    showMsg("异常:" + msg.obj.toString());
                    if (bleSocketThread != null) {
                        bleSocketThread.close();
                        bleSocketThread = null;
                    }
                    break;
            }
        }
    };

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // =======================================蓝牙搜索===============================================

    @BindView(R.id.layout_scan)
    protected ViewGroup layoutScan;

    @BindView(R.id.cycler_ble_scan)
    protected RecyclerView recyclerBleScan;
    private BleAdapter bleScanAdapter;

    private List<String> bleScanList;

    @OnClick({R.id.bt_hide, R.id.bt_start_scan, R.id.bt_stop_scan})
    public void onScanClick(View v) {
        switch (v.getId()) {
            case R.id.bt_hide:
                layoutScan.setVisibility(View.GONE);
                break;
            case R.id.bt_start_scan:
                if (!bluetoothAdapter.isDiscovering()) {
                    bleScanList.clear();
                    bleScanAdapter.notifyDataSetChanged();
                    bluetoothAdapter.startDiscovery();
                } else {
                    showToast("正在进行蓝牙搜索，请等待");
                }
                break;
            case R.id.bt_stop_scan:
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                break;
        }
    }

    /**
     * 蓝牙搜索监听广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String str = device.getName() + "=" + device.getAddress();
                bleScanList.add(0, str);
                bleScanAdapter.notifyDataSetChanged();

                String address = SharePreferencesUtil.getBleAddress(BleActivity.this);
                String[] strs = address.split("=");
                if (strs.length == 2 && strs[1].equals(device.getAddress())) {
                    showToast("查找到匹配蓝牙");
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }

                    // 连接蓝牙地址
                    bleSocketThread = new BleSocketThread(device, handler);
                    bleSocketThread.start();
                }
            }
        }
    };

    private void initScanAdapter() {
        bleScanList = new ArrayList<>();

        bleScanAdapter = new BleAdapter(this, bleScanList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerBleScan.setLayoutManager(manager);
        recyclerBleScan.setAdapter(bleScanAdapter);

        bleScanAdapter.setListener(new BleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                showToast("选择的蓝牙：" + bean);
//                SharePreferencesUtil.saveBleAddress(BleActivity.this, bean);
            }
        });
    }

}
