package com.lilanz.tooldemo.multiplex.qrcode;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.lilanz.tooldemo.R;

public class CodeScanExaActivity extends Activity implements View.OnClickListener {

    private DecoratedBarcodeView decoratedBarcodeView;
    private Button btStartScan;
    private Button btStopScan;
    private TextView tvMsg;

    private CodeScanHelper codeScanHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scan_exa);

        initUI();
        codeScanHelper = new CodeScanHelper(this, decoratedBarcodeView);
        codeScanHelper.setOnScanResultListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_scan:
                codeScanHelper.startScan();
                break;
            case R.id.bt_stop_scan:
                codeScanHelper.stopScanCode();
                break;
        }
    }

    private CodeScanHelper.OnScanResultListener listener = new CodeScanHelper.OnScanResultListener() {
        @Override
        public void onScanResult(String content) {
            tvMsg.append(content + "\n");
        }
    };

    private void initUI() {
        decoratedBarcodeView = findViewById(R.id.scan_code);
        btStartScan = findViewById(R.id.bt_start_scan);
        btStopScan = findViewById(R.id.bt_stop_scan);
        tvMsg = findViewById(R.id.tv_msg);
        btStartScan.setOnClickListener(this);
        btStopScan.setOnClickListener(this);
    }
}
