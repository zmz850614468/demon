package com.lilanz.wificonnect.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.adapters.FileAdapter;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.beans.SongBean;
import com.lilanz.wificonnect.controls.SongControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;
import com.lilanz.wificonnect.threads.WifiService;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileActivity extends Activity {

    @BindView(R.id.rv_song)
    protected RecyclerView recycler;
    private FileAdapter songAdapter;
    private List<SongBean> songList;

    private boolean isSellecAll;
    private WifiService wifiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);

        initService();
        initData();
        initAdapter();
    }

    @OnClick({R.id.tv_select, R.id.bt_begin})
    public void onSelectAllClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:
                isSellecAll = !isSellecAll;
                for (SongBean songBean : songList) {
                    songBean.isSelect = isSellecAll;
                }
                songAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_begin:
                sendFile();
                break;
        }
    }

    /**
     * 发送文件信息
     */
    private void sendFile() {
        List<MsgBean> msgList = new ArrayList<>();
        try {
            int arrSize = 1024 * 1024;  // 1M
            for (SongBean songBean : songList) {
                if (!songBean.isServiceExit && songBean.isSelect) {
                    FileInputStream inputStream = new FileInputStream(songBean.path);

                    int count = 0;
                    byte[] bytes = new byte[arrSize];
                    while (true) {
                        inputStream.read(bytes);
                        if (count <= 0) {
                            break;
                        }
                        MsgBean msgBean = new MsgBean(MsgBean.FILE_EXCHANGE, songBean.path);
                        msgBean.bytes = new byte[count];
                        System.arraycopy(bytes, 0, msgBean.bytes, 0, count);
                        msgList.add(msgBean);
                    }

                    inputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 添加文件序号和最后一个标记
        int size = msgList.size();
        for (int i = 0; i < size; i++) {
            msgList.get(i).orderId = i;
            if (i == size - 1) {
                msgList.get(i).isLast = true;
            }
        }

        if (wifiService != null) {
            wifiService.sendMsg(msgList);
        }

    }

    private void initAdapter() {
        songAdapter = new FileAdapter(this, songList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(songAdapter);

    }

    private void initData() {
        List<SongBean> list = DBControl.quaryAll(this, SongBean.class);
        songList = SongControl.getSongList(this);

        for (SongBean songBean : songList) {
            for (SongBean bean : list) {
                if (songBean.path.equals(bean.path)) {
                    songBean.isServiceExit = true;
                    break;
                }
            }
        }
    }

    private void initService() {
        Intent intent = new Intent(this, WifiService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wifiService = ((WifiService.WifiBind) service).getService();
            wifiService.addMsgCallBackListener(callbackListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            wifiService = null;
        }
    };

    /**
     * 消息回调监听对象
     */
    private MsgCallbackListener callbackListener = new MsgCallbackListener() {

    };

    @Override
    protected void onDestroy() {
        if (wifiService != null) {
            wifiService.removeCallbackListener(callbackListener);
            unbindService(connection);
            wifiService = null;
        }
        super.onDestroy();
    }
}
