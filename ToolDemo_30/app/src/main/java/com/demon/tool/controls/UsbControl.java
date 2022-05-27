package com.demon.tool.controls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
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
            // 这里可以拿到插入的USB设备对象
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

            switch (action) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    showLog("ACTION_USB_DEVICE_ATTACHED");
                    showToast("ACTION_USB_DEVICE_ATTACHED");
                    if (onUsbConnectListener != null) {
                        onUsbConnectListener.onUsbAttached(usbDevice);
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    showLog("ACTION_USB_DEVICE_DETACHED");
                    showToast("ACTION_USB_DEVICE_DETACHED");
                    if (onUsbConnectListener != null) {
                        onUsbConnectListener.onUsbDetached(usbDevice);
                    }
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


    private static final int USB_CAMERA_TYPE = 14; //可能跟不同系统设备相关，一般是某个固定值，可以打Log验证。
    /**
     * 判断当前Usb设备是否是Camera设备
     */
    public boolean isUsbCameraDevice(UsbDevice usbDevice) {
        if (usbDevice == null) {
            return false;
        }
        boolean isCamera = false;
        int interfaceCount = usbDevice.getInterfaceCount();
        for (int interIndex = 0; interIndex < interfaceCount; interIndex++) {
            UsbInterface usbInterface = usbDevice.getInterface(interIndex);
            //usbInterface.getName()遇到过为null的情况
            if ((usbInterface.getName() == null || usbDevice.getProductName().equals(usbInterface.getName())) && usbInterface.getInterfaceClass() == USB_CAMERA_TYPE) {
                isCamera = true;
                break;
            }
        }
        return isCamera;
    }

    public void close() {
        unregister();
    }

    public OnUsbConnectListener onUsbConnectListener;

    public interface OnUsbConnectListener {
        void onUsbAttached(UsbDevice usbDevice);

        void onUsbDetached(UsbDevice usbDevice);
    }

    public void setOnUsbConnectListener(OnUsbConnectListener onUsbConnectListener) {
        this.onUsbConnectListener = onUsbConnectListener;
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("UsbControl", msg);
    }

}
