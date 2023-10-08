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
        list.add("入手：   \n  1.交易量放大，且大于前4根中的3根 \n  2.至少前4根K线是横版**  \n  3.均线方向和放量方向相同\n" +
                "出手：    \n  均线M5反向穿过M30后的第一根同向K线");
        list.add("反向-入手：    \n  1.交易量放大，且大于前4根中的3根 \n  2.至少前4根K线是趋势方向**  \n  3.均线方向和放量方向相同\n" +
                "1-出手：    \n  均线M5反向穿过M30后的第一根同向K线\n" +
                "2-出手：  \n  有满足第一条的情况出现*");
        list.add("反向-入手：    \n  1.多根交易量相较前5根交易量有显著放量  \n  2.方向相同，且有显著的涨跌幅 \n  3.之后的第一根方向K线\n" +
                "出手：   \n  1.有满足第一条的情况出现* \n  2.15:00前要或周末前结束");


//        list.add("卖点\n    趋势：两阴线或三小阴线\n");
//        list.add("交易量明显放大\n    均线趋势反转\n    十日均线回踩或回调\n好现象：同趋势交易量放大，反趋势交易量减少；反之为坏现象");
//        list.add("---");
//        list.add("10日均线做多\n    回踩支撑线做多，量能缩小\n    阳线上穿5日均线和10日均线\n买点：交易量放大，大阳线，5分K线趋势向上");


//        list.add("卖点\n    小趋势：三小阴线或一大阴线\n    大趋势：跌破boll中线");
//        list.add("   ");
//        list.add("10日均线做多\n    回踩支撑线做多，量能缩小\n    阳线上穿5日均线和10日均线\n买点：交易量放大，大阳线，5分K线趋势向上\n卖点：30分钟内没趋势上涨");
//        list.add("破轨回踩做多\n    突破布林中轨后，回调并受到中轨支撑\n买点：交易量放大，大阳线，5分K线趋势向上");
//        list.add("大阳夹小阴阳线做多\n    头尾是大阳线并有交集，中间多根小阴阳线\n买点：分时线、MACD和5分K线趋势均向上\n卖点：价格小于大阳线底端");
//        list.add("时K上吊线做空\n    在时K上升趋势最高点出现一根标准的上吊线\n买点：5分K线趋势向下");
//        list.add("   ");
//        list.add("大阴线做空-(模拟仓测试)\n    前期一波上涨，突然一根大阴线\n    5分K线准备改变趋势方向\n买点：大阴线并跌破bool中线，伴随交易量放量");
//        list.add("中大阴线破支撑位-(模拟仓测试)\n    中阴线或大阴线，秃头阴或小头阴\n    刚跌破支撑位或欲跌破支撑位\n    5分K线也是下跌趋势\n卖点：一个小时内");
//        list.add("二次探底做多\n    第二次探底回调，并得到布林中轨支撑");


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






























