package com.demon.fit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.CompareOperateResultAdapter;
import com.demon.fit.bean.CompareResultBean;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.daos.DBControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompareOperateResultActivity extends AppCompatActivity {

    @BindView(R.id.rv_compare_operate_result)
    protected RecyclerView compareRecycler;
    private CompareOperateResultAdapter compareAdapter;

    private List<CompareResultBean> compareList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_operate_result);
        ButterKnife.bind(this);

        initAdapterData();
        initAdapter();
    }

    private void initAdapter() {
        compareAdapter = new CompareOperateResultAdapter(this, compareList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        compareRecycler.setLayoutManager(manager);
        compareRecycler.setAdapter(compareAdapter);

        compareAdapter.setListener(bean -> {
            Intent intent = new Intent(this, AnalyzeOperateResultActivity.class);
            intent.putExtra(AnalyzeOperateResultActivity.SELECTED_TYPE, bean.name);
            startActivity(intent);
        });
    }

    private void initAdapterData() {
        compareList = new ArrayList<>();

        List<OperateTodayBean> list = DBControl.quaryAll(this, OperateTodayBean.class);
        Map<String, CompareResultBean> map = new HashMap<>();
        for (OperateTodayBean bean : list) {
            if (!map.containsKey(bean.name)) {
                map.put(bean.name, new CompareResultBean(bean.name));
            }

            CompareResultBean compareBean = map.get(bean.name);
            compareBean.result += bean.result;

            if (bean.isFollow) {
                continue;
            }
            if (bean.isBadOperate) {
                compareBean.badCount++;
            }
            if (bean.result > 0) {
                compareBean.posCount++;
            } else if (bean.result < 0) {
                compareBean.negCount++;
            }
            compareBean.totalCount++;
        }

        compareList.addAll(map.values());
        Collections.sort(compareList, (o1, o2) -> {
            if (o2.totalCount != o1.totalCount) {
                return o2.totalCount - o1.totalCount;
            }
            return o2.result - o1.result;
        });
        for (CompareResultBean bean : compareList) {
            bean.calculatePercent();
        }
    }

    @OnClick(R.id.tv_detail)
    public void onDetailClicked(View v) {
        Intent intent = new Intent(this, AnalyzeOperateResultActivity.class);
        startActivity(intent);
    }

}
