package com.demon.accountmanagement.di.component;

import com.demon.accountmanagement.di.module.GroupDetailModule;
import com.demon.accountmanagement.di.scope.ActivityScope;
import com.demon.accountmanagement.view.GroupDetailActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {GroupDetailModule.class})
public interface GroupDetailComponent {

    void injectActivity(GroupDetailActivity activity);
}
