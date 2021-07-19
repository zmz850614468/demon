package com.lilanz.tooldemo.prints;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.prints.fuliaocang.FuLiaoCangActivity;
import com.lilanz.tooldemo.prints.hanYin.HanYinActivity;
import com.lilanz.tooldemo.prints.jiabo.JiaBoActivity;
import com.lilanz.tooldemo.prints.jiabo.JiaBoBluetoothActivity;
import com.lilanz.tooldemo.prints.zicoxPrint.ZicoxActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 打印机相关主界面
 */
public class PrintsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prints);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_zicox, R.id.bt_jia_bo, R.id.bt_han_yin, R.id.bt_jia_bo_bluetooth,
            R.id.bt_fu_liao_cang})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_zicox:
                Intent intent = new Intent(this, ZicoxActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_jia_bo:
                intent = new Intent(this, JiaBoActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_jia_bo_bluetooth:
                intent = new Intent(this, JiaBoBluetoothActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_han_yin:
                intent = new Intent(this, HanYinActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_fu_liao_cang:
                intent = new Intent(this, FuLiaoCangActivity.class);
                startActivity(intent);
                break;
        }
    }
}
