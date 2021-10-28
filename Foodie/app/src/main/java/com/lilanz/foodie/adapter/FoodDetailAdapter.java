package com.lilanz.foodie.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.foodie.R;
import com.lilanz.foodie.bean.MaterialBean;
import com.lilanz.foodie.daos.DBControl;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailAdapter extends RecyclerView.Adapter<FoodDetailAdapter.DetailHodler> {

    private Context context;
    private List<MaterialBean> beanList;

    private boolean isTimesVisible;
    private float times = 1;

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

        return new MaterialHolder(view);
    }


    private long lastClickTime;
    private int lastIndex = -1;

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

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long curTime = System.currentTimeMillis();
                    if (lastIndex == i && (curTime - lastClickTime) < 500) {
                        listener.onItemClick(bean);
                        lastIndex = -1;
                    } else {
                        lastIndex = i;
                        lastClickTime = curTime;
                    }
                }
            });
        }
    }

    private void initMaterialUI(MaterialHolder holder, MaterialBean bean, int i) {
        if (bean.typeOrder == 1) {
            holder.layoutMaterial.setVisibility(View.VISIBLE);
            holder.initNiceSpinner();
        } else {
            holder.layoutMaterial.setVisibility(View.GONE);
        }
        holder.tvName.setText(String.format("%d.%s", bean.typeOrder, bean.name));
        holder.tvCount.setText(String.format("%6.1f %s", bean.number * times, bean.unit));
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

        @BindView(R.id.layout_material)
        ViewGroup layoutMaterial;
        @BindView(R.id.layout_times)
        ViewGroup layoutTimes;
        @BindView(R.id.tv_times)
        TextView tvTimes;
        @BindView(R.id.ns_times)
        NiceSpinner nsTimes;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_count)
        TextView tvCount;

        private List<String> list;

        public MaterialHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.tv_material)
        public void onClicked(View v) {
            isTimesVisible = !isTimesVisible;
            if (isTimesVisible) {
                layoutTimes.setVisibility(View.VISIBLE);
            } else {
                layoutTimes.setVisibility(View.INVISIBLE);
            }
        }

        @OnClick(R.id.tv_times)
        public void onTimesClicked(View v) {
            for (MaterialBean bean : beanList) {
                bean.number *= times;
            }
            DBControl.createOrUpdate(context, MaterialBean.class, beanList);
            times = 1;
            setNsTimes(times);
            showToast("缩放数据成功");
        }

        public void initNiceSpinner() {
            if (isTimesVisible) {
                layoutTimes.setVisibility(View.VISIBLE);
            } else {
                layoutTimes.setVisibility(View.INVISIBLE);
            }
            list = new ArrayList<>();
            list.add("0.1");
            list.add("0.2");
            list.add("0.3");
            list.add("0.4");
            list.add("0.5");
            list.add("0.6");
            list.add("0.7");
            list.add("0.8");
            list.add("0.9");
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            list.add("5");
            list.add("6");
            list.add("7");
            list.add("8");
            list.add("9");

            nsTimes.attachDataSource(list);
            setNsTimes(times);

            nsTimes.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                @Override
                public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                    if (view instanceof TextView) {
                        String content = ((TextView) view).getText().toString();
                        try {
                            times = Float.parseFloat(content);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }

        private void setNsTimes(float content) {
            String des = null;
            if (content >= 1.0f) {
                des = String.format("%d", (int) content);
            } else {
                des = String.format("%.1f", content);
            }
            for (int i = 0; i < list.size(); i++) {
                if (des.equals(nsTimes.getItemAtPosition(i))) {
                    nsTimes.setSelectedIndex(i);
                    break;
                }
            }
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
        void onItemClick(MaterialBean bean);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
