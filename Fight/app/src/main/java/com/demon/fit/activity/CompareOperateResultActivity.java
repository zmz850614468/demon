package com.demon.fit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

/**
 * 对比不同品类 的操作结果
 */
public class CompareOperateResultActivity extends AppCompatActivity {


    @BindView(R.id.tv_type_order)
    public TextView tvTypeOrder;
    @BindView(R.id.rv_compare_operate_result)
    protected RecyclerView compareRecycler;
    private CompareOperateResultAdapter compareAdapter;

    private List<CompareResultBean> compareList;

    private List<String> showList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_operate_result);
        ButterKnife.bind(this);

        initShowList();
        initAdapterData();
        initAdapter();
        updateOrder();
    }

    private void initShowList() {
        showList = new ArrayList<>();
        showList.add("菜油");
        showList.add("豆油");
        showList.add("螺纹钢");
        showList.add("聚氯乙烯");
        showList.add("燃油");
        showList.add("乙二醇");
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
            if (!showList.contains(bean.name)) {
                continue;
            }

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
//        Collections.sort(compareList, (o1, o2) -> {
//            if (o2.totalCount != o1.totalCount) {
//                return o2.totalCount - o1.totalCount;
//            }
//            return o2.result - o1.result;
//        });
        for (CompareResultBean bean : compareList) {
            bean.calculatePercent();
        }
    }

    @OnClick(R.id.tv_detail)
    public void onDetailClicked(View v) {
        Intent intent = new Intent(this, AnalyzeOperateResultActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_type_order)
    public void onClicked(View v) {
        String type = tvTypeOrder.getText().toString();
        switch (type) {
            case "盈利":
                tvTypeOrder.setText("次数");
                break;
            case "次数":
                tvTypeOrder.setText("胜率");
                break;
            case "胜率":
                tvTypeOrder.setText("盈利");
                break;
        }
        updateOrder();
    }

    /**
     * 更新排序方式
     */
    private void updateOrder() {
        String type = tvTypeOrder.getText().toString();
        switch (type) {
            case "盈利":
                Collections.sort(compareList, (o1, o2) -> o2.result - o1.result);
                break;
            case "次数":
                Collections.sort(compareList, (o1, o2) -> {
                    if (o2.totalCount != o1.totalCount) {
                        return o2.totalCount - o1.totalCount;
                    }
                    return o2.result - o1.result;
                });
                break;
            case "胜率":
                Collections.sort(compareList, (o1, o2) -> {
                    if (o2.percent == o1.percent) {
                        return o2.totalCount - o1.totalCount;
                    }
                    return (int) (o2.percent * 100 - o1.percent * 100);
                });
                break;
        }
        compareAdapter.notifyDataSetChanged();
    }

}
