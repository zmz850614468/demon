package com.lilanz.wificonnect.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.HomeDeviceActivity;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.controls.MediaControl;
import com.lilanz.wificonnect.data.GREE_ElectricFan_IRData;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 风扇控制面板
 */
public class FanControlDialog extends Dialog implements DialogInterface.OnClickListener {


    private Context context;
    private int selectedMode;
    private DeviceBean deviceBean;

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
    }

    private void initUI() {
    }

    @OnClick({R.id.tv_open_or_exchange, R.id.tv_close, R.id.tv_timing, R.id.tv_type, R.id.tv_shake})
    public void onClicked(View v) {
        Esp8266ControlBean controlBean = null;
        switch (v.getId()) {
            case R.id.tv_open_or_exchange:
                controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, GREE_ElectricFan_IRData.OPEN_OR_EXCHANGE + "~");
                break;
            case R.id.tv_close:
                controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, GREE_ElectricFan_IRData.CLOSE + "~");
                break;
            case R.id.tv_timing:
                controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, GREE_ElectricFan_IRData.TIMING + "~");
                break;
            case R.id.tv_type:
                controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, GREE_ElectricFan_IRData.WIND_TYPE + "~");
                break;
            case R.id.tv_shake:
                controlBean = new Esp8266ControlBean(deviceBean.ip, deviceBean.port, GREE_ElectricFan_IRData.SHAKE + "~");
                break;
        }
        if (controlBean != null) {
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
