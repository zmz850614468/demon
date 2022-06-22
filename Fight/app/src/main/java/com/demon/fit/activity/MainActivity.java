package com.demon.fit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demon.fit.R;
import com.demon.fit.adapter.ItemAdapter;
import com.demon.fit.bean.ItemBean;
import com.demon.fit.data_transfer.activity.DataTransferActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_item)
    protected RecyclerView recycler;
    private ItemAdapter adapter;
    private List<ItemBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initAdapter();


        Intent intent = new Intent(MainActivity.this, OperateActivity.class);
        startActivity(intent);
    }

    private void initAdapter() {
        list = new ArrayList<>();
        list.add(new ItemBean(R.drawable.fl, "复利"));
        list.add(new ItemBean(R.drawable.zxl, "执行力"));
        list.add(new ItemBean(R.drawable.jd, "经典语录"));
//        list.add(new ItemBean(R.drawable.fy, "交易费用"));
        list.add(new ItemBean(R.drawable.mb, "目标"));
        list.add(new ItemBean(R.drawable.jg, "旧-执行结果"));
        list.add(new ItemBean(R.drawable.jg, "操作结果分析"));
        list.add(new ItemBean(R.drawable.jg, "新-执行结果"));
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
                case "经典语录":
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
                case "新-执行结果":
                    intent = new Intent(MainActivity.this, OperateResultActivity.class);
                    startActivity(intent);
                    break;
                case "操作结果分析":
                    intent = new Intent(MainActivity.this, CompareOperateResultActivity.class);
                    startActivity(intent);
                    break;
            }
        });
    }

    @OnLongClick(R.id.tv_transfer_data)
    public void onLongClicked(View v) {
        Intent intent = new Intent(this, DataTransferActivity.class);
        startActivity(intent);
    }
}
