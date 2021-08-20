package com.demon.agv.usbSerial;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.util.Map;

/**
 * usb转串口类
 */
public class UsbSerial {

    private Context context;
    private Handler msgHandle;
    private UsbSerialDevice serial;

    public UsbSerial(Context context, Handler msgHandle) {
        this.context = context;
        this.msgHandle = msgHandle;
    }

    /**
     * 初始化串口，并设置波特率
     *
     * @param baudRate
     */
    public void initSerial(UsbManager usbManager, int baudRate, int vendorId, int productId) {
        Map<String, UsbDevice> connectedDevices = usbManager.getDeviceList();
        for (UsbDevice device : connectedDevices.values()) {
            if (device.getVendorId() == vendorId && device.getProductId() == productId) {
                connectSerial(baudRate, usbManager, device);
                readSerial();
                break;
            }
        }
    }

    String DIALOG_USB_PERMISSION = "com.example.usbtest.GRANT_USB";

    /**
     * 连接串口
     *
     * @param usbManager
     * @param device
     */
    private void connectSerial(int baudRate, UsbManager usbManager, UsbDevice device) {
        if (usbManager.hasPermission(device)) {
            UsbDeviceConnection connection = usbManager.openDevice(device);
            serial = UsbSerialDevice.createUsbSerialDevice(device, connection);

            if (serial != null && serial.open()) {
                serial.setBaudRate(baudRate);
                serial.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serial.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serial.setParity(UsbSerialInterface.PARITY_NONE);
                serial.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                serial.setDTR(false);
            }
            msgHandle.sendEmptyMessage(10);
        } else {
            // 弹出权限申请窗口
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(DIALOG_USB_PERMISSION), 0);
            usbManager.requestPermission(device, usbPermissionIntent);
            msgHandle.sendEmptyMessage(11);
        }
    }

    /**
     * 读取串口数据
     */
    private void readSerial() {
        if (serial != null) {
            serial.read((data) -> {
                if (onSerialListener != null) {
                    onSerialListener.onSerialData(data);
                }
            });
        }
    }

    /**
     * 写串口数据
     *
     * @param bytes
     * @return
     */
    public boolean writeSerial(byte[] bytes) {
        if (serial != null) {
            serial.setDTR(false);
            serial.write(bytes);

            msgHandle.sendEmptyMessage(12);
            return true;
        }
        return false;
    }

    public boolean isSerialAvailable() {
        return serial != null;
    }

    public interface OnSerialListener {
        void onSerialData(byte[] bytes);
    }

    private OnSerialListener onSerialListener;

    public void setOnSerialListener(OnSerialListener onSerialListener) {
        this.onSerialListener = onSerialListener;
    }

    public static final byte SYNC_BYTE0 = (byte) 0xA5;
    public static final byte START_MOTOR = (byte) 0xF0;

    public void startMotor(int speed) {
        sendPayLoad(START_MOTOR, speed);
    }

    protected void sendPayLoad(byte command, int payLoadInt) {
        byte[] payLoad = new byte[2];

        //load payload little Endian
        payLoad[0] = (byte) payLoadInt;
        payLoad[1] = (byte) (payLoadInt >> 8);

        sendPayLoad(command, payLoad);
    }

    // 0xf0 32 00
    protected void sendPayLoad(byte command, byte[] payLoad) {
        byte[] bytes = new byte[1024];

        // a5 f0 02 32 00
        bytes[0] = SYNC_BYTE0;
        bytes[1] = command;

        //add payLoad and calculate checksum
        bytes[2] = (byte) payLoad.length;
        int checksum = 0 ^ bytes[0] ^ bytes[1] ^ (bytes[2] & 0xFF);

        for (int i = 0; i < payLoad.length; i++) {
            bytes[3 + i] = payLoad[i];
            checksum ^= bytes[3 + i];
        }

        //add checksum - now total length is 3 + payLoad.length + 1
        bytes[3 + payLoad.length] = (byte) checksum;

        if (serial != null) {
            serial.write(bytes);
        }
    }
}
