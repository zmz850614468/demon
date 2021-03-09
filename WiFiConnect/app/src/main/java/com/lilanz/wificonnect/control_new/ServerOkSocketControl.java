package com.lilanz.wificonnect.control_new;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.bean_new.Esp8266IOBean;
import com.lilanz.wificonnect.bean_new.OkSocketBean;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.dispatcher.IRegister;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerActionListener;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerShutdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络间，端对端的socket通信
 */
public class ServerOkSocketControl {

    private static ServerOkSocketControl instance;

    public static ServerOkSocketControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new ServerOkSocketControl(context);
                }
            }
        }
        return instance;
    }

    private Context context;
    private IRegister iRegister;
    private Map<String, IClient> clientMap;

    private ServerOkSocketControl(Context context) {
        this.context = context;
        clientMap = new HashMap<>();
    }

    public void beginServerSocket(int port) {
        if (iRegister == null) {
            iRegister = OkSocket.server(port);
            IServerManager serverManager = (IServerManager) iRegister.registerReceiver(serverActionListener);
            serverManager.listen();
        }
    }

    public void endServerSocket() {
        if (iRegister != null) {
            iRegister.unRegisterReceiver(serverActionListener);
            iRegister = null;
        }
    }

    /**
     * 发送数据
     *
     * @param ip
     * @param msg
     */
    public void sendMsg(String ip, String msg) {
    }

    IServerActionListener serverActionListener = new IServerActionListener() {
        @Override
        public void onServerListening(int serverPort) {
            showLog("打开服务端口：" + serverPort);
        }

        @Override
        public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
            showLog("客服端接入：" + client.getHostIp());
            if (!clientMap.containsKey(client.getHostIp())) {
                clientMap.put(client.getHostIp(), client);
                client.addIOCallback(new IClientIOCallback() {
                    @Override
                    public void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
                        showLog("read:" + new String(originalData.getBodyBytes()));
//                        client.send(new OkSocketBean("received"));
                    }

                    @Override
                    public void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool) {
                        showLog("write:" + ((OkSocketBean) sendable).getMsg());
                    }
                });
            }
        }

        @Override
        public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
            showLog("客服端断开连接：" + client.getHostIp());
            if (clientMap.containsKey(client.getHostIp())) {
                clientMap.remove(client.getHostIp());
                client.removeAllIOCallback();
            }
        }

        @Override
        public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
            showLog("关闭服务器端口：" + serverPort);
            for (Map.Entry<String, IClient> entry : clientMap.entrySet()) {
                entry.getValue().removeAllIOCallback();
            }
            clientMap.clear();
        }

        @Override
        public void onServerAlreadyShutdown(int serverPort) {
            showLog("服务器端口已关闭：" + serverPort);
        }
    };


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("serverOkSocket", msg);
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
