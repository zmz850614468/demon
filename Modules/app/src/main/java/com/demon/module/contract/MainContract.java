package com.demon.module.contract;

import com.demon.module.base.BasePresenter;
import com.demon.module.base.BaseView;

public interface MainContract {

    interface IView extends BaseView {

        void updateList();

        void updateMsg();
    }

    interface Presenter extends BasePresenter<IView> {

        void getStallInfo();

        void getData();
    }

}
