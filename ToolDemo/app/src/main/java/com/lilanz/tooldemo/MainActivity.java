package com.lilanz.tooldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
    }

    @OnClick({R.id.bt_internal_check, R.id.bt_reuse, R.id.bt_prints})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_prints:
                Intent intent = new Intent(this, PrintsActivity.class);
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

//    private void requestTest() {
//        APIRequest<Nullable> request = new APIRequest<>(Nullable.class);
//        request.setParseListener(new ParseListener<Nullable>() {
//            @Override
//            public void onTip(String msg) {
//                showToast(msg);
//            }
//
//            @Override
//            public void onError(String msg) {
//                showToast(msg);
//            }
//        });
//        request.requestByJson(new HashMap<String, Object>(), "getNumberList", APIRequest.PARSE_TYPE_NULL);
//    }

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


