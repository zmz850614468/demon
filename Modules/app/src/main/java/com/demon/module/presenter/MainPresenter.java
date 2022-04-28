package com.demon.module.presenter;

import android.util.Log;

import com.demon.module.base.App;
import com.demon.module.contract.MainContract;
import com.demon.module.base.BaseMvpPresenter;
import com.demon.module.di.component.DaggerMainComponent;
import com.demon.module.model.HttpModel;

import javax.inject.Inject;

public class MainPresenter extends BaseMvpPresenter<MainContract.IView> implements MainContract.Presenter {

    @Inject
    HttpModel httpModel;

    public MainPresenter() {
        DaggerMainComponent.create().injectPresenter(this);
    }

    private void showLog(String msg) {
        Log.e("MainPresenter", msg);
    }

    @Override
    public void getStallInfo() {
        showLog("请求员工信息");
//        if (httpModel == null) {  // 获取单例模式对象
//            httpModel = App.getAppComponent().getHttpModel();
//        }

        httpModel.requestData("请求员工信息");
        baseView.updateList();
    }

    @Override
    public void getData() {
        showLog("查询数据库信息");
        baseView.updateList();
    }

}
