package com.lilanz.tooldemo.multiplex.BLELIB;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.junkchen.blelib.BleService;
import com.lilanz.tooldemo.R;

import java.util.ArrayList;
import java.util.List;

public class BleScanActivity extends Activity implements View.OnClickListener {

    private RecyclerView recyclerBle;
    private BleScanAdapter bleScanAdapter;
    private List<String> bleScanList;

    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivSetting;

    private Button btStartScan;
    private Button btStopScan;
    private Button btOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scan);

        initTitle();
        initUI();
        initAdapter();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BleService.ACTION_BLUETOOTH_DEVICE);
        filter.addAction(BleService.ACTION_SCAN_FINISHED);
        registerReceiver(bleReceiver, filter);
    }

    private BroadcastReceiver bleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BleService.ACTION_BLUETOOTH_DEVICE)) {
                String name = intent.getStringExtra("name");
                String address = intent.getStringExtra("address");
                bleScanList.add(name + "=" + address);
                bleScanAdapter.notifyDataSetChanged();
            } else if (intent.getAction().equals(BleService.ACTION_SCAN_FINISHED)) {
                //扫描Ble设备停止
                showToast("蓝牙扫描结束");
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_start_scan:
                bleScanList.clear();
                bleScanAdapter.notifyDataSetChanged();
                BLELIBActivity.blelibHelper.startScan(10000);
                break;
            case R.id.bt_stop_scan:
                BLELIBActivity.blelibHelper.stopScan();
                break;
            case R.id.bt_ok:
                ;
                break;
        }
    }

    private void initAdapter() {
        recyclerBle = findViewById(R.id.recycler_ble);
        bleScanList = new ArrayList<>();

        bleScanAdapter = new BleScanAdapter(this, bleScanList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerBle.setLayoutManager(manager);
        recyclerBle.setAdapter(bleScanAdapter);

        bleScanAdapter.setListener(new BleScanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                // TODO 点击事件
            }
        });
    }

    private void initTitle() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivSetting = findViewById(R.id.iv_setting);
        ivSetting.setVisibility(View.GONE);
        tvTitle.setText("蓝牙选择");
        ivBack.setOnClickListener(this);
    }

    private void initUI() {
        btStartScan = findViewById(R.id.bt_start_scan);
        btStopScan = findViewById(R.id.bt_stop_scan);
        btOk = findViewById(R.id.bt_ok);
        btStartScan.setOnClickListener(this);
        btStopScan.setOnClickListener(this);
        btOk.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bleReceiver);
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
