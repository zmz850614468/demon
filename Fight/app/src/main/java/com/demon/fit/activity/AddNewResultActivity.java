package com.demon.fit.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.OperateTodayAdapter;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.daos.DBControl;
import com.demon.fit.util.SharePreferencesUtil;
import com.demon.fit.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewResultActivity extends AppCompatActivity {

    @BindView(R.id.layout_result)
    ViewGroup layoutResult;
    @BindView(R.id.et_result)
    public EditText etResult;
    @BindView(R.id.et_poundage)
    public EditText etPoundage;     // 手续费用
    @BindView(R.id.et_pos_operate_count)
    public EditText etPosOperateCount;
    @BindView(R.id.et_neg_operate_count)
    public EditText etNegOperateCount;


    @BindView(R.id.rv_operate_today)
    protected RecyclerView operate_todayRecycler;
    private OperateTodayAdapter operate_todayAdapter;
    private List<OperateTodayBean> operate_todayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_result);
        ButterKnife.bind(this);

        initAdapter();
        initData();
    }

    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        String result = etResult.getText().toString();
        String poundage = etPoundage.getText().toString();
        String posCount = etPosOperateCount.getText().toString();
        String negCount = etNegOperateCount.getText().toString();

        int intResult = 0;
        int intPoundage = 0;
        int intPosCount = 0;
        int intNegCount = 0;

        if (!StringUtil.isEmpty(result)) {
            intResult = Integer.parseInt(result);
        }
        if (!StringUtil.isEmpty(poundage)) {
            intPoundage = Integer.parseInt(poundage);
        }
        if (!StringUtil.isEmpty(posCount)) {
            intPosCount = Integer.parseInt(posCount);
        }
        if (!StringUtil.isEmpty(negCount)) {
            intNegCount = Integer.parseInt(negCount);
        }

        OperateResultBean bean = new OperateResultBean();
        bean.timeStamp = System.currentTimeMillis();
        bean.result = intResult;
        bean.poundage = intPoundage;
        bean.posCount = intPosCount;
        bean.negCount = intNegCount;

        DBControl.createOrUpdate(this, OperateResultBean.class, bean);
        showToast("添加数据成功");
        operate_todayList.clear();
        SharePreferencesUtil.saveOperateToday(this, "");
        finish();
    }

    @OnClick(R.id.bt_refresh_ui)
    public void onRefreshUiClicked(View v) {
        layoutResult.setVisibility(View.GONE);
//        showLog(new Gson().toJson(operate_todayList));
        while (operate_todayList.size() > 1) {
            int index = operate_todayList.size() - 1;
            if (isAllEmpty(operate_todayList.get(index)) && isAllEmpty(operate_todayList.get(index - 1))) {
                operate_todayList.remove(index);
            } else {
                break;
            }
        }
        int index = operate_todayList.size() - 1;
        if (!isAllEmpty(operate_todayList.get(index))) {
            operate_todayList.add(new OperateTodayBean());
        }
        operate_todayAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_calculate)
    public void onCalculateClicked(View v) {
        layoutResult.setVisibility(View.VISIBLE);

        int totalResult = 0;
        int posCount = 0;
        int negCount = 0;
        for (OperateTodayBean bean : operate_todayList) {
            bean.result = (bean.outPrice - bean.inPrice) * ("买入".equals(bean.inType) ? 1 : -1);
            totalResult += bean.result;
            if (bean.result > 0) {
                posCount++;
            } else if (bean.result < 0) {
                negCount++;
            }
        }

        etResult.setText(totalResult + "");
        etPosOperateCount.setText(posCount + "");
        etNegOperateCount.setText(negCount + "");

        SharePreferencesUtil.saveOperateToday(this, new Gson().toJson(operate_todayList));
    }

    private boolean isAllEmpty(OperateTodayBean bean) {
        if (!StringUtil.isEmpty(bean.name) || bean.inPrice != 0 || bean.outPrice != 0) {
            return false;
        }
        return true;
    }

    private void initAdapter() {
        operate_todayList = new ArrayList<>();

        operate_todayAdapter = new OperateTodayAdapter(this, operate_todayList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        operate_todayRecycler.setLayoutManager(manager);
        operate_todayRecycler.setAdapter(operate_todayAdapter);
    }

    private void initData() {
        String data = SharePreferencesUtil.getOperateToday(this);
        if (!StringUtil.isEmpty(data)) {
            operate_todayList.clear();
            operate_todayList.addAll(new Gson().fromJson(data, new TypeToken<ArrayList<OperateTodayBean>>() {
            }.getType()));
            operate_todayAdapter.notifyDataSetChanged();
            onCalculateClicked(null);
        }
    }

    private void showLog(String msg) {
        Log.e("AddNewResultActivity", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
