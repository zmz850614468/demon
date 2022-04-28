package com.demon.accountmanagement.base;

/**
 * 不带mvp的presenter的基类
 *
 * @param <T>
 */
public interface BasePresenter<T extends BaseView> {

    void attachView(T baseView);

    void detachView();
}
