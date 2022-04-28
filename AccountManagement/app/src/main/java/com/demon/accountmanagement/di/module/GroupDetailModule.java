package com.demon.accountmanagement.di.module;

import com.demon.accountmanagement.contract.GroupDetailContract;
import com.demon.accountmanagement.di.scope.ActivityScope;
import com.demon.accountmanagement.presenter.GroupDetailPresenter;

import dagger.Module;
import dagger.Provides;

@ActivityScope
@Module
public class GroupDetailModule {

    @ActivityScope
    @Provides
    public GroupDetailContract.GroupDetailPresenter providePresenter() {
        return new GroupDetailPresenter();
    }
}
