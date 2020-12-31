package com.lilanz.tooldemo.multiplex.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class CodeScanHelper {

    // 扫描二维码
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private String qrCodeResult;          // 二维码扫描结果

    private Activity activity;
    private boolean isOpened = false;   // 二维码是否在开启

    public CodeScanHelper(Activity activity, DecoratedBarcodeView barcodeScannerView) {
        this.activity = activity;
        this.barcodeScannerView = barcodeScannerView;

        initUI();
    }


    /**
     * 开启条码扫描
     */
    public void startScan() {
        isOpened = true;
        capture.decode();
        capture.onResume();
        barcodeScannerView.setVisibility(View.VISIBLE);
    }

    /**
     * 结束条码扫描
     */
    public void stopScanCode() {
        isOpened = false;
        capture.onPause();
        capture.onDestroy();
        barcodeScannerView.setVisibility(View.INVISIBLE);
    }

    private CaptureManager.OnScanResultListener listener = new CaptureManager.OnScanResultListener() {
        @Override
        public void onResultBack(int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(resultCode, data);
                String qrContent = scanResult.getContents();

                if (onScanResultListener != null) {
                    onScanResultListener.onScanResult(qrContent);
                }

                qrCodeResult = qrContent;
                // 扫描二维码成功后，开始视频录制
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(activity, "取消二维码扫描", Toast.LENGTH_SHORT).show();
            }

            isOpened = false;
            barcodeScannerView.setVisibility(View.INVISIBLE);
        }
    };

    private void initUI() {
        capture = new CaptureManager(activity, barcodeScannerView);
        Intent intent = new Intent(activity, activity.getClass());
//        intent.setAction(Intents.Scan.ACTION);
//        intent.putExtra(Intents.Scan.CAMERA_ID, 2);
        capture.initializeFromIntent(intent, null);
        capture.setOnScanResultListener(listener);
    }

    private OnScanResultListener onScanResultListener;

    public void setOnScanResultListener(OnScanResultListener listener) {
        onScanResultListener = listener;
    }

    public interface OnScanResultListener {
        public void onScanResult(String content);
    }

    public boolean isOpened() {
        return isOpened;
    }

}
