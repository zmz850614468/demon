package com.demon.module.base;

/**
 * 带mvp的presenter的基类
 * @param <T>
 */
public class BaseMvpPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T baseView;

    @Override
    public void attachView(T baseView) {
        this.baseView = baseView;
    }

    @Override
    public void detachView() {
        this.baseView = null;
    }
}
