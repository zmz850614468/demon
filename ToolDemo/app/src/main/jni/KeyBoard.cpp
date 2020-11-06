//
// Created by liang on 2017/1/3.
//

#include <jni.h>
#include <stdio.h>
#include <linux/input.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <pthread.h>
#include <unistd.h>
#include "KeyBoard.h"

using namespace std;

const char* PathString2;
pthread_t MyPthread2;

char InputString[128];
int InputStringPc=0;
bool InputFlag=false;
__u16 lastCode = 0;     // 上一次按钮值

void* pthread_read_keyboard(void* arg)
{
    int keys_fd;
    struct input_event event;
    keys_fd=open(PathString2,O_RDONLY);
    if(keys_fd <= 0)
    {
        return 0;
    }
    for(;;)
    {
        usleep(500);
        if(read(keys_fd,&event,sizeof(event)) == sizeof(event))
        {
            if(event.type==EV_KEY)
                if(event.value==0)// || event.value==1
                {
                    if(event.code == KEY_ESC)
                    {
                        break;
                    }
                    else if(event.code == KEY_ENTER)
                    {
                        InputString[InputStringPc++]=0;
                        InputFlag=true;
                    }
                    else if(event.code == KEY_0)
                    {
                        InputString[InputStringPc++]=0x30;
                    }
                    else if((event.code >= KEY_1)&&(event.code <= KEY_9))
                    {
                        InputString[InputStringPc++]=(0x31)+(event.code-KEY_1);
                    }
                    else if(((event.code >= KEY_Q)&&(event.code <= KEY_P))
                        ||((event.code >= KEY_A)&&(event.code <= KEY_L))
                        ||((event.code >= KEY_Z)&&(event.code <= KEY_M)))
                    {
                        switch(event.code)
                        {
                            case KEY_A: InputString[InputStringPc++]='A'; break;
                            case KEY_B: InputString[InputStringPc++]='B'; break;
                            case KEY_C: InputString[InputStringPc++]='C'; break;
                            case KEY_D: InputString[InputStringPc++]='D'; break;
                            case KEY_E: InputString[InputStringPc++]='E'; break;
                            case KEY_F: InputString[InputStringPc++]='F'; break;
                            case KEY_G: InputString[InputStringPc++]='G'; break;
                            case KEY_H: InputString[InputStringPc++]='H'; break;
                            case KEY_I: InputString[InputStringPc++]='I'; break;
                            case KEY_J: InputString[InputStringPc++]='J'; break;
                            case KEY_K: InputString[InputStringPc++]='K'; break;
                            case KEY_L: InputString[InputStringPc++]='L'; break;
                            case KEY_M: InputString[InputStringPc++]='M'; break;
                            case KEY_N: InputString[InputStringPc++]='N'; break;
                            case KEY_O: InputString[InputStringPc++]='O'; break;
                            case KEY_P: InputString[InputStringPc++]='P'; break;
                            case KEY_Q: InputString[InputStringPc++]='Q'; break;
                            case KEY_R: InputString[InputStringPc++]='R'; break;
                            case KEY_S: InputString[InputStringPc++]='S'; break;
                            case KEY_T: InputString[InputStringPc++]='T'; break;
                            case KEY_U: InputString[InputStringPc++]='U'; break;
                            case KEY_V: InputString[InputStringPc++]='V'; break;
                            case KEY_W: InputString[InputStringPc++]='W'; break;
                            case KEY_X: InputString[InputStringPc++]='X'; break;
                            case KEY_Y: InputString[InputStringPc++]='Y'; break;
                            case KEY_Z: InputString[InputStringPc++]='Z'; break;
                        }
                    }else if(event.code == KEY_MINUS){
                        if (lastCode == KEY_LEFTSHIFT || lastCode == KEY_RIGHTSHIFT){
                            InputString[InputStringPc++] = '_';
                        } else{
                            InputString[InputStringPc++]='-';
                        }
                    } else if (event.code == KEY_BACKSPACE){
                        if(InputStringPc > 0){
                            InputStringPc--;
                        }
                    }

                    // ================= 以下 小键盘的按钮 =====================
                    switch(event.code){
                        case KEY_KP0:   // 0
                            InputString[InputStringPc++] = '0';
                            break;
                        case KEY_KP1:   // 1
                            InputString[InputStringPc++] = '1';
                            break;
                        case KEY_KP2:   // 2
                            InputString[InputStringPc++] = '2';
                            break;
                        case KEY_KP3:   // 3
                            InputString[InputStringPc++] = '3';
                            break;
                        case KEY_KP4:   // 4
                            InputString[InputStringPc++] = '4';
                            break;
                        case KEY_KP5:   // 5
                            InputString[InputStringPc++] = '5';
                            break;
                        case KEY_KP6:   // 6
                            InputString[InputStringPc++] = '6';
                            break;
                        case KEY_KP7:   // 7
                            InputString[InputStringPc++] = '7';
                            break;
                        case KEY_KP8:   // 8
                            InputString[InputStringPc++] = '8';
                            break;
                        case KEY_KP9:   // 9
                            InputString[InputStringPc++] = '9';
                            break;
                        case KEY_KPSLASH:       // /
                            InputString[InputStringPc++] = '/';
                            break;
                        case KEY_KPASTERISK:    // *
                            InputString[InputStringPc++] = '*';
                            break;
                        case KEY_KPMINUS:       // -
                            InputString[InputStringPc++] = '-';
                            break;
                        case KEY_KPPLUS:        // +
                            InputString[InputStringPc++] = '+';
                            break;
                        case KEY_KPDOT:         // .
                            InputString[InputStringPc++] = '.';
                            break;
                        case KEY_KPENTER:       // 回车
                            InputString[InputStringPc++] = 0;
                            InputFlag=true;
                            break;
                    }
                    // =================== 以上 小键盘的按钮 ===========================


                    if(InputStringPc>=128)  InputStringPc=0;
                    lastCode = event.code;
                }
        }
    }
    //LOGE("pthread_read_keyboard exit\n");
    close(keys_fd);
    return 0;
}


