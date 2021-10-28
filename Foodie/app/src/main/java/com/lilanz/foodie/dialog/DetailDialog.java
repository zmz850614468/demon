package com.lilanz.foodie.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.foodie.R;
import com.lilanz.foodie.bean.MaterialBean;
import com.lilanz.foodie.util.StringUtil;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.et_material_name)
    EditText etMaterialName;
    @BindView(R.id.et_material_count)
    EditText etMaterialCount;
    @BindView(R.id.ns_dan_wei)
    NiceSpinner nsDanWei;
    @BindView(R.id.tv_material)
    TextView tvMaterial;
    @BindView(R.id.tv_step)
    TextView tvStep;
    @BindView(R.id.tv_memo)
    TextView tvMemo;
    @BindView(R.id.layout_memo)
    LinearLayout layoutMemo;
    @BindView(R.id.layout_step)
    LinearLayout layoutStep;
    @BindView(R.id.layout_material)
    LinearLayout layoutMaterial;
    @BindView(R.id.et_memo)
    EditText etMemo;
    @BindView(R.id.et_step)
    EditText etStep;
    private Context context;
    private List<String> list;

    private String desType = MaterialBean.DES_TYPE_MATERIAL;
    private MaterialBean updateMaterialBean;

    public DetailDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detail);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initUI();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void updateData(MaterialBean bean) {
        desType = bean.desType;
        switch (desType) {
            case MaterialBean.DES_TYPE_MATERIAL:
                onTypeClicked(tvMaterial);
                etMaterialName.setText(bean.name);
                etMaterialCount.setText(String.format("%.1f", bean.number));
                for (int i = 0; i < list.size(); i++) {
                    String str = (String) nsDanWei.getItemAtPosition(i);
                    if (bean.unit.equals(str)) {
                        nsDanWei.setSelectedIndex(i);
                    }
                }
                break;
            case MaterialBean.DES_TYPE_STEP:
                onTypeClicked(tvStep);
                etStep.setText(bean.des);
                break;
            case MaterialBean.DES_TYPE_MEMO:
                etMemo.setText(bean.des);
                onTypeClicked(tvMemo);
                break;
        }
        updateMaterialBean = bean;
    }


    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        switch (desType) {
            case MaterialBean.DES_TYPE_MATERIAL:
                dealMaterial();
                break;
            case MaterialBean.DES_TYPE_STEP:
                dealStep();
                break;
            case MaterialBean.DES_TYPE_MEMO:
                dealMemo();
                break;
        }
        dismiss();
    }

    private void dealMaterial() {
        try {
            String name = etMaterialName.getText().toString();
            float count = Float.parseFloat(etMaterialCount.getText().toString());
            String danWei = nsDanWei.getText().toString();

            if (StringUtil.isEmpty(name)) {
                showToast("数据不能为空");
                return;
            }

            if (updateMaterialBean == null) {
                MaterialBean bean = new MaterialBean();
                bean.desType = MaterialBean.DES_TYPE_MATERIAL;
                bean.name = name;
                bean.number = count;
                bean.unit = danWei;
                if (listener != null) {
                    listener.onConfirm(false, bean);
                }
            } else {
                updateMaterialBean.name = name;
                updateMaterialBean.number = count;
                updateMaterialBean.unit = danWei;
                listener.onConfirm(true, updateMaterialBean);
//                updateMaterialBean = null;
            }
            etMaterialCount.setText("");
            etMaterialName.setText("");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showToast("请填写正确数据");
        }

    }

    private void dealStep() {
        String stepDes = etStep.getText().toString();
        if (StringUtil.isEmpty(stepDes)) {
            showToast("数据不能为空");
            return;
        }

        if (updateMaterialBean == null) {
            MaterialBean bean = new MaterialBean();
            bean.desType = MaterialBean.DES_TYPE_STEP;
            bean.des = stepDes;
            if (listener != null) {
                listener.onConfirm(false, bean);
            }
        } else {
            updateMaterialBean.des = stepDes;
            listener.onConfirm(true, updateMaterialBean);
//            updateMaterialBean = null;
        }
        etStep.setText("");
    }

    private void dealMemo() {
        String memoDes = etMemo.getText().toString();
        if (StringUtil.isEmpty(memoDes)) {
            showToast("数据不能为空");
            return;
        }

        if (updateMaterialBean == null) {
            MaterialBean bean = new MaterialBean();
            bean.desType = MaterialBean.DES_TYPE_MEMO;
            bean.des = memoDes;
            if (listener != null) {
                listener.onConfirm(false, bean);
            }
        } else {
            updateMaterialBean.des = memoDes;
            listener.onConfirm(true, updateMaterialBean);
//            updateMaterialBean = null;
        }
        etMemo.setText("");
    }

    public void dismiss() {
        super.dismiss();
        if (updateMaterialBean != null) {
            etMaterialName.setText("");
            etMaterialCount.setText("");
            etStep.setText("");
            etMemo.setText("");
            updateMaterialBean = null;
        }
    }

    @OnClick({R.id.tv_material, R.id.tv_step, R.id.tv_memo})
    public void onTypeClicked(View v) {
        if (updateMaterialBean != null) {
            showToast("修改内容不允许选择类型");
            return;
        }
        switch (v.getId()) {
            case R.id.tv_material:
                desType = MaterialBean.DES_TYPE_MATERIAL;
                tvMaterial.setBackgroundResource(R.drawable.shape_box_gray);
                tvStep.setBackgroundResource(R.drawable.shape_box_white);
                tvMemo.setBackgroundResource(R.drawable.shape_box_white);
                layoutMaterial.setVisibility(View.VISIBLE);
                layoutStep.setVisibility(View.GONE);
                layoutMemo.setVisibility(View.GONE);
                break;
            case R.id.tv_step:
                desType = MaterialBean.DES_TYPE_STEP;
                tvMaterial.setBackgroundResource(R.drawable.shape_box_white);
                tvStep.setBackgroundResource(R.drawable.shape_box_gray);
                tvMemo.setBackgroundResource(R.drawable.shape_box_white);
                layoutMaterial.setVisibility(View.GONE);
                layoutStep.setVisibility(View.VISIBLE);
                layoutMemo.setVisibility(View.GONE);
                break;
            case R.id.tv_memo:
                desType = MaterialBean.DES_TYPE_MEMO;
                tvMaterial.setBackgroundResource(R.drawable.shape_box_white);
                tvStep.setBackgroundResource(R.drawable.shape_box_white);
                tvMemo.setBackgroundResource(R.drawable.shape_box_gray);
                layoutMaterial.setVisibility(View.GONE);
                layoutStep.setVisibility(View.GONE);
                layoutMemo.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initUI() {
        list = new ArrayList<>();
        list.add("g");
        list.add("ml");
        list.add("个");
        list.add("勺");
        nsDanWei.attachDataSource(list);
    }

    //  ====================== 时间监听 ===========================

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {

        void onConfirm(boolean isUpdate, MaterialBean bean);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
