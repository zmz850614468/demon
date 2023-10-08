package com.demon.fit.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.FuLiAdapter;
import com.demon.fit.bean.FuLiBean;
import com.demon.fit.util.SharePreferencesUtil;
import com.demon.fit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 复利计算界面
 */
public class CalculateActivity extends AppCompatActivity {

    @BindView(R.id.et_base)
    EditText etBase;
    @BindView(R.id.tv_fl)
    TextView tvFl;
    @BindView(R.id.tv_times)
    TextView tvTimes;
    @BindView(R.id.sb_fl)
    SeekBar sbFl;
    @BindView(R.id.sb_times)
    SeekBar sbTimes;
    @BindView(R.id.tv_result)
    TextView tvResult;

    @BindView(R.id.rv_fl)
    protected RecyclerView recycler;
    private FuLiAdapter fuLiAdapter;

    private List<FuLiBean> fuLiList;

    @BindView(R.id.rv_record)
    protected RecyclerView recordRecycler;
    private FuLiAdapter recordAdapter;

    private List<FuLiBean> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        ButterKnife.bind(this);

        initAdapter();
        initRecordAdapter();
        initSeekBar();
        initUI();
    }

    @OnClick(R.id.bt_add)
    public void onClicked(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("注意:")
                .setMessage("确定添加吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    FuLiBean bean = fuLiList.get(1);
                    bean.date = StringUtil.getMonthDay();
                    recordList.add(0, bean);
                    if (recordList.size() > 5) {
                        recordList.remove(recordList.size() - 1);
                    }
                    SharePreferencesUtil.saveFuLiRecode(this, recordList);
                    recordAdapter.notifyDataSetChanged();
                });
        builder.create().show();
    }

    private void calculate() {
        try {
            float base = Float.parseFloat(etBase.getText().toString());
            float fl = Float.parseFloat(tvFl.getText().toString());
            int times = Integer.parseInt(tvTimes.getText().toString());

            double result = base * Math.pow(fl, times);

            tvResult.setText(String.format("%.2f", result));

            fuLiList.clear();
            fl -= 0.01;
            if (fl < 1.01f) {
                fl = 1.01f;
            }

            FuLiBean bean;
            for (int i = 0; i < 3; i++) {
                bean = new FuLiBean();
                bean.base = base;
                bean.fuL = fl;
                bean.times = times;
                bean.result = base * Math.pow(fl, times);
                fl += 0.01;
                fuLiList.add(bean);
            }
            fuLiAdapter.notifyDataSetChanged();
        } catch (NumberFormatException e) {
            tvResult.setText("0");
            e.printStackTrace();
        }
    }

    private void initUI() {
        float base = SharePreferencesUtil.getFuLBase(this);
        etBase.setText(base + "");

        int fl = SharePreferencesUtil.getFuLi(this);
        sbFl.setProgress(fl);
        int times = SharePreferencesUtil.getFuLTimes(this);
        sbTimes.setProgress(times);
        calculate();
    }

    private void initSeekBar() {
        sbFl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvFl.setText(String.format("%.2f", (1 + progress * 2 / 100.0f)));
                calculate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbTimes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTimes.setText(progress + "");
                calculate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initAdapter() {
        fuLiList = new ArrayList<>();

        fuLiAdapter = new FuLiAdapter(this, fuLiList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(fuLiAdapter);

        fuLiAdapter.setListener(new FuLiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FuLiBean bean) {

            }
        });
    }

    private void initRecordAdapter() {
        recordList = SharePreferencesUtil.getFuLiRecode(this);
//        recordList = new ArrayList<>();

        recordAdapter = new FuLiAdapter(this, recordList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recordRecycler.setLayoutManager(manager);
        recordRecycler.setAdapter(recordAdapter);

        recordAdapter.setListener(bean -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CalculateActivity.this)
                    .setTitle("注意:")
                    .setMessage("确定删除吗?")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        recordList.remove(bean);
                        SharePreferencesUtil.saveFuLiRecode(CalculateActivity.this, recordList);
                        recordAdapter.notifyDataSetChanged();
                    });
            builder.create().show();
        });
    }

    @Override
    protected void onDestroy() {
        SharePreferencesUtil.saveFuLi(this, sbFl.getProgress());
        SharePreferencesUtil.saveFuLTimes(this, sbTimes.getProgress());
        try {
            float base = Float.parseFloat(etBase.getText().toString());
            SharePreferencesUtil.saveFuLBase(this, base);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
