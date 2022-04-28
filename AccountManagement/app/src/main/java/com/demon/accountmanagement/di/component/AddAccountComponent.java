package com.demon.accountmanagement.di.component;

import com.demon.accountmanagement.di.module.AddAccountModule;
import com.demon.accountmanagement.di.scope.ActivityScope;
import com.demon.accountmanagement.view.AddAccountActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {AddAccountModule.class})
public interface AddAccountComponent {

    void injectActivity(AddAccountActivity activity);

}
