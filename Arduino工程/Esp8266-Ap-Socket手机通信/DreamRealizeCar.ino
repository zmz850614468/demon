

#include "DreamRealizeCar.h"

char *apSsid = "DreamingCar";
char *apPwd = "12345678";

const char *ssid = "car";
const char *pwd = "12345678";

void setup() {
    delay(2000);
    init(115200);
    initMode(0);
    initData();
    setCloseTime(0);

    beginWifiAp(apSsid, apPwd);
}

void loop() {
//    digitalWrite(4, LOW);   // Turn the LED on (Note that LOW is the voltage level
//    delayMicroseconds(1000);
//    digitalWrite(4, HIGH);  // Turn the LED off by making the voltage HIGH
//    delayMicroseconds(1000);
    handleTcpClient();
    fly();
}
