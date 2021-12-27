package com.demon.fit.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
 * 目标计划界面
 */
public class MoneyTargetActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rv_target)
    protected RecyclerView targetRecycler;
    private TargetAdapter targetAdapter;

    private List<String> targetList;
    private int currentYear = 2022;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        ButterKnife.bind(this);

        initUI();
        initAdapter();
        refreshData(currentYear, true);
    }

    private void refreshData(int year, boolean isNew) {
        List<String> list = initTargetData(year);
        if (list != null) {
            currentYear = year;
            targetList.clear();
            targetList.addAll(list);
            targetAdapter.notifyDataSetChanged();
            tvTitle.setText(String.format("%d年期货目标", currentYear));
        } else {
            if (isNew) {
                showToast("已经是最新的计划");
            } else {
                showToast("没有历史数据了");
            }
        }
    }

    private List<String> initTargetData(int year) {
        switch (year) {
            case 2021:
                return target2021();
            case 2022:
                return target2022();
        }

        return null;
    }

    private List<String> target2022() {

        List<String> target = new ArrayList<>();
        target.add("第三阶段目标--未确定");
        target.add("时间范围：6个月 -- 2022-7-1~2022-12-31");
        target.add("初始资金：4.83");
        target.add("每月目标：10%");
        target.add("目标资金：8.56");
        target.add("");
        target.add("第二阶段目标--未确定");
        target.add("时间范围：5个月 -- 2022-2-1~2022-6-30");
        target.add("初始资金：3");
        target.add("每月目标：10%");
        target.add("目标资金：4.83");
        target.add("");
        target.add("第一阶段目标");
        target.add("时间范围：1个月 -- 2022-1-1~2022-1-31");
        target.add("初始资金：1.3");
        target.add("每月目标：10%");
        target.add("目标资金：1.43");
        return target;
    }

    private List<String> target2021() {
        List<String> target = new ArrayList<>();
        target.add("时间范围：(8个月) - 2021-5-8~2021-12-31");
        target.add("初始资金：1.59");
        target.add("目标资金：2.18");
        target.add("目标范围：2.01~2.53");
        target.add("每月目标：4% (636~872)");
        target.add("每月范围：3%~6% (477~1518)");
        return target;
    }

    private void initAdapter() {
        targetList = new ArrayList<>();

        targetAdapter = new TargetAdapter(this, targetList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        targetRecycler.setLayoutManager(manager);
        targetRecycler.setAdapter(targetAdapter);
    }

    private void initUI() {
        tvTitle.setClickable(true);
        tvTitle.setOnTouchListener(new View.OnTouchListener() {

            private float x1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (x1 - event.getX() > 100) {  // 向右滑动
                            refreshData(currentYear + 1, true);
                            return true;
                        } else if (event.getX() - x1 > 100) { // 向左滑动
                            refreshData(currentYear - 1, false);
                            return true;
                        }

                        break;
                }
                return false;
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("MoneyTargetActivity", msg);
    }
}
