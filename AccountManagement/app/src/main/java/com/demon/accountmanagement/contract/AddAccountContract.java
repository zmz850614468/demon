package com.demon.accountmanagement.contract;

import android.content.Context;

import com.demon.accountmanagement.base.BasePresenter;
import com.demon.accountmanagement.base.BaseView;
import com.demon.accountmanagement.bean.AccountBean;

public interface AddAccountContract {

    interface AddAccountView extends BaseView {
        /**
         * 显示提示消息
         *
         * @param msg
         */
        void toastMsg(String msg);

        /**
         * 关闭界面
         */
        void close();
    }

    interface AddAccountPresenter extends BasePresenter<AddAccountView> {
        void setContext(Context context);

        /**
         * 添加新账号
         *
         * @param bean
         */
        void addAccount(AccountBean bean);
    }
}
