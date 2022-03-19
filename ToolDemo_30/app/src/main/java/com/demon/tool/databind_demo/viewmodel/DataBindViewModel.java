package com.demon.tool.databind_demo.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableMap;

import com.demon.tool.databind_demo.bean.DataTestBean;
import com.demon.tool.databind_demo.fragment.DataBindFragment;
import com.demon.tool.databinding.FragmenDataBindBinding;

import java.util.ArrayList;
import java.util.List;

public class DataBindViewModel {


    public ObservableInt intType = new ObservableInt(10);

    public ObservableField<String> strType = new ObservableField<>("str type");

    public ObservableField<String> edtType = new ObservableField<>("edt type");

    public ObservableBoolean boolType = new ObservableBoolean(true);

    public ObservableArrayList<String> listType = new ObservableArrayList<>();

    public ObservableMap<String, String> mapType = new ObservableArrayMap<>();

    private DataBindFragment fragment;

    public DataBindViewModel() {
        listType.add("list item1");
        listType.add("list item2");

        mapType.put("key-1", "value-1");
        mapType.put("key-2", "value-2");
    }

//    public String getIntType() {
//        return intType.get() + "";
//    }

    public ObservableInt getIntType() {
        return intType;
    }

    public void onClicked(View v) {
        showLog(edtType.get());

        // 更新数据
        intType.set(100);   // todo 数值修改不会对应改变
        strType.set("change str type");
        listType.add(0, "change list item1");
        edtType.set("change edt type");

        List<DataTestBean> list = new ArrayList<>();
        list.add(new DataTestBean(14, "change - 恶魔"));
        list.add(new DataTestBean(15, "change - 恶魔-2"));
        list.add(new DataTestBean(16, "change - 恶魔-3"));

        fragment.updateList(list);
    }

    private void showLog(String msg) {
        Log.e("DataBindViewModel", msg);
    }

    public void setFragment(DataBindFragment fragment) {
        this.fragment = fragment;
    }
}
