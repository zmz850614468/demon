package com.lilanz.wificonnect.activitys;

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
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.lilanz.wificonnect.utils.WiFiUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * wifi设备IP地址设置
 */
public class WifiSettingActivity extends Activity {

    @BindView(R.id.et_wifi_name)
    EditText etWifiName;
    @BindView(R.id.et_wifi_pwd)
    EditText etWifiPwd;
    @BindView(R.id.et_host_ip)
    EditText etHostIP;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_device_ip)
    TextView tvDeviceIp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_setting);
        ButterKnife.bind(this);

//        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefreshClicked(null);
    }

    private APIRequest<String> setIpRequest;

    @OnClick({R.id.bt_setting_ip, R.id.bt_get_ip, R.id.bt_restart_wifi})
    public void onIpSettingClicked(View v) {
        String ip = etWifiName.getText().toString();
        String port = etWifiPwd.getText().toString();

//        if (setIpRequest == null) {
        setIpRequest = new APIRequest<>(String.class);
        setIpRequest.setParseListener(new ParseListener<String>() {
            @Override
            public void onError(int errCode, String errMsg) {
//                tvDeviceIp.setText("路由器分配的设备ip：未获取");
                showToast("errcode:" + errMsg + " ; errmsg:" + errMsg);
            }

            @Override
            public void jsonResult(String jsonStr) {
                super.jsonResult(jsonStr);
//                showMsg(jsonStr);
                tvDeviceIp.setText("路由器分配的设备ip：" + jsonStr);
            }
        });
//        }

        Map<String, Object> map = new HashMap<>();
        String hostIP = etHostIP.getText().toString();
        switch (v.getId()) {
            case R.id.bt_setting_ip:
                setIpRequest.setRequestBasePath("http://" + hostIP + "/ipSetting/");
                map.put("wifiname", ip);
                map.put("wifipwd", port);
                break;
            case R.id.bt_get_ip:
                setIpRequest.setRequestBasePath("http://" + hostIP + "/ipInfo/");
                break;
            case R.id.bt_restart_wifi:
                setIpRequest.setRequestBasePath("http://" + hostIP + "/restart/");
                break;
        }

        setIpRequest.requestNormal(map, "wifiSetting", APIRequest.PARSE_TYPE_JSON);
    }

    @OnClick(R.id.iv_refresh)
    public void onRefreshClicked(View v) {
        tvDeviceIp.setText("路由器分配的设备ip：");
        initUI();
    }

    private void initUI() {
        String localIp = WiFiUtil.getIpAddr(this);
        tvAddress.setText("ip：" + localIp);
        int last = localIp.lastIndexOf(".");
        etHostIP.setText(localIp.substring(0, last + 1) + "1");

        if ("0.0.0.0".equals(localIp)) {
            showToast("请先连接设备wifi热点！");
        }

        String wifiName = SharePreferencesUtil.getLuYQName(this);
        String wifiPwd = SharePreferencesUtil.getLuYQPwd(this);
        etWifiName.setText(wifiName);
        etWifiPwd.setText(wifiPwd);
    }

    @Override
    protected void onDestroy() {
        String wifiName = etWifiName.getText().toString();
        String wifiPwd = etWifiPwd.getText().toString();
        SharePreferencesUtil.saveLuYQName(this, wifiName);
        SharePreferencesUtil.saveLuYQPwd(this, wifiPwd);

        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("ipSetting", msg);
        }
    }

//    @OnClick(R.id.tv_address)
//    public void onClicked(View v) {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    byte[] bytes = "udp msg".getBytes();
//                    DatagramPacket dp = new DatagramPacket(bytes, 0, bytes.length);
//                    DatagramSocket socket = new DatagramSocket();
//                    dp.setAddress(InetAddress.getByName("192.168.4.1"));
//                    dp.setPort(8080);
//                    socket.send(dp);
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
}
