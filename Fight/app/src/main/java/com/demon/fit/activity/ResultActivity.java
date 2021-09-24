package com.demon.fit.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.fit.R;
import com.demon.fit.bean.ResultBean;
import com.demon.fit.control_ui.ResultUiControl;
import com.demon.fit.daos.DBControl;

import butterknife.ButterKnife;

/**
 * 操作结果记录
 */
public class ResultActivity extends AppCompatActivity {

    private ResultUiControl resultUiControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        resultUiControl = new ResultUiControl(this);
    }

    public void addResultBean(ResultBean bean) {
        DBControl.createOrUpdate(this, ResultBean.class, bean);
        showToast("数据添加成功");
        resultUiControl.refreshData();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLog(String msg) {
        Log.e("ResultActivity", msg);
    }
}
