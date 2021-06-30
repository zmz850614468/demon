package demon.websocket;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WSocketServer extends WebSocketServer {

    public WSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        showLog("onOpen:" + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        showLog("onClose:" + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        showLog("onMessage:" + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        showLog("onError:" + ex.getMessage());
    }

    @Override
    public void onStart() {
        showLog("onStart");
    }

    private void showLog(String msg) {
        Log.e("WSocketServer", msg);
    }

}
