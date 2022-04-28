package com.demon.accountmanagement.presenter;

import android.content.Context;

import com.demon.accountmanagement.base.BaseMvpPresenter;
import com.demon.accountmanagement.bean.AccountBean;
import com.demon.accountmanagement.contract.AddAccountContract;
import com.demon.accountmanagement.daos.DBControl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAccountPresenter extends BaseMvpPresenter<AddAccountContract.AddAccountView> implements AddAccountContract.AddAccountPresenter {

    private Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void addAccount(AccountBean bean) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_name", bean.groupName);
        map.put("name", bean.name);
        List<AccountBean> list = DBControl.queryByColumn(context, AccountBean.class, map);
        if (!list.isEmpty()) {
            baseView.toastMsg("已存在的 账号备注");
        } else {
            DBControl.createOrUpdate(context, AccountBean.class, bean);
            baseView.toastMsg("添加账号备注成功");
            baseView.close();
        }
    }
}
