package com.lilanz.foodie.uicontrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lilanz.foodie.R;
import com.lilanz.foodie.activity.FoodDetailActivity;
import com.lilanz.foodie.activity.FoodListActivity;
import com.lilanz.foodie.adapter.FoodListAdapter;
import com.lilanz.foodie.bean.FoodBean;
import com.lilanz.foodie.bean.MaterialBean;
import com.lilanz.foodie.control.TouchControl;
import com.lilanz.foodie.daos.DBControl;
import com.lilanz.foodie.dialog.InputDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodListUi {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rv_food)
    protected RecyclerView recycler;
    private FoodListAdapter adapter;

    private List<FoodBean> list;

    private String foodType;

    private FoodListActivity activity;

    private InputDialog inputDialog;

    public FoodListUi(FoodListActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initDialog();
        initUI();
        initData();
        initAdapter();
    }

    @OnClick(R.id.tv_add)
    public void onClicked(View v) {
        inputDialog.setInputHint("菜单名称");
        inputDialog.show();
    }

    private void initData() {
//        list = DBControl.queryAll(activity, FoodBean.class);
        Map<String, Object> map = new HashMap<>();
        map.put("food_type", foodType);
        list = DBControl.queryByColumn(activity, FoodBean.class, map);
        Collections.sort(list, new Comparator<FoodBean>() {
            @Override
            public int compare(FoodBean o1, FoodBean o2) {
                return o1.order - o2.order;
            }
        });
    }

    private void initUI() {
        foodType = activity.getIntent().getStringExtra("foodType");
        tvTitle.setText(foodType);
    }

    private void initAdapter() {
        adapter = new FoodListAdapter(activity, list);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodBean bean) {
                Intent intent = new Intent(activity, FoodDetailActivity.class);
                intent.putExtra("foodName", bean.name);
                activity.startActivity(intent);
            }
        });
        TouchControl touchControl = new TouchControl(recycler, adapter, list, new TouchControl.OnUpdateListener() {
            @Override
            public void onUpdate(List list) {
                updateList(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDelete(int position) {
                adapter.notifyDataSetChanged();
                FoodBean bean = list.get(position);
                showLog("删除 : " + new Gson().toJson(bean));
                if (bean.count > 0) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("文件夹不为空，无法进行删除操作");
                        }
                    });
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("注意：");
                builder.setMessage("请确定是否要删除数据。");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(bean);
                        DBControl.delete(activity, FoodBean.class, bean);
                        updateList(list);
                        adapter.notifyDataSetChanged();

                        Map<String, Object> map = new HashMap<>();
                        map.put("food_name", bean.name);
                        DBControl.deleteByColumn(activity, MaterialBean.class, map);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
        touchControl.attachToRecyclerView(recycler);
    }

    private void updateList(List<FoodBean> list) {
        boolean needUpdate = false;
        FoodBean bean = null;
        for (int i = 1; i <= list.size(); i++) {
            bean = list.get(i - 1);
            if (bean.order != i) {
                needUpdate = true;
                bean.order = i;
            }
        }
        if (needUpdate) {
            DBControl.createOrUpdate(activity, FoodBean.class, (ArrayList) list);
        }
    }

    private void initDialog() {
        inputDialog = new InputDialog(activity, R.style.DialogStyleOne);
        inputDialog.show();
        inputDialog.dismiss();
        inputDialog.setListener(new InputDialog.OnClickListener() {
            @Override
            public void onConfirm(String str) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", str);
                long count = DBControl.queryCount(activity, FoodBean.class, map);
                if (count > 0) {
                    showToast("菜单已经存在");
                    return;
                }
//                for (FoodBean bean : list) {
//                    if (str.equals(bean.name)) {
//                        showToast("菜单已经存在");
//                        return;
//                    }
//                }
                FoodBean bean = new FoodBean();
                bean.name = str;
                bean.foodType = foodType;
                bean.order = list.size() + 1;
                list.add(bean);
                adapter.notifyDataSetChanged();
                DBControl.createOrUpdate(activity, FoodBean.class, bean);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FoodListUi", msg);
    }


}
