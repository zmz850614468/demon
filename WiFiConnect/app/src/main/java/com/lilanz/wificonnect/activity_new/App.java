package com.lilanz.wificonnect.activity_new;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechUtility;
import com.lilanz.wificonnect.controls.SoundControl;
import com.lilanz.wificonnect.controls.XunFeiVoiceControl;
import com.lilanz.wificonnect.utils.BuildUtil;

public class App extends Application {

    public static Context context;
    public static boolean isDebug;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        isDebug = BuildUtil.isApkInDebug(this);

//        Bugly.init(context, "1fa246be97", true);
//        Bugly.setUserId(this, DeviceUtils.getPhoneName() + "_" + DeviceUtils.getSystemModel() + "--" + DeviceUtils.getSystemVersion());

        SoundControl.getInstance(context).initData();

        // 讯飞语音识别
        StringBuffer param = new StringBuffer();
        param.append("appid=5fc9e265");
//        param.append(",");
//        // 设置使用v5+
//        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(context, param.toString());
        XunFeiVoiceControl.getInstance(context);
    }

}
