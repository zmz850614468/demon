package com.lilanz.tooldemo.utils;

import android.graphics.Bitmap;

public class BitUtil {

    /**
     * 黑白图：获取 图片的 0、1位图
     *
     * @param bit
     */
    public static int[] getBit(Bitmap bit) {
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
    public static byte[][] getByteFromBitImage(int[] pixels, int width, int height) {
        // TODO 打印机横线支持的最大打印像素
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

}
