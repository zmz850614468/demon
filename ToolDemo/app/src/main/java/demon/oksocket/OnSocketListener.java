package demon.oksocket;

/**
 * OkSock 监听对象
 */
public abstract class OnSocketListener {

    protected abstract void onMsgCallback(String ip, String msg);

    protected void onConnected(String ip) {
    }

    protected void onDisconnected(String ip) {
    }
}