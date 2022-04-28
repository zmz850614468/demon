package com.demon.accountmanagement.view;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.demon.accountmanagement.R;
import com.demon.accountmanagement.adapter.AccountGroupAdapter;
import com.demon.accountmanagement.base.BaseMvpActivity;
import com.demon.accountmanagement.contract.MainContract;
import com.demon.accountmanagement.databinding.ActivityMainBinding;
import com.demon.accountmanagement.di.component.DaggerMainComponent;
import com.demon.accountmanagement.dialog.InputDialog;

import java.util.List;

/**
 * 显示分组界面
 */
public class MainActivity extends BaseMvpActivity<MainContract.MainPresenter> implements MainContract.MainView {

    private ActivityMainBinding binding;

    private List<String> accountGroupList;
    private AccountGroupAdapter accountGroupAdapter;

    private InputDialog inputDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initUI();
        initAdapter();
        initDialog();
    }

    /**
     * 添加新分组
     *
     * @param v
     */
    public void onAddClicked(View v) {
        showLog("添加新的分组");
        inputDialog.show();
    }

    @Override
    protected void initInject() {
        DaggerMainComponent.create().injectActivity(this);
        basePresenter.setContext(this);
    }

    private void initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVariable(BR.main, this);

//        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("账号管理");
    }

    private void initAdapter() {
        accountGroupList = basePresenter.getAccountGroup();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvAccountGroup.setLayoutManager(layoutManager);

        accountGroupAdapter = new AccountGroupAdapter(this, accountGroupList);
        binding.rvAccountGroup.setAdapter(accountGroupAdapter);

        accountGroupAdapter.setOnClickListener(new AccountGroupAdapter.OnClickListener() {
            @Override
            public void onClicked(String name) {
                // todo
                basePresenter.toGroupDetail(name);
            }

            @Override
            public void onLongClicked(String name) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("注意:")
                        .setMessage("确定删除分组：" + name)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                basePresenter.deleteGroup(name);
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void initDialog() {
        inputDialog = new InputDialog(this, R.style.DialogStyleOne);
        inputDialog.setListener(str -> basePresenter.addNewGroup(str));
        inputDialog.show();
        inputDialog.dismiss();
    }

    @Override
    public void updateAccountList(List<String> list) {
        accountGroupList.clear();
        accountGroupList.addAll(list);
        accountGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("MainActivity", msg);
    }
}
