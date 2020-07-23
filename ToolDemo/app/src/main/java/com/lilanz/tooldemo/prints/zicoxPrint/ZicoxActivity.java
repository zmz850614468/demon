package com.lilanz.tooldemo.prints.zicoxPrint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.bleModel.BleActivity;
import com.lilanz.tooldemo.utils.BitmapUtil;
import com.lilanz.tooldemo.utils.SharePreferencesUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 芝柯 便携打印机
 */
public class ZicoxActivity extends Activity {

    @BindView(R.id.layout_1)
    public ViewGroup layoutPrint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_zicox);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_print, R.id.bt_save_bitmap, R.id.bt_select_ble_address})
    public void onClick(View v) {
        Bitmap bitmap = BitmapUtil.getBitmapFromView(layoutPrint);
        switch (v.getId()) {
            case R.id.bt_select_ble_address:
                Intent intent = new Intent(this, BleActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_save_bitmap:
                BitmapUtil.saveBitmap(this, bitmap);
                break;
            case R.id.bt_print:
                String address = SharePreferencesUtil.getBleAddress(this);
                if (StringUtil.isEmpty(address)) {
                    showToast("没有选择蓝牙地址");
                    return;
                }
                if (address.contains("=")) {
                    address = address.substring(address.lastIndexOf("=") + 1, address.length());
                }
                PrintUtil.bitmapPrint(address, bitmap);
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
