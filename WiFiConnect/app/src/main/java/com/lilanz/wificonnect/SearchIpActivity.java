package com.lilanz.wificonnect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.threads.UDPThread;
import com.lilanz.wificonnect.utils.PingUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class SearchIpActivity extends Activity implements View.OnClickListener {

    private Button btSearchIps;
    private TextView tvMsg;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serarch_ip);

        initUI();
        verifyStoragePermissions(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search_ip:

//                discover();
                read();
                break;
        }
    }

    private void discover() {
        String newip = "";
        String ipseg = "192.168.1.";
        for (int i = 2; i < 255; i++) {
            newip = ipseg + String.valueOf(i);
            Thread ut = new UDPThread(newip);
            ut.start();
        }
    }

    private void read() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    PingUtil.sendUdpMsg();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readArp();
            }
        };
        thread.start();
    }

    private void readArp() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("/proc/net/arp"));
            String line = "";
//            String ip = "";
//            String flag = "";
//            String mac = "";

            while ((line = br.readLine()) != null) {
                try {
                    line = line.trim();
                    final String l = line;
                    if (line.length() < 63) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMsg(l);
                            }
                        });
                        continue;
                    }
                    if (line.toUpperCase(Locale.US).contains("IP")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMsg(l);
                            }
                        });
                        continue;
                    }
                    final String ip = line.substring(0, 17).trim();
                    final String flag = line.substring(29, 32).trim();
                    final String mac = line.substring(41, 63).trim();
                    if (mac.contains("00:00:00:00:00:00")) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showMsg(l);
//                            }
//                        });
                        continue;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMsg(l);
                        }
                    });
                    Log.e("scanner", "readArp: mac= " + mac + " ; ip= " + ip + " ;flag= " + flag);


                } catch (Exception e) {
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        btSearchIps = findViewById(R.id.bt_search_ip);
        btSearchIps.setOnClickListener(this);
        tvMsg = findViewById(R.id.tv_msg);
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }
}
