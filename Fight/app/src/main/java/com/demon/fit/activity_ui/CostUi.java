package com.demon.fit.activity_ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.activity.CostActivity;
import com.demon.fit.adapter.CostAdapter;
import com.demon.fit.bean.CostBean;
import com.demon.fit.control.TouchControl;
import com.demon.fit.daos.DBControl;
import com.demon.fit.dialog.CostDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CostUi {

    @BindView(R.id.rv_cost)
    protected RecyclerView costRecycler;
    private CostAdapter costAdapter;

    private List<CostBean> costList;

    private CostActivity activity;

    private CostDialog costDialog;

    public CostUi(CostActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initDailog();
        initAdapter();
        refreshData();
    }

    @OnClick(R.id.tv_add)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                costDialog.show();
                break;
        }
    }

    private void initAdapter() {
        costList = new ArrayList<>();

        costAdapter = new CostAdapter(activity, costList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        costRecycler.setLayoutManager(manager);
        costRecycler.setAdapter(costAdapter);
        TouchControl<CostBean> touchControl = new TouchControl<>(costRecycler, costAdapter, costList, new TouchControl.OnUpdateListener<CostBean>() {
            @Override
            public void onUpdate(List<CostBean> list) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).index = i + 1;
                }
                DBControl.createOrUpdate(activity, CostBean.class, list);
                costAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDelete(CostBean bean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setTitle("注意:")
                        .setMessage("确定删除:" + bean.name)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", (dialog, which) -> {
                            DBControl.delete(activity, CostBean.class, bean);
                            costList.remove(bean);
                            costAdapter.notifyDataSetChanged();
//                            refreshData();
                        });
                builder.create().show();
            }
        });
        touchControl.attachToRecyclerView(costRecycler);

        costAdapter.setListener(new CostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CostBean bean) {
                costDialog.updateUI(bean);
                costDialog.show();
            }

            @Override
            public void onLongClick(CostBean bean) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
//                        .setTitle("注意:")
//                        .setMessage("确定删除:" + bean.name)
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                DBControl.delete(activity, CostBean.class, bean);
//                                refreshData();
//                            }
//                        });
//                builder.create().show();
            }
        });
    }

    private void refreshData() {
        costList.clear();
        costList.add(new CostBean("菜油-OI", 4.4f));
        costList.add(new CostBean("棕榈油-P", 5.5f));
        costList.add(new CostBean("豆油-Y", 5.5f));
        costList.add(new CostBean("菜粕-RM", 3.3f));

//        costList.addAll(DBControl.quaryAll(activity, CostBean.class));
//        Collections.sort(costList, (o1, o2) -> o1.index - o2.index);

        costAdapter.notifyDataSetChanged();
    }

    private void initDailog() {
        costDialog = new CostDialog(activity, R.style.DialogStyleOne);
        costDialog.setListener((product, cost) -> {
            for (CostBean bean : costList) {
                if (bean.name.equals(product)) {
                    bean.cost = cost;
                    DBControl.createOrUpdate(activity, CostBean.class, bean);
                    refreshData();
                    return;
                }
            }

            CostBean bean = new CostBean();
            bean.index = 1;
            bean.name = product;
            bean.cost = cost;
            if (!costList.isEmpty()) {
                bean.index = costList.get(costList.size() - 1).index + 1;
            }
            DBControl.createOrUpdate(activity, CostBean.class, bean);
            refreshData();
        });
        costDialog.show();
        costDialog.dismiss();
    }
}
