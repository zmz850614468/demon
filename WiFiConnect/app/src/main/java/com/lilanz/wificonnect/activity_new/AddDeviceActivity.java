package com.lilanz.wificonnect.activity_new;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.data.myenum.BrandType;
import com.lilanz.wificonnect.data.myenum.DeviceType;
import com.lilanz.wificonnect.data.myenum.SceneType;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;
import com.lilanz.wificonnect.utils.StringUtil;


import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDeviceActivity extends Activity {

    public static final String ACTION_UPDATE = "update";    // 更新设备信息
    public static final String ACTION_ADD_NEW = "addNew";   // 添加新的设备

    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_device_name)
    EditText etDeviceName;
    @BindView(R.id.et_device_ip)
    EditText etDeviceIp;
    @BindView(R.id.et_device_port)
    EditText etDevicePort;
    @BindView(R.id.ns_device_type)
    NiceSpinner nsDeviceType;
    @BindView(R.id.ns_device_position)
    NiceSpinner nsDevicePosition;
    @BindView(R.id.ns_brand_type)
    NiceSpinner nsBrandType;
    @BindView(R.id.bt_delete)
    Button btDelete;
    @BindView(R.id.layout_open_setting)
    LinearLayout layoutOpenSetting;

    @BindView(R.id.layout_control)
    LinearLayout layoutControl;

    @BindView(R.id.tv_function_1)
    TextView tvFunction1;
    @BindView(R.id.tv_function_2)
    TextView tvFunction2;
    @BindView(R.id.tv_function_3)
    TextView tvFunction3;
    @BindView(R.id.tv_function_4)
    TextView tvFunction4;
    @BindView(R.id.et_function_1)
    EditText etFunction1;
    @BindView(R.id.et_function_2)
    EditText etFunction2;
    @BindView(R.id.et_function_3)
    EditText etFunction3;
    @BindView(R.id.et_function_4)
    EditText etFunction4;


    private List<String> deviceTypeList;
    private List<String> brandTypeList;
    private List<String> deviceControlList;
    private List<String> devicePositionList;

    private String action;
    private DeviceBean newDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
        action = getIntent().getStringExtra("action");
        if (!StringUtil.isEmpty(action)) {
            String json = getIntent().getStringExtra("device");
            newDevice = new Gson().fromJson(json, DeviceBean.class);
        }

        initService();
        initData();
        initUI();
    }

    @OnClick(R.id.bt_delete)
    public void onClicked(View v) {
        if ("客户端".equals(AppDataControl.selectedType)) {
            MsgBean msgBean = new MsgBean(MsgBean.DEVICE_DELETE, newDevice.toString());
            AppDataControl.sendMsg(msgBean);
        } else if ("直接控制设备".equals(AppDataControl.selectedType)) {
            DBControl.delete(this, DeviceBean.class, newDevice);
            showToast("删除设备成功！");
            HomeDeviceActivity.needUpdate = true;
            finish();
        }
    }

    @OnClick(R.id.bt_ok)
    public void onOkClicked(View v) {
        String deviceName = etDeviceName.getText().toString();
        String deviceIp = etDeviceIp.getText().toString();
        String devicePort = etDevicePort.getText().toString();

        if (StringUtil.isEmpty(deviceName)) {
            showToast("设备名称不能为空！");
            return;
        }
        if (StringUtil.isEmpty(deviceIp) || StringUtil.isEmpty(devicePort)) {
            showToast("设备ip和port不能为空！");
            return;
        }

        String deviceType = nsDeviceType.getText().toString();
        String devicePosition = nsDevicePosition.getText().toString();
        String brand = nsBrandType.getText().toString();

        if (newDevice == null) {
            newDevice = new DeviceBean();
        }
        newDevice.name = deviceName;
        newDevice.ip = deviceIp;
        newDevice.devicePosition = devicePosition;
        newDevice.port = Integer.parseInt(devicePort);
        newDevice.deviceType = DeviceType.getDeviceType(deviceType);
        newDevice.brand = BrandType.getBrandType(brand);
        Map<String, String> controlMap = new HashMap<>();
        controlMap.put(tvFunction1.getText().toString(), etFunction1.getText().toString());
        controlMap.put(tvFunction2.getText().toString(), etFunction2.getText().toString());
        controlMap.put(tvFunction3.getText().toString(), etFunction3.getText().toString());
        controlMap.put(tvFunction4.getText().toString(), etFunction4.getText().toString());
        newDevice.controlMap = new Gson().toJson(controlMap);

        if ("客户端".equals(AppDataControl.selectedType)) {
            if (AppDataControl.wifiService != null) {
                MsgBean bean = new MsgBean(MsgBean.DEVICE_ADD_OR_UPDATE, newDevice.toString());
                AppDataControl.wifiService.sendMsg(bean.toString());
            }
        } else if ("直接控制设备".equals(AppDataControl.selectedType)) {
            DBControl.createOrUpdate(this, DeviceBean.class, newDevice);
            showToast("添加或修改设备成功！");
            HomeDeviceActivity.needUpdate = true;
            if (ACTION_ADD_NEW.equals(action)) {
                SearchDeviceActivity.needUpdate = true;
            }
            finish();
        }
    }

    private void initUI() {
        if ("客户端".equals(AppDataControl.selectedType)) {
            etDevicePort.setText("80");
        } else if ("直接控制设备".equals(AppDataControl.selectedType)) {
            etDevicePort.setText("81");
        }

        btDelete.setVisibility(View.GONE);
        nsDeviceType.attachDataSource(deviceTypeList);
        nsDevicePosition.attachDataSource(devicePositionList);
        nsBrandType.attachDataSource(brandTypeList);

        nsDeviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        nsDeviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String deviceName = ((TextView) view).getText().toString();
                updateBrandType(deviceName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (newDevice != null) {
            tvTitle.setText("更新设备信息");
            etDeviceName.setText(newDevice.name);
            etDeviceIp.setText(newDevice.ip);
            etDevicePort.setText(newDevice.port + "");

            switch (newDevice.deviceType) {
                case LAMP:
                    ivPic.setBackgroundResource(R.mipmap.lamp);
                    break;
                case WATER_HEATER:
                    ivPic.setBackgroundResource(R.mipmap.water_heater);
                    break;
                case ELECTRIC_FAN:
                    ivPic.setBackgroundResource(R.mipmap.electric_fans);
                    break;
                case ELECTRIC_POT:
                    ivPic.setBackgroundResource(R.mipmap.electric_pot);
                    break;
            }

            // 设备类型
            for (int i = 1; i < deviceTypeList.size(); i++) {
                if (newDevice.deviceType.name.equals(deviceTypeList.get(i))) {
                    nsDeviceType.setSelectedIndex(i);
                    nsDeviceType.setSelected(true);
                    updateBrandType(newDevice.deviceType.name);
                    break;
                }
            }

            // 场景
            for (int i = 0; i < devicePositionList.size(); i++) {
                if (devicePositionList.get(i).equals(newDevice.devicePosition)) {
                    nsDevicePosition.setSelectedIndex(i);
                    nsDevicePosition.setSelected(true);
                }
            }

            // 品牌
            if (newDevice.brand != null) {
                for (int i = 0; i < brandTypeList.size(); i++) {
                    if (brandTypeList.get(i).equals(newDevice.brand.name)) {
                        nsBrandType.setSelectedIndex(i);
                        nsBrandType.setSelected(true);
                    }
                }
            }

            if (!StringUtil.isEmpty(action) && action.equals(ACTION_UPDATE)) {
                btDelete.setVisibility(View.VISIBLE);
            }

            // 控制映射对 TODO 之后可能需要修改
            if (newDevice.controlMap != null) {
                Map<String, String> controlMap = new Gson().fromJson(newDevice.controlMap, Map.class);
                etFunction1.setText(controlMap.get(tvFunction1.getText().toString()));
                etFunction2.setText(controlMap.get(tvFunction2.getText().toString()));
                etFunction3.setText(controlMap.get(tvFunction3.getText().toString()));
                etFunction4.setText(controlMap.get(tvFunction4.getText().toString()));
            }
        }
    }

    /**
     * 更新设备品牌
     */
    private void updateBrandType(String deviceName) {
        layoutControl.setVisibility(View.GONE);
        switch (DeviceType.getDeviceType(deviceName)) {
            case LAMP:
                brandTypeList = BrandType.getLampBrand();
                ivPic.setBackgroundResource(R.mipmap.lamp);
                break;
            case ELECTRIC_FAN:
                brandTypeList = BrandType.getElectricFanBrand();
                ivPic.setBackgroundResource(R.mipmap.electric_fans);
                break;
            case WATER_HEATER:
                brandTypeList = BrandType.getHeatWaterBrand();
                ivPic.setBackgroundResource(R.mipmap.water_heater);
                break;
            case AIR_CONDITION:
                brandTypeList = BrandType.getAirConditionBrand();
                ivPic.setBackgroundResource(R.mipmap.air_condition);
                break;
            case ELECTRIC_POT:
                brandTypeList = BrandType.getElectricPotBrang();
                ivPic.setBackgroundResource(R.mipmap.electric_pot);
                layoutControl.setVisibility(View.VISIBLE);
                break;
        }
        nsBrandType.attachDataSource(brandTypeList);
    }

    private void initData() {
        deviceTypeList = DeviceType.getDeviceName();
        brandTypeList = BrandType.getLampBrand();
        devicePositionList = SceneType.getSceneName();

        deviceControlList = new ArrayList<>();
        deviceControlList.add("开关式");
    }

    private void initService() {
        AppDataControl.addCallbackListener(callbackListener);
    }

    /**
     * 消息回调监听
     */
    private MsgCallbackListener callbackListener = new MsgCallbackListener() {
        @Override
        public void onDeviceUpdateCallback(String msg) {
            super.onDeviceUpdateCallback(msg);
            AddDeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(msg);
                }
            });
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        AppDataControl.removeCallbackListener(callbackListener);
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
