package com.lilanz.foodie.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.foodie.R;
import com.lilanz.foodie.bean.MaterialBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodDetailAdapter extends RecyclerView.Adapter<FoodDetailAdapter.DetailHodler> {

    private Context context;
    private List<MaterialBean> beanList;

    // 1.修改对象
    public FoodDetailAdapter(Context context, List<MaterialBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public DetailHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_material, viewGroup, false);

//        MaterialBean bean = beanList.get(i);
//        switch (bean.desType) {
//            case MaterialBean.DES_TYPE_MATERIAL:
//                // 2.修改布局
//                View view = LayoutInflater.from(context).inflate(R.layout.item_material, viewGroup, false);
//                return new MaterialHolder(view);
//            case MaterialBean.DES_TYPE_STEP:
//            case MaterialBean.DES_TYPE_MEMO:
//                // 2.修改布局
//                view = LayoutInflater.from(context).inflate(R.layout.item_step, viewGroup, false);
//                return new StepHolder(view);
//        }
//        return null;
//
        return new MaterialHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHodler holder, final int i) {
        ViewGroup viewGroup = ((ViewGroup) holder.itemView);
        viewGroup.removeAllViews();
        // 3.设置界面数据
        MaterialBean bean = beanList.get(i);

        switch (bean.desType) {
            case MaterialBean.DES_TYPE_MATERIAL:
                View view = LayoutInflater.from(context).inflate(R.layout.item_material, viewGroup, true);
                holder = new MaterialHolder(view);
                initMaterialUI((MaterialHolder) holder, bean, i);
                break;
            case MaterialBean.DES_TYPE_STEP:
                view = LayoutInflater.from(context).inflate(R.layout.item_step, viewGroup, true);
                holder = new StepHolder(view);
                initStepUI((StepHolder) holder, bean, i);
                break;
            case MaterialBean.DES_TYPE_MEMO:
                view = LayoutInflater.from(context).inflate(R.layout.item_step, viewGroup, true);
                holder = new StepHolder(view);
                initMemoUI((StepHolder) holder, bean, i);
                break;
        }
    }

    private void initMaterialUI(MaterialHolder holder, MaterialBean bean, int i) {
        if (bean.typeOrder == 1) {
            holder.tvMaterial.setVisibility(View.VISIBLE);
        } else {
            holder.tvMaterial.setVisibility(View.GONE);
        }
        holder.tvName.setText(String.format("%d.%s", bean.typeOrder, bean.name));
        holder.tvCount.setText(String.format("%6.1f %s", bean.number, bean.unit));
    }

    private void initStepUI(StepHolder holder, MaterialBean bean, int i) {
        holder.tvStep.setText(String.format("步骤%d", bean.typeOrder));
        holder.tvStepDes.setText(bean.des);
    }

    private void initMemoUI(StepHolder holder, MaterialBean bean, int i) {
        holder.tvStep.setText(String.format("备注%d", bean.typeOrder));
        holder.tvStepDes.setText(bean.des);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    /**
     * 步骤和备注 对应的界面对象
     */
    public class StepHolder extends DetailHodler {
        @BindView(R.id.tv_step)
        TextView tvStep;
        @BindView(R.id.tv_step_des)
        TextView tvStepDes;

        public StepHolder(@NonNull View view) {
            super(view);
        }
    }

    /**
     * 材料界面对象
     */
    public class MaterialHolder extends DetailHodler {

        @BindView(R.id.tv_material)
        TextView tvMaterial;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_count)
        TextView tvCount;

        public MaterialHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class DetailHodler extends RecyclerView.ViewHolder {

        public DetailHodler(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            // 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(MaterialBean bean);
    }
}
