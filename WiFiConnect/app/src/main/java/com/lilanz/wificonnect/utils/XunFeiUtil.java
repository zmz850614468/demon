package com.lilanz.wificonnect.utils;

import com.google.gson.Gson;
import com.lilanz.wificonnect.beans.XunFeiBean;

public class XunFeiUtil {

    /**
     * 解析讯飞返回后的信息数据
     *
     * @param voiceMsg
     * @return
     */
    public static String getVoiceResult(String voiceMsg) {
        XunFeiBean xunFeiBean = new Gson().fromJson(voiceMsg, XunFeiBean.class);
        StringBuffer buffer = new StringBuffer();
        for (XunFeiBean.Ws w : xunFeiBean.ws) {
            for (XunFeiBean.Cw cw : w.cw) {
                buffer.append(cw.w);
            }
        }

        return buffer.toString();
    }

}
