
#include "EspHttp.h"

char *apSsid = "esp8266_";
char *apPwd = "12345678";

const char *ssid = "edp8266";
const char *pwd = "12345678";


uint8_t MAC_array_AP[6];
char MAC_char_AP[18];

void setup() {
    delay(2000);
    init(115200);
    initMode(2);
    initData();

    // MAC地址后六位
    WiFi.softAPmacAddress(MAC_array_AP);
    for (int i = 0; i < sizeof(MAC_array_AP); ++i) {
        sprintf(MAC_char_AP, "%s%02x:", MAC_char_AP, MAC_array_AP[i]);
    }
    String macAp = String(MAC_char_AP);
    macAp.replace(":", "");
    macAp = macAp.substring(macAp.length()-6, macAp.length());

    
    // 清除路由器账号和密码缓存
    //int size = sizeof(config_type);
    //EEPROM.begin(size);//向系统申请1024kb ROM
    //for (int i = 0; i < size; i++){
    //    EEPROM.write(i, -1);
    // }
    //EEPROM.commit();//执行写入ROM

    // 连接路由器
    config_type config = loadConfig();
    int ssidLength = strlen(config.stassid);
    int pwdLength = strlen(config.stapsw);
    if(config.stassid && config.stapsw && pwdLength >= 8 &&  pwdLength <= 64 && ssidLength <= 32){
        //Serial.println("config content:");
        //Serial.println(config.stassid);
        //Serial.println(config.stapsw);
        beginWifiStation(config.stassid, config.stapsw);
    }else{
        beginWifiStation(ssid, pwd);
    }
    
    beginWifiAp((apSsid + macAp).c_str(), apPwd);
}

void loop() {
    handleWebClient();
    handleTcpClient();
    //delay(100);
}
