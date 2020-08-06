package com.lilanz.tooldemo.prints.hanYin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lilanz.tooldemo.R;

import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tspl.HPRTPrinterHelper;

/**
 * 汉印打印机 例子
 */
public class HanYinActivity extends Activity {

    private static final String ACTION_USB_PERMISSION = "com.HPRTSDKSample";

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    private UsbManager mUsbManager = null;
    private UsbDevice device = null;
    private PendingIntent mPermissionIntent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_han_yin);
        ButterKnife.bind(this);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, filter);
    }

    @OnClick({R.id.bt_connect_usb, R.id.bt_disconnect_usb, R.id.bt_print_image, R.id.bt_home})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_connect_usb:   // 连接USB打印机
                connectUSB();
                break;
            case R.id.bt_disconnect_usb:    // 断开USB打印机
                disconnectUSB();
                break;
            case R.id.bt_print_image:       // 打印图片资源
                printImage();
                break;
            case R.id.bt_home:
                home();
                break;
        }
    }

    /**
     * 连接USB打印机
     */
    private void connectUSB() {
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        boolean HavePrinter = false;
        while (deviceIterator.hasNext()) {
            device = deviceIterator.next();
            int count = device.getInterfaceCount();
            for (int i = 0; i < count; i++) {
                UsbInterface intf = device.getInterface(i);
                if (intf.getInterfaceClass() == 7) {
                    HavePrinter = true;
                    mUsbManager.requestPermission(device, mPermissionIntent);
                }
            }
        }
        if (!HavePrinter) {
            showMsg("请先连接USB打印机");
        }
    }

    /**
     * 断开USB打印机连接
     */
    private void disconnectUSB() {
        if (!CheckClick.isClickEvent()) return;

        try {
            HPRTPrinterHelper.PortClose();
            showMsg("请先连接USB打印机");
            return;
        } catch (Exception e) {
            showMsg("打印机断开异常：" + e.getMessage().toString());
            Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> onClickClose ")).append(e.getMessage()).toString());
        }
    }

    /**
     * 打印图片资源
     */
    private void printImage() {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.test);
            int height = bmp.getHeight() / 8;
            HPRTPrinterHelper.CLS();
            if (HPRTPrinterHelper.printAreaSize("100", "80") == -1) {
                showMsg("打印机已断开连接");
                return;
            }
//            HPRTPrinterHelper.Gap("2", "0"); // (mm)两个标签的间距, 标签里的内容与标签底部的间距

            int a = HPRTPrinterHelper.printImage("0", "0", bmp, true);
            HPRTPrinterHelper.Print("1", "1");
            if (a > 0) {
                showMsg("打印成功");
            } else {
                showMsg("打印失败");
            }
        } catch (Exception e) {
            showMsg("打印失败");
        }
    }

    private void home(){
        try {
            HPRTPrinterHelper.CLS();
            HPRTPrinterHelper.Home();
//            if (HPRTPrinterHelper.printAreaSize("100", "80") == -1) {
//                showMsg("打印机已断开连接");
//                return;
//            }
////            HPRTPrinterHelper.Gap("2", "0"); // (mm)两个标签的间距, 标签里的内容与标签底部的间距
//
//            HPRTPrinterHelper.Print("1", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (HPRTPrinterHelper.PortOpen(HanYinActivity.this, device) != 0) {
                                showMsg("打印机连接错误");
                                return;
                            } else {
                                showMsg("打印机连接成功");
                            }
                        } else {
                            return;
                        }
                    }
                }
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                    device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        int count = device.getInterfaceCount();
                        for (int i = 0; i < count; i++) {
                            UsbInterface intf = device.getInterface(i);
                            //Class ID 7代表打印机
                            if (intf.getInterfaceClass() == 7) {
                                HPRTPrinterHelper.PortClose();
                                showMsg("请先连接USB打印机");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                showMsg("打印机异常：" + e.getMessage().toString());
                Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> mUsbReceiver ")).append(e.getMessage()).toString());
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mUsbReceiver);
        disconnectUSB();
        super.onDestroy();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }
}
