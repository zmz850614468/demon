package com.lilanz.tooldemo.prints.jiabo;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.tools.command.EscCommand;
import com.tools.command.LabelCommand;
import com.tools.io.PortManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class PrintUtil {

    /**
     * 表格打印格式
     *
     * @param portManager
     */
    public static void tablePrint(PortManager portManager, Bitmap bitmap) {
        LabelCommand tsc = new LabelCommand();
        /* 清除打印缓冲区 */
        tsc.addCls();
        /* 设置标签尺寸，按照实际尺寸设置 */
        tsc.addSize(100, 100);

        /* 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 */
        tsc.addGap(0);
        /* 设置打印方向 */
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        /* 开启带Response的打印，用于连续打印 */
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.OFF);
        /* 设置原点坐标 */
        tsc.addReference(0, 0);
        /* 撕纸模式开启 */
        tsc.addTear(EscCommand.ENABLE.ON);
        // 切纸模式
        tsc.addCutter(EscCommand.ENABLE.ON);

        // 打印图片
        tsc.addBitmap(0, 0, LabelCommand.BITMAP_MODE.OVERWRITE, 800, bitmap);

        tsc.addPrint(1);
        /* 打印标签后 蜂鸣器响 */

//        tsc.addSound( 2, 100 );
//        tsc.addCashdrwer( LabelCommand.FOOT.F5, 255, 255 );
        Vector<Byte> datas = tsc.getCommand();
        /* 发送数据 */
        try {
            portManager.writeDataImmediately(datas, 0, datas.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取usb佳博打印设备
     *
     * @param context
     * @return 没有打印机返回null值
     */
    public static UsbDevice getUsbPrintDevice(Context context) {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        // Get the list of attached devices
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        if (count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                if (checkUsbDevicePidVid(device)) {
                    return device;
                }
            }
        }
        return null;
    }

    /**
     * 检查是否有佳博打印机
     *
     * @param dev
     * @return
     */
    public static boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        return ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
                || (vid == 6790 && pid == 30084)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
                || (vid == 26728 && pid == 1024) || (vid == 26728 && pid == 1280)
                || (vid == 26728 && pid == 1536));
    }

}
