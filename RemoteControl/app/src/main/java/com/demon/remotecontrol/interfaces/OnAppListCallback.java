package com.demon.remotecontrol.interfaces;

import com.demon.remotecontrol.bean.AppInfoBean;

import java.util.List;

public interface OnAppListCallback {

    void onResult(List<AppInfoBean> list);
}
