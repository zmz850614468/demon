package com.demon.fit.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.StringAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 经典语录记录界面
 */
public class OperateTypeActivity extends AppCompatActivity {

    @BindView(R.id.rv_jing_dian)
    protected RecyclerView recycler;
    private StringAdapter adapter;

    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jing_dian);
        ButterKnife.bind(this);

        initAdapter();
    }

    private void initAdapter() {
        list = new ArrayList<>();
        list.add("卖点\n    小趋势：三小阴线或一大阴线\n    大趋势：跌破boll中线");
        list.add("   ");
        list.add("大阳夹小阴阳线做多\n    头尾是大阳线并有交集，中间多根小阴阳线\n\n买点：分时线、MACD和5分K线趋势均向上");
        list.add("10日均线做多\n    回踩支撑线做多，量能缩小\n    阳线上穿5日均线和10日均线\n\n买点：交易量放大，大阳线，5分K线趋势向上");
        list.add("破轨回踩做多\n    突破布林中轨后，回调并受到中轨支撑\n\n买点：交易量放大，大阳线，5分K线趋势向上");
        list.add("   ");
        list.add("大阴线做空(模拟仓测试)\n    前期一波上涨，突然一根大阴线\n\n买点：大阴线并跌破bool中线，伴随交易量放量");
        list.add("二次探底做多\n    第二次探底回调，并得到布林中轨支撑");


        adapter = new StringAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new StringAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {

            }
        });
    }

}
