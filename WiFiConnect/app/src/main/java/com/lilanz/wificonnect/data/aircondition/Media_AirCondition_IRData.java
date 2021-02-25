package com.lilanz.wificonnect.data.aircondition;

import com.lilanz.wificonnect.data.myenum.ProtocolType;

/**
 * 美的_空调接口类
 */
public class Media_AirCondition_IRData extends AirCondition_IRData {

    /**
     * 打开
     */
    public static final String OPEN = "B24D9F603CC3";

    /**
     * 关闭
     */
    public static final String CLOSE = "B24D1FE0649B";

    /**
     * 温度-17~30
     */
    public static final String TEMPERATURE_17 = "B24D9F600CF3";
    public static final String TEMPERATURE_18 = "B24D9F601CE3";
    public static final String TEMPERATURE_19 = "B24D9F603CC3";
    public static final String TEMPERATURE_20 = "B24D9F602CD3";
    public static final String TEMPERATURE_21 = "B24D9F606C93";
    public static final String TEMPERATURE_22 = "B24D9F607C83";
    public static final String TEMPERATURE_23 = "B24D9F605CA3";
    public static final String TEMPERATURE_24 = "B24D9F604CB3";
    public static final String TEMPERATURE_25 = "B24D9F60CC33";
    public static final String TEMPERATURE_26 = "B24D9F60DC23";
    public static final String TEMPERATURE_27 = "B24D9F609C63";
    public static final String TEMPERATURE_28 = "B24D9F608C73";
    public static final String TEMPERATURE_29 = "B24D9F60AC53";
    public static final String TEMPERATURE_30 = "B24D9F60BC43";


    /**
     * 模式-自动
     */
    public static final String MODE_AUTO = "B24D1FE0A857";
    /**
     * 模式-制冷
     */
    public static final String MODE_COLD = "B24D9F60A05F";
    /**
     * 模式-抽湿
     */
    public static final String MODE_DEHUMIDIFICATION = "B24D1FE0A45B";
    /**
     * 模式-制热
     */
    public static final String MODE_HEAT = "B24D9F60AC53";
    /**
     * 模式-送风
     */
    public static final String MODE_BLOW = "B24D9F60E41B";

    /**
     * 风速-静音
     */
    public static final String WIND_SPEED_QUIET = "B24DFF00A05F";
    /**
     * 风速-强度1
     */
    public static final String WIND_SPEED_1 = "B24D9F60A05F";
    /**
     * 风速-强度2
     */
    public static final String WIND_SPEED_2 = "B24D5FA0A05F";
    /**
     * 风速-强度3
     */
    public static final String WIND_SPEED_3 = "B24D3FC0A05F";
    /**
     * 风速-自动
     */
    public static final String WIND_SPEED_AUTO = "B24DBF40A05F";

    public static final String TIMING = "";

    public static final String SLEEPING = "";
    public static final String DRY = "";

    /**
     * 打开上下移动风页
     */
    public static final String OPEN_VER_WIND = "B946F50A04FB";
    /**
     * 关闭上下移动风页
     */
    public static final String CLOSE_VER_WIND = "B946F50A05FA";


    /**
     * 打开左右移动风页
     */
    public static final String OPEN_HOR_WIND = "B946F50A07F8";
    /**
     * 关闭左右移动风页
     */
    public static final String CLOSE_HOR_WIND = "B946F50A08F7";


    /**
     * 协议类型
     */
    private static final String protocol = ProtocolType.SAMSUNG.name;

    public String getCloseData() {
        return protocol + "," + CLOSE + "~";
    }

    public String getOpenData() {
        return protocol + "," + OPEN;
    }

    public String getWindType() {
        return null;
    }

    public String getTiming() {

        return null;
    }

    public String getTemperature(int temperature) {
        String result = TEMPERATURE_26;
        switch (temperature) {
            case 17:
                result = TEMPERATURE_17;
                break;
            case 18:
                result = TEMPERATURE_18;
                break;
            case 19:
                result = TEMPERATURE_19;
                break;
            case 20:
                result = TEMPERATURE_20;
                break;
            case 21:
                result = TEMPERATURE_21;
                break;
            case 22:
                result = TEMPERATURE_22;
                break;
            case 23:
                result = TEMPERATURE_23;
                break;
            case 24:
                result = TEMPERATURE_24;
                break;
            case 25:
                result = TEMPERATURE_25;
                break;
            case 26:
                result = TEMPERATURE_26;
                break;
            case 27:
                result = TEMPERATURE_27;
                break;
            case 28:
                result = TEMPERATURE_28;
                break;
            case 29:
                result = TEMPERATURE_29;
                break;
            case 30:
                result = TEMPERATURE_30;
                break;
        }
        return result;
    }
}