extern "C" JNIEXPORT void JNICALL Java_android_1keyboard_1listen_KeyBoard_Start_1KeyBoard(JNIEnv *env, jobject, jstring path)
{
    PathString2 = env->GetStringUTFChars(path, NULL);
    pthread_create(&MyPthread2,NULL,pthread_read_keyboard,NULL);
}




extern "C" JNIEXPORT jstring JNICALL Java_android_1keyboard_1listen_KeyBoard_Get_1Input(JNIEnv *env, jobject)
{
    if(InputFlag==false)
    {
        const char* tmpstr = "null";
        jstring rtstr = env->NewStringUTF(tmpstr);
        return rtstr;
    }

    InputString[InputStringPc++]=0;
    jstring rtstr = env->NewStringUTF(InputString);

    InputFlag=false;
    InputStringPc=0;
    return rtstr;
}

//==================================  dev3  =============================================

const char* PathString3;
pthread_t MyPthread3;

char InputString3[128];
int InputStringPc3=0;
bool InputFlag3=false;
__u16 lastCode3 = 0;     // 上一次按钮值

void* pthread_read_keyboard3(void* arg)
{
    int keys_fd;
    struct input_event event;
    keys_fd=open(PathString3,O_RDONLY);
    if(keys_fd <= 0)
    {
        return 0;
    }
    for(;;)
    {
        usleep(500);
        if(read(keys_fd,&event,sizeof(event)) == sizeof(event))
        {
            if(event.type==EV_KEY)
                if(event.value==0)// || event.value==1
                {
                    if(event.code == KEY_ESC)
                    {
                        break;
                    }
                    else if(event.code == KEY_ENTER)
                    {
                        InputString3[InputStringPc3++]=0;
                        InputFlag3=true;
                    }
                    else if(event.code == KEY_0)
                    {
                        InputString3[InputStringPc3++]=0x30;
                    }
                    else if((event.code >= KEY_1)&&(event.code <= KEY_9))
                    {
                        InputString3[InputStringPc3++]=(0x31)+(event.code-KEY_1);
                    }
                    else if(((event.code >= KEY_Q)&&(event.code <= KEY_P))
                            ||((event.code >= KEY_A)&&(event.code <= KEY_L))
                            ||((event.code >= KEY_Z)&&(event.code <= KEY_M)))
                    {
                        switch(event.code)
                        {
                            case KEY_A: InputString3[InputStringPc3++]='A'; break;
                            case KEY_B: InputString3[InputStringPc3++]='B'; break;
                            case KEY_C: InputString3[InputStringPc3++]='C'; break;
                            case KEY_D: InputString3[InputStringPc3++]='D'; break;
                            case KEY_E: InputString3[InputStringPc3++]='E'; break;
                            case KEY_F: InputString3[InputStringPc3++]='F'; break;
                            case KEY_G: InputString3[InputStringPc3++]='G'; break;
                            case KEY_H: InputString3[InputStringPc3++]='H'; break;
                            case KEY_I: InputString3[InputStringPc3++]='I'; break;
                            case KEY_J: InputString3[InputStringPc3++]='J'; break;
                            case KEY_K: InputString3[InputStringPc3++]='K'; break;
                            case KEY_L: InputString3[InputStringPc3++]='L'; break;
                            case KEY_M: InputString3[InputStringPc3++]='M'; break;
                            case KEY_N: InputString3[InputStringPc3++]='N'; break;
                            case KEY_O: InputString3[InputStringPc3++]='O'; break;
                            case KEY_P: InputString3[InputStringPc3++]='P'; break;
                            case KEY_Q: InputString3[InputStringPc3++]='Q'; break;
                            case KEY_R: InputString3[InputStringPc3++]='R'; break;
                            case KEY_S: InputString3[InputStringPc3++]='S'; break;
                            case KEY_T: InputString3[InputStringPc3++]='T'; break;
                            case KEY_U: InputString3[InputStringPc3++]='U'; break;
                            case KEY_V: InputString3[InputStringPc3++]='V'; break;
                            case KEY_W: InputString3[InputStringPc3++]='W'; break;
                            case KEY_X: InputString3[InputStringPc3++]='X'; break;
                            case KEY_Y: InputString3[InputStringPc3++]='Y'; break;
                            case KEY_Z: InputString3[InputStringPc3++]='Z'; break;
                        }
                    }else if(event.code == KEY_MINUS){
                        if (lastCode3 == KEY_LEFTSHIFT || lastCode3 == KEY_RIGHTSHIFT){
                            InputString3[InputStringPc3++] = '_';
                        } else{
                            InputString3[InputStringPc3++]='-';
                        }
                    } else if (event.code == KEY_BACKSPACE){
                        if(InputStringPc3 > 0){
                            InputStringPc3--;
                        }
                    }

                    // ================= 以下 小键盘的按钮 =====================
                    switch(event.code){
                        case KEY_KP0:   // 0
                            InputString3[InputStringPc3++] = '0';
                            break;
                        case KEY_KP1:   // 1
                            InputString3[InputStringPc3++] = '1';
                            break;
                        case KEY_KP2:   // 2
                            InputString3[InputStringPc3++] = '2';
                            break;
                        case KEY_KP3:   // 3
                            InputString3[InputStringPc3++] = '3';
                            break;
                        case KEY_KP4:   // 4
                            InputString3[InputStringPc3++] = '4';
                            break;
                        case KEY_KP5:   // 5
                            InputString3[InputStringPc3++] = '5';
                            break;
                        case KEY_KP6:   // 6
                            InputString3[InputStringPc3++] = '6';
                            break;
                        case KEY_KP7:   // 7
                            InputString3[InputStringPc3++] = '7';
                            break;
                        case KEY_KP8:   // 8
                            InputString3[InputStringPc3++] = '8';
                            break;
                        case KEY_KP9:   // 9
                            InputString3[InputStringPc3++] = '9';
                            break;
                        case KEY_KPSLASH:       // /
                            InputString3[InputStringPc3++] = '/';
                            break;
                        case KEY_KPASTERISK:    // *
                            InputString3[InputStringPc3++] = '*';
                            break;
                        case KEY_KPMINUS:       // -
                            InputString3[InputStringPc3++] = '-';
                            break;
                        case KEY_KPPLUS:        // +
                            InputString3[InputStringPc3++] = '+';
                            break;
                        case KEY_KPDOT:         // .
                            InputString3[InputStringPc3++] = '.';
                            break;
                        case KEY_KPENTER:       // 回车
                            InputString3[InputStringPc3++] = 0;
                            InputFlag3=true;
                            break;
                    }
                    // =================== 以上 小键盘的按钮 ===========================


                    if(InputStringPc3>=128)  InputStringPc3=0;
                    lastCode3 = event.code;
                }
        }
    }
    //LOGE("pthread_read_keyboard exit\n");
    close(keys_fd);
    return 0;
}

