package demon.websocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * webSocket 客户端
 */
public class WSocketClient extends WebSocketClient {

    public WSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        showLog("onOpen: 打开webSocket");
    }

    @Override
    public void onMessage(String message) {
        showLog("onMessage: 接收webSocket信息：" + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        showLog("onClose: 关闭webSocket");
    }

    @Override
    public void onError(Exception ex) {
        showLog("onError: " + ex.getMessage());
        ex.printStackTrace();
    }

    private void showLog(String msg) {
        Log.e("webSocket", msg);
    }

    @Override
    public void close() {
        super.close();
    }


}
