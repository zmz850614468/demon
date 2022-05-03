package com.demon.fit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.fit.R;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.daos.DBControl;
import com.demon.fit.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewResultActivity extends AppCompatActivity {

    @BindView(R.id.et_result)
    public EditText etResult;
    @BindView(R.id.et_poundage)
    public EditText etPoundage;     // 手续费用
    @BindView(R.id.et_pos_operate_count)
    public EditText etPosOperateCount;
    @BindView(R.id.et_neg_operate_count)
    public EditText etNegOperateCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_result);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        String result = etResult.getText().toString();
        String poundage = etPoundage.getText().toString();
        String posCount = etPosOperateCount.getText().toString();
        String negCount = etNegOperateCount.getText().toString();

        int intResult = 0;
        int intPoundage = 0;
        int intPosCount = 0;
        int intNegCount = 0;

        if (!StringUtil.isEmpty(result)) {
            intResult = Integer.parseInt(result);
        }
        if (!StringUtil.isEmpty(poundage)) {
            intPoundage = Integer.parseInt(poundage);
        }
        if (!StringUtil.isEmpty(posCount)) {
            intPosCount = Integer.parseInt(posCount);
        }
        if (!StringUtil.isEmpty(negCount)) {
            intNegCount = Integer.parseInt(negCount);
        }

        OperateResultBean bean = new OperateResultBean();
        bean.timeStamp = System.currentTimeMillis();
        bean.result = intResult;
        bean.poundage = intPoundage;
        bean.posCount = intPosCount;
        bean.negCount = intNegCount;

        DBControl.createOrUpdate(this, OperateResultBean.class, bean);
        showToast("添加数据成功");
        finish();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
