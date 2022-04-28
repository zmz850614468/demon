package com.demon.accountmanagement.contract;

import android.content.Context;

import com.demon.accountmanagement.base.BasePresenter;
import com.demon.accountmanagement.base.BaseView;
import com.demon.accountmanagement.bean.AccountBean;

import java.util.List;

public interface MainContract {

    interface MainView extends BaseView {

        /**
         * 更新所有分组信息
         *
         * @param list
         */
        void updateAccountList(List<String> list);

        /**
         * 显示提示信息
         *
         * @param msg
         */
        void toastMsg(String msg);
    }


    interface MainPresenter extends BasePresenter<MainView> {

        void setContext(Context context);

        /**
         * 获取所有分组信息
         *
         * @return
         */
        List<String> getAccountGroup();

        /**
         * 添加新分组
         *
         * @param groupName
         */
        void addNewGroup(String groupName);

        /**
         * 删除分组
         *
         * @param groupName
         */
        void deleteGroup(String groupName);

        /**
         * 跳转到分组详情页面
         *
         * @param groupName
         */
        void toGroupDetail(String groupName);
    }

}
