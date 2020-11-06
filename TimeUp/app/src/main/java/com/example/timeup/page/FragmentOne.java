package com.example.timeup.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timeup.R;
import com.example.timeup.adapters.TypeAdapter;
import com.example.timeup.beans.TypeBean;
import com.example.timeup.controls.TypeDBControl;
import com.example.timeup.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentOne extends Fragment {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_during)
    EditText etDuring;

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    private TypeAdapter typrAdapter;
    private List<TypeBean> typeBeanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_1, container, false);
        ButterKnife.bind(this, chatView);
        initAdapter();
        updateData();

        return chatView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @OnClick(R.id.bt_save)
    public void onClicked(View v) {
        String name = etName.getText().toString();
        String during = etDuring.getText().toString();

        int time = 0;
        try {
            time = Integer.parseInt(during);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtil.isEmpty(name)) {
            showToast("名字不能为空");
            return;
        }

        if (time <= 0) {
            showToast("时间不能为零");
            return;
        }

        //  保存数据
        TypeBean typeBean = null;
        for (TypeBean bean : typeBeanList) {
            if (name.equals(bean.name)) {
                typeBean = bean;
                break;
            }
        }
        if (typeBean == null) {
            typeBean = new TypeBean();
            typeBean.name = name;
        }
        try {
            typeBean.during = Integer.parseInt(during);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (typeBean.during <= 0) {
            showToast("时长必须大于0");
            return;
        }

        TypeDBControl.createOrUpdate(getActivity(), typeBean);
        clear();
        updateData();
    }

    private void updateData() {
        typeBeanList.clear();
        typeBeanList.addAll(TypeDBControl.quaryAll(getActivity()));
        typrAdapter.notifyDataSetChanged();
    }

    private void clear() {
        etName.setText("");
        etDuring.setText("");
    }

    private void initAdapter() {
        typeBeanList = new ArrayList<>();

        typrAdapter = new TypeAdapter(getActivity(), typeBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvType.setLayoutManager(manager);
        rvType.setAdapter(typrAdapter);

        typrAdapter.setListener(new TypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TypeBean bean) {
                etName.setText(bean.name);
                etDuring.setText(bean.during + "");
            }

            @Override
            public void onStartClick(TypeBean bean) {
                SlipPageActivity.addTimer(bean.name, bean.during * 60);
                showToast("添加计时任务成功！");
            }

            @Override
            public void onLongClick(final TypeBean bean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("注意")
                        .setMessage("确定删除计时器：" + bean.name)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TypeDBControl.delete(getActivity(), bean);
                                clear();
                                updateData();
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
