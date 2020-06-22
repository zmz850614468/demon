package android_serialport_api;

import android.content.Context;
import android.util.Log;

import com.lilanz.tooldemo.utils.NumberUtil;
import com.lilanz.tooldemo.utils.SerialPortUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class SerialPortUtils {
    private final String TAG = "SerialPortUtils";
    //    private String path = "/dev/ttyS4";
//    private int baudrate = 115200;
    public boolean serialPortStatus = false; //是否打开串口标志
    public String data_;
    public boolean threadStatus; //线程状态，为了安全终止线程

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;

    public static final int LENGTH_MSG_TYPE_6_BIT = 1;
    public static final int WEIGTH_MSG_TYPE_6_BIT = 2;
    /**
     * 6Byte的数据类型
     * 1：默认为米长数据类型
     * 2：为重量数据类型
     */
    public int msgTypeOf6Bit = LENGTH_MSG_TYPE_6_BIT;

    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort(Context context, String path, int baudrate) {
        try {
            serialPort = new SerialPort(new File(path), baudrate, 0);
            this.serialPortStatus = true;
            threadStatus = false; //线程状态

            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            new ReadThread().start(); //开始线程监控是否有数据要接收
        } catch (IOException e) {
            Log.e(TAG, "openSerialPort: ：" + e.toString());
//            ToastUtil.Toast(context,"打开串口异常"+ e.toString());
            return serialPort;
        }
//        ToastUtil.Toast(context,"打开串口");
        Log.d(TAG, "openSerialPort: 打开串口");
        return serialPort;
    }

    /**
     * 串口是否打开
     */
    public void serialPortStatu() {

    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        try {
            inputStream.close();
            outputStream.close();

            this.serialPortStatus = false;
            this.threadStatus = true; //线程状态
            serialPort.close();
        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常：" + e.toString());
            return;
        }
        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }

    /**
     * 发送串口指令（字符串）
     *
     * @param data String数据指令
     */
    public void sendSerialPort(byte[] data) {
        Log.d(TAG, "sendSerialPort: 发送数据");

        try {
            if (data.length > 0) {
                outputStream.write(data);
                outputStream.flush();
                Log.d(TAG, "sendSerialPort: 串口数据发送成功");
            }
        } catch (IOException e) {
            Log.e(TAG, "sendSerialPort: 串口数据发送失败：" + e.toString());
        }
    }

    /**
     * 单开一线程，来读数据
     */
    public class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            while (!threadStatus) {
                Log.d(TAG, "进入线程run");
                //64   1024
                byte[] buffer = new byte[10];
                int size; //读取数据的大小
                try {
                    size = inputStream.read(buffer);
                    Log.d(TAG, "run: " + SerialPortUtil.byte2hexStr(buffer));
                    // 判断是否是 米长数据，且刚好收到6个byte
                    if (msgTypeOf6Bit == LENGTH_MSG_TYPE_6_BIT && size == 6) {
                        parse6ByteLengthMsg(buffer);
                        continue;
                        // 判定桌：判断是否是重量数据，且刚好收到6个字节
                    }else if (msgTypeOf6Bit == WEIGTH_MSG_TYPE_6_BIT && size == 6) {
                        parse6ByteWeightMsg(buffer);
                        continue;
                    }
                    // 接收的是9个字节，表示的是重量测量的结果
                    // 01 03 04 00 08 00 00 7b f1
                    else if (size == 9) {
                        parse9ByteWeightMsg(buffer);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：" + e.toString());
                }
            }
        }

        // 解析9直接的重量数据
        private void parse9ByteWeightMsg(byte[] buffer) {
            // 获取高低位数据：文档定义的
            String low_1 = Integer.toHexString(buffer[3] & 0xff);
            String low_2 = Integer.toHexString(buffer[4] & 0xff);
            String hig_1 = Integer.toHexString(buffer[5] & 0xff);
            String hig_2 = Integer.toHexString(buffer[6] & 0xff);
            int num = NumberUtil.hex2Int(hig_1 + hig_2 + low_1 + low_2);

            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String result = decimalFormat.format(num / 1000.00f);//format 返回的是字符串
            if (onDataReceiveListener != null) {
                onDataReceiveListener.onDataReceive(Float.parseFloat(result));
            }
        }

        // 解析6字节的长度数据
        private void parse6ByteLengthMsg(byte[] buffer) {
            String msgType = SerialPortUtil.byte2hexStr(new byte[]{buffer[0], buffer[1], buffer[5]});
            String type = SerialPortUtil.byte2hexStr(new byte[]{buffer[2]});
            String str_2 = SerialPortUtil.byte2hexStr(new byte[]{buffer[3]});
            String str_3 = SerialPortUtil.byte2hexStr(new byte[]{buffer[4]});
            int num = NumberUtil.hex2Int(str_2 + str_3);
            if (onDataReceiveListener != null) {
                if ("aa550a".equals(msgType)) {
                    // 有布数据
                    if ("01".equals(type)) {
//                        onDataReceiveListener.onDataReceive(JsMethods.TYPE_HAS_CLOTH, num / 10.0f);
                        // 没布数据
                    } else if ("00".equals(type)) {
//                        onDataReceiveListener.onDataReceive(JsMethods.TYPE_HAS_NOT_CLOTH, num / 10.0f);
                        // 清零成功
                    } else if ("ff".equals(type)) {
                        onDataReceiveListener.showMsg("米数清零成功");
                    }
                }
            }
        }

        // 解析6字节的重量数据
        private void parse6ByteWeightMsg(byte[] buffer) {
            String msgType = SerialPortUtil.byte2hexStr(new byte[]{buffer[0]});
            String str_1 = SerialPortUtil.byte2hexStr(new byte[]{buffer[1]});
            // 是否是有效数据
            if ("ff".equals(msgType)) {
                // 小数的位数
                int dotCount = Integer.parseInt(Integer.toHexString(buffer[1] & 0x07));
                // 工作模式： 00(0)：计重模式；01(1)：计数模式；10(2)：
                String model = Integer.toHexString((buffer[1] & 0x18) / 0x08);
                // 重量正负 1：负  0：正
                String sign = Integer.toHexString((buffer[1] & 0x20) / 0x20);
                // 重量的稳定值： 1：稳定  0：不稳定
                String isValid = Integer.toHexString((buffer[1] & 0x40) / 0x40);
                // 重量是否溢出： 80：溢出  00：不溢出
                String isOverFlow = SerialPortUtil.byte2hexStr(new byte[]{(byte) (buffer[1] & 0x80)});

                // 不稳定或溢出，则丢弃数据 "0".equals(isValid) ||
                if ("80".equals(isOverFlow)) {
                    return;
                }
                String data = SerialPortUtil.byte2hexStr(new byte[]{buffer[4], buffer[3], buffer[2]});
                float weight = Float.parseFloat(data);
//                            float weight = StringUtil.hexStr2Int(data);
                // 是否为负数
                if ("1".equals(sign)) {
                    weight *= -1;
                }
                // 小数点位数
                for (int i = 1; i < dotCount; i++) {
                    weight /= 10;
                }
                if (onDataReceiveListener != null){
                    onDataReceiveListener.onDataReceive(weight);
                }
            }
        }
    }

    //这是写了一监听器来监听接收数据
    public OnDataReceiveListener onDataReceiveListener = null;

//    public static final int MSG_TYPE_REAL = 1;   // 实时消息传输
//    public static final int MSG_TYPE_END = 2;    // 结束消息传输

    public static interface OnDataReceiveListener {
        // num：回传的单位是(米)
        public void onDataReceive(int msgType, float num);

        // weight:返回的是重量（0.00kg）
        public void onDataReceive(float weight);

        // 显示提示信息
        public void showMsg(String msg);
    }

    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public void setMsgTypeOf6Bit(int type) {
        msgTypeOf6Bit = type;
    }

}
