package com.demon.module.di.component;

import com.demon.module.di.module.HttpModule;
import com.demon.module.model.HttpModel;
import com.demon.module.presenter.MainPresenter;
import com.demon.module.view.MainActivity;
import com.demon.module.di.module.MainModule;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainModule.class, HttpModule.class})
public interface MainComponent {

    void injectActivity(MainActivity activity);

//    HttpModel getHttpModel();

//    @Inject
//    MainPresenter getPresent(HttpModel httpModel);

//    void injectPresenter(MainPresenter presenter);
}
