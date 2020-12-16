package demon.BLELib;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.junkchen.blelib.BleService;
import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BLELibDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.tv_ble_scan)
    protected TextView tvBleScan;
    @BindView(R.id.tv_connect)
    protected TextView tvConnect;
    @BindView(R.id.rv_ble_device)
    protected RecyclerView recyclerView;
    private BLELibAdapter bleDeviceAdapter;

    public BLELibHelper blelibHelper;

    private Map<String, String> bleMap = new HashMap<>();

    private Context context;
    private List<String> bleDeviceList;
    private String selectedDevice;

    public BLELibDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ble_device);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initBLEHelper();
        initUI();
    }

    /**
     * 断开连接后，重新初始化
     */
    public void onResume() {
        if (blelibHelper != null && !blelibHelper.isConnect()) {
            initBLEHelper();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initUI() {
        bleDeviceList = new ArrayList<>();
        bleDeviceAdapter = new BLELibAdapter(context, bleDeviceList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(bleDeviceAdapter);
        bleDeviceAdapter.setListener(new BLELibAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                selectedDevice = bean;
            }
        });
    }

    @OnClick(R.id.tv_ble_scan)
    public void onBleScanClicked(View v) {
        if (blelibHelper.isScaning()) {
            blelibHelper.stopScan();
        } else {
            selectedDevice = null;
            bleDeviceAdapter.setSelectedInde(-1);
            bleDeviceList.clear();
            bleDeviceAdapter.notifyDataSetChanged();
            blelibHelper.startScan(10000);
            showToast("蓝牙设备扫描中...");
        }
    }

    @OnClick(R.id.tv_connect)
    public void onConnectClicked(View v) {
        if (StringUtil.isEmpty(selectedDevice)) {
            showToast("请先选择新的分组！");
            return;
        }

        boolean isConnected = blelibHelper.isConnect();
        if (isConnected) {
            blelibHelper.disConnect();
            tvConnect.setBackgroundResource(R.drawable.shape_box_white);
            tvBleScan.setVisibility(View.VISIBLE);
        } else {
            if (!StringUtil.isEmpty(selectedDevice)) {
                String address = bleMap.get(selectedDevice);
                // 发起蓝牙设备连接
                blelibHelper.connect(selectedDevice, address);
                tvConnect.setBackgroundResource(R.drawable.shape_box_gray);
                tvConnect.setText("连接蓝牙设备中...");
                tvConnect.setEnabled(false);

                tvBleScan.setVisibility(View.INVISIBLE);
            }
        }


        dismiss();
    }

    private void initBLEHelper() {
        blelibHelper = new BLELibHelper(((Activity) context));
        blelibHelper.setOnBleListener(new OnBLEListener() {
            @Override
            public void onDataBack(String data) {
                super.onDataBack(data);
                if (onBleCallBackListener != null) {
                    onBleCallBackListener.onCallBack(data);
                }
            }

            @Override
            public void onScanBack(BluetoothDevice device) {
                super.onScanBack(device);
                String name = device.getName();
                String address = device.getAddress();
                if (!StringUtil.isEmpty(name) && !bleDeviceList.contains(name)) {
                    bleMap.put(name, address);
                    bleDeviceList.add(name);
                    bleDeviceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onConnected() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvConnect.setEnabled(true);
                        tvConnect.setBackgroundResource(R.drawable.shape_box_red);
                        tvConnect.setText("断开蓝牙设备");
                    }
                });
            }

            @Override
            public void onDisConnected() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvConnect.setEnabled(true);
                        tvConnect.setBackgroundResource(R.drawable.shape_box_white);
                        tvConnect.setText("连接蓝牙设备");
                        tvBleScan.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        blelibHelper.doBindService();
    }

    public void send(byte[] bytes) {
        blelibHelper.send(bytes);
    }

    public void send(String msg) {
        blelibHelper.send(msg);
    }

    public void onDestroy() {
        if (blelibHelper != null) {
            blelibHelper.doUnBindService();
        }
    }

    //  ====================== 时间监听 ===========================

    private OnBleCallBackListener onBleCallBackListener;

    public void setOnBleCallBackListener(OnBleCallBackListener onBleCallBackListener) {
        this.onBleCallBackListener = onBleCallBackListener;
    }

    public interface OnBleCallBackListener {
        void onCallBack(String msg);
    }


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
