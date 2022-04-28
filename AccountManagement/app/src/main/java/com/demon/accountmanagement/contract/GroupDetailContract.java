package com.demon.accountmanagement.contract;

import android.content.Context;

import com.demon.accountmanagement.base.BasePresenter;
import com.demon.accountmanagement.base.BaseView;
import com.demon.accountmanagement.bean.AccountBean;

import java.util.List;

public interface GroupDetailContract {

    interface GroupDetailView extends BaseView {

        /**
         * 更新界面信息
         *
         * @param list
         */
        void updateAccount(List<AccountBean> list);

        void toastMsg(String msg);
    }

    interface GroupDetailPresenter extends BasePresenter<GroupDetailView> {
        void setContext(Context context);

        /**
         * 添加新的分组账号信息
         */
        void addGroupDetail(String groupName);

        /**
         * 获取同组下的所有账号
         *
         * @param groupName
         * @return
         */
        List<AccountBean> getAccountList(String groupName);

        /**
         * 删除账号
         *
         * @param bean
         */
        void deleteAccount(AccountBean bean);

    }
}
