#ifndef _ESPHTTP_H_
#define _ESPHTTP_H_

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>     //导入Flash库文件

ESP8266WebServer webServer(80);     // 开启http服务端口
WiFiServer wifiServer(81);          // 开启Tcp服务断开
const int PIN_OPERATE = 0;          // 控制引脚
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

/**
 * 初始化数据
 */
void initData(){
    pinMode(PIN_OPERATE, OUTPUT);
    digitalWrite(PIN_OPERATE, HIGH);
}
// ==============================    wifi设置保存    =============================

struct config_type
{
  char stassid[32];//定义配网得到的WIFI名长度(最大32字节)
  char stapsw[64];//定义配网得到的WIFI密码长度(最大64字节)
};

config_type config;//声明定义内容

void saveConfig()//保存函数
{
    EEPROM.begin(1024);//向系统申请1024kb ROM
    //开始写入
    uint8_t *p = (uint8_t*)(&config);
    for (int i = 0; i < sizeof(config); i++)
    {
      EEPROM.write(i, *(p + i)); //在闪存内模拟写入
    }
    EEPROM.commit();//执行写入ROM
}

config_type loadConfig()//读取函数
{
    EEPROM.begin(1024);
    uint8_t *p = (uint8_t*)(&config);
    for (int i = 0; i < sizeof(config); i++)
    {
      *(p + i) = EEPROM.read(i);
    }
    EEPROM.commit();
    return config;
}

// ==============================     Ap     =============================
/**
 * AP模式：处理IP设置
 */
 String ap;
void dealIpSetting() {
    //Serial.print("参数个数：");
    //Serial.println(webServer.args());

    if (webServer.hasArg("type")) {
        String type = webServer.arg("type");
        //Serial.print("type：");
        //Serial.println(type.c_str());

        if (type.equals("setting") && webServer.hasArg("wifiName") && webServer.hasArg("wifiPwd")) {       // 设置路由器名称和密码
            strcpy(config.stassid, webServer.arg("wifiName").c_str()) ;
            strcpy(config.stapsw, webServer.arg("wifiPwd").c_str());
            Serial.print(config.stassid);
            saveConfig();//调用保存函数

            webServer.send(200, "text/html", "{\"code\":0;\"msg\":\"setting succeed\"}");
            delay(500);
            //ESP.reset();          // 硬件重启芯片
            ESP.restart();          // 软件重启芯片
        } else if (type.equals("ip")) {     // 返回自己的ip地址
            String ip = WiFi.localIP().toString().c_str();
            webServer.send(200, "text/html", "{\"code\":0;\"msg\":\"get ip succeed\";\"ap\":\""+ap+"\";\"ip\":\""+ip+"\"}");
        } else if (type.equals("reset")) {
            webServer.send(200, "text/html", "{\"code\":0;\"msg\":\"reset succeed\"}");
            delay(500);
            ESP.reset();      // 硬件重启芯片
        } else if(type.equals("operate") && webServer.hasArg("status")){
            String status = webServer.arg("status");
            if(status.equals("open")){          // 打开，低电平
                digitalWrite(PIN_OPERATE, LOW);
                webServer.send(200, "text/html", "{\"code\":0;\"msg\":\"open succeed\";\"status\":\"open\"}");
            }else if(status.equals("close")){   // 关闭，高电平
                digitalWrite(PIN_OPERATE, HIGH);
                webServer.send(200, "text/html", "{\"code\":0;\"msg\":\"close succeed\";\"status\":\"close\"}");
            }else if(status.equals("query")){   // 查询当前状态
                int sta = digitalRead(PIN_OPERATE);
                if(sta == 1){
                    webServer.send(200, "text/html", "{\"code\":0;\"msg\":\"query succeed\";\"status\":\"close\"}");
                }else{
                    webServer.send(200, "text/html", "{\"code\":0;\"msg\":\"query succeed\";\"status\":\"open\"}");
                }
            }
        }
        else{
            webServer.send(200, "text/html", "{\"code\":-1;\"msg\":\"error params!!\";\"status\":\"open\"}");
        }
    } else {
        webServer.send(200, "text/html", "{\"code\":-1;\"msg\":\"error params!!\";\"status\":\"open\"}");
    }

}

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

    ap = ssid;
    WiFi.softAP(ssid, pwd);

    IPAddress myIp = WiFi.softAPIP();
    Serial.print("AP IP address: ");
    Serial.println(myIp);

    webServer.on("/wifi", dealIpSetting);
    webServer.begin();
}

/**
 * loop()
 * AP模式：等待客户端连接
 */
void handleWebClient() {
    webServer.handleClient();
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

/**
 * loop()
 * 处理
 **/
void handleTcpClient(){
    if(WiFi.status() != WL_CONNECTED){
        return;
    }

    if(myClient && myClient.available()){
        String req = myClient.readStringUntil('\r');
        req.replace("\n","");
        if(req.length() > 0){
            if(req.equals("open")){
                digitalWrite(PIN_OPERATE, LOW);
                sendMsg("opened");
            }else if(req.equals("close")){
                digitalWrite(PIN_OPERATE, HIGH);
                sendMsg("closed");
            }else if(req.equals("query")){
                int sta = digitalRead(PIN_OPERATE);
                if(sta == 1){
                    sendMsg("close");
                }else{
                    sendMsg("open");
                }
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

/**
 * setup()
 * 站点模式，连接路由器
 * @param ssid 路由器名称
 * @param pwd 路由器密码
 */
void beginWifiStation(const char *ssid, const char *pwd) {
    // 1.站点模式，连接路由器WIFI_AP_STA
//    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, pwd);
    WiFi.setAutoConnect(true);  // 设置自动连接
    // 2.等待连接路由器成功,10次失败后则退出
    int count = 0;
    while (WiFi.status() != WL_CONNECTED) {
        if (count >= 10) {
            Serial.println("connect ap failure...");
            return;
        }
        Serial.println("delay...");
        delay(500);
        count++;
    }

    wifiServer.begin();
    // 3.连接成功后的IP地址
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
}

#endif
