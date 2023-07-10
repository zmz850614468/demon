package com.demon.fit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demon.fit.R;
import com.demon.fit.adapter.ItemAdapter;
import com.demon.fit.bean.ItemBean;
import com.demon.fit.control.SoundControl;
import com.demon.fit.data_transfer.activity.DataTransferActivity;
import com.demon.fit.dialog.TipSelectDialog;
import com.demon.fit.service.TimerService;
import com.demon.fit.util.NotificationUtil;
import com.demon.fit.util.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_time)
    public TextView tvTime;
    @BindView(R.id.rv_item)
    protected RecyclerView recycler;
    private ItemAdapter adapter;
    private List<ItemBean> list;

    private TipSelectDialog tipSelectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SoundControl.getInstance(this).initData();
        initAdapter();
        initDialog();

//        Intent intent = new Intent(MainActivity.this, OperateActivity.class);
//        startActivity(intent);
    }

    private void initAdapter() {
        list = new ArrayList<>();
        list.add(new ItemBean(R.drawable.fl, "复利"));
        list.add(new ItemBean(R.drawable.jd, "素质需求"));
        list.add(new ItemBean(R.drawable.zxl, "操作类型"));
//        list.add(new ItemBean(R.drawable.zxl, "执行力"));
//        list.add(new ItemBean(R.drawable.fy, "交易费用"));
//        list.add(new ItemBean(R.drawable.mb, "目标"));
//        list.add(new ItemBean(R.drawable.jg, "旧-执行结果"));
        list.add(new ItemBean(R.drawable.jg, "操作结果分析"));
        list.add(new ItemBean(R.drawable.jg, "执行结果"));
//        list.add(new ItemBean(R.drawable.ic_launcher_foreground, "测试"));

        adapter = new ItemAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(bean -> {
            switch (bean.name) {
                case "目标":
                    Intent intent = new Intent(MainActivity.this, TargetActivity.class);
                    startActivity(intent);
                    break;
                case "复利":
                    intent = new Intent(MainActivity.this, CalculateActivity.class);
                    startActivity(intent);
                    break;
                case "执行力":
                    intent = new Intent(MainActivity.this, OperateActivity.class);
                    startActivity(intent);
                    break;
                case "测试":
                    intent = new Intent(MainActivity.this, OperateActivity.class);
                    intent.putExtra("type", "k-test");
                    startActivity(intent);
                    break;
                case "素质需求":
                    intent = new Intent(MainActivity.this, JingDianActivity.class);
                    startActivity(intent);
                    break;
                case "旧-执行结果":
                    intent = new Intent(MainActivity.this, ResultActivity.class);
                    startActivity(intent);
                    break;
                case "交易费用":
                    intent = new Intent(MainActivity.this, CostActivity.class);
                    startActivity(intent);
                    break;
                case "操作类型":
                    intent = new Intent(MainActivity.this, OperateTypeActivity.class);
                    startActivity(intent);
                    break;
                case "操作结果分析":
                    intent = new Intent(MainActivity.this, CompareOperateResultActivity.class);
                    startActivity(intent);
                    break;
                case "执行结果":
                    intent = new Intent(MainActivity.this, OperateResultActivity.class);
                    startActivity(intent);
                    break;
            }
        });
    }

    @OnLongClick({R.id.tv_transfer_data, R.id.tv_time})
    public void onLongClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_transfer_data:
                Intent intent = new Intent(this, DataTransferActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_time:
                tipSelectDialog.show();
//                NotificationUtil.sendNotification(this);
//                VibratorUtil.vibrator(this);
                break;
        }
    }

    @OnClick(R.id.tv_transfer_data)
    public void onAddClicked(View v) {
        Intent intent = new Intent(this, AddNewResultActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_time)
    public void onClicked(View v) {
        String content = tvTime.getText().toString();
        if ("未计时".equals(content)) {
            tvTime.setText("计时中");
            Intent intent = new Intent(this, TimerService.class);
            startService(intent);
        } else {
            tvTime.setText("未计时");
            Intent intent = new Intent(this, TimerService.class);
            startService(intent);
        }
    }

    private void initDialog() {
        tipSelectDialog = new TipSelectDialog(this, R.style.DialogStyleOne);
        tipSelectDialog.show();
        tipSelectDialog.dismiss();
    }
}
