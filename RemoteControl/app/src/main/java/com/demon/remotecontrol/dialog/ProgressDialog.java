package com.demon.remotecontrol.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.bean.ProgressBean;
import com.demon.remotecontrol.fileclone.FileWriteControl;
import com.demon.remotecontrol.socketcontrol.SocketMsgControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgressDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.rv_ble_device)
    protected RecyclerView recyclerView;
    private ProgressAdapter progressAdapter;

    private Context context;
    private List<ProgressBean> progressList;

    private Timer updateTimer;

    public ProgressDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initUI();
    }

    @Override
    public void show() {
        super.show();
        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressList.clear();
                progressList.addAll(FileWriteControl.getInstance(context).getProgressList());
                progressList.addAll(SocketMsgControl.getInstance(context).getProgressList());
                Collections.sort(progressList, (o1, o2) -> (int) (o2.createTime - o1.createTime));
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() -> progressAdapter.notifyDataSetChanged());
                }
            }
        }, 0, 200);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initUI() {
        progressList = new ArrayList<>();
        progressAdapter = new ProgressAdapter(context, progressList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(progressAdapter);
    }

    //  ====================== 时间监听 ===========================


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
