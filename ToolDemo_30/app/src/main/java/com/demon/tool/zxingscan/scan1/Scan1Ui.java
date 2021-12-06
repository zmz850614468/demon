package com.demon.tool.zxingscan.scan1;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * 扫码识别比较快，扫码类型过多时，存在扫错码的情况
 */
public class Scan1Ui {

    private ViewGroup scanLayout;
    private ZXingScannerView mScannerView;
    private TextView tvLightTip;

    private boolean isContinue;

    public Scan1Ui(@NonNull ViewGroup scanLayout, @NonNull ZXingScannerView scannerView,
                   @NonNull TextView tvLightTip) {
        this.scanLayout = scanLayout;
        this.mScannerView = scannerView;
        this.tvLightTip = tvLightTip;

        initScan1();
    }

    private void initScan1() {
        mScannerView.setResultHandler(rawResult -> {
            if (!isContinue) {
                closeScan1();
            }
            if (onScanResultListener != null) {
                onScanResultListener.onResult(rawResult.getBarcodeFormat().name(), rawResult.getText());
            }
        });

        mScannerView.setOnClickListener(v -> {
            mScannerView.toggleFlash();
            if (mScannerView.getFlash()) {
                tvLightTip.setText("轻触关闭");
            } else {
                tvLightTip.setText("轻触照亮");
            }
        });
    }

    public void openScan1() {
        isContinue = false;
        tvLightTip.setText("轻触照亮");
        mScannerView.startCamera();
        scanLayout.setVisibility(View.VISIBLE);
    }

    public void continueOpenScan1() {
        isContinue = true;
        tvLightTip.setText("轻触照亮");
        mScannerView.setContinueScan(true);
        mScannerView.startCamera();
        scanLayout.setVisibility(View.VISIBLE);
    }

    public void closeScan1() {
        mScannerView.setFlash(false);
        mScannerView.stopCamera();
        scanLayout.setVisibility(View.GONE);
    }

    private OnScanResultListener onScanResultListener;

    public void setOnScanResultListener(OnScanResultListener onScanResultListener) {
        this.onScanResultListener = onScanResultListener;
    }

    public interface OnScanResultListener {
        void onResult(String type, String content);
    }
}
