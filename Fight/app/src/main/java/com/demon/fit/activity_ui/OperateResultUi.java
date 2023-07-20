package com.demon.fit.activity_ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.activity.DrawActivity;
import com.demon.fit.activity.OperateDetailActivity;
import com.demon.fit.activity.OperateResultActivity;
import com.demon.fit.adapter.MonthResultAdapter;
import com.demon.fit.bean.MonthResultBean;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.daos.DBControl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperateResultUi {

    @BindView(R.id.tv_add)
    TextView tvAdd;

    public static String type = "月";
    private OperateResultActivity activity;

    @BindView(R.id.rv_result)
    protected RecyclerView resultRecycler;
    private MonthResultAdapter resultAdapter;

    private List<MonthResultBean> resultList;


    public OperateResultUi(OperateResultActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        tvAdd.setText(type);

        initAdapter();
    }

    @OnClick(R.id.tv_title)
    public void onTitleClicked(View v) {
        Intent intent = new Intent(activity, DrawActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 添加新的数据
     *
     * @param v
     */
    @OnClick(R.id.tv_add)
    public void onClicked(View v) {
        switch (type) {
            case "月":
                type = "季";
                break;
            case "季":
                type = "年";
                break;
            case "年":
                type = "月";
                break;
        }
        tvAdd.setText(type);
        updateData();
//        Intent intent = new Intent(activity, AddNewResultActivity.class);
//        activity.startActivity(intent);
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
        dealMonthData(resultList);      // 月份统计

        if ("季".equals(type)) {         // 季度统计
            dealSeasonData(resultList);
        } else if ("年".equals(type)) {  // 年份统计
            dealYearData(resultList);
        }

        // 添加总计数据
        MonthResultBean resultBean = new MonthResultBean();
        for (MonthResultBean bean : resultList) {
            resultBean.operateResult += bean.operateResult;
            resultBean.poundage += bean.poundage;
            resultBean.posCount += bean.posCount;
            resultBean.negCount += bean.negCount;
        }
        resultList.add(resultBean);

        // 计算统计数据
        for (MonthResultBean bean : resultList) {
            bean.totalCount = bean.posCount + bean.negCount;
            bean.percent = bean.posCount * 100.0f / bean.totalCount;
            bean.totalResult = bean.operateResult - bean.poundage;
        }

        resultAdapter.notifyDataSetChanged();
    }

    /**
     * 按月份处理数据
     */
    private void dealMonthData(List<MonthResultBean> resultList) {
        List<OperateResultBean> list = DBControl.quaryAll(activity, OperateResultBean.class);
        showLog("数据量：" + list.size());
        resultList.clear();

        Calendar calendar = Calendar.getInstance();
        for (OperateResultBean bean : list) {
            calendar.setTimeInMillis(bean.timeStamp);

            boolean isAdded = false;
            if (!resultList.isEmpty() && resultList.get(0).month == calendar.get(Calendar.MONTH)) {
                MonthResultBean monthResultBean = resultList.get(0);
                isAdded = true;

                monthResultBean.operateResult += bean.result;
                monthResultBean.poundage += bean.poundage;
                monthResultBean.posCount += bean.posCount;
                monthResultBean.negCount += bean.negCount;
            }

            if (!isAdded) {
                MonthResultBean resultBean = new MonthResultBean();
                resultBean.operateResult = bean.result;
                resultBean.poundage = bean.poundage;
                resultBean.posCount = bean.posCount;
                resultBean.negCount = bean.negCount;
                resultBean.month = calendar.get(Calendar.MONTH);
                resultBean.year = calendar.get(Calendar.YEAR);
                resultList.add(0, resultBean);
            }
        }
    }

    /**
     * 按季度处理数据
     */
    private void dealSeasonData(List<MonthResultBean> resultList) {
        List<MonthResultBean> tempList = new ArrayList<>();
        MonthResultBean resultBean = new MonthResultBean();
        for (MonthResultBean bean : resultList) {
            resultBean.operateResult += bean.operateResult;
            resultBean.poundage += bean.poundage;
            resultBean.posCount += bean.posCount;
            resultBean.negCount += bean.negCount;
            if (bean.month % 3 == 0) {
                resultBean.month = bean.month;
                resultBean.year = bean.year;
                tempList.add(resultBean);
                resultBean = new MonthResultBean();
            }
        }
        if ((resultBean.posCount + resultBean.negCount) > 0) {
            resultBean.month = resultList.get(resultList.size() - 1).month;
            resultBean.year = resultList.get(resultList.size() - 1).year;
            tempList.add(resultBean);
        }
        resultList.clear();
        resultList.addAll(tempList);
    }

    /**
     * 按年份处理数据
     */
    private void dealYearData(List<MonthResultBean> resultList) {
        List<MonthResultBean> tempList = new ArrayList<>();
        MonthResultBean resultBean = new MonthResultBean();
        for (MonthResultBean bean : resultList) {
            resultBean.operateResult += bean.operateResult;
            resultBean.poundage += bean.poundage;
            resultBean.posCount += bean.posCount;
            resultBean.negCount += bean.negCount;
            if (bean.month % 12 == 0) {
                resultBean.year = bean.year;
                tempList.add(resultBean);
                resultBean = new MonthResultBean();
            }
        }
        if ((resultBean.posCount + resultBean.negCount) > 0) {
            resultBean.year = resultList.get(resultList.size() - 1).year;
            tempList.add(resultBean);
        }
        resultList.clear();
        resultList.addAll(tempList);
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
