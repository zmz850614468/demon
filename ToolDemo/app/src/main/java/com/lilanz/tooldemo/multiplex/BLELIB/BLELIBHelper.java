package com.lilanz.tooldemo.multiplex.BLELIB;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.lilanz.tooldemo.multiplex.BLELIB.lib.BleService;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * 蓝牙扫描、连接帮助类
 */
public class BLELIBHelper {

    private Activity activity;

    private BleService mBleService;
    private boolean mIsBind = false;

    public BLELIBHelper(Activity activity) {
        this.activity = activity;

        requestPermi();
    }

    /**
     * 蓝牙权限动态申请
     */
    private void requestPermi() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_PRIVILEGED,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

    /**
     * 绑定蓝牙服务
     */
    public void doBindService() {
        if (!mIsBind) {
            Intent serviceIntent = new Intent(activity, BleService.class);
            activity.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            showToast("已绑定蓝牙服务");
        }
    }

    /**
     * 解绑蓝牙服务
     */
    public void doUnBindService() {
        if (mIsBind) {
            activity.unbindService(serviceConnection);
            mBleService = null;
            mIsBind = false;
        } else {
            showToast("已经解绑蓝牙服务");
        }
    }

    /**
     * 发起蓝牙设备扫描功能
     *
     * @param time 蓝牙设备扫描时间：毫秒值
     */
    public void startScan(int time) {
        //打开蓝牙
        if (mBleService != null && mBleService.enableBluetooth(true)) {
            //Ble扫描回调
            mBleService.scanLeDevice(false);
            mBleService.setOnLeScanListener(leScanListener);
            mBleService.scanLeDevice(true, time);
        } else {
            showToast("蓝牙服务不能为null");
        }
    }

    /**
     * 接收蓝牙扫描功能
     */
    public void stopScan() {
        if (mBleService != null) {
            mBleService.scanLeDevice(false);
        }
    }

    /**
     * 蓝牙服务连接监听
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIsBind = true;
            mBleService = ((BleService.LocalBinder) service).getService();
            //Ble初始化操作
            if (mBleService.initialize()) {
                showToast("蓝牙绑定成功");
            } else {
                showToast("不支持蓝牙");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBleService = null;
            mIsBind = false;
        }
    };

    /**
     * 蓝牙设备扫描监听
     */
    private BleService.OnLeScanListener leScanListener = new BleService.OnLeScanListener() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            //每当扫描到一个Ble设备时就会返回，（扫描结果重复的库中已处理）
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device.getName() != null && bleListener != null) {
                        bleListener.onScanBack(device);
                    }
                }
            });
        }
    };

    /**
     * 连接指定的蓝牙设备
     *
     * @param name    蓝牙设备名称
     * @param address 蓝牙设备地址
     */
    public void connect(String name, String address) {
        if (mBleService != null) {
            mBleService.setOnConnectListener(connectionStateChangeListener);
            mBleService.setOnServicesDiscoveredListener(discoveredListener);
            mBleService.setOnDataAvailableListener(dataAvailableListener);
            //设置连接监听
            //连接Ble
            mBleService.connect(address);
            showToast("连接ble设备：" + address);
        } else {
            showToast("蓝牙服务为null");
        }
    }

    /**
     * 蓝牙连接状态监听
     */
    private BleService.OnConnectionStateChangeListener connectionStateChangeListener = new BleService.OnConnectionStateChangeListener() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                showToast("Ble连接已断开");
            } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                showToast("Ble正在连接");
            } else if (newState == BluetoothProfile.STATE_CONNECTED) {
                showToast("Ble已连接");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                showToast("Ble正在断开连接");
            } else {
                showToast(status + ";" + newState);
            }
        }
    };

    /**
     * Ble服务发现监听
     */
    private BleService.OnServicesDiscoveredListener discoveredListener = new BleService.OnServicesDiscoveredListener() {
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, final int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //查找服务
                findAndInitService(gatt);
            } else {

            }
        }
    };

    private UUID read_UUID_chara;
    private UUID read_UUID_service;
    private UUID write_UUID_chara;
    private UUID write_UUID_service;
    private UUID notify_UUID_chara;
    private UUID notify_UUID_service;
    private UUID indicate_UUID_chara;
    private UUID indicate_UUID_service;

    int count = 0;
    int writeCount = 0;

    /**
     * 查找并初始化蓝牙服务
     *
     * @param gatt
     */
    public void findAndInitService(BluetoothGatt gatt) {
        List<BluetoothGattService> bluetoothGattServices = gatt.getServices();
//        showMsg("GATT服务总数:" + bluetoothGattServices.size());

        for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
            List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    count++;
                    read_UUID_chara = characteristic.getUuid();
                    read_UUID_service = bluetoothGattService.getUuid();
                    Log.e("TAG", count + " read_chara=" + read_UUID_chara + "----read_service=" + read_UUID_service);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    writeCount++;
                    write_UUID_chara = characteristic.getUuid();
                    write_UUID_service = bluetoothGattService.getUuid();
                    Log.e("TAG", writeCount + " write_chara=" + write_UUID_chara + "----write_service=" + write_UUID_service);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                    write_UUID_chara = characteristic.getUuid();
                    write_UUID_service = bluetoothGattService.getUuid();
                    Log.e("TAG", "write_chara=" + write_UUID_chara + "----write_service=" + write_UUID_service);

                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    notify_UUID_chara = characteristic.getUuid();
                    notify_UUID_service = bluetoothGattService.getUuid();
                    mBleService.setCharacteristicNotification(characteristic, true);
                    Log.e("TAG", "notify_chara=" + notify_UUID_chara + "----notify_service=" + notify_UUID_service);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    indicate_UUID_chara = characteristic.getUuid();
                    indicate_UUID_service = bluetoothGattService.getUuid();
                    Log.e("TAG", "indicate_chara=" + indicate_UUID_chara + "----indicate_service=" + indicate_UUID_service);

                }
            }
        }
    }

    /**
     * Ble数据回调监听
     */
    private BleService.OnDataAvailableListener dataAvailableListener = new BleService.OnDataAvailableListener() {
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //处理特性读取返回的数据
            showToast("处理特性读取返回的数据");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            //处理通知返回的数据
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (bleListener != null) {
                            bleListener.onDataBack(new String(characteristic.getValue(), "gbk"));
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            showToast("onDescriptorRead");
        }
    };

    private void showToast(String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 服务是否处于连接状态
     *
     * @return
     */
    public boolean isConnect() {
        return mBleService.isConnect();
    }

    /**
     * 端口蓝牙服务连接
     */
    public void disConnect() {
        if (isConnect()) {
            mBleService.disconnect();
        }
    }

    private OnBLEListener bleListener;

    public void setOnBleListener(OnBLEListener bleListener) {
        this.bleListener = bleListener;
    }

}