extern "C" JNIEXPORT void JNICALL Java_android_1keyboard_1listen_KeyBoard_Start_1KeyBoard3(JNIEnv *env, jobject, jstring path)
{
    PathString3 = env->GetStringUTFChars(path, NULL);
    pthread_create(&MyPthread3,NULL,pthread_read_keyboard3,NULL);
}

extern "C" JNIEXPORT jstring JNICALL Java_android_1keyboard_1listen_KeyBoard_Get_1Input3(JNIEnv *env, jobject)
{
    if(InputFlag3==false)
    {
        const char* tmpstr = "null";
        jstring rtstr = env->NewStringUTF(tmpstr);
        return rtstr;
    }

    InputString3[InputStringPc3++]=0;
    jstring rtstr = env->NewStringUTF(InputString3);

    InputFlag3=false;
    InputStringPc3=0;
    return rtstr;
}

//==================================  dev4  =============================================

const char* PathString4;
pthread_t MyPthread4;

char InputString4[128];
int InputStringPc4=0;
bool InputFlag4=false;
__u16 lastCode4 = 0;     // 上一次按钮值


void* pthread_read_keyboard4(void* arg)
{
    int keys_fd;
    struct input_event event;
    keys_fd=open(PathString4,O_RDONLY);
    if(keys_fd <= 0)
    {
        return 0;
    }
    for(;;)
    {
        usleep(500);
        if(read(keys_fd,&event,sizeof(event)) == sizeof(event))
        {
            if(event.type==EV_KEY)
                if(event.value==0)// || event.value==1
                {
                    if(event.code == KEY_ESC)
                    {
                        break;
                    }
                    else if(event.code == KEY_ENTER)
                    {
                        InputString4[InputStringPc4++]=0;
                        InputFlag4=true;
                    }
                    else if(event.code == KEY_0)
                    {
                        InputString4[InputStringPc4++]=0x30;
                    }
                    else if((event.code >= KEY_1)&&(event.code <= KEY_9))
                    {
                        InputString4[InputStringPc4++]=(0x31)+(event.code-KEY_1);
                    }
                    else if(((event.code >= KEY_Q)&&(event.code <= KEY_P))
                            ||((event.code >= KEY_A)&&(event.code <= KEY_L))
                            ||((event.code >= KEY_Z)&&(event.code <= KEY_M)))
                    {
                        switch(event.code)
                        {
                            case KEY_A: InputString4[InputStringPc4++]='A'; break;
                            case KEY_B: InputString4[InputStringPc4++]='B'; break;
                            case KEY_C: InputString4[InputStringPc4++]='C'; break;
                            case KEY_D: InputString4[InputStringPc4++]='D'; break;
                            case KEY_E: InputString4[InputStringPc4++]='E'; break;
                            case KEY_F: InputString4[InputStringPc4++]='F'; break;
                            case KEY_G: InputString4[InputStringPc4++]='G'; break;
                            case KEY_H: InputString4[InputStringPc4++]='H'; break;
                            case KEY_I: InputString4[InputStringPc4++]='I'; break;
                            case KEY_J: InputString4[InputStringPc4++]='J'; break;
                            case KEY_K: InputString4[InputStringPc4++]='K'; break;
                            case KEY_L: InputString4[InputStringPc4++]='L'; break;
                            case KEY_M: InputString4[InputStringPc4++]='M'; break;
                            case KEY_N: InputString4[InputStringPc4++]='N'; break;
                            case KEY_O: InputString4[InputStringPc4++]='O'; break;
                            case KEY_P: InputString4[InputStringPc4++]='P'; break;
                            case KEY_Q: InputString4[InputStringPc4++]='Q'; break;
                            case KEY_R: InputString4[InputStringPc4++]='R'; break;
                            case KEY_S: InputString4[InputStringPc4++]='S'; break;
                            case KEY_T: InputString4[InputStringPc4++]='T'; break;
                            case KEY_U: InputString4[InputStringPc4++]='U'; break;
                            case KEY_V: InputString4[InputStringPc4++]='V'; break;
                            case KEY_W: InputString4[InputStringPc4++]='W'; break;
                            case KEY_X: InputString4[InputStringPc4++]='X'; break;
                            case KEY_Y: InputString4[InputStringPc4++]='Y'; break;
                            case KEY_Z: InputString4[InputStringPc4++]='Z'; break;
                        }
                    }else if(event.code == KEY_MINUS){
                        if (lastCode4 == KEY_LEFTSHIFT || lastCode4 == KEY_RIGHTSHIFT){
                            InputString4[InputStringPc4++] = '_';
                        } else{
                            InputString4[InputStringPc4++]='-';
                        }
                    } else if (event.code == KEY_BACKSPACE){
                        if(InputStringPc4 > 0){
                            InputStringPc4--;
                        }
                    }

                    // ================= 以下 小键盘的按钮 =====================
                    switch(event.code){
                        case KEY_KP0:   // 0
                            InputString4[InputStringPc4++] = '0';
                            break;
                        case KEY_KP1:   // 1
                            InputString4[InputStringPc4++] = '1';
                            break;
                        case KEY_KP2:   // 2
                            InputString4[InputStringPc4++] = '2';
                            break;
                        case KEY_KP3:   // 3
                            InputString4[InputStringPc4++] = '3';
                            break;
                        case KEY_KP4:   // 4
                            InputString4[InputStringPc4++] = '4';
                            break;
                        case KEY_KP5:   // 5
                            InputString4[InputStringPc4++] = '5';
                            break;
                        case KEY_KP6:   // 6
                            InputString4[InputStringPc4++] = '6';
                            break;
                        case KEY_KP7:   // 7
                            InputString4[InputStringPc4++] = '7';
                            break;
                        case KEY_KP8:   // 8
                            InputString4[InputStringPc4++] = '8';
                            break;
                        case KEY_KP9:   // 9
                            InputString4[InputStringPc4++] = '9';
                            break;
                        case KEY_KPSLASH:       // /
                            InputString4[InputStringPc4++] = '/';
                            break;
                        case KEY_KPASTERISK:    // *
                            InputString4[InputStringPc4++] = '*';
                            break;
                        case KEY_KPMINUS:       // -
                            InputString4[InputStringPc4++] = '-';
                            break;
                        case KEY_KPPLUS:        // +
                            InputString4[InputStringPc4++] = '+';
                            break;
                        case KEY_KPDOT:         // .
                            InputString4[InputStringPc4++] = '.';
                            break;
                        case KEY_KPENTER:       // 回车
                            InputString4[InputStringPc4++] = 0;
                            InputFlag4=true;
                            break;
                    }
                    // =================== 以上 小键盘的按钮 ===========================


                    if(InputStringPc4>=128)  InputStringPc4=0;
                    lastCode4 = event.code;
                }
        }
    }
    //LOGE("pthread_read_keyboard exit\n");
    close(keys_fd);
    return 0;
}

extern "C" JNIEXPORT void JNICALL Java_android_1keyboard_1listen_KeyBoard_Start_1KeyBoard4(JNIEnv *env, jobject, jstring path)
{
    PathString4 = env->GetStringUTFChars(path, NULL);
    pthread_create(&MyPthread4,NULL,pthread_read_keyboard4,NULL);
}

extern "C" JNIEXPORT jstring JNICALL Java_android_1keyboard_1listen_KeyBoard_Get_1Input4(JNIEnv *env, jobject)
{
    if(InputFlag4==false)
    {
        const char* tmpstr = "null";
        jstring rtstr = env->NewStringUTF(tmpstr);
        return rtstr;
    }

    InputString4[InputStringPc4++]=0;
    jstring rtstr = env->NewStringUTF(InputString4);

    InputFlag4=false;
    InputStringPc4=0;
    return rtstr;
}