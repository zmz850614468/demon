package com.lilanz.tooldemo.multiplex.BLELIB;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.BLELIB.lib.BleService;
import com.lilanz.tooldemo.multiplex.bleModel.BleAdapter;

import java.util.ArrayList;
import java.util.List;

public class BLELIBActivity extends Activity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private BleAdapter bleAdapter;
    private List<String> bleList;

    private BleService mBleService;
    private boolean mIsBind = false;

    public static BLELIBHelper blelibHelper;

    private Button btBindService;
    private Button btUnbindService;
    private Button btStartScan;
    private Button btStopScan;
    private TextView tvMsg;

    private String bleAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_lib);

        initBLEHelper();
        initUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bind_service:
                blelibHelper.doBindService();
                break;
            case R.id.bt_unbind_service:
                blelibHelper.doUnBindService();
                tvMsg.setText("");
                break;
            case R.id.bt_start_scan:
                bleList.clear();
                bleAdapter.notifyDataSetChanged();
                Intent intent = new Intent(this, BleScanActivity.class);
                startActivity(intent);
                tvMsg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blelibHelper.startScan(10000);
                    }
                }, 1000);
                break;
            case R.id.bt_stop_scan:
                blelibHelper.stopScan();
                tvMsg.setText("");
                break;
        }
    }

    private void initBLEHelper() {
        blelibHelper = new BLELIBHelper(this);
        blelibHelper.setOnBleListener(new OnBLEListener() {
            @Override
            public void onDataBack(String data) {
                super.onDataBack(data);
                showMsg(data);
            }

            @Override
            public void onScanBack(BluetoothDevice device) {
                super.onScanBack(device);
                bleList.add(device.getName() + "=" + device.getAddress());
                bleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initUI() {
        tvMsg = findViewById(R.id.tv_msg);
        btBindService = findViewById(R.id.bt_bind_service);
        btUnbindService = findViewById(R.id.bt_unbind_service);
        btStartScan = findViewById(R.id.bt_start_scan);
        btStopScan = findViewById(R.id.bt_stop_scan);
        btStopScan.setOnClickListener(this);
        btBindService.setOnClickListener(this);
        btUnbindService.setOnClickListener(this);
        btStartScan.setOnClickListener(this);
        recyclerView = findViewById(R.id.rv_ble_device);

        bleList = new ArrayList<>();
        bleAdapter = new BleAdapter(this, bleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bleAdapter);
        bleAdapter.setListener(new BleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                String[] strings = content.split("=");
                // 发起蓝牙设备连接
//                connect(strings[0], strings[1]);
                blelibHelper.connect(strings[0], strings[1]);
                bleAddress = strings[1];
            }
        });
    }

    @Override
    protected void onDestroy() {
        blelibHelper = null;
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }
}
