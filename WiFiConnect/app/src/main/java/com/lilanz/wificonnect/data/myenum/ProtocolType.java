package com.lilanz.wificonnect.data.myenum;

/**
 * 红外传输协议类型
 */
public enum ProtocolType {
    RAW("raw"),
    SAMSUNG("samsung"),
    NEC("NEC"),
    ;

    public String name;

    ProtocolType(String name) {
        this.name = name;
    }
}
