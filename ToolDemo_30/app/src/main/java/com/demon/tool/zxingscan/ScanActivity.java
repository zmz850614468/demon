package com.demon.tool.zxingscan;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.R;
import com.demon.tool.zxingscan.scan1.Scan1Ui;
import com.demon.tool.zxingscan.scan1.ZXingScannerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanActivity extends AppCompatActivity {

    @BindView(R.id.layout_scan_1)
    ViewGroup layoutScan1;
    @BindView(R.id.tv_light_tip)
    TextView tvLightTip;
    @BindView(R.id.scanner_view)
    ZXingScannerView mScannerView;

    private Scan1Ui scan1Ui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        initScan1();
    }

    @OnClick(R.id.bt_toggle_scan_1)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_toggle_scan_1:
                if (layoutScan1.getVisibility() == View.VISIBLE) {
                    scan1Ui.closeScan1();
                } else {
                    scan1Ui.openScan1();
                }
                break;
        }
    }

    private void initScan1() {
        scan1Ui = new Scan1Ui(layoutScan1, mScannerView, tvLightTip);
        scan1Ui.setOnScanResultListener((type, content) -> {
            showLog("form: " + type + " ; code: " + content);
            showToast("form: " + type + " ; code: " + content);
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("ScanActivity", msg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scan1Ui.closeScan1();
    }
}
