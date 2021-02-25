package com.lilanz.wificonnect.control_new;

import android.content.Context;

import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.data.myenum.DeviceType;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.lilanz.wificonnect.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备语音控制类
 * 把讯飞返回的语音结果 转换成对应的设备命令
 */
public class DeviceVoiceControl {

    private static DeviceVoiceControl instance;

    public static DeviceVoiceControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new DeviceVoiceControl(context);
                }
            }
        }
        return instance;
    }

    private Context context;
    private List<String> sceneList;

    private DeviceVoiceControl(Context context) {
        this.context = context;
        initData();
    }

    /**
     * 解析语音结果 转换成 设备对应命令
     *
     * @param status      区别 唤醒词
     * @param voiceResult 语音结果
     * @return 命令控制结果
     */
    public Esp8266ControlBean parseVoiceResult(boolean status, String voiceResult) {
        if (StringUtil.isEmpty(voiceResult)) {
            return null;
        }
        String control = null;
        String scene = null;
        DeviceType deviceType = null;
        String content = null;

        // 1.场景
        for (String s : sceneList) {
            if (voiceResult.contains(s)) {
                scene = s;
                break;
            }
        }
        if (StringUtil.isEmpty(scene)) {
            scene = SharePreferencesUtil.getMyRoom(context);
        }

        // 2.设备
        if (voiceResult.contains("灯")) {
            deviceType = DeviceType.LAMP;
        } else if (voiceResult.contains("热水器")) {
            deviceType = DeviceType.WATER_HEATER;
        } else if (voiceResult.contains("风扇")) {
            deviceType = DeviceType.ELECTRIC_FAN;
            if (voiceResult.contains("摇头")) {
                content = DeviceBean.CONTENT_SHAKE;
            }
        }

        if (!StringUtil.isEmpty(scene) && deviceType != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("device_type", deviceType);
            if (DeviceType.WATER_HEATER != deviceType) {
                map.put("device_position", scene);
            }
            List<DeviceBean> beanList = DBControl.quaryByColumn(context, DeviceBean.class, map);

            for (DeviceBean deviceBean : beanList) {
                // 3.控制方式
                if (voiceResult.contains("打开")) {
                    control = deviceBean.getControlData(DeviceBean.STATUS_OPEN, content);
                } else if (voiceResult.contains("关闭")) {
                    control = deviceBean.getControlData(DeviceBean.STATUS_CLOSE, content);
                }
//                switch (deviceType) {
//                    case LAMP:
//                    case WATER_HEATER:
//                        break;
//                    case ELECTRIC_FAN:
//                        break;
//                }

                Esp8266ControlBean controlBean = new Esp8266ControlBean();
                controlBean.ip = deviceBean.ip;
                controlBean.port = deviceBean.port;
                controlBean.control = control;
                return controlBean;
            }
        }

        return null;
    }

    private void initData() {
        sceneList = new ArrayList<>();
        sceneList.add("客厅");
        sceneList.add("门口");
        sceneList.add("卧室");
        sceneList.add("主卧");
        sceneList.add("侧卧");
        sceneList.add("客卧");
        sceneList.add("阳台");
    }
}
