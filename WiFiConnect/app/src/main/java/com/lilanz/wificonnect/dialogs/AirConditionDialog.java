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
import com.lilanz.wificonnect.data.aircondition.AirCondition_IRData;
import com.lilanz.wificonnect.utils.StringUtil;

import org.angmarch.views.NiceSpinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 风扇控制面板
 */
public class AirConditionDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.ns_temperature)
    NiceSpinner nsTemperature;
    @BindView(R.id.ns_mode)
    NiceSpinner nsMode;
    @BindView(R.id.ns_wind_speed)
    NiceSpinner nsWindSpeed;

    private List<String> temperatureList;
    private List<String> modeList;
    private List<String> windSpeedList;

    private Context context;
    private DeviceBean deviceBean;

    private AirCondition_IRData airConditionIrData;

    public AirConditionDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_air_condition);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void update(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
        // 风扇的品牌
        this.airConditionIrData = AirCondition_IRData.getInstance(deviceBean.brand);
        updateUI();
    }

    private void updateUI() {
        temperatureList = airConditionIrData.getTemperatureList();
        modeList = airConditionIrData.getModeList();
        windSpeedList = airConditionIrData.getWindSpeedList();

        nsTemperature.attachDataSource(temperatureList);
        nsMode.attachDataSource(modeList);
        nsWindSpeed.attachDataSource(windSpeedList);
    }

    @OnClick({R.id.tv_open, R.id.tv_close})
    public void onClicked(View v) {
        String controlMsg = null;
        switch (v.getId()) {
            case R.id.tv_open:
                controlMsg = airConditionIrData.getOpenData() + "~";
                break;
            case R.id.tv_close:
                controlMsg = airConditionIrData.getCloseData() + "~";
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

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("control", msg);
        }
    }
}
