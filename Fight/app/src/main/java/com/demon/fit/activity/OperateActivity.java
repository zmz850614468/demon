package com.demon.fit.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.TargetAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 操作说明界面
 */
public class OperateActivity extends AppCompatActivity {

    @BindView(R.id.rv_operate)
    protected RecyclerView operateRecycler;
    private TargetAdapter operateAdapter;
    private List<String> operatetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = null;
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        if (type == null) {
            setContentView(R.layout.activity_operate);
        } else if ("k-test".equals(type)) {
            setContentView(R.layout.activity_operate_test);
        }
        ButterKnife.bind(this);

        initData();
        initAdapter();
    }

    private void initAdapter() {
        operateAdapter = new TargetAdapter(this, operatetList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        operateRecycler.setLayoutManager(manager);
        operateRecycler.setAdapter(operateAdapter);
    }

    private void initData() {
        operatetList = new ArrayList<>();
        operatetList.add("注：控制回撤，才是稳定盈利的基准");
        operatetList.add("注：先确认操作方式：趋势操作 或 反趋势操作");
        operatetList.add("1.确认日K线的趋势方向");
        operatetList.add("2.确认5K线的趋势方向-趋势要清晰、明朗");
        operatetList.add("3.确认分时MACD的趋势方向");
        operatetList.add("");
        operatetList.add("4.在日K趋势上，可以关注时K线趋势");
        operatetList.add("5.把周K线趋势加入观察集合");
        operatetList.add("6.反趋势下跌或上涨，次日不要急于操作");
        operatetList.add("7.备选的多只趋势股，优先等待后发力的那只");
    }
}






























