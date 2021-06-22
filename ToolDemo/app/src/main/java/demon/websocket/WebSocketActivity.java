package demon.websocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lilanz.tooldemo.App;
import com.lilanz.tooldemo.R;

import java.io.IOException;
import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebSocketActivity extends AppCompatActivity {

    private static final int DELAY_TIME = 10000;

    private WSocketClient wSocketClient;
    private static final String url = "ws://192.168.1.118:8899";

    private WSocketServer wSocketServer;
    private static final int port = 8899;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        ButterKnife.bind(this);

//        initWebSocket();
        wSocketServer = new WSocketServer(port);
    }

    @OnClick({R.id.bt_open, R.id.bt_close, R.id.bt_send, R.id.bt_open_server, R.id.bt_close_server})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_open_server:
                wSocketServer.start();
                break;
            case R.id.bt_close_server:
                try {
                    wSocketServer.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.bt_open:
                initWebSocket();
//                wSocketServer.start();
//                if (wSocketClient == null) {
//                    wSocketClient = new WSocketClient(URI.create(url));
//                    wSocketClient.connect();
//
//                    heartBeatHandler.sendEmptyMessageDelayed(1, DELAY_TIME);
//                }
                break;
            case R.id.bt_close:
                if (wSocketClient != null) {
                    wSocketClient.close();
                    wSocketClient = null;
                }
                heartBeatHandler.removeMessages(1);
//                try {
//                    wSocketServer.stop();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.bt_send:
                if (wSocketClient != null && wSocketClient.isOpen()) {
                    wSocketClient.send("哈哈哈，webSocket");
                }
                break;
        }
    }

    private void initWebSocket() {
        if (wSocketClient == null) {
            wSocketClient = new WSocketClient(URI.create(url));
            wSocketClient.connect();

            heartBeatHandler.sendEmptyMessageDelayed(1, DELAY_TIME);
        }
    }

    @Override
    protected void onDestroy() {
        if (wSocketClient != null) {
            wSocketClient.close();
            wSocketClient = null;
        }

        heartBeatHandler.removeMessages(1);
        super.onDestroy();
    }

    private int count = 0;
    private Handler heartBeatHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showLog("发送心跳包:" + ++count);
                if (wSocketClient != null) {
                    if (wSocketClient.isClosed()) {
                        wSocketClient.reconnect();
                    }
                } else {
                    //如果client已为空，重新初始化websocket
                    initWebSocket();
                    return;
                }
                //定时对长连接进行心跳检测
                heartBeatHandler.sendEmptyMessageDelayed(1, DELAY_TIME);
            }
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //重连
                    wSocketClient.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showLog(String msg) {
        Log.e("webSocketActivity", msg);
    }
}
