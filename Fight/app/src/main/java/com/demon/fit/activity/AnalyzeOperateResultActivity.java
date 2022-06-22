package com.demon.fit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.OperateResultAnalyzeAdapter;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.daos.DBControl;
import com.demon.fit.util.StringUtil;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分析操作结果类
 */
public class AnalyzeOperateResultActivity extends AppCompatActivity {

    public static final String SELECTED_TYPE = "selectedType";

    @BindView(R.id.layout_result)
    public ViewGroup layoutResult;

    @BindView(R.id.tv_pos_count)
    public TextView tvPosCount;
    @BindView(R.id.tv_neg_count)
    public TextView tvNegCount;
    @BindView(R.id.tv_operate_count)
    public TextView tvOperateCount;
    @BindView(R.id.tv_bad_count)
    public TextView tvBadCount;
    @BindView(R.id.tv_operate_result)
    public TextView tvOperateResult;
    @BindView(R.id.tv_bad_percent)
    public TextView tvBadPercent;
    @BindView(R.id.tv_percent)
    public TextView tvPercent;

    @BindView(R.id.nice_spinner)
    public NiceSpinner selectionSpinner;
    private List<String> selectionList;

    @BindView(R.id.rv_operate_result_analyze)
    protected RecyclerView analyseRecycler;
    private OperateResultAnalyzeAdapter analyseAdapter;

    private List<OperateTodayBean> analyseList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_operate_result);
        ButterKnife.bind(this);

        initSpinner();
        initAdapter();
        String name = getIntent().getStringExtra(SELECTED_TYPE);
        query(name);
        if (name != null) {
            for (int i = 0; i < selectionList.size(); i++) {
                if (selectionList.get(i).contains(name)) {
                    selectionSpinner.setSelectedIndex(i);
                    break;
                }
            }
        }
        onClicked(null);
    }

    private void initAdapter() {
        analyseList = new ArrayList<>();

        analyseAdapter = new OperateResultAnalyzeAdapter(this, analyseList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        analyseRecycler.setLayoutManager(manager);
        analyseRecycler.setAdapter(analyseAdapter);

        analyseAdapter.setListener(bean -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("注意:")
                    .setMessage("确定删除当前选项吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        DBControl.delete(AnalyzeOperateResultActivity.this, OperateTodayBean.class, bean);
                        showToast("删除成功");
                        analyseList.remove(bean);
                        analyseAdapter.notifyDataSetChanged();
                    });
            builder.create().show();
        });
    }

    private void initSpinner() {
        selectionList = new ArrayList<>();

        List<OperateTodayBean> list = DBControl.quaryAll(this, OperateTodayBean.class);
        Map<String, Integer> map = new HashMap<>();
        int total = 0;
        for (OperateTodayBean bean : list) {
            if (bean.isFollow) {
                continue;
            }
            if (map.containsKey(bean.name)) {
                map.put(bean.name, map.get(bean.name) + 1);
            } else {
                map.put(bean.name, 1);
            }
            total++;
        }
        selectionList.add("全部-" + total);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            selectionList.add(entry.getKey() + "-" + entry.getValue());
        }

        Collections.sort(selectionList, (o1, o2) -> {
            int count1 = Integer.parseInt(o1.substring(o1.lastIndexOf("-") + 1));
            int count2 = Integer.parseInt(o2.substring(o2.lastIndexOf("-") + 1));
            return count2 - count1;
        });

        selectionSpinner.attachDataSource(selectionList);
        selectionSpinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            String name = (String) parent.getItemAtPosition(position);
            showLog("selected:" + name);
            name = name.substring(0, name.lastIndexOf("-"));
            query(name);
        });

//        List<OperateTodayBean> list = DBControl.getDistinct(this, OperateTodayBean.class, "name");
//        for (OperateTodayBean bean : list) {
//            selectionList.add(bean.name);
//        }
//        selectionSpinner.attachDataSource(selectionList);
//        selectionSpinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
//            String name = (String) parent.getItemAtPosition(position);
//            showLog("selected:" + name);
//            query(name);
//        });
    }

    /**
     * 查询结果
     */
    private void query(String name) {
        analyseList.clear();
        if ("全部".equals(name) || StringUtil.isEmpty(name)) {
            analyseList.addAll(DBControl.quaryAll(this, OperateTodayBean.class));
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            analyseList.addAll(DBControl.quaryByColumn(this, OperateTodayBean.class, map));
        }
        Collections.reverse(analyseList);

        refreshResult();
        analyseAdapter.notifyDataSetChanged();
    }

    /**
     * 更新结果界面信息
     */
    private void refreshResult() {
        int posCount = 0;
        int negCount = 0;
        int totalCount = 0;
        int badCount = 0;
        int result = 0;

        for (OperateTodayBean bean : analyseList) {

            result += bean.result;

            if (bean.isFollow) {
                continue;
            }

            if (bean.isBadOperate) {
                badCount++;
            }

            if (bean.result > 0) {
                posCount++;
            } else if (bean.result < 0) {
                negCount++;
            }
            totalCount++;
        }

        tvPosCount.setText("盈/次:" + posCount);
        tvNegCount.setText("亏/次:" + negCount);
        tvOperateCount.setText("总/次:" + totalCount);
        tvBadCount.setText("糟糕/次:" + badCount);
        tvOperateResult.setText("操作结果:" + result);

        tvBadPercent.setText(String.format("糟糕占比:%.2f", badCount * 100.0f / totalCount) + "%");
        tvPercent.setText(String.format("操作胜率:%.2f", posCount * 100.0f / totalCount) + "%");
    }

    @OnClick(R.id.tv_result)
    public void onClicked(View v) {
        if (layoutResult.getVisibility() == View.VISIBLE) {
            layoutResult.setVisibility(View.GONE);
        } else {
            layoutResult.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("AnalyzeOperateResult", msg);
    }
}
