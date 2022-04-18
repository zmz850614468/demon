package com.demon.module.di.module;

import com.demon.module.contract.MainContract;
import com.demon.module.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class MainModule {

    @Singleton
    @Provides
    public MainContract.Presenter providePresenter() {
        return new MainPresenter();
    }
}
