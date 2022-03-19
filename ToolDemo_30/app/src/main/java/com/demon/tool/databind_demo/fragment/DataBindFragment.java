package com.demon.tool.databind_demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demon.tool.R;
import com.demon.tool.databind_demo.adapter.DataTestAdapter;
import com.demon.tool.databind_demo.bean.DataTestBean;
import com.demon.tool.databind_demo.viewmodel.DataBindViewModel;
import com.demon.tool.databinding.FragmenDataBindBinding;

import java.util.ArrayList;
import java.util.List;

public class DataBindFragment extends Fragment {

    private FragmenDataBindBinding binding;
    private DataBindViewModel viewModel;

    private DataTestAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_data_bind, container, false);
        if (binding == null) {
            binding = FragmenDataBindBinding.bind(view);
        }

        initAdapter();
        binding.setVModel(viewModel);

        return binding.getRoot();
    }

    public void updateList(List<DataTestBean> list) {
        this.list.clear();
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private List<DataTestBean> list = new ArrayList<>();

    private void initAdapter() {
        list.add(new DataTestBean(4, "恶魔"));
        list.add(new DataTestBean(5, "恶魔-2"));
        list.add(new DataTestBean(6, "恶魔-3"));

        binding.rvDataTest.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DataTestAdapter(getContext(), list);
        binding.rvDataTest.setAdapter(adapter);
    }

    public void setViewModel(DataBindViewModel viewModel) {
        this.viewModel = viewModel;
    }
}






