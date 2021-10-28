package com.lilanz.foodie.uicontrol;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lilanz.foodie.R;
import com.lilanz.foodie.activity.FoodListActivity;
import com.lilanz.foodie.activity.MainActivity;
import com.lilanz.foodie.adapter.FileAdapter;
import com.lilanz.foodie.bean.FoodBean;
import com.lilanz.foodie.bean.FoodTypeBean;
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

public class MainUi {

    public static final String ACTION_UPDATE_DATA = "MainUi.updateData";

    @BindView(R.id.rv_file)
    RecyclerView rvFile;
    private FileAdapter fileAdapter;
    private List<FoodTypeBean> foodTypeBeanList;

    private MainActivity activity;

    private InputDialog inputDialog;

    public MainUi(MainActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initDialog();
        initData();
        initAdapter();
    }

    @OnClick(R.id.tv_add)
    public void onClicked(View v) {
        inputDialog.setInputHint("文件夹名称");
        inputDialog.show();
    }

    private void initAdapter() {
        fileAdapter = new FileAdapter(activity, foodTypeBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFile.setLayoutManager(manager);
        rvFile.setAdapter(fileAdapter);

        fileAdapter.setListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FoodTypeBean bean) {
                Intent intent = new Intent(activity, FoodListActivity.class);
                intent.putExtra("foodType", bean.name);
                activity.startActivity(intent);
            }

            @Override
            public void onDoubleClick(FoodTypeBean bean) {
            }
        });

        TouchControl touchControl = new TouchControl(rvFile, fileAdapter, foodTypeBeanList, new TouchControl.OnUpdateListener() {
            @Override
            public void onUpdate(List list) {
                updateList(list);
            }

            @Override
            public void onDelete(int position) {
                fileAdapter.notifyDataSetChanged();
                FoodTypeBean bean = foodTypeBeanList.get(position);
//                showLog("删除 : " + new Gson().toJson(bean));

                Map<String, Object> map = new HashMap<>();
                map.put("food_type", bean.name);
                int count = (int) DBControl.queryCount(activity, FoodBean.class, map);
                if (count > 0) {
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
                        foodTypeBeanList.remove(bean);
                        DBControl.delete(activity, FoodTypeBean.class, bean);
                        updateList(foodTypeBeanList);
                        fileAdapter.notifyDataSetChanged();
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
        touchControl.attachToRecyclerView(rvFile);
    }

    private void updateList(List<FoodTypeBean> list) {
        boolean needUpdate = false;
        FoodTypeBean bean = null;
        for (int i = 1; i <= list.size(); i++) {
            bean = list.get(i - 1);
            if (bean.order != i) {
                needUpdate = true;
                bean.order = i;
            }
        }
        if (needUpdate) {
            DBControl.createOrUpdate(activity, FoodTypeBean.class, (ArrayList) list);
        }
    }

    private void initData() {
        if (foodTypeBeanList == null) {
            foodTypeBeanList = new ArrayList<>();
        }
        foodTypeBeanList.clear();
        List tempList = DBControl.queryAll(activity, FoodTypeBean.class);
        foodTypeBeanList.addAll(tempList);
        Map<String, Object> map = new HashMap<>();
        for (FoodTypeBean bean : foodTypeBeanList) {
            map.put("food_type", bean.name);
            bean.count = (int) DBControl.queryCount(activity, FoodBean.class, map);
        }
        Collections.sort(foodTypeBeanList, new Comparator<FoodTypeBean>() {
            @Override
            public int compare(FoodTypeBean o1, FoodTypeBean o2) {
                return o1.order - o2.order;
            }
        });
    }

    private void initDialog() {
        inputDialog = new InputDialog(activity, R.style.DialogStyleOne);
        inputDialog.show();
        inputDialog.dismiss();
        inputDialog.setListener(new InputDialog.OnClickListener() {
            @Override
            public void onConfirm(String str) {
                for (FoodTypeBean bean : foodTypeBeanList) {
                    if (str.equals(bean.name)) {
                        showToast("文件夹已经存在");
                        return;
                    }
                }
                FoodTypeBean bean = new FoodTypeBean();
                bean.name = str;
                bean.order = foodTypeBeanList.size() + 1;
                foodTypeBeanList.add(bean);
                fileAdapter.notifyDataSetChanged();
                DBControl.createOrUpdate(activity, FoodTypeBean.class, bean);
            }
        });
    }

    public void register() {
        IntentFilter filter = new IntentFilter(ACTION_UPDATE_DATA);
        activity.registerReceiver(receiver, filter);
    }

    public void unRegister() {
        activity.unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_UPDATE_DATA:
                    initData();
                    fileAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("MainUi", msg);
    }

}
