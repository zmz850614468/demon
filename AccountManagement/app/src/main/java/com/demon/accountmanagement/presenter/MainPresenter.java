package com.demon.accountmanagement.presenter;

import android.content.Context;
import android.content.Intent;

import com.demon.accountmanagement.base.BaseMvpPresenter;
import com.demon.accountmanagement.bean.AccountBean;
import com.demon.accountmanagement.contract.MainContract;
import com.demon.accountmanagement.daos.DBControl;
import com.demon.accountmanagement.view.GroupDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPresenter extends BaseMvpPresenter<MainContract.MainView> implements MainContract.MainPresenter {

    private Context context;

    @Override
    public List<String> getAccountGroup() {
        Map<String, Object> map = new HashMap<>();
        map.put("is_just_group", true);
        List<AccountBean> beanList = DBControl.queryByColumn(context, AccountBean.class, map);

        List<String> list = new ArrayList<>();
        for (AccountBean bean : beanList) {
            list.add(bean.groupName);
        }
        return list;
    }

    @Override
    public void addNewGroup(String groupName) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_name", groupName);
        map.put("is_just_group", true);
        List<AccountBean> list = DBControl.queryByColumn(context, AccountBean.class, map);
        if (!list.isEmpty()) {  // 已经存在
            baseView.toastMsg("分组已存在");
        } else {
            AccountBean bean = new AccountBean();
            bean.groupName = groupName;
            bean.isJustGroup = true;
            DBControl.createOrUpdate(context, AccountBean.class, bean);
            baseView.toastMsg("添加新分组成功");

            baseView.updateAccountList(getAccountGroup());
        }
    }

    @Override
    public void deleteGroup(String groupName) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_name", groupName);
        map.put("is_just_group", false);
        List<AccountBean> list = DBControl.queryByColumn(context, AccountBean.class, map);
        if (!list.isEmpty()) {
            baseView.toastMsg("不能直接删除非空的分组");
        } else {
            map.remove("is_just_group");
            DBControl.deleteByColumn(context, AccountBean.class, map);

            baseView.toastMsg("删除分组成功");
            baseView.updateAccountList(getAccountGroup());
        }
    }

    @Override
    public void toGroupDetail(String groupName) {
        Intent intent = new Intent(context, GroupDetailActivity.class);
        intent.putExtra(GroupDetailActivity.GROUP_NAME, groupName);
        context.startActivity(intent);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
