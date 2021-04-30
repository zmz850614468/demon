package com.demon.opencvbase.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.opencvbase.R;
import com.demon.opencvbase.control_ui.OperateUIControl;
import com.demon.opencvbase.jni_opencv.jni.BitmapNative;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpencvActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    private OperateUIControl operateUIControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_opencv);
        ButterKnife.bind(this);

        operateUIControl = new OperateUIControl(this);
    }

    /**
     * 灰度处理
     */
    public void grayOperate(Bitmap src) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmap2gray2(src, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 查表法，减少像素种类
     *
     * @param src
     * @param divideValue
     */
    public void lutOperate(Bitmap src, int divideValue) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapLut(src, divideValue, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 修改图片饱和度和亮度
     *
     * @param src
     * @param alpha
     * @param beta
     */
    public void convertToOperate(Bitmap src, double alpha, int beta) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapConverTo(src, alpha, beta, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 对图片进行侵蚀操作
     *
     * @param src
     */
    public void erodeOperate(Bitmap src) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapErode(src, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 对图片进行扩展操作
     *
     * @param src
     */
    public void dilateOperate(Bitmap src) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapDilate(src, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 图片二值化处理
     *
     * @param src
     * @param threshold
     * @param type
     */
    public void thresholdOperate(Bitmap src, double threshold, int type) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapThreshold(src, threshold, type, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 图片归一化模糊
     *
     * @param src
     * @param kSize
     */
    public void blurOperate(Bitmap src, int kSize) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapBlur(src, kSize, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 图片中值模糊
     *
     * @param src
     * @param kSize
     */
    public void mediaBlurOperate(Bitmap src, int kSize) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapMediaBlur(src, kSize, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 图片高斯模糊
     *
     * @param src
     * @param kSize
     */
    public void gaussBlurOperate(Bitmap src, int kSize) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapGaussBlur(src, kSize, des);
        operateUIControl.setPreview(des);
    }

    /**
     * inRange 像素值范围检测
     *
     * @param src
     * @param rMin
     * @param gMin
     * @param bMin
     * @param rMax
     * @param gMax
     * @param bMax
     */
    public void inRangeOperate(Bitmap src, int rMin, int gMin, int bMin, int rMax, int gMax, int bMax) {
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        BitmapNative.bitmapInRange(src, rMin, gMin, bMin, rMax, gMax, bMax, des);
        operateUIControl.setPreview(des);
    }

    /**
     * 图片位操作
     *
     * @param src1
     * @param src2
     * @param type
     */
    public void bitwiseOperate(Bitmap src1, Bitmap src2, int type) {
        Bitmap des = src1.copy(src1.getConfig(), src1.isMutable());
        BitmapNative.bitmapBitwise(src1, src2, type, des);
        operateUIControl.setPreview(des);
    }

    @OnClick({R.id.bt_reset_bitmap, R.id.bt_save_bitmap})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_reset_bitmap:
                operateUIControl.resetBitmap();
                break;
            case R.id.bt_save_bitmap:
                operateUIControl.updateBitmap();
                break;
        }
    }

}
