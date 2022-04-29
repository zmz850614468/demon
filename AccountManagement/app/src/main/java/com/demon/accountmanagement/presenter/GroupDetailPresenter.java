package com.demon.accountmanagement.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArrayMap;

import com.demon.accountmanagement.base.BaseMvpPresenter;
import com.demon.accountmanagement.bean.AccountBean;
import com.demon.accountmanagement.contract.GroupDetailContract;
import com.demon.accountmanagement.daos.DBControl;
import com.demon.accountmanagement.view.AddAccountActivity;
import com.demon.accountmanagement.view.GroupDetailActivity;

import java.util.List;
import java.util.Map;

public class GroupDetailPresenter extends BaseMvpPresenter<GroupDetailContract.GroupDetailView> implements GroupDetailContract.GroupDetailPresenter {

    public static final String UPDATE_UI = "updateUI";
    private Context context;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UPDATE_UI.equals(intent.getAction())) {
                baseView.updateUI();
            }
        }
    };

    @Override
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_UI);
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void unRegisterReceiver() {
        context.unregisterReceiver(receiver);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void addGroupDetail(String groupName) {
        Intent intent = new Intent(context, AddAccountActivity.class);
        intent.putExtra(GroupDetailActivity.GROUP_NAME, groupName);
        context.startActivity(intent);
    }

    @Override
    public List<AccountBean> getAccountList(String groupName) {
        Map<String, Object> map = new ArrayMap<>();
        map.put("group_name", groupName);
        map.put("is_just_group", false);

        List<AccountBean> list = DBControl.queryByColumn(context, AccountBean.class, map);
        return list;
    }

    @Override
    public void deleteAccount(AccountBean bean) {
        DBControl.delete(context, AccountBean.class, bean);
        baseView.toastMsg("删除账号成功");
        baseView.updateAccount(getAccountList(bean.groupName));
    }

}
