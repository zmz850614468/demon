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
public class JingDianActivity extends AppCompatActivity {

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
        list.add("宁愿错过，也不要去试错");
        list.add("侥幸心理可能导致毁灭性的结果");
        list.add("控制回撤，才是稳定盈利的基准");
        list.add("期货交易能够稳定获利的三个要素是技能、执行力和心态");
        list.add("警防重大损失，这将会带来一系列的变形操作");
//        list.add("趋势交易：看好日K趋势，前一天日K线趋势良好，看5分K线顺势，最后看分时MACD也顺势");
//        list.add("宣贯的密度+力度=执行的深度+高度");
//        list.add("执行力没有如果，只有结果");
//        list.add("止损时，迅速止损，不用抱有侥幸心理");

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
