package com.demon.module.di.module;

import com.demon.module.contract.MainContract;
import com.demon.module.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 提供注入依赖用的
 */
@Singleton
@Module
public class MainModule {

    @Singleton
    @Provides
    public MainContract.Presenter providePresenter() {
        return new MainPresenter();
    }
}
