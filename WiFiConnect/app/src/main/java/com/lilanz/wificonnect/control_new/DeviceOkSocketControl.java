package com.lilanz.wificonnect.control_new;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.bean_new.Esp8266IOBean;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用OkSocket控制设备
 */
public class DeviceOkSocketControl {

    private static DeviceOkSocketControl instance;

    public static DeviceOkSocketControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new DeviceOkSocketControl(context);
                }
            }
        }
        return instance;
    }

    private Context context;
    private Map<String, Esp8266ControlBean> esp8266ControlMap;
    private Map<String, IConnectionManager> connectionManagerMap;

    public DeviceOkSocketControl(Context context) {
        this.context = context;
        connectionManagerMap = new HashMap<>();
        esp8266ControlMap = new HashMap<>();
    }

    public void sendControlMsg(Esp8266ControlBean controlBean) {
        IConnectionManager connectionManager;
        if (connectionManagerMap.containsKey(controlBean.ip)) {
            connectionManager = connectionManagerMap.get(controlBean.ip);
            connectionManager.send(new Esp8266IOBean(controlBean.control, controlBean.controlBytes));
        } else {
            esp8266ControlMap.put(controlBean.ip, controlBean);
            connectionManager = OkSocket.open(controlBean.ip, controlBean.port);
            connectionManager.registerReceiver(socketActionAdapter);
            connectionManager.connect();
            connectionManagerMap.put(controlBean.ip, connectionManager);
        }
    }

    private SocketActionAdapter socketActionAdapter = new SocketActionAdapter() {
        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            super.onSocketConnectionSuccess(info, action);
            if (esp8266ControlMap.containsKey(info.getIp())) {
                Esp8266ControlBean controlBean = esp8266ControlMap.remove(info.getIp());
                connectionManagerMap.get(controlBean.ip).send(new Esp8266IOBean(controlBean.control, controlBean.controlBytes));
            }
            showLog(info.getIp() + ":连接成功");
        }

        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            super.onSocketDisconnection(info, action, e);
            IConnectionManager connectionManager = connectionManagerMap.remove(info.getIp());
            if (connectionManager != null && !connectionManager.isDisconnecting()) {
                connectionManager.disconnect();
            }
            showLog(info.getIp() + ":断开连接");
            for (OnSocketListener onStatusListener : listenerList) {
                onStatusListener.onDisconnect(info.getIp());
            }
        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
            super.onSocketConnectionFailed(info, action, e);
            showLog(info.getIp() + ":连接失败");
            if (connectionManagerMap.containsKey(info.getIp())) {
                connectionManagerMap.get(info.getIp()).disconnect();
            }
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(info, action, data);
            showLog(info.getIp() + ":读成功 : " + new String(data.getBodyBytes()));
            for (OnSocketListener onStatusListener : listenerList) {
                onStatusListener.onMsgCallback(info.getIp(), new String(data.getBodyBytes()));
            }
        }

        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(info, action, data);
            showLog(info.getIp() + ":写成功 : " + ((Esp8266IOBean) data).getMsg());
        }
    };

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("okSocket", msg);
        }
    }

    private List<OnSocketListener> listenerList = new ArrayList<>();

    public void addListener(OnSocketListener listener) {
        listenerList.add(listener);
    }

    public void removeListener(OnSocketListener listener) {
        listenerList.remove(listener);
    }

    public interface OnSocketListener {
        void onMsgCallback(String ip, String msg);

        void onDisconnect(String ip);
    }
}
