// Generated by Dagger (https://google.github.io/dagger).
package com.demon.accountmanagement.base;

import javax.inject.Provider;

import dagger.MembersInjector;

public final class BaseMvpActivity_MembersInjector<T extends BasePresenter>
    implements MembersInjector<BaseMvpActivity<T>> {
  private final Provider<T> basePresenterProvider;

  public BaseMvpActivity_MembersInjector(Provider<T> basePresenterProvider) {
    this.basePresenterProvider = basePresenterProvider;
  }

  public static <T extends BasePresenter> MembersInjector<BaseMvpActivity<T>> create(
      Provider<T> basePresenterProvider) {
    return new BaseMvpActivity_MembersInjector<T>(basePresenterProvider);
  }

  @Override
  public void injectMembers(BaseMvpActivity<T> instance) {
    injectBasePresenter(instance, basePresenterProvider.get());
  }

  public static <T extends BasePresenter> void injectBasePresenter(
      BaseMvpActivity<T> instance, T basePresenter) {
    instance.basePresenter = basePresenter;
  }
}
