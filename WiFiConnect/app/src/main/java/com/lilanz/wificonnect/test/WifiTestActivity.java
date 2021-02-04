package com.lilanz.wificonnect.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.wificonnect.API.APIRequest;
import com.lilanz.wificonnect.API.ParseListener;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.App;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiTestActivity extends Activity {

    private String REQUEST_PATH = "http://192.168.4.1/";

    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.et_ip)
    EditText etIp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_test);
        ButterKnife.bind(this);

        openSocket();
//        sendSocketMsg("query");
    }

    @OnClick({R.id.bt_setting_ip, R.id.bt_query_ip, R.id.bt_open, R.id.bt_close,
            R.id.bt_query_status, R.id.bt_socket_open, R.id.bt_socket_close,
            R.id.bt_socket_query_status})
    public void onClicked(View v) {
        String ip = etIp.getText().toString();
        if (ip.equals("")) {
            showToast("ip不能为空!");
            return;
        }
        REQUEST_PATH = "http://" + ip + "/";
        switch (v.getId()) {
            case R.id.bt_setting_ip:
                wifiSetting("TP-LINK_Giao_9104", "87654321");
                break;
            case R.id.bt_query_ip:
                getWifiIp();
                break;
            case R.id.bt_open:
                operate("open");
                break;
            case R.id.bt_close:
                operate("close");
                break;
            case R.id.bt_query_status:
                operate("query");
                break;
            case R.id.tv_msg:
                tvMsg.setText("");
                break;


            case R.id.bt_socket_open:
                sendSocketMsg("open\r");
                break;
            case R.id.bt_socket_close:
                sendSocketMsg("close\r");
                break;
            case R.id.bt_socket_query_status:
                sendSocketMsg("query\r");
                break;
        }
    }

    /**
     * @param wifiName
     * @param wifiPwd
     */
    private void wifiSetting(String wifiName, String wifiPwd) {
        APIRequest<String> ipSettingRequest = new APIRequest<>(String.class);
        ipSettingRequest.setRequestBasePath(REQUEST_PATH);
        ipSettingRequest.setParseListener(new ParseListener<String>() {
            @Override
            public void onError(int errCode, String errMsg) {
                showMsg(errMsg + " ; " + errMsg);
            }

            @Override
            public void jsonResult(String jsonStr) {
                super.jsonResult(jsonStr);
                showMsg(jsonStr);
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("type", "setting");
        map.put("wifiName", wifiName);
        map.put("wifiPwd", wifiPwd);
        ipSettingRequest.requestNormal(map, "wifiSettingTest", APIRequest.PARSE_TYPE_JSON);
    }

    private void getWifiIp() {
        APIRequest<String> getIpRequest = new APIRequest<>(String.class);
        getIpRequest.setRequestBasePath(REQUEST_PATH);
        getIpRequest.setParseListener(new ParseListener<String>() {
            @Override
            public void onError(int errCode, String errMsg) {
                showMsg(errMsg + " ; " + errMsg);
            }

            @Override
            public void jsonResult(String jsonStr) {
                super.jsonResult(jsonStr);
                showMsg(jsonStr);
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("type", "ip");
        getIpRequest.requestNormal(map, "wifiSettingTest", APIRequest.PARSE_TYPE_JSON);
    }

    private void operate(String status) {
        APIRequest<String> operateOperate = new APIRequest<>(String.class);
        operateOperate.setRequestBasePath(REQUEST_PATH);
        operateOperate.setParseListener(new ParseListener<String>() {
            @Override
            public void onError(int errCode, String errMsg) {
                showMsg(errMsg + " ; " + errMsg);
            }

            @Override
            public void jsonResult(String jsonStr) {
                super.jsonResult(jsonStr);
                showMsg(jsonStr);
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("type", "operate");
        map.put("status", status);
        operateOperate.requestNormal(map, "wifiSettingTest", APIRequest.PARSE_TYPE_JSON);
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private IConnectionManager connectionManager;

    private void openSocket() {
        // 简单长连接
//        ConnectionInfo info = new ConnectionInfo("192.168.1.118", 81);
//        OkSocket.open(info).connect();

        // 有回调的长连接
        ConnectionInfo info = new ConnectionInfo("192.168.1.118", 81);
        connectionManager = OkSocket.open(info);
        connectionManager.registerReceiver(new SocketActionAdapter() {

            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                super.onSocketConnectionSuccess(info, action);
                showLog("onSocketConnectionSuccess : " + action);
            }

            @Override
            public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
                super.onSocketDisconnection(info, action, e);
                showLog("onSocketDisconnection : " + action);
//                if (connectionManager != null) {
//                    connectionManager.disconnect();
//                }
            }

            @Override
            public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
                super.onSocketConnectionFailed(info, action, e);
                showLog("onSocketConnectionFailed : " + action);
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(info, action, data);
                showLog("onSocketReadResponse : " + action + "; data : " + new String(data.getBodyBytes()));
            }
        });
        connectionManager.connect();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("okSocket", msg);
        }
    }

    private void sendSocketMsg(String msg) {
        if (connectionManager != null) {
            TestBean testBean = new TestBean(msg);
            connectionManager.send(testBean);
        }

//        bb.putInt(body.length);
    }
}
