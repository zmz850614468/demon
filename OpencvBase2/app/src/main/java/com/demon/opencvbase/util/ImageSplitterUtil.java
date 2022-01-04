package com.demon.opencvbase.util;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片切割工具类
 */
public class ImageSplitterUtil {


    /**
     * 图片切割
     *
     * @param bitmap 导入图片
     * @param x      x轴切割份数
     * @param y      y轴切割份数
     * @return
     */
    public static List<Bitmap> split(Bitmap bitmap, int x, int y) {
        List<Bitmap> bitmapList = new ArrayList<>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / x;
        int pieceHeight = height / y;
        Bitmap tempBitMap;
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                tempBitMap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                bitmapList.add(tempBitMap);
            }
        }
        return bitmapList;
    }

}
