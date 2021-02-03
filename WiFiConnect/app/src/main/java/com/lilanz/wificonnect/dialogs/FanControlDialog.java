package com.lilanz.wificonnect.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.data.electricfan.ElectricFan_IRData;
import com.lilanz.wificonnect.data.electricfan.GREE_ElectricFan_IRData;
import com.lilanz.wificonnect.utils.StringUtil;

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
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void update(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
        // TODO 风扇的品牌
        this.electricFanIrData = new GREE_ElectricFan_IRData();
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
            case R.id.tv_timing:
                controlMsg = electricFanIrData.getTiming() + "~";
                break;
            case R.id.tv_type:
                controlMsg = electricFanIrData.getWindType() + "~";
                break;
            case R.id.tv_shake:
                controlMsg = electricFanIrData.getShake() + "~";
                break;
        }
        if (!StringUtil.isEmpty(controlMsg)) {
            Esp8266ControlBean controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, controlMsg);
            DeviceOkSocketControl.getInstance(context).sendControlMsg(controlBean);
//            showToast("发送指令成功");
        }
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
}