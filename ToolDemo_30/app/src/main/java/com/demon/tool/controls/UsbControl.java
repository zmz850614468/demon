package com.demon.tool.controls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

/**
 * usb控制类
 * usb扫码枪拔插，会导致界刷新，会丢失部分数据(不确定丢失什么数据)
 * <p>
 * 防止界面刷新的方法
 * android:configChanges="orientation|keyboard|keyboardHidden"
 * public void onConfigurationChanged(@NonNull Configuration newConfig)
 */
public class UsbControl {

    private Context context;

    public UsbControl(Context context) {
        this.context = context;
        register();
    }

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);

        context.registerReceiver(usbReceiver, filter);
    }

    private void unregister() {
        context.unregisterReceiver(usbReceiver);
    }

    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    showLog("ACTION_USB_DEVICE_ATTACHED");
                    showToast("ACTION_USB_DEVICE_ATTACHED");
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    showLog("ACTION_USB_DEVICE_DETACHED");
                    showToast("ACTION_USB_DEVICE_DETACHED");
                    break;
                case UsbManager.ACTION_USB_ACCESSORY_ATTACHED:
                    showLog("ACTION_USB_ACCESSORY_ATTACHED");
                    showToast("ACTION_USB_ACCESSORY_ATTACHED");
                    break;
                case UsbManager.ACTION_USB_ACCESSORY_DETACHED:
                    showLog("ACTION_USB_ACCESSORY_DETACHED");
                    showToast("ACTION_USB_ACCESSORY_DETACHED");
                    break;
            }
        }
    };

    public void close() {
        unregister();
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("UsbControl", msg);
    }

}
