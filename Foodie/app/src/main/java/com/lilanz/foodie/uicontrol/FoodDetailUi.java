package com.lilanz.foodie.uicontrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.foodie.R;
import com.lilanz.foodie.activity.FoodDetailActivity;
import com.lilanz.foodie.adapter.FoodDetailAdapter;
import com.lilanz.foodie.bean.FoodBean;
import com.lilanz.foodie.bean.MaterialBean;
import com.lilanz.foodie.control.TouchControl;
import com.lilanz.foodie.daos.DBControl;
import com.lilanz.foodie.dialog.DetailDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailUi {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rv_food_detail)
    protected RecyclerView recycler;
    private FoodDetailAdapter adapter;

    private List<MaterialBean> list;

    private String foodName;

    private DetailDialog detailDialog;

    private FoodDetailActivity activity;

    public FoodDetailUi(FoodDetailActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

        initAdapter();
        initDialog();
        initUI();
        updateList();
    }

    @OnClick(R.id.tv_add)
    public void onClicked(View v) {
        detailDialog.show();
    }

    private void initDialog() {
        detailDialog = new DetailDialog(activity, R.style.DialogStyleOne);
        detailDialog.show();
        detailDialog.dismiss();
        detailDialog.setListener(new DetailDialog.OnClickListener() {
            @Override
            public void onConfirm(MaterialBean bean) {
                bean.foodName = foodName;
                bean.orderId = list.size() + 1;
                DBControl.createOrUpdate(activity, MaterialBean.class, bean);
                updateList();
            }
        });
    }

    private void initAdapter() {
        list = new ArrayList<>();

        adapter = new FoodDetailAdapter(activity, list);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new FoodDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MaterialBean bean) {

            }
        });

        TouchControl<MaterialBean> touchControl = new TouchControl(recycler, adapter, list, new TouchControl.OnUpdateListener<MaterialBean>() {
            @Override
            public void onUpdate(List<MaterialBean> list) {
                updateListOrder(list);
                DBControl.createOrUpdate(activity, MaterialBean.class, (ArrayList) list);
            }

            @Override
            public void onDelete(int position) {
                adapter.notifyDataSetChanged();
                MaterialBean bean = list.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("注意：");
                builder.setMessage("请确定是否要删除数据。");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(bean);
                        DBControl.delete(activity, MaterialBean.class, bean);
                        updateListOrder(list);
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

    private void updateListOrder(List<MaterialBean> list) {
        int materialIndex = 1;
        int desIndex = 1;
        int memoIndex = 1;
        int orderId = 1;
        for (MaterialBean bean : list) {
            bean.orderId = orderId;
            orderId++;
            switch (bean.desType) {
                case MaterialBean.DES_TYPE_MATERIAL:
                    bean.typeOrder = materialIndex;
                    materialIndex++;
                    break;
                case MaterialBean.DES_TYPE_STEP:
                    bean.typeOrder = desIndex;
                    desIndex++;
                    break;
                case MaterialBean.DES_TYPE_MEMO:
                    bean.typeOrder = memoIndex;
                    memoIndex++;
                    break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateList() {
        list.clear();
        List<MaterialBean> temList = DBControl.queryAll(activity, MaterialBean.class);
        list.addAll(temList);
        Collections.sort(list, new Comparator<MaterialBean>() {
            @Override
            public int compare(MaterialBean o1, MaterialBean o2) {
                return o1.orderId - o2.orderId;
            }
        });

        updateListOrder(list);
//        int materialIndex = 1;
//        int desIndex = 1;
//        int memoIndex = 1;
//        for (MaterialBean bean : list) {
//            switch (bean.desType) {
//                case MaterialBean.DES_TYPE_MATERIAL:
//                    bean.typeOrder = materialIndex;
//                    materialIndex++;
//                    break;
//                case MaterialBean.DES_TYPE_STEP:
//                    bean.typeOrder = desIndex;
//                    desIndex++;
//                    break;
//                case MaterialBean.DES_TYPE_MEMO:
//                    bean.typeOrder = memoIndex;
//                    memoIndex++;
//                    break;
//            }
//        }
//        adapter.notifyDataSetChanged();
    }

    private void initUI() {
        foodName = activity.getIntent().getStringExtra("foodName");
        tvTitle.setText(foodName);
    }

}
