package com.demon.accountmanagement.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.demon.accountmanagement.R;
import com.demon.accountmanagement.base.BaseMvpActivity;
import com.demon.accountmanagement.bean.AccountBean;
import com.demon.accountmanagement.contract.AddAccountContract;
import com.demon.accountmanagement.databinding.ActivityAddAccountBinding;
import com.demon.accountmanagement.di.component.DaggerAddAccountComponent;
import com.demon.accountmanagement.presenter.GroupDetailPresenter;
import com.demon.accountmanagement.util.StringUtil;

/**
 * 添加新账号界面
 */
public class AddAccountActivity extends BaseMvpActivity<AddAccountContract.AddAccountPresenter> implements AddAccountContract.AddAccountView {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> pwd = new ObservableField<>();

    private ActivityAddAccountBinding binding;
    private String groupName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initView();
        initUI();
    }

    /**
     * 确定添加添加
     *
     * @param view
     */
    public void onDoneClicked(View view) {
        if (StringUtil.isEmpty(name.get())) {
            showToast("账号备注 不能为空");
            return;
        }
        if (StringUtil.isEmpty(account.get())) {
            showToast("账号名称 不能为空");
            return;
        }
        if (StringUtil.isEmpty(pwd.get())) {
            showToast("账号密码 不能为空");
            return;
        }

        AccountBean bean = new AccountBean();
        bean.name = name.get();
        bean.account = account.get();
        bean.pwd = pwd.get();
        bean.groupName = groupName;
        bean.isJustGroup = false;

        basePresenter.addAccount(bean);
    }

    @Override
    protected void initInject() {
        DaggerAddAccountComponent.create().injectActivity(this);
        basePresenter.setContext(this);
    }

    private void initUI() {
        groupName = getIntent().getStringExtra(GroupDetailActivity.GROUP_NAME);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_account);
        binding.setVariable(BR.activity, this);

        binding.toolbar.setTitle("添加新账号");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close() {
        Intent intent = new Intent();
        intent.setAction(GroupDetailPresenter.UPDATE_UI);
        sendBroadcast(intent);

        finish();
    }
}
