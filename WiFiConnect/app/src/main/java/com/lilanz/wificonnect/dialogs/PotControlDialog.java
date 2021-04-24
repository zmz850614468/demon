package com.lilanz.wificonnect.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.data.electricfan.ElectricFan_IRData;
import com.lilanz.wificonnect.utils.SerialPortUtil;
import com.lilanz.wificonnect.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 电饭锅制面板
 */
public class PotControlDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.tv_function_1)
    TextView tvFunction1;
    @BindView(R.id.tv_function_2)
    TextView tvFunction2;
    @BindView(R.id.tv_function_3)
    TextView tvFunction3;
    @BindView(R.id.tv_function_4)
    TextView tvFunction4;

    private Context context;
    private DeviceBean deviceBean;

    private long lastClickedTime;

    public PotControlDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pot_control);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initUI();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void update(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
        controlMap = new Gson().fromJson(deviceBean.controlMap, Map.class);
    }

    Map<String, String> controlMap;

    private void initUI() {
    }

    @OnClick({R.id.tv_function_1, R.id.tv_function_2, R.id.tv_function_3, R.id.tv_function_4})
    public void onClicked(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickedTime < 2000) {
            showToast("请不要连续点击，稍后再尝试!");
            return;
        }
        lastClickedTime = currentTime;

        onClicked(v.getId());
    }

    public void onClicked(int resId) {
        String controlMsg = null;
        switch (resId) {
            case R.id.tv_function_1:
                controlMsg = controlMap.get(tvFunction1.getText().toString() + "：");
                break;
            case R.id.tv_function_2:
                controlMsg = controlMap.get(tvFunction2.getText().toString() + "：");
                break;
            case R.id.tv_function_3:
                controlMsg = controlMap.get(tvFunction3.getText().toString() + "：");
                break;
            case R.id.tv_function_4:
                controlMsg = controlMap.get(tvFunction4.getText().toString() + "：");
                break;
        }
        if (!StringUtil.isEmpty(controlMsg)) {
            for (int i = 0; i < controlMsg.length(); i++) {
                switch (controlMsg.charAt(i)) {
                    case '1':
                        msgList.add("A0 01 01 A2");
                        msgList.add("A0 01 00 A1");
                        break;
                    case '2':
                        msgList.add("A0 02 01 A3");
                        msgList.add("A0 02 00 A2");
                        break;
                    case '3':
                        msgList.add("A0 03 01 A4");
                        msgList.add("A0 03 00 A3");
                        break;
                    case '4':
                        msgList.add("A0 04 01 A5");
                        msgList.add("A0 04 00 A4");
                        break;
                }
            }

            msgHandle.sendEmptyMessageDelayed(1, 50);
        }
    }

    private List<String> msgList = new ArrayList<>();
    private Handler msgHandle = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (!msgList.isEmpty()) {
                    String control = msgList.remove(0);
                    Esp8266ControlBean controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, SerialPortUtil.hexStr2Byte(control));
                    DeviceOkSocketControl.getInstance(context).sendControlMsg(controlBean);
                }
                if (!msgList.isEmpty()) {
                    msgHandle.sendEmptyMessageDelayed(1, 150);
                }
            }
            return true;
        }
    });

    //  ====================== 时间监听 ===========================

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("control", msg);
        }
    }
}
