package com.lilanz.tooldemo.multiplex.scanhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.widget.TextView;

import com.lilanz.tooldemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    private ScanControl scanControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        initScan();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        InputDevice device = event.getDevice();
        if ((device.getProductId() == 33639 && device.getVendorId() == 9354)
                || (device.getProductId() == 0 && device.getVendorId() == 0)) {
            return super.dispatchKeyEvent(event);
        }

        scanControl.analysis(event);
        return true;

//        return super.dispatchKeyEvent(event);
    }

    private void initScan() {
        scanControl = new ScanControl();
        scanControl.setOnScanResult(new ScanControl.OnScanResult() {
            @Override
            public void scanResult(int id, String code) {
                showMsg(id + " : " + code);
                Log.e("result", id + " : " + code);
            }
        });
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }
}
