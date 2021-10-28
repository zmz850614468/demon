package com.demon.fit.control_ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.activity.ResultActivity;
import com.demon.fit.adapter.AnalyseAdapter;
import com.demon.fit.adapter.AnalyseV2Adapter;
import com.demon.fit.adapter.ResultAdapter;
import com.demon.fit.bean.AnalyseBean;
import com.demon.fit.bean.ResultBean;
import com.demon.fit.daos.DBControl;
import com.demon.fit.util.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultUiControl {

    @BindView(R.id.layout_detail)
    ViewGroup layoutDetail;
    @BindView(R.id.tv_type_input)
    TextView tvType;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_is_right_input)
    TextView tvIsRight;
    @BindView(R.id.et_result)
    EditText etResult;

    @BindView(R.id.rv_result)
    protected RecyclerView resultRecycler;
    private ResultAdapter resultAdapter;
    private List<ResultBean> resultList;

    @BindView(R.id.rv_analyse)
    protected RecyclerView analyseRecycler;
    //    private AnalyseAdapter analyseAdapter;
    private AnalyseV2Adapter analyseAdapter;
    private List<AnalyseBean> analyseList;

    private boolean isShowDetail;

    private ResultActivity activity;

    public ResultUiControl(ResultActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
        initResultAdapter();
        initAnalyseAdapter();
    }

    /**
     * 添加新数据
     *
     * @param v
     */
    @OnClick(R.id.bt_add)
    public void onAddClicked(View v) {
        String name = etName.getText().toString();
        String result = etResult.getText().toString();
        String type = tvType.getText().toString();
        boolean isIn = "入手".equals(type);
        if (!StringUtil.isEmpty(name) && (!StringUtil.isEmpty(result) || isIn)) {
            ResultBean bean = new ResultBean();
            bean.name = name;
            if (isIn) {
                bean.result = 0;
            } else {
                bean.result = Integer.parseInt(result);
            }
            bean.type = type;
            bean.isRight = "好".equals(tvIsRight.getText().toString());
            bean.timeStamp = System.currentTimeMillis();
            activity.addResultBean(bean);
            etName.setText("");
            etResult.setText("");
        } else {
            activity.showToast("名称和结果不能为空！");
        }
    }

    /**
     * 详情界面
     *
     * @param v
     */
    @OnClick(R.id.bt_detail)
    public void onDetailClicked(View v) {
        isShowDetail = !isShowDetail;
        if (isShowDetail) {
//            resultList.clear();
//            resultList.addAll(DBControl.quaryAll(activity, ResultBean.class));
            layoutDetail.setVisibility(View.VISIBLE);
        } else {
            layoutDetail.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_type_input, R.id.tv_is_right_input})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_type_input:
                if ("入手".equals(getText(v))) {
                    setText(v, "出手");
                } else {
                    setText(v, "入手");
                }
                break;
            case R.id.tv_is_right_input:
                if ("好".equals(getText(v))) {
                    setText(v, "坏");
                } else {
                    setText(v, "好");
                }
                break;
        }
    }

    public void refreshData() {
        resultList.clear();
        resultList.addAll(DBControl.quaryAll(activity, ResultBean.class));
        resultAdapter.notifyDataSetChanged();

        analyseList.clear();
        initAnalyseData();
        analyseAdapter.notifyDataSetChanged();
    }

    private void initAnalyseAdapter() {
        analyseList = new ArrayList<>();
        initAnalyseData();

        analyseAdapter = new AnalyseV2Adapter(activity, analyseList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        analyseRecycler.setLayoutManager(manager);
        analyseRecycler.setAdapter(analyseAdapter);

//        analyseAdapter.setListener(new AnalyseAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(AnalyseBean bean) {
//
//            }
//        });
    }

    private void initAnalyseData() {
        AnalyseBean analyseBean = null;
        Calendar calendar = Calendar.getInstance();
        for (ResultBean resultBean : resultList) {
            calendar.setTimeInMillis(resultBean.timeStamp);
            if (analyseBean == null || analyseBean.time != calendar.get(Calendar.MONTH)) {
                analyseBean = new AnalyseBean();
                analyseBean.time = calendar.get(Calendar.MONTH);
                analyseList.add(0, analyseBean);
            }

            analyseBean.operateTimes++;
            if (resultBean.isRight) {
                analyseBean.goodTimes++;
                analyseBean.goodResult += resultBean.result;
            } else {
                analyseBean.badTimes++;
                analyseBean.badResult += resultBean.result;
            }

            if ("出手".equals(resultBean.type)) {
                analyseBean.resultTime++;
                if (resultBean.result > 0) {
                    analyseBean.posTimes++;
                    analyseBean.posResult += resultBean.result;
                } else if (resultBean.result < 0) {
                    analyseBean.negTimes++;
                    analyseBean.negResult += resultBean.result;
                }
            }

            analyseBean.totalResult += resultBean.result;
        }
    }

    private void initResultAdapter() {
//        resultList = new ArrayList<>();
        resultList = DBControl.quaryAll(activity, ResultBean.class);

        resultAdapter = new ResultAdapter(activity, resultList);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        resultRecycler.setLayoutManager(manager);
        resultRecycler.setAdapter(resultAdapter);

        resultAdapter.setListener(new ResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ResultBean bean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("数据删除");
                builder.setMessage("注意：请确认是否删除数据!");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBControl.delete(activity, ResultBean.class, bean);
                        refreshData();
//                        resultList.clear();
//                        resultList.addAll(DBControl.quaryAll(activity, ResultBean.class));
//                        resultAdapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
            }
        });
    }

    /**
     * 获取视图内容
     *
     * @param v
     * @return
     */
    public String getText(View v) {
        if (v instanceof TextView) {
            return ((TextView) v).getText().toString();
        }
        return "";
    }

    public void setText(View v, String text) {
        if (v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }
}
