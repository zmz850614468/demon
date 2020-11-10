package com.demon.myapplication.activitys;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demon.myapplication.Beans.MaterialBean;
import com.demon.myapplication.R;
import com.demon.myapplication.adapters.AddAdapter;
import com.demon.myapplication.controls.DBControl;
import com.demon.myapplication.utils.FileUtil;
import com.demon.myapplication.utils.SharePreferencesUtil;
import com.demon.myapplication.utils.StringUtil;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDessertActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.ns_unit)
    NiceSpinner nsUnit;

    @BindView(R.id.rv_detail)
    RecyclerView rvDetail;
    private AddAdapter adapter;
    private List<MaterialBean> materialList;

    private String type;
    private List<String> unitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dessert);
        ButterKnife.bind(this);
        this.type = getIntent().getStringExtra("type");

        initUI();
        initNiceSpinner();
        initAdapter();
        updateData();
    }


    @OnClick(R.id.bt_save)
    public void onClick(View v) {
        String name = etName.getText().toString();
        String numberStr = etNumber.getText().toString();
        String unit = nsUnit.getText().toString();

        double number = 0;
        try {
            number = Double.parseDouble(numberStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtil.isEmpty(name)) {
            showToast("名称不能为空！");
            return;
        }
        if (number <= 0) {
            showToast("请输入有效的数量！");
            return;
        }

        MaterialBean bean = new MaterialBean();
        for (MaterialBean materialBean : materialList) {
            if (name.equals(materialBean.name)) {
                bean = materialBean;
                break;
            }
        }

        bean.name = name;
        bean.number = number;
        bean.unit = unit;
        bean.type = type;
        DBControl.createOrUpdate(this, bean);
        updateData();
        clearUI();
    }

    @OnClick(R.id.tv_delete)
    public void onDeleteClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("注意:")
                .setMessage("确定删除配方:" + type)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBControl.deleteAll(AddDessertActivity.this, materialList);
                        SharePreferencesUtil.removeDessert(AddDessertActivity.this, type);
                        MainActivity.needUpdate = true;
                        finish();
                    }
                });
        builder.create().show();
    }

    private void clearUI() {
        etName.setText("");
        etNumber.setText("");
        nsUnit.attachDataSource(unitList);
    }

    private void updateData() {
        List<MaterialBean> list = DBControl.quaryByType(this, type);
        materialList.clear();
        materialList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        materialList = new ArrayList<>();

        adapter = new AddAdapter(this, materialList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetail.setLayoutManager(manager);
        rvDetail.setAdapter(adapter);

        adapter.setListener(new AddAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MaterialBean bean) {
                etName.setText(bean.name);
                etNumber.setText(String.format("%.1f", bean.number));
                nsUnit.attachDataSource(unitList);
                if ("ml".equals(bean.unit)) {
                    nsUnit.setSelectedIndex(1);
                } else if ("个".equals(bean.unit)) {
                    nsUnit.setSelectedIndex(2);
                }
            }

            @Override
            public void onItemLongClick(final MaterialBean bean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddDessertActivity.this)
                        .setTitle("注意:")
                        .setMessage("确定删除材料：" + bean.name)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBControl.delete(AddDessertActivity.this, bean);
                                updateData();
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void initNiceSpinner() {
        unitList = new ArrayList<>();
        unitList.add("g");
        unitList.add("ml");
        unitList.add("个");
        nsUnit.attachDataSource(unitList);
    }

    @Override
    protected void onDestroy() {
        DBControl.saveDB2File(this);
        super.onDestroy();
    }

    private void initUI() {
        tvTitle.setText(type);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}

