package com.demon.accountmanagement.base;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected void initView() {
    }

    protected abstract void initInject();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
