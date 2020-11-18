package com.demon.myapplication.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demon.myapplication.Beans.GroupBean;
import com.demon.myapplication.Beans.MaterialBean;
import com.demon.myapplication.R;
import com.demon.myapplication.adapters.GroupAdapter;
import com.demon.myapplication.adapters.PeiFangAdapter;
import com.demon.myapplication.controls.DBControl;
import com.demon.myapplication.dialogs.ListDialog;
import com.demon.myapplication.utils.StringUtil;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerActivity extends Activity {

    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rg_select)
    RadioGroup rgSelect;
    @BindView(R.id.ns_group)
    NiceSpinner nsGroup;
    @BindView(R.id.layout_bottom)
    RelativeLayout layoutBottom;

    @BindView(R.id.rv_group)
    RecyclerView rvGroup;
    private List<String> groupList;
    private GroupAdapter groupAdapter;

    @BindView(R.id.rv_pei_fangs)
    RecyclerView rvPeiFangs;
    private List<GroupBean> groupBeanList;
    private PeiFangAdapter peiFangAdapter;

    private ListDialog listDialog;
    private GroupBean selectedGroupBean;
    private boolean isShow = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        ButterKnife.bind(this);


        initData();
        initGroupAdapter();
        initPeiFangAdapter();
        initDialog();
        groupAdapter.clickIndex(0);
    }

    @OnClick(R.id.tv_add)
    public void onAddClicked(View view) {
        if (isShow) {
            isShow = false;
            tvAdd.setText("+");
            layoutBottom.setVisibility(View.GONE);
        } else {
            isShow = true;
            tvAdd.setText("--");
            layoutBottom.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.bt_save)
    public void onSaveClick(View v) {
        String name = etName.getText().toString();
        String group = nsGroup.getText().toString();

        if (StringUtil.isEmpty(name)) {
            showToast("名称不能为空！");
            return;
        }

        int selectedId = rgSelect.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.rb_group:
                if (groupList.contains(name)) {
                    showToast("已经存在分组!");
                    break;
                }
                groupList.add(name);
                groupAdapter.notifyDataSetChanged();
                break;
            case R.id.rb_pei_fang:
                GroupBean bean = null;
                for (GroupBean groupBean : groupBeanList) {
                    if (name.equals(groupBean.peiFangName)) {
                        bean = groupBean;
                        break;
                    }
                }
                if (bean == null) {
                    bean = new GroupBean();
                }

                bean.groupName = group;
                bean.peiFangName = name;
                bean.orderId = Integer.MAX_VALUE;
                DBControl.createOrUpdate(this, GroupBean.class, bean);
                updatePeiFangData(bean.groupName);
                break;
        }

        clear();
        MainActivity.needUpdate = true;
    }

    /**
     * 重新拉取数据库 配方数据
     */
    private void updatePeiFangData(String groupName) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_name", groupName);
        List<GroupBean> list = DBControl.quaryByColumn(ManagerActivity.this, GroupBean.class, map);
        groupBeanList.clear();
        groupBeanList.addAll(list);
        peiFangAdapter.notifyDataSetChanged();
    }

    private void clear() {
        etName.setText("");
    }

    private void initPeiFangAdapter() {
        groupBeanList = new ArrayList<>();

        peiFangAdapter = new PeiFangAdapter(this, groupBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPeiFangs.setLayoutManager(manager);
        rvPeiFangs.setAdapter(peiFangAdapter);

        peiFangAdapter.setListener(new PeiFangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GroupBean bean) {
                Intent intent = new Intent(ManagerActivity.this, AddDessertActivity.class);
                intent.putExtra("type", bean.peiFangName);
                startActivity(intent);
            }

            @Override
            public void onChangeClick(GroupBean bean) {
                selectedGroupBean = bean;
                List<String> list = new ArrayList<>();
                list.addAll(groupList);
                if (list.contains(bean.groupName)) {
                    list.remove(bean.groupName);
                }
                listDialog.update(list);
                listDialog.show();
            }

            @Override
            public void onDeleteClick(GroupBean bean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this)
                        .setTitle("注意:")
                        .setMessage("确定删除")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBControl.delete(ManagerActivity.this, GroupBean.class, bean);
                                groupBeanList.remove(bean);
                                peiFangAdapter.notifyDataSetChanged();
                                List<MaterialBean> list = DBControl.quaryByType(ManagerActivity.this, bean.peiFangName);
                                DBControl.deleteAll(ManagerActivity.this, list);
                                MainActivity.needUpdate = true;
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void initGroupAdapter() {
        List<GroupBean> list = DBControl.getDistinct(this, GroupBean.class, "group_name");
        groupList = new ArrayList<>();
        for (GroupBean bean : list) {
            groupList.add(bean.groupName);
        }
        if (!groupList.contains("未分组")) {
            groupList.add(0, "未分组");
        }

        groupAdapter = new GroupAdapter(this, groupList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGroup.setLayoutManager(manager);
        rvGroup.setAdapter(groupAdapter);

        groupAdapter.setListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                updatePeiFangData(bean);
                groupAdapter.notifyDataSetChanged();
            }
        });

        nsGroup.attachDataSource(groupList);
    }

    private void initData() {
        rgSelect.check(R.id.rb_pei_fang);

        rgSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_group:
                        nsGroup.setVisibility(View.GONE);
                        break;
                    case R.id.rb_pei_fang:
                        nsGroup.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    private void initDialog() {
        listDialog = new ListDialog(this, R.style.DialogStyleOne);
        listDialog.show();
        listDialog.dismiss();
        listDialog.setListener(new ListDialog.OnClickListener() {
            @Override
            public void onConfirm(String str) {
                selectedGroupBean.groupName = str;
                DBControl.createOrUpdate(ManagerActivity.this, GroupBean.class, selectedGroupBean);
                groupBeanList.remove(selectedGroupBean);
                peiFangAdapter.notifyDataSetChanged();
                MainActivity.needUpdate = true;
            }
        });
    }

    private boolean needSave;

    @Override
    protected void onPause() {
        super.onPause();
        needSave = MainActivity.needUpdate;
    }

    @Override
    protected void onDestroy() {
        if (needSave) {
            DBControl.saveDB2File(this);
        }

        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
