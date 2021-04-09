package com.demon.agv.oksocket;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
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
 * 单例模式：OkSocket的服务端
 */
public class ServerOkSocket {

    // 服务端口
    public static final int SERVER_PORT = 8899;

    private static ServerOkSocket instance;

    public static ServerOkSocket getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new ServerOkSocket(context);
                    instance.beginServerSocket();
                }
            }
        }
        return instance;
    }

    private Context context;
    private IRegister iRegister;
    private Map<String, IClient> clientMap;     // 保存所有连接的客户端

    private IServerManager serverManager;

    private ServerOkSocket(Context context) {
        this.context = context;
        clientMap = new HashMap<>();
    }

    public void beginServerSocket() {
        if (iRegister == null) {
            iRegister = OkSocket.server(SERVER_PORT);
            serverManager = (IServerManager) iRegister.registerReceiver(serverActionListener);
            serverManager.listen();
        }
    }

    public void endServerSocket() {
        if (iRegister != null) {
            clientMap.clear();
//            beginServerSocket();    // 错误关闭socket服务器
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
        if (clientMap.containsKey(ip)) {
            clientMap.get(ip).send(new OkSocketBean(msg.getBytes()));
        }
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
                        client.send(new OkSocketBean("received".getBytes()));
                        for (OnSocketListener listener : listenerList) {
                            listener.onMsgCallback(client.getHostIp(), new String(originalData.getBodyBytes()));
                        }
                    }

                    @Override
                    public void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool) {
                        showLog("write:" + new String(((OkSocketBean) sendable).getBytes()));
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
            for (OnSocketListener listener : listenerList) {
                listener.onDisconnected(client.getHostIp());
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
        Log.e("serverOkSocket", msg);
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
