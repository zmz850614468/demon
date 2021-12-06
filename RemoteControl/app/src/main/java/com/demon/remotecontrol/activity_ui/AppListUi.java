package com.demon.remotecontrol.activity_ui;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.activity.AppListActivity;
import com.demon.remotecontrol.adapter.AppListAdapter;
import com.demon.remotecontrol.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppListUi {

    private AppListActivity activity;

    @BindView(R.id.rv_app_list)
    protected RecyclerView appListRecycler;
    private AppListAdapter appListAdapter;

    private List<AppInfoBean> appInfoList;


    public AppListUi(AppListActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, this.activity);

        initAdapter();

//        appInfoList.addAll(AppUtil.getInstallApp(activity, false));
//        appListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_refresh)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_refresh:
                activity.queryAllApp();
                break;
        }
    }

    public void updateUI(List<AppInfoBean> list) {
        appInfoList.clear();
        appInfoList.addAll(list);
        appListAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        appInfoList = new ArrayList<>();

        appListAdapter = new AppListAdapter(activity, appInfoList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        appListRecycler.setLayoutManager(manager);
        appListRecycler.setAdapter(appListAdapter);

        appListAdapter.setListener(bean -> activity.showUninstallDialog(bean.packageName));
    }


}
