package com.demon.module.di.module;

import com.demon.module.model.HttpModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Singleton
@Module
public class HttpModule {

    @Singleton
    @Provides
    public HttpModel provideHttpModel() {
        return new HttpModel();
    }
}
