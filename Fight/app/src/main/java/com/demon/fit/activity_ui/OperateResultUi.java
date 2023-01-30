package com.demon.fit.activity_ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.activity.AddNewResultActivity;
import com.demon.fit.activity.OperateDetailActivity;
import com.demon.fit.activity.OperateResultActivity;
import com.demon.fit.adapter.MonthResultAdapter;
import com.demon.fit.bean.MonthResultBean;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.daos.DBControl;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperateResultUi {

    private OperateResultActivity activity;

    @BindView(R.id.rv_result)
    protected RecyclerView resultRecycler;
    private MonthResultAdapter resultAdapter;

    private List<MonthResultBean> resultList;


    public OperateResultUi(OperateResultActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initAdapter();
    }

    /**
     * 添加新的数据
     *
     * @param v
     */
    @OnClick(R.id.bt_add)
    public void onClicked(View v) {
        Intent intent = new Intent(activity, AddNewResultActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 详情界面
     *
     * @param v
     */
    @OnClick(R.id.bt_operate_detail)
    public void onDetailClicked(View v) {
        Intent intent = new Intent(activity, OperateDetailActivity.class);
        activity.startActivity(intent);
    }

    private void initAdapter() {
        resultList = new ArrayList<>();

        resultAdapter = new MonthResultAdapter(activity, resultList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        resultRecycler.setLayoutManager(manager);
        resultRecycler.setAdapter(resultAdapter);

        resultAdapter.setListener(bean -> {

        });
    }

    /**
     * 更新数据
     */
    public void updateData() {
        List<OperateResultBean> list = DBControl.quaryAll(activity, OperateResultBean.class);
        showLog("数据量：" + list.size());
        resultList.clear();

        Calendar calendar = Calendar.getInstance();
        for (OperateResultBean bean : list) {
            calendar.setTimeInMillis(bean.timeStamp);

            boolean isAdded = false;
            for (MonthResultBean monthResultBean : resultList) {
                if (monthResultBean.month == calendar.get(Calendar.MONTH)) {
                    isAdded = true;

                    monthResultBean.operateResult += bean.result;
                    monthResultBean.poundage += bean.poundage;
                    monthResultBean.posCount += bean.posCount;
                    monthResultBean.negCount += bean.negCount;
                    break;
                }
            }

            if (!isAdded) {
                MonthResultBean resultBean = new MonthResultBean();
                resultBean.operateResult = bean.result;
                resultBean.poundage = bean.poundage;
                resultBean.posCount = bean.posCount;
                resultBean.negCount = bean.negCount;
                resultBean.month = calendar.get(Calendar.MONTH);
                resultList.add(0, resultBean);
            }
        }

        // 添加之前的数据
//        addOldData();

        for (MonthResultBean resultBean : resultList) {
            resultBean.totalCount = resultBean.posCount + resultBean.negCount;
            resultBean.percent = resultBean.posCount * 100.0f / resultBean.totalCount;
            resultBean.totalResult = resultBean.operateResult - resultBean.poundage;
        }

        resultAdapter.notifyDataSetChanged();

    }

    /**
     * 添加旧的数据
     * 2021.8~2022.4 的数据
     */
    private void addOldData() {
        MonthResultBean resultBean = new MonthResultBean();
        resultBean.operateResult = 215;
        resultBean.poundage = 0;
        resultBean.posCount = 17;
        resultBean.negCount = 16;
        resultBean.month = 3;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = -9360;
        resultBean.poundage = 0;
        resultBean.posCount = 19;
        resultBean.negCount = 32;
        resultBean.month = 2;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = -3822;
        resultBean.poundage = 0;
        resultBean.posCount = 14;
        resultBean.negCount = 11;
        resultBean.month = 1;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = 1430;
        resultBean.poundage = 0;
        resultBean.posCount = 15;
        resultBean.negCount = 9;
        resultBean.month = 0;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = 2135;
        resultBean.poundage = 0;
        resultBean.posCount = 17;
        resultBean.negCount = 15;
        resultBean.month = 11;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = -4720;
        resultBean.poundage = 0;
        resultBean.posCount = 20;
        resultBean.negCount = 17;
        resultBean.month = 10;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = 900;
        resultBean.poundage = 0;
        resultBean.posCount = 8;
        resultBean.negCount = 11;
        resultBean.month = 9;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = -23518;
        resultBean.poundage = 0;
        resultBean.posCount = 21;
        resultBean.negCount = 12;
        resultBean.month = 8;
        resultList.add(resultBean);

        resultBean = new MonthResultBean();
        resultBean.operateResult = 2840;
        resultBean.poundage = 0;
        resultBean.posCount = 10;
        resultBean.negCount = 2;
        resultBean.month = 7;
        resultList.add(resultBean);

    }

    private void showLog(String msg) {
        Log.e("OperateResultUi", msg);
    }

}
