package com.lilanz.wificonnect.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.adapters.ItemBeanAdapter;
import com.lilanz.wificonnect.beans.ItemBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.threads.WifiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayVoiceActivity extends Activity {

    private WifiService wifiService;

    @BindView(R.id.rv_voice)
    protected RecyclerView recycler;
    private ItemBeanAdapter adapter;

    private List<ItemBean> beanList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_voice);
        ButterKnife.bind(this);

        initData();
        initAdapter();
        initService();
    }

    private void initAdapter() {
        adapter = new ItemBeanAdapter(this, beanList);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new ItemBeanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemBean bean) {
                showToast(bean.name);
                wifiService.sendMsg(new MsgBean(3, bean.voiceId + "").toString());
            }
        });
    }

    private void initData() {
        beanList = new ArrayList<>();
        beanList.add(new ItemBean(R.mipmap.call_me, "联系我", R.raw.connect_me));
        beanList.add(new ItemBean(R.mipmap.eat_what, "想吃啥", R.raw.want_to_eat));
        beanList.add(new ItemBean(R.mipmap.get_up, "起床了", R.raw.pig_get_up));
        beanList.add(new ItemBean(R.mipmap.at_home, "到家了吗", R.raw.at_home));
        beanList.add(new ItemBean(R.mipmap.going_home, "回家路上", R.raw.going_home));
        beanList.add(new ItemBean(R.mipmap.need_comfort, "要安慰", R.raw.need_comfort));
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
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            wifiService = null;
        }
    };

    @Override
    protected void onDestroy() {
        if (wifiService != null) {
            unbindService(connection);
            wifiService = null;
        }
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
