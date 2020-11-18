package demon.BLELib;

import android.bluetooth.BluetoothDevice;

public abstract class OnBLEListener {
    /**
     * 数据返回
     *
     * @param data
     */
    public void onDataBack(String data) {
    }

    /**
     * 回调搜索到的蓝牙设备
     *
     * @param device
     */
    public void onScanBack(BluetoothDevice device) {
    }

    /**
     * 蓝牙连接成功
     */
    public void onConnected() {
    }

    /**
     * 断开蓝牙连接
     */
    public void onDisConnected() {
    }
}