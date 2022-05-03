package com.demon.accountmanagement.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;

public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity {

    @Inject
    protected  T basePresenter;

    @Override
    protected void initView() {
        super.initView();
        initInject();
        if (basePresenter != null) {
            basePresenter.attachView(this);
        }
    }

    @Override
    protected abstract void initInject();

    @Override
    protected void onDestroy() {
        if (basePresenter != null) {
            basePresenter.detachView();
            basePresenter = null;
        }
        super.onDestroy();
    }
}
