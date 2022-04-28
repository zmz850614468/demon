package com.demon.accountmanagement.di.component;

import com.demon.accountmanagement.di.module.MainModule;
import com.demon.accountmanagement.di.scope.ActivityScope;
import com.demon.accountmanagement.view.MainActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {MainModule.class})
public interface MainComponent {

    void injectActivity(MainActivity activity);
}
