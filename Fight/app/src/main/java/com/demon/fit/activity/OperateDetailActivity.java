package com.demon.fit.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.OperateDetailAdapter;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.daos.DBControl;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperateDetailActivity extends AppCompatActivity {

    @BindView(R.id.rv_operate_detail)
    protected RecyclerView operateDetailRecycler;
    private OperateDetailAdapter operateDetailAdapter;

    private List<OperateResultBean> operateDetailList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_detail);
        ButterKnife.bind(this);

        initAdapter();
    }

    private void initAdapter() {
        operateDetailList = DBControl.quaryAll(this, OperateResultBean.class);
        showLog("数量：" + operateDetailList.size());
        Collections.reverse(operateDetailList);

        operateDetailAdapter = new OperateDetailAdapter(this, operateDetailList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        operateDetailRecycler.setLayoutManager(manager);
        operateDetailRecycler.setAdapter(operateDetailAdapter);

        operateDetailAdapter.setListener(bean -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(OperateDetailActivity.this)
                    .setTitle("注意:")
                    .setMessage("确定删除")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        DBControl.delete(OperateDetailActivity.this, OperateResultBean.class, bean);
                        operateDetailList.remove(bean);
                        operateDetailAdapter.notifyDataSetChanged();
                    });
            builder.create().show();
        });
    }

    private void showLog(String msg) {
        Log.e("OperateDetailActivity", msg);
    }
}
