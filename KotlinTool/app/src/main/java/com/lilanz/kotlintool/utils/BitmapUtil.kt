package com.lilanz.kotlintool.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.widget.ScrollView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class BitmapUtil {
    companion object {
        /**
         * scrollView转为Bitmap ： 可以处理布局超出界面的情况
         * @param scrollView
         * @return
         */
        fun getBitmapByView(scrollView: ScrollView): Bitmap? {
            var h = 0
            var bitmap: Bitmap? = null
            for (i in 0 until scrollView.childCount) {
                h += scrollView.getChildAt(i).height
            }
            bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            scrollView.draw(canvas)
            return bitmap
        }

        /**
         * 获取视图的bitmap图片
         *
         * @param v
         * @return
         */
        fun getBitmapFromView(v: View): Bitmap? {
            val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.RGB_565)
            val c = Canvas(b)
            v.layout(v.left, v.top, v.right, v.bottom)
            // Draw background
            val bgDrawable = v.background
            if (bgDrawable != null) bgDrawable.draw(c) else c.drawColor(Color.WHITE)
            v.draw(c)
            return b
        }

        /**
         * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
         *
         * @param content 将要生成一维码的内容
         * @return 返回生成好的一维码bitmap
         * @throws WriterException WriterException异常
         */
        @Throws(WriterException::class)
        fun createOneDCode(content: String?, w: Int, h: Int): Bitmap? {
            val matrix = MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, w, h)
            val width = matrix.width
            val height = matrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (matrix[x, 0]) {
                        pixels[y * width + x] = -0x1000000
                    } else pixels[y * width + x] = -0x1
                }
            }
            val bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        }

        /**
         * 字符串转二维码图片
         *
         * @param content
         * @return 二维码图片
         */
        fun createQRcodeBitmap(content: String?): Bitmap? {
            val w = 300
            val h = 300
            try { //判断URL合法性
                if (StringUtil.isEmpty(content)) {
                    return null
                }
                val hints =
                    Hashtable<EncodeHintType, String?>()
                hints[EncodeHintType.CHARACTER_SET] = "utf-8"
                //图像数据转换，使用了矩阵转换
                val bitMatrix =
                    QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, w, h, hints)
                val pixels = IntArray(w * h)
                //下面这里按照二维码的算法，逐个生成二维码的图片，
//两个for循环是图片横列扫描的结果
                for (y in 0 until h) {
                    for (x in 0 until w) {
                        if (bitMatrix[x, y]) {
                            pixels[y * w + x] = -0x1000000
                        } else {
                            pixels[y * w + x] = -0x1
                        }
                    }
                }
                //生成二维码图片的格式，使用ARGB_8888
                val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
                //显示到我们的ImageView上面
//            im1.setImageBitmap(bitmap);
                return bitmap
            } catch (e: WriterException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 保存字节图片
         *
         * @param context
         * @param imageBytes    字节图片数据
         * @param path          保存的图片路径
         */
        fun saveImageByte(context: Context?, imageBytes: ByteArray, path: String?) {
            val temp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            saveBitmap(context, temp, path)
        }

        /**
         * 保存bitmap图片到本地内存
         *
         * @param context
         * @param bmp
         */
        fun saveBitmap(context: Context?, bmp: Bitmap) {
            val path = FileUtil.getPictureFile(context!!)!!.absolutePath
            saveBitmap(context, bmp, path)
        }

        /**
         * 保存bitmap图片到本地内存
         *
         * @param bmp
         * @param path：可以为空，默认到本地DCIM文件加中
         */
        fun saveBitmap(context: Context?, bmp: Bitmap, path: String?) {
            val outFile = File(path)
            var outStream: BufferedOutputStream? = null
            try {
                outStream = BufferedOutputStream(FileOutputStream(outFile))
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    outStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}