package com.demon.breastmilkcalcu.view;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.breastmilkcalcu.MainActivity;
import com.demon.breastmilkcalcu.R;
import com.demon.breastmilkcalcu.adapter.BreastMilkAdapter;
import com.demon.breastmilkcalcu.bean.BreastMilkBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainUi {

    private MainActivity activity;

    @BindView(R.id.tv_result)
    protected TextView tvResult;
    @BindView(R.id.rv_result)
    protected RecyclerView resultRecycler;
    private BreastMilkAdapter resultAdapter;

    private List<BreastMilkBean> resultList;

    public MainUi(MainActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        initAdapter();
    }

    @OnClick({R.id.bt_paste, R.id.bt_calcu})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_paste:
                activity.pasteContent();
                activity.showToast("开始统计数据");
                break;
            case R.id.bt_calcu:
                break;
        }
    }

    /**
     * 更新界面数据
     *
     * @param list
     * @param size 这个月的总数据个数，不重复数据
     */
    public void updateData(List<BreastMilkBean> list, int size) {
        resultList.clear();
        resultList.addAll(list);
        resultAdapter.notifyDataSetChanged();

        tvResult.setText("本月数据总个数：" + size);
    }

    private void initAdapter() {
        resultList = new ArrayList<>();

        resultAdapter = new BreastMilkAdapter(activity, resultList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        resultRecycler.setLayoutManager(manager);
        resultRecycler.setAdapter(resultAdapter);

//        resultAdapter.setListener(new BreastMilkAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BreastMilkBean bean) {
//
//            }
//        });
    }

}
