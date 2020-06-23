package com.lilanz.tooldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.lilanz.tooldemo.camera2.Camera2ExaActivity;
import com.lilanz.tooldemo.daos.DaoExaActivity;
import com.lilanz.tooldemo.qrcode.CodeScanExaActivity;
import com.lilanz.tooldemo.utils.internetcheck.InternetCheckUtil;
import com.lilanz.tooldemo.views.CustomToast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_internal_check, R.id.bt_camera2, R.id.bt_scan_code, R.id.bt_dao_test})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera2:
                Intent intent = new Intent(this, Camera2ExaActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_internal_check:
                InternetCheckUtil.internetCheck(this, "webt.lilang.com");//"webt.lilang.com" ; "www.baidu.com"
                break;
            case R.id.bt_scan_code:
                CustomToast.show(this, "常规提醒信息。。。");
                intent = new Intent(this, CodeScanExaActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_dao_test:
                intent = new Intent(this, DaoExaActivity.class);
                startActivity(intent);
                CustomToast.testShow("测试用的提示信息");
                break;
        }
    }

}
