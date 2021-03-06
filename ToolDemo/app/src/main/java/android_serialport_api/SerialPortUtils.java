package android_serialport_api;

import android.content.Context;
import android.util.Log;

import com.lilanz.tooldemo.utils.SerialPortUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortUtils {
    private final String TAG = "SerialPortUtils";
    public boolean serialPortStatus = false; //是否打开串口标志
    public boolean threadStatus; // 线程状态，为了安全终止线程

//    public static final String PORT_PATH_485_4 = "/dev/ttyS4";
//    public static final int PORT_BAUDRATE_485_4 = 115200;
    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;

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
            return serialPort;
        }
        Log.d(TAG, "openSerialPort: 打开串口");
        return serialPort;
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
                byte[] buffer = new byte[100];
                int size; //读取数据的大小
                try {
                    size = inputStream.read(buffer);
                    Log.d(TAG, "run: " + SerialPortUtil.byte2hexStr(buffer));

                    // 返回接收到的数据
                    if (onDataReceiveListener != null) {
                        onDataReceiveListener.onDataReceive(SerialPortUtil.byte2hexStr(buffer));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "run: 数据读取异常：" + e.toString());
                }
            }
        }
    }

    //这是写了一监听器来监听接收数据
    public OnDataReceiveListener onDataReceiveListener = null;

    public static interface OnDataReceiveListener {

        public void onDataReceive(String msg);

        // 显示提示信息
        public void showMsg(String msg);
    }

    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

}
