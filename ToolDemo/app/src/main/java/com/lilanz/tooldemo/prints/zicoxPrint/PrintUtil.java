package com.lilanz.tooldemo.prints.zicoxPrint;

import android.graphics.Bitmap;
import android.os.Looper;

public class PrintUtil {

    /**
     * 判断是否支持蓝牙打印
     *
     * @param BDAddress
     * @return .
     * 0：一切正常
     * 1：没有选择打印机地址
     */
    public static int getPrinterStatus(String BDAddress) {
        if (BDAddress == "" || BDAddress == null) {
//            Toast.makeText(this, "没有选择打印机", Toast.LENGTH_LONG).show();
            return 1;
        }

        return 0;
    }

    /**
     * 打印图片
     *
     * @param bleAddress
     * @param bitmap 芝柯打印机，最大宽度:576px
     */
    public static void bitmapPrint(String bleAddress, Bitmap bitmap) {
        if (!BtSPP.OpenPrinter(bleAddress)) {
            showToast(BtSPP.ErrorMessage);
            return;
        }

        BtSPP.SPPWrite(new byte[]{0x1B, 0x40});         //打印机复位
        int[] bit = getBit(bitmap);
        printBitImage(getByteImage(bit, bitmap.getWidth(), bitmap.getHeight()));

        showToast("打印完成");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        BtSPP.SPPClose();
    }

    /**
     * 获取 图片的 0、1位图
     *
     * @param bit
     */
    private static int[] getBit(Bitmap bit) {
        int[] pixels = new int[bit.getWidth() * bit.getHeight()];//保存所有的像素的数组，图片宽×高
        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());
        for (int i = 0; i < pixels.length; i++) {

            int clr = pixels[i];
            int red = (clr & 0x00ff0000) >> 16; //取高两位
            int green = (clr & 0x0000ff00) >> 8; //取中两位
            int blue = clr & 0x000000ff; //取低两位
            if (red + green + blue < 600) {
                pixels[i] = 1;
            } else {
                pixels[i] = 0;
            }
        }

        return pixels;
    }

    /**
     * 纵向8字节 转一个byte
     *
     * @param pixels
     * @param width
     * @param height
     * @return
     */
    private static byte[][] getByteImage(int[] pixels, int width, int height) {
        // 打印机横线支持的最大打印像素
        if (width > 576) {
            width = 576;
        }
        // 纵向只支持8倍数的像素
        height = height - height % 8;
        byte[][] bytes = new byte[height / 8][width];
        for (int i = 0; i < height; i = i + 8) {
            for (int j = 0; j < width; j++) {
                int bit = pixels[i * width + j];
                for (int k = 1; k < 8; k++) {
                    bit = bit * 2 + pixels[(i + k) * width + j];
                }
                bytes[i / 8][j] = (byte) bit;
            }
        }
        return bytes;
    }

    /**
     * 使用便携打印机，打印位图
     *
     * @param byteArr
     */
    private static void printBitImage(byte[][] byteArr) {
        try {
            int heightByte = 2;     // 纵向一次打印多少个byte
            int width = byteArr[0].length;
            int height = byteArr.length;
            for (int j = 0; j < height; j = j + heightByte) {
                BtSPP.SPPWrite(new byte[]{0x1B, 0x40});         //打印机复位
                BtSPP.SPPWrite(new byte[]{0x1B, 0x4D, 0x01});   // 00:24点阵字库 ;01:16点阵字库
                BtSPP.SPPWrite(new byte[]{0x1B, 0x33, 0x00});   // 设置为缺省行间距：1mm（8个垂直点距）

                // 下传位图指令,打印高度2个byte
                byte[] bytes = new byte[width * heightByte + 4];
                bytes[0] = 0x1D;
                bytes[1] = 0x2A;
                bytes[2] = (byte) (width / 8);
                bytes[3] = (byte) heightByte;
                for (int i = 4; i < bytes.length; ) {
                    for (int k = 0; k < width; k++) {
                        for (int l = 0; l < heightByte; l++) {
                            bytes[i] = byteArr[j + l][k];
                            i++;
                        }
                    }
                }

                BtSPP.SPPWrite(bytes);                          // 下传位图
                BtSPP.SPPWrite(new byte[]{0x1D, 0X2F, 0});      // 打印位图
                BtSPP.SPPWrite(new byte[]{0x1B, 0x4A, 0});      // 走纸，每个垂直点距为0.125mm
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果是主线程则提示
     *
     * @param msg
     */
    public static void showToast(String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
//            Toast.makeText(MyApplication.context, msg, Toast.LENGTH_SHORT).show();
        } else {
//            if (Looper.myLooper() == null) {
//                Looper.prepare();
//            }
//            Toast.makeText(MyApplication.context, msg, Toast.LENGTH_SHORT).show();
//            Looper.loop();
        }
    }
}
