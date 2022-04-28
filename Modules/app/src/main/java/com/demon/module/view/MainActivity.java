package com.demon.module.view;

import android.os.Bundle;
import android.util.Log;

import com.demon.module.R;
import com.demon.module.contract.MainContract;
import com.demon.module.di.component.DaggerMainComponent;
import com.demon.module.base.BaseMvpActivity;
import com.demon.module.di.module.HttpModule;
import com.demon.module.di.module.MainModule;

public class MainActivity extends BaseMvpActivity<MainContract.Presenter> implements MainContract.IView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        basePresenter.getStallInfo();
        basePresenter.getData();
    }

    @Override
    protected void initInject() {
        // 1.不需要传参数
        DaggerMainComponent.create().injectActivity(this);

        // 2.需要传参数的可以使用第二种
//        DaggerMainComponent.builder()
//                .httpModule(new HttpModule())
//                .mainModule(new MainModule())
//                .build()
//                .injectActivity(this);
    }

    @Override
    public void updateList() {
        showLog("接收到 更新列表的信息");
    }

    @Override
    public void updateMsg() {
        showLog("接收到 更新数据信息");
    }

    private void showLog(String msg) {
        Log.e("MainActivity", msg);
    }
}