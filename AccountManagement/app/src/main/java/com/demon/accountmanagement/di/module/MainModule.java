package com.demon.accountmanagement.di.module;

import com.demon.accountmanagement.contract.MainContract;
import com.demon.accountmanagement.di.scope.ActivityScope;
import com.demon.accountmanagement.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class MainModule {

    @ActivityScope
    @Provides
    public MainContract.MainPresenter providePresenter() {
        return new MainPresenter();
    }

}
