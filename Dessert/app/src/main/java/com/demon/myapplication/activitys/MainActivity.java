package com.demon.myapplication.activitys;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demon.myapplication.Beans.MaterialBean;
import com.demon.myapplication.R;
import com.demon.myapplication.adapters.AddAdapter;
import com.demon.myapplication.controls.DBControl;
import com.demon.myapplication.dialogs.InputDialog;
import com.demon.myapplication.utils.SharePreferencesUtil;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static boolean needUpdate = false;

    @BindView(R.id.ns_dessert)
    NiceSpinner nsDessert;
    @BindView(R.id.ns_number)
    NiceSpinner nsNumber;

    @BindView(R.id.rv_detail)
    RecyclerView rvDetail;
    private AddAdapter MaterialAdapter;
    private List<MaterialBean> materialList;

    private List<String> dessertList;
    private List<String> numberList;

    private InputDialog inputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPermission();
        initUI();
        initAdapter();
        initNiceSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needUpdate) {
            needUpdate = false;
            dessertList = SharePreferencesUtil.getDessert(this);
            nsDessert.attachDataSource(dessertList);
            updateData();
        } else {
            updateData();
        }
    }

    @OnClick({R.id.iv_setting, R.id.tv_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting:
                if (dessertList.isEmpty()) {
                    showToast("配方不能为空！");
                    return;
                }
                String type = ((String) nsDessert.getSelectedItem());
                Intent intent = new Intent(this, AddDessertActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.tv_add:
                inputDialog.show();
                break;
        }
    }

    private void updateData() {
        String type = nsDessert.getText().toString();
        int number = Integer.parseInt(nsNumber.getText().toString());
        updateData(type, number);
    }

    private void updateData(String type, int number) {
        List<MaterialBean> list = DBControl.quaryByType(this, type);
        for (MaterialBean bean : list) {
            bean.number *= number;
        }

        materialList.clear();
        materialList.addAll(list);
        MaterialAdapter.notifyDataSetChanged();
    }

    private void initNiceSpinner() {
        dessertList = SharePreferencesUtil.getDessert(this);
        nsDessert.attachDataSource(dessertList);

        numberList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            numberList.add(i + "");
        }
        nsNumber.attachDataSource(numberList);

        nsDessert.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String type = ((TextView) view).getText().toString();
                int number = Integer.parseInt(nsNumber.getText().toString());
                updateData(type, number);
            }
        });
        nsNumber.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String type = nsDessert.getText().toString();
                int number = Integer.parseInt(((TextView) view).getText().toString());
                updateData(type, number);
            }
        });

        updateData();
    }

    private void initAdapter() {
        materialList = new ArrayList<>();

        MaterialAdapter = new AddAdapter(this, materialList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetail.setLayoutManager(manager);
        rvDetail.setAdapter(MaterialAdapter);
    }

    private void initUI() {
        inputDialog = new InputDialog(this, R.style.DialogStyleOne);
        inputDialog.setListener(new InputDialog.OnClickListener() {
            @Override
            public void onConfirm(String str) {
                if (dessertList.contains(str)) {
                    showToast("配方已经存在");
                } else {
                    dessertList.add(str);
                    SharePreferencesUtil.saveDessert(MainActivity.this, dessertList);
                    nsDessert.attachDataSource(dessertList);
                }
            }
        });
        inputDialog.show();
        inputDialog.dismiss();
    }

    private void initPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

