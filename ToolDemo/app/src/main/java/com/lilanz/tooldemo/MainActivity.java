package com.lilanz.tooldemo;

import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import demon.CopyUseActivity;

import com.lilanz.tooldemo.multiplex.activitys.ReuseActivity;
import com.lilanz.tooldemo.prints.PrintsActivity;
import com.lilanz.tooldemo.utils.internetcheck.InternetCheckUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        initMenu();

    }

    @OnClick({R.id.bt_internal_check, R.id.bt_reuse, R.id.bt_prints, R.id.bt_copy_use})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_copy_use:
                Intent intent = new Intent(this, CopyUseActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_prints:
                intent = new Intent(this, PrintsActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_reuse:
                intent = new Intent(this, ReuseActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_internal_check:
                InternetCheckUtil.internetCheck(this, "webt.lilang.com");//"webt.lilang.com" ; "www.baidu.com"
                break;
        }
    }

//    private void initMenu() {
//        SlidingMenu menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.layout_menu);
//    }
    
    private void initUSB(){
        UsbManager manager = (UsbManager) getSystemService(USB_SERVICE);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }

    private void test() {
        boolean b = false;
    }
}


