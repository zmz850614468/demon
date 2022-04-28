package com.demon.accountmanagement.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demon.accountmanagement.R;
import com.demon.accountmanagement.adapter.AccountAdapter;
import com.demon.accountmanagement.base.BaseMvpActivity;
import com.demon.accountmanagement.bean.AccountBean;
import com.demon.accountmanagement.contract.GroupDetailContract;
import com.demon.accountmanagement.databinding.ActivityGroupDetailBinding;
import com.demon.accountmanagement.di.component.DaggerGroupDetailComponent;
import com.demon.accountmanagement.dialog.InputDialog;

import java.util.List;

/**
 * 分组详情页面
 */
public class GroupDetailActivity extends BaseMvpActivity<GroupDetailContract.GroupDetailPresenter> implements GroupDetailContract.GroupDetailView {

    public static final String GROUP_NAME = "groupName";

    private ActivityGroupDetailBinding binding;
    private String groupName;

    private List<AccountBean> accountList;
    private AccountAdapter accountAdapter;

    private InputDialog inputDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        initView();
        initUI();
        initAdapter();
        initDialog();
    }

    /**
     * 添加账号详情按钮
     *
     * @param view
     */
    public void onAddClicked(View view) {
        basePresenter.addGroupDetail(groupName);
    }

    @Override
    protected void initInject() {
        DaggerGroupDetailComponent.create().injectActivity(this);
        basePresenter.setContext(this);
    }

    private void initUI() {
        groupName = getIntent().getStringExtra(GROUP_NAME);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_detail);
        binding.setVariable(BR.activity, this);

        binding.toolbar.setTitle(groupName);
        setSupportActionBar(binding.toolbar);   // 支持使用右侧按钮

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_show_pwd) {
                inputDialog.show();
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initAdapter() {
        accountList = basePresenter.getAccountList(groupName);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvAccount.setLayoutManager(manager);

        accountAdapter = new AccountAdapter(this, accountList);
        binding.rvAccount.setAdapter(accountAdapter);

        accountAdapter.setOnLongClicked(bean -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this)
                    .setTitle("注意:")
                    .setMessage("确定删除账号：" + bean.name)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> basePresenter.deleteAccount(bean));
            builder.create().show();
        });
    }

    private void initDialog() {
        inputDialog = new InputDialog(this, R.style.DialogStyleOne);
        inputDialog.setListener(str -> {
            if ("882356".equals(str)) {
                accountAdapter.setPwdShow(true);
                accountAdapter.notifyDataSetChanged();
            } else {
                toastMsg("密码错误");
            }
        });
        inputDialog.show();
        inputDialog.dismiss();
        inputDialog.updateHin("密码");
    }

    private void showLog(String msg) {
        Log.e("GroupDetailActivity", msg);
    }

    @Override
    public void updateAccount(List<AccountBean> list) {
        accountList.clear();
        accountList.addAll(list);
        accountAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
