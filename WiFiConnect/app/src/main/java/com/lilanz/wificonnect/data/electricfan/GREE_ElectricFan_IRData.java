package com.lilanz.wificonnect.data.electricfan;

/**
 * GREE品牌 - 风扇 - 红外控制数据
 */
public class GREE_ElectricFan_IRData extends ElectricFan_IRData {

    /**
     * 关闭
     */
    private static final String CLOSE = "1200,500,1150,500,400,1250,1200,500,1150,500,400,1250,400,1300,400,1250,400,1250,400,1300,400,1250,1200";

    /**
     * 打开或调速
     */
    private static final String OPEN_OR_EXCHANGE = "1150,500,1200,450,400,1300,1200,450,1200,450,400,1300,400,1250,400,1250,400,1300,400,1250,1200,450,400";

    /**
     * 风类
     */
    private static final String WIND_TYPE = "1200,500,1150,500,400,1250,1200,500,1150,500,400,1250,400,1300,400,1250,400,1250,1200,500,400,1250,400";

    /**
     * 摇头
     */
    private static final String SHAKE = "1150,500,1200,450,400,1300,1150,500,1200,450,400,1300,400,1250,1200,450,400,1300,400,1250,400,1250,400";

    /**
     * 定时
     */
    private static final String TIMING = "1200,450,1250,450,400,1250,1200,500,1200,450,400,1250,400,1250,400,1300,1150,500,400,1250,400,1300,400";

    @Override
    public String getCloseData() {
        return CLOSE;
    }

    @Override
    public String getOpenOrExchange() {
        return OPEN_OR_EXCHANGE;
    }

    @Override
    public String getWindType() {
        return WIND_TYPE;
    }

    @Override
    public String getShake() {
        return SHAKE;
    }

    @Override
    public String getTiming() {
        return TIMING;
    }
}
