package com.lilanz.wificonnect.data.electricfan;

import com.lilanz.wificonnect.data.myenum.ProtocolType;

/**
 * GREE品牌 - 风扇 - 红外控制数据
 */
public class GREE_ElectricFan_IRData extends ElectricFan_IRData {

    /**
     * 关闭：A32AB931
     */
    private static final String CLOSE = "1200,500,1150,500,400,1250,1200,500,1150,500,400,1250,400,1300,400,1250,400,1250,400,1300,400,1250,1200";

    /**
     * 打开或调速：143226DB
     */
    private static final String OPEN_OR_EXCHANGE = "1150,500,1200,450,400,1300,1200,450,1200,450,400,1300,400,1250,400,1250,400,1300,400,1250,1200,450,400";

    /**
     * 风类：371A3C86
     */
    private static final String WIND_TYPE = "1200,500,1150,500,400,1250,1200,500,1150,500,400,1250,400,1300,400,1250,400,1250,1200,500,400,1250,400";

    /**
     * 摇头：39D41DC6
     */
    private static final String SHAKE = "1150,500,1200,450,400,1300,1150,500,1200,450,400,1300,400,1250,1200,450,400,1300,400,1250,400,1250,400";

    /**
     * 定时：E0984BB6
     */
    private static final String TIMING = "1200,450,1250,450,400,1250,1200,500,1200,450,400,1250,400,1250,400,1300,1150,500,400,1250,400,1300,400";

    /**
     * 协议类型
     */
    private static final String protocol = ProtocolType.RAW.name;

    @Override
    public String getCloseData() {
        return protocol + "," + CLOSE;
    }

    @Override
    public String getOpenOrExchange() {
        return protocol + "," + OPEN_OR_EXCHANGE;
    }

    @Override
    public String getWindType() {
        return protocol + "," + WIND_TYPE;
    }

    @Override
    public String getShake() {
        return protocol + "," + SHAKE;
    }

    @Override
    public String getTiming() {
        return protocol + "," + TIMING;
    }
}
