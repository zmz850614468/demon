package com.demon.cameratest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.demon.cameratest.adapter.StrAdapter;
import com.demon.cameratest.bean.MainBean;
import com.demon.cameratest.bean.VModel;
import com.demon.cameratest.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainBean mainBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUI();
        initAdapter();
    }

    private StrAdapter adapter;

    private List<String> list;

    public void onTest(View v) {
        mainBean.setMsg("4561321351");
        mainBean.add("2022 ---");
//        showToast("添加数据");
//        adapter.add("2022");
//        mainBean.setStrAdapter(adapter);
//        mainBean.msg = "24563";
//        mainBean.setMsg(binding.tvMsg, "246132");

//        vModel.mainBean.setMsg("6464616516");
//        adapter.notifyDataSetChanged();
//        binding.executePendingBindings();
//        list.add(0, "2022");
//        binding.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        binding.setMsg();
//        binding.executePendingBindings();
    }

    private void initAdapter() {
        list = new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        list.add("17");
        list.add("18");
        list.add("19");

        adapter = new StrAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvStr.setLayoutManager(manager);
//        binding.rvStr.setAdapter(adapter);

        mainBean.setStrAdapter(adapter);
//        binding.setAdapter(adapter);

//        adapter.setListener(new StrAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String bean) {
//
//            }
//        });
    }


    VModel vModel = new VModel();

    private void initUI() {
//        vModel.mainBean.msg = "测试测试测试测试";
//        binding.setVm(vModel);

        mainBean = new MainBean();
        mainBean.msg = "mainBean测试";
        binding.setMainBean(mainBean);

//        binding.setMsg("binding测试信息");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
