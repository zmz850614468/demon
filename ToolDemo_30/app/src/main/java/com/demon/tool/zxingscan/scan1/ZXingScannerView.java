package com.demon.tool.zxingscan.scan1;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.core.BarcodeScannerView;
import me.dm7.barcodescanner.core.DisplayUtils;

public class ZXingScannerView extends BarcodeScannerView {

    private static final String TAG = "ZXingScannerView";
    private boolean isContinueScan;

    private int scanTimes;

    public interface ResultHandler {
        public void handleResult(Result rawResult);
    }

    private MultiFormatReader mMultiFormatReader;
    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList<BarcodeFormat>();
    private List<BarcodeFormat> mFormats;
    private ResultHandler mResultHandler;

    static {
//        ALL_FORMATS.add(BarcodeFormat.UPC_A);
//        ALL_FORMATS.add(BarcodeFormat.UPC_E);
//        ALL_FORMATS.add(BarcodeFormat.EAN_13);
//        ALL_FORMATS.add(BarcodeFormat.EAN_8);
//        ALL_FORMATS.add(BarcodeFormat.RSS_14);
//        ALL_FORMATS.add(BarcodeFormat.CODE_39);
//        ALL_FORMATS.add(BarcodeFormat.CODE_93);
        ALL_FORMATS.add(BarcodeFormat.CODE_128);
//        ALL_FORMATS.add(BarcodeFormat.ITF);
//        ALL_FORMATS.add(BarcodeFormat.CODABAR);
        ALL_FORMATS.add(BarcodeFormat.QR_CODE);
//        ALL_FORMATS.add(BarcodeFormat.DATA_MATRIX);
//        ALL_FORMATS.add(BarcodeFormat.PDF_417);
    }

    public ZXingScannerView(Context context) {
        super(context);
        initMultiFormatReader();
    }

    public ZXingScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initMultiFormatReader();
    }

    /**
     * 设置允许扫码的类型，没有设置，默认扫全部的码
     *
     * @param formats
     */
    public void setFormats(List<BarcodeFormat> formats) {
        mFormats = formats;
        initMultiFormatReader();
    }

    @Override
    public void startCamera() {
        super.startCamera();
        scanTimes = 1;
    }

    /**
     * 设置扫码次数
     *
     * @param continueScan
     */
    public void setContinueScan(boolean continueScan) {
        isContinueScan = continueScan;
    }


    public void setResultHandler(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
    }

    public Collection<BarcodeFormat> getFormats() {
        if (mFormats == null) {
            return ALL_FORMATS;
        }
        return mFormats;
    }

    private void initMultiFormatReader() {
        Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, getFormats());
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(hints);
//        setShouldScaleToFill(false);
    }

    private void pointFocus(int x, int y) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mResultHandler == null) {
            return;
        }

        try {
            Camera.Parameters parameters = camera.getParameters();
//            List<Camera.Size> list = parameters.getSupportedPreviewSizes();
            Camera.Size size = parameters.getPreviewSize();
            int width = size.width;
            int height = size.height;
//            if (parameters.getMaxNumMeteringAreas() > 0) {
//                List<Camera.Area> areas = new ArrayList<Camera.Area>();
//                Rect area = new Rect(width - 100, height - 100, width + 100, height + 100);
//                areas.add(new Camera.Area(area, width));
//                parameters.setMeteringAreas(areas);
//            }
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters);
            if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
                byte[] rotatedData = new byte[data.length];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++)
                        rotatedData[x * height + height - y - 1] = data[x + y * width];
                }
                int tmp = width;
                width = height;
                height = tmp;
                data = rotatedData;
            }

            Result rawResult = null;
            PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);

            if (source != null) {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    rawResult = mMultiFormatReader.decodeWithState(bitmap);
                } catch (ReaderException re) {
                    // continue
                } catch (NullPointerException npe) {
                    // This is terrible
                } catch (ArrayIndexOutOfBoundsException aoe) {

                } finally {
                    mMultiFormatReader.reset();
                }
            }

            final Result finalRawResult = rawResult;

            if (finalRawResult != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scanTimes--;    // 应为异步问题，所以添加一个次数，防止多次回调扫码数据
                        if (!isContinueScan) {
                            stopCameraPreview();
                        }

                        if (mResultHandler != null && (isContinueScan || scanTimes >= 0)) {
                            mResultHandler.handleResult(finalRawResult);
                        }
                    }
                });
            } else {
                camera.setPreviewCallback(this);
            }
        } catch (RuntimeException e) {
            Log.e(TAG, e.toString(), e);
        }
    }

    public void resumeCameraPreview() {
//        mResultHandler = resultHandler;
        super.resumeCameraPreview();
    }


    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview(width, height);
        if (rect == null) {
            return null;
        }
        PlanarYUVLuminanceSource source = null;

        try {
            source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                    rect.width(), rect.height(), false);
        } catch (Exception e) {
        }

        return source;
    }
}
