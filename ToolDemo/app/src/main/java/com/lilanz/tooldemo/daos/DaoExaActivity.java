package com.lilanz.tooldemo.daos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.beans.BaseBean;
import com.lilanz.tooldemo.utils.NumberUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoExaActivity extends Activity {

    private TextView tvAll;
    private TextView tvSearch;

    private EditText etName;
    private EditText etAge;
    private EditText etIsBoy;
    private EditText etValue1;
    private EditText etValue2;

    private Button btInsert;
    private Button btSearch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao_test);

        initUI();
        initInsert();
        initSearch();

        searchAll();
    }


    private void initSearch() {
        etValue1 = findViewById(R.id.et_value_1);
        etValue2 = findViewById(R.id.et_value_2);
        btSearch = findViewById(R.id.bt_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value1 = etValue1.getText().toString();
                String value2 = etValue2.getText().toString();

                // 字符串查询
                Map<String, Object> map = new HashMap<>();
                if (!StringUtil.isEmpty(value1)) {
                    map.put("name", value1);
                }

                if (!StringUtil.isEmpty(value2)) {
                    Integer i = Integer.parseInt(value2);
                    if (i == 0) {
                        map.put("isboy", false);
                    } else {
                        map.put("isboy", true);
                    }
                }

                BeanDao dao = BeanDao.getDaoOperate(DaoExaActivity.this, BaseBean.class);
                BaseBean bean = (BaseBean) dao.queryWhereForOne(map);
                if (bean == null) {
                    tvSearch.setText("search null");
                } else {
                    tvSearch.setText(bean.toString());
                }

            }
        });
    }

    private void initInsert() {
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etIsBoy = findViewById(R.id.et_isboy);
        btInsert = findViewById(R.id.bt_insert);
        btInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseBean bean = new BaseBean();
                bean.name = etName.getText().toString();
                bean.age = 0;
                bean.isBoy = false;

                String ageStr = etAge.getText().toString();
                String isBoyStr = etIsBoy.getText().toString();
                if (!StringUtil.isEmpty(ageStr) && NumberUtil.isInt(ageStr)) {
                    bean.age = Integer.parseInt(ageStr);
                }
                if (!StringUtil.isEmpty(isBoyStr) && NumberUtil.isInt(isBoyStr)) {
                    if (Integer.parseInt(isBoyStr) != 0) {
                        bean.isBoy = true;
                    }
                }

                // 插入数据
                BeanDao dao = BeanDao.getDaoOperate(DaoExaActivity.this, BaseBean.class);
                dao.insert(bean);
                searchAll();
            }
        });
    }

    private void initUI() {
        tvAll = findViewById(R.id.tv_all);
        tvSearch = findViewById(R.id.tv_reault);
    }

    /**
     * 查询所有数据
     */
    private void searchAll() {
        tvAll.setText("");
        BeanDao dao = BeanDao.getDaoOperate(this, BaseBean.class);
        List<BaseBean> list = dao.queryAll();
        for (BaseBean bean : list) {
            tvAll.append(bean.toString() + "\n");
        }
    }
}
