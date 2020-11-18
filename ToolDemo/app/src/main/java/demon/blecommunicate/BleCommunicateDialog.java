package demon.blecommunicate;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BleCommunicateDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.bt_service)
    Button btService;
    @BindView(R.id.bt_connect)
    Button btConnect;
    private Context context;
    private RecyclerView recyclerView;
    private BleCommunicateAdapter bleDeviceAdapter;

    private List<String> bleDeviceList;
    private String selectedBean;

    private BluetoothAdapter bluetoothAdapter;
    private BleSocketThread bleSocketThread;

    private boolean isAutoService;     // 是否自动单服务器用

    public BleCommunicateDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ble_communicate);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initAdapter();
        initBle();
    }

    public void update(List<String> list) {
        selectedBean = null;
        bleDeviceAdapter.update(null);
        bleDeviceList.clear();
        bleDeviceList.addAll(list);
        Collections.sort(bleDeviceList);
        bleDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initAdapter() {
        bleDeviceList = new ArrayList<>();
        bleDeviceAdapter = new BleCommunicateAdapter(context, bleDeviceList);
        recyclerView = findViewById(R.id.recycle_account);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(bleDeviceAdapter);
        bleDeviceAdapter.setListener(new BleCommunicateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                selectedBean = bean;
            }
        });
    }

    @OnClick(R.id.bt_connect)
    public void onConnectClicked(View v) {      // 连接蓝牙服务器
        if (StringUtil.isEmpty(selectedBean)) {
            showToast("请先选择新的分组！");
            return;
        }

        if (bleSocketThread != null) {
            onDestroy();
            btConnect.setBackgroundResource(R.drawable.shape_box_white);
            setButtonEnable(true, true);
            return;
        }


        String[] strs = selectedBean.split("=");
        if (strs.length == 2) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strs[1]);
            bleSocketThread = new BleSocketThread(device, handler);
            bleSocketThread.start();
            setButtonEnable(false, false);

        } else {
            showToast("没有选择要连接的蓝牙设备");
        }

        dismiss();
    }

    @OnClick(R.id.bt_service)
    public void onServiceClicked() {  // 开启蓝牙服务器
        if (bluetoothAdapter == null) {
            showToast("不支持蓝牙设备");
            return;
        }

        if (bleSocketThread != null) {
            onDestroy();
            btService.setBackgroundResource(R.drawable.shape_box_white);
            setButtonEnable(true, true);
            return;
        }

        bleSocketThread = new BleSocketThread(bluetoothAdapter, handler);
        bleSocketThread.start();
        setButtonEnable(false, false);
        dismiss();
    }

    private Handler handler = new Handler() {
//        private StringBuffer buffer = new StringBuffer();

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: // 显示信息
                    showToast("提示:" + msg.obj.toString());
                    break;
                case 2:
                    if (onBleCallBackListener != null) {
                        onBleCallBackListener.onCallBack(msg.obj.toString());
                    }
//                    if (buffer.length() == 0) { // 处理一段时间的信息
//                        recyclerView.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                showToast("接收:" + buffer.toString());
//                                if (buffer.length() > 0) {
//                                    buffer.delete(0, buffer.length());
//                                }
//                            }
//                        }, 500);
//                    }
//                    buffer.append(msg.obj.toString());
                    break;
                case 3: // 断开连接
                    showToast("异常:" + msg.obj.toString());
                    if (bleSocketThread != null) {
                        bleSocketThread.close();
                        bleSocketThread = null;
                    }

                    btService.setText("开启蓝牙服务器");
                    btService.setBackgroundResource(R.drawable.shape_box_white);
                    btConnect.setText("连接蓝牙服务器");
                    btConnect.setBackgroundResource(R.drawable.shape_box_white);
                    setButtonEnable(true, true);
                    showToast("断开连接");

                    if (isAutoService) {
                        onServiceClicked();
                    }
                    break;
                case 200:   // 服务端： 等待连接
                    btService.setText("关闭蓝牙服务器");
                    btService.setBackgroundResource(R.drawable.shape_box_red);
                    setButtonEnable(true, false);
                    showToast("开启蓝牙服务器完成！");
                    break;
                case 201: // 客服端： 连接服务器成功
                    btConnect.setText("断开蓝牙服务器");
                    btConnect.setBackgroundResource(R.drawable.shape_box_red);
                    setButtonEnable(false, true);
                    showToast("已连接蓝牙服务器");
                    break;
                case 202:   // 服务端： 等待蓝牙连接异常
                    showToast("等待蓝牙连接异常");
                    break;
            }
        }
    };

    private void setButtonEnable(boolean serviceEnable, boolean connectEnable) {
        if (serviceEnable) {
            btService.setVisibility(View.VISIBLE);
        } else {
            btService.setVisibility(View.INVISIBLE);
        }
        if (connectEnable) {
            btConnect.setVisibility(View.VISIBLE);
        } else {
            btConnect.setVisibility(View.INVISIBLE);
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
            ((Activity) context).startActivityForResult(enableBtIntent, 2);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() <= 0) {
            showToast("没有找到已匹配的蓝牙设备");
            return;
        }

        // 添加已匹配的设备信息
        for (BluetoothDevice device : pairedDevices) {
            bleDeviceList.add(String.format("%s=%s", device.getName(), device.getAddress()));
        }

        bleDeviceAdapter.notifyDataSetChanged();
    }

    public void setAutoService(boolean autoService) {
        isAutoService = autoService;
        if (bleSocketThread == null) {
            onServiceClicked();
        }
    }

    public void onDestroy() {
        isAutoService = false;
        if (bleSocketThread != null) {
            bleSocketThread.close();
            bleSocketThread = null;
        }
    }

    // ================   蓝牙数据发送/接收   ======================

    public void sendMsg(String msg) {
        if (bleSocketThread != null) {
            bleSocketThread.send(msg);
        }
    }

    public void sendMsg(byte[] bytes) {
        if (bleSocketThread != null) {
            bleSocketThread.send(bytes);
        }
    }

    //  ====================== 事件监听 ===========================

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
