#ifndef _DREAM_REALIZE_CAR_H_
#define _DREAM_REALIZE_CAR_H_

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>     //导入Flash库文件

ESP8266WebServer webServer(80);     // 开启http服务端口
WiFiServer wifiServer(81);          // 开启Tcp服务断开
/**
 * 初始化Esp8266
 * @param bote 波特率
 */
void init(int bote) {
    Serial.begin(bote);     // 开启传输波特率
}

/**
 * 设置wifi模式
 * @param mode
 * 0：AP
 * 1：STA
 * 2：AP+STA
 */
void initMode(int mode) {
    switch (mode) {
        case 0:
            WiFi.mode(WIFI_AP);
            break;
        case 1:
            WiFi.mode(WIFI_STA);
            break;
        case 2:
            WiFi.mode(WIFI_AP_STA);
            break;
    }
}



// ==============================     Ap     =============================
/**
 * setup()
 * AP模式：等待设备连接
 * @param ssid
 * @param pwd
 */
void beginWifiAp(const char *ssid,const char *pwd) {
    Serial.println();
    Serial.print("Ap名称：");
    Serial.print(ssid);
    Serial.print(";   Ap密码：");
    Serial.println(pwd);

    WiFi.softAP(ssid, pwd);

    IPAddress myIp = WiFi.softAPIP();
    Serial.print("AP IP address: ");
    Serial.println(myIp);

    wifiServer.begin();
    //webServer.on("/wifi", dealIpSetting);
    //webServer.begin();
}

// ================================   Sta   =========================================

WiFiClient myClient;            // 只能响应最新的连接
long updateTime = 0;            // socket的更新时间
long autoCloseTime = 15000;     // socket 15秒无通信则关闭( >0:生效; <=0:长连接)

/**
 * 自动关闭时间
 **/
void setCloseTime(long closeTime){
    autoCloseTime = closeTime;
}

/**
 * 为数据添加头数据:数据长度(4个字节)
 **/
void sendMsg(char *arr){
    char head[4];
    for(int i = 0; i < 4; i++){
        head[i] = 0x00;
    }

    int length = strlen(arr);
    for(int i = 3; i >= 0; i--){
        int count = length % 256;
        length = length / 256;
        head[i] = count;
        if(length <= 0){
            break;
        }
    }

    myClient.write(head, 4);
    myClient.write(arr);
}


const int PIN_OPERATE = 4;      // 控制引脚
const int PIN_DIRECT = 5;       // 方向引脚
/**
 * 初始化数据
 */
void initData(){
    pinMode(PIN_OPERATE, OUTPUT);
    pinMode(PIN_DIRECT, OUTPUT);
}

/**
 * loop()
 * 起飞吧，小车
 **/
const int FIRST_DELAY = 1000;    // 最高每秒1000:2.5转速
int upOrDown = 0;       // 1:up ; 0:stop ; -1:down
int leftOrRight = 0;    // 1:left ; 0:stop ; -1:right
void fly(){
    int delayTime = FIRST_DELAY;
    if(upOrDown == 1){
        digitalWrite(PIN_DIRECT, LOW);
        digitalWrite(PIN_OPERATE, LOW);
        delayMicroseconds(FIRST_DELAY);
        digitalWrite(PIN_OPERATE, HIGH);
        delayMicroseconds(FIRST_DELAY);
    }else if(upOrDown == -1){ 
        digitalWrite(PIN_DIRECT, HIGH);
        digitalWrite(PIN_OPERATE, LOW);
        delayMicroseconds(FIRST_DELAY);
        digitalWrite(PIN_OPERATE, HIGH);
        delayMicroseconds(FIRST_DELAY);
    }else{
        digitalWrite(PIN_OPERATE, HIGH);
    }
}

/**
 * loop()
 * 处理
 **/
void handleTcpClient(){
//    if(WiFi.status() != WL_CONNECTED){
//        Serial.println("WiFi.status()");
//        return;
//    }

//    Serial.println("handleTcpClient");
    if(myClient && myClient.available()){
        String req = myClient.readStringUntil('\r');
        req.replace("\n","");
        Serial.println(req);
        if(req.length() > 0){
            if(req.equals("left")){
                leftOrRight = 1;
            }else if(req.equals("right")){
                leftOrRight = -1;
            }else if(req.equals("up")){
                upOrDown = 1;
            }else if(req.equals("down")){
                upOrDown = -1;
            }else if(req.equals("hStop")){
                leftOrRight = 0;
            }else if(req.equals("vStop")){
                upOrDown = 0;
            }
        }
        updateTime = millis();
    }

    if(myClient){
        // 如果时间溢出，则重新计时
        long currentTime = millis();
        if(currentTime < updateTime){
            updateTime = currentTime;
        }
    
        // 长时间不通信，则关闭socket
        if(autoCloseTime > 0 && currentTime - updateTime > autoCloseTime){
            myClient.stop();
            //Serial.println("主动关闭socket");
        }
    }

    //检查客户端是否已连接
    WiFiClient client = wifiServer.available();
    if (!client) {
        return;
    }

    if(myClient){
        myClient.stop();
    }
    myClient = client;
    updateTime = millis();
}

#endif
