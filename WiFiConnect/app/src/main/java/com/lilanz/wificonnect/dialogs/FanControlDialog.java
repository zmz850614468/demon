package com.lilanz.wificonnect.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.data.electricfan.ElectricFan_IRData;
import com.lilanz.wificonnect.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 风扇控制面板
 */
public class FanControlDialog extends Dialog implements DialogInterface.OnClickListener {

    private Context context;
    private DeviceBean deviceBean;

    private ElectricFan_IRData electricFanIrData;

    public FanControlDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fan_control);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initUI();
//        test();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void update(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
        // 风扇的品牌
//        this.electricFanIrData = new GREE_ElectricFan_IRData();
        this.electricFanIrData = ElectricFan_IRData.getInstance(deviceBean.brand);
    }

    private void initUI() {
    }

    @OnClick({R.id.tv_open_or_exchange, R.id.tv_close, R.id.tv_timing, R.id.tv_type, R.id.tv_shake})
    public void onClicked(View v) {
        String controlMsg = null;
        switch (v.getId()) {
            case R.id.tv_open_or_exchange:
                controlMsg = electricFanIrData.getOpenOrExchange() + "~";
                break;
            case R.id.tv_close:
                controlMsg = electricFanIrData.getCloseData() + "~";
                break;
            case R.id.tv_type:
                controlMsg = electricFanIrData.getWindType() + "~";
                break;
            case R.id.tv_shake:
                controlMsg = electricFanIrData.getShake() + "~";
                break;
            case R.id.tv_timing:
                controlMsg = electricFanIrData.getTiming() + "~";
                break;
        }
        if (!StringUtil.isEmpty(controlMsg)) {
            Esp8266ControlBean controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, controlMsg);
            DeviceOkSocketControl.getInstance(context).sendControlMsg(controlBean);
//            showToast("发送指令成功");
        }
    }

    private List<String> orderList = new ArrayList<>();
    private int testIndex = 0;

    private void test() {

        orderList.add("1200,450,1250,450,400,1250,1250,450,1200,450,400,1250,450,1250,450,1250,400,1250,450,1250,450,1250,1250");
        orderList.add("1300,400,1250,450,400,1250,1250,450,1200,450,400,1250,450,1250,450,1250,450,1150,500,1250,450,1250,1250");
        orderList.add("1250,450,1250,400,450,1250,1250,450,1200,450,400,1250,450,1250,450,1250,400,1250,450,1250,450,1250,1250");
        orderList.add("1250,400,1250,450,400,1250,1250,450,1250,400,450,1250,400,1300,400,1250,450,1250,450,1250,400,1300,1200");
        orderList.add("1250,400,1300,400,450,1250,1250,400,1250,450,400,1250,450,1250,400,1300,400,1250,450,1250,450,1250,1250");
        orderList.add("1250,450,1200,450,450,1250,1250,400,1250,400,450,1250,450,1250,450,1250,400,1250,450,1250,450,1200,1300");
        orderList.add("1250,450,1200,400,450,1250,1250,400,1250,450,450,1200,450,1250,450,1250,450,1250,400,1250,450,1250,1250");
        orderList.add("1250,450,1200,450,400,1250,1250,450,1200,450,400,1300,400,1250,450,1250,450,1250,450,1250,400,1250,1250");
//        orderList.add("");
////        orderList.add("");

    }

    //  ====================== 时间监听 ===========================

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onConfirm(int stopMode, int count);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("control", msg);
        }
    }
}
