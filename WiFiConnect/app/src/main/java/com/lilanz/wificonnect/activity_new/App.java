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
//        StringBuffer param = new StringBuffer();
//        param.append("appid=604595a8");
//        SpeechUtility.createUtility(context, param.toString());
//        XunFeiVoiceControl.getInstance(context);
    }

}
