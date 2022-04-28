package com.demon.module.di.component;

import com.demon.module.di.module.HttpModule;
import com.demon.module.presenter.MainPresenter;
import com.demon.module.view.MainActivity;
import com.demon.module.di.module.MainModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 联系inject和Module的桥梁
 */
@Singleton
@Component(modules = {MainModule.class, HttpModule.class})
public interface MainComponent {

    void injectActivity(MainActivity activity);

    void injectPresenter(MainPresenter presenter);
}

