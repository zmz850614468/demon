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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timeup.R;
import com.example.timeup.adapters.TypeAdapter;
import com.example.timeup.beans.TypeBean;
import com.example.timeup.controls.TouchControl;
import com.example.timeup.controls.TypeDBControl;
import com.example.timeup.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentOne extends Fragment {


    @BindView(R.id.layout_bottom)
    RelativeLayout layoutBottom;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_during)
    EditText etDuring;
    @BindView(R.id.tv_show_Or_hind)
    TextView tvShowOrHind;

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    private TypeAdapter typeAdapter;
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
    public void onSaveClicked(View v) {
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
            typeBean.orderId = typeBeanList.size();
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
        rvType.scrollToPosition(typeBeanList.size() - 1);

        TypeDBControl.saveDB2File(getActivity());
    }


    private boolean isShow = false;

    @OnClick(R.id.tv_show_Or_hind)
    public void onShowOrHindClicked(View v) {
        if (isShow) {
            layoutBottom.setVisibility(View.GONE);
            tvShowOrHind.setText("+");
        } else {
            layoutBottom.setVisibility(View.VISIBLE);
            tvShowOrHind.setText("--");
            clear();
        }
        isShow = !isShow;
    }

    private void updateData() {
        typeBeanList.clear();
        typeBeanList.addAll(TypeDBControl.quaryAll(getActivity()));
        Collections.sort(typeBeanList, new Comparator<TypeBean>() {
            @Override
            public int compare(TypeBean o1, TypeBean o2) {
                return o1.orderId - o2.orderId;
            }
        });
        typeAdapter.notifyDataSetChanged();
    }

    private void clear() {
        etName.setText("");
        etDuring.setText("");
    }

    private void initAdapter() {
        typeBeanList = new ArrayList<>();

        typeAdapter = new TypeAdapter(getActivity(), typeBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvType.setLayoutManager(manager);
        rvType.setAdapter(typeAdapter);
        TouchControl touchControl = new TouchControl<>(rvType, typeAdapter, typeBeanList, new TouchControl.OnUpdateListener<TypeBean>() {
            @Override
            public void onUpdate(final List<TypeBean> list) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).orderId = i;
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        TypeDBControl.createOrUpdate(getActivity(), list);
                        TypeDBControl.saveDB2File(getActivity());
                    }
                }.start();
            }
        });
        touchControl.attachToRecyclerView(rvType);


        typeAdapter.setListener(new TypeAdapter.OnItemClickListener() {
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
            public void onDeleteClick(final TypeBean bean) {
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
                                TypeDBControl.saveDB2File(getActivity());
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
