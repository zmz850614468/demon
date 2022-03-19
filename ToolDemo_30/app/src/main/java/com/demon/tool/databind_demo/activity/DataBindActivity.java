package com.demon.tool.databind_demo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.R;
import com.demon.tool.databind_demo.fragment.DataBindFragment;
import com.demon.tool.databind_demo.util.ActivityUtils;
import com.demon.tool.databind_demo.viewmodel.DataBindViewModel;

public class DataBindActivity extends AppCompatActivity {

    private DataBindViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_bind);

        initUI();
    }

    private void initUI() {
        viewModel = new DataBindViewModel();

        DataBindFragment dataBindFragment = new DataBindFragment();
        dataBindFragment.setViewModel(viewModel);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), dataBindFragment, R.id.layout_content);

        viewModel.setFragment(dataBindFragment);
    }
}
