package com.demon.accountmanagement.di.module;

import com.demon.accountmanagement.contract.AddAccountContract;
import com.demon.accountmanagement.di.scope.ActivityScope;
import com.demon.accountmanagement.presenter.AddAccountPresenter;

import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class AddAccountModule {

    @ActivityScope
    @Provides
    public AddAccountContract.AddAccountPresenter providePresenter() {
        return new AddAccountPresenter();
    }
}
