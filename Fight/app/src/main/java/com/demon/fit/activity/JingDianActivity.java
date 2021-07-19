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
        list.add("期货交易能够稳定获利的三个要素是技能、执行力和心态。");
        list.add("止损时，迅速止损，不用抱有侥幸心理。");

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
