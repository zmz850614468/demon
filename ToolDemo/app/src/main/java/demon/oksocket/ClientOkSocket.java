package demon.oksocket;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.lilanz.tooldemo.utils.SerialPortUtil;
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
 * 单例模式：OkSocket客户端
 */
public class ClientOkSocket {

    private static ClientOkSocket instance;

    public static ClientOkSocket getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new ClientOkSocket(context);
                }
            }
        }
        return instance;
    }

    private Context context;
    //    private Map<String, byte[]> msgMap;
    private Map<String, List<byte[]>> msgListMap;  // 保存所有未发送的信息
    private Map<String, IConnectionManager> connectionManagerMap;

    private ClientOkSocket(Context context) {
        this.context = context;
        connectionManagerMap = new HashMap<>();
//        msgMap = new HashMap<>();
        msgListMap = new HashMap<>();
    }

    private Handler msgHandle = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                String ip = (String) msg.obj;
                int port = msg.arg1;
                sendMsg(ip, port, "".getBytes());
            }
            return true;
        }
    });

    public void sendMsg(String ip, int port, byte[] msg) {
        IConnectionManager connectionManager;

        // 1.判断连接是否有效
        if (connectionManagerMap.containsKey(ip)) {
            connectionManager = connectionManagerMap.get(ip);
            if (connectionManager == null || connectionManager.isDisconnecting()) {
                connectionManagerMap.remove(ip);
            }
        }

        // 2.存在连接，则发送信息
        if (connectionManagerMap.containsKey(ip)) {
            connectionManager = connectionManagerMap.get(ip);
            connectionManager.send(new OkSocketBean(msg));

            // 3.不存在连接，则先发起连接
        } else {
            if (!msgListMap.containsKey(ip)) {
                List<byte[]> list = new ArrayList<>();
                msgListMap.put(ip, list);
            }
            msgListMap.get(ip).add(msg);
//            msgMap.put(ip, msg);
            connectionManager = OkSocket.open(ip, port);
            connectionManager.registerReceiver(socketActionAdapter);

            connectionManager.connect();
            connectionManagerMap.put(ip, connectionManager);
        }
    }

    private SocketActionAdapter socketActionAdapter = new SocketActionAdapter() {
        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            super.onSocketConnectionSuccess(info, action);
            if (msgListMap.containsKey(info.getIp())) {
                List<byte[]> list = msgListMap.get(info.getIp());
                for (byte[] bytes : list) {
                    connectionManagerMap.get(info.getIp()).send(new OkSocketBean(bytes));
                }
                list.clear();
//                byte[] msg = msgMap.remove(info.getIp());
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

            Message msg = new Message();
            msg.what = 1;
            msg.obj = info.getIp();
            msg.arg1 = info.getPort();
            msgHandle.sendMessageDelayed(msg, 5000);
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
            showLog(info.getIp() + ":读成功 : " + SerialPortUtil.byte2hexStr(data.getBodyBytes()));
            for (OnSocketListener onStatusListener : listenerList) {
                onStatusListener.onMsgCallback(info.getIp(), new String(data.getBodyBytes()));
            }
        }

        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(info, action, data);
            showLog(info.getIp() + ":写成功 : " + new String(((OkSocketBean) data).getBytes()));
        }
    };

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("clientOkSocket", msg);
    }

    /**
     * 服务端消息监听：可注册多个监听事件
     */

    private List<OnSocketListener> listenerList = new ArrayList<>();

    public void addListener(OnSocketListener listener) {
        listenerList.add(listener);
    }

    public void removeListener(OnSocketListener listener) {
        listenerList.remove(listener);
    }
}
