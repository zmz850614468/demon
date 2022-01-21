package com.demon.cameratest.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.demon.cameratest.adapter.StrAdapter;

public class MainBean extends BaseObservable {

    public String msg;

    public StrAdapter strAdapter;

    public void setMsg(String msg) {
        this.msg = msg;
        notifyPropertyChanged(BR.msg);
    }

    @Bindable
    public String getMsg() {
        return msg;
    }

    public void setStrAdapter(StrAdapter strAdapter) {
        this.strAdapter = strAdapter;
        notifyPropertyChanged(BR.strAdapter);
    }

    public void add(String msg) {
        this.strAdapter.add(msg);
        notifyPropertyChanged(BR.strAdapter);
    }

    @Bindable
    public StrAdapter getStrAdapter() {
        return strAdapter;
    }

    //    @BindingAdapter("msg")
//    public static void setMsg(TextView tv, String msg) {
//        tv.setText(msg);
//    }
}
