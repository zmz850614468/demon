package com.example.timeup.page;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timeup.R;
import com.example.timeup.SettingActivity;
import com.example.timeup.adapters.ExecuteAdapter;
import com.example.timeup.beans.TypeBean;
import com.example.timeup.controls.MediaControl;
import com.example.timeup.services.TimingService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentTwo extends Fragment {

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    private ExecuteAdapter executeAdapter;
    private List<TypeBean> typeBeanList;
    private static Timer timer;

    private TimingService timingService;
    public static boolean isDestroy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_2, container, false);
        ButterKnife.bind(this, chatView);
        initAdapter();
        initService();
        isDestroy = false;
        return chatView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initService() {
        Intent intent = new Intent(getActivity(), TimingService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timingService = ((TimingService.TimingBind) service).getService();
            timingService.setListener(new TimingService.OnUpdateUIListener() {
                @Override
                public void onUpdate() {
                    if (!isDestroy && getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                executeAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timingService = null;
        }
    };

    private void initAdapter() {
        typeBeanList = SlipPageActivity.typeBeanList;

        executeAdapter = new ExecuteAdapter(getActivity(), typeBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvType.setLayoutManager(manager);
        rvType.setAdapter(executeAdapter);

        executeAdapter.setListener(new ExecuteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TypeBean bean) {
                if (bean.during <= 0) {
                    SlipPageActivity.removerTimer(bean);
                    executeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onStartClick(TypeBean bean) {
            }

            @Override
            public void onDeleteClick(final TypeBean bean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("注意")
                        .setMessage("确定删除计时任务：" + bean.name)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SlipPageActivity.removerTimer(bean);
                                executeAdapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (timingService != null) {
            Intent intent = new Intent(getActivity(), TimingService.class);
            getActivity().unbindService(connection);
            timingService = null;
        }
        isDestroy = true;
        showLog("fragmentTwo sonDestroyView");
        super.onDestroyView();
    }

    private void showLog(String msg) {
        if (!App.isDebug) {
            Log.e("timeup", msg);
        }
    }
}
