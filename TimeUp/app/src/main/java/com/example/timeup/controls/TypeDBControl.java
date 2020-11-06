package com.example.timeup.controls;

import android.content.Context;

import com.example.timeup.beans.TypeBean;
import com.example.timeup.daos.BeanDao;

import java.util.List;

public class TypeDBControl {

    public static void createOrUpdate(Context context, TypeBean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, TypeBean.class);
        dao.update(bean);
    }

    public static void delete(Context context, TypeBean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, TypeBean.class);
        dao.delete(bean);
    }

    public static List<TypeBean> quaryAll(Context context) {
        BeanDao dao = BeanDao.getDaoOperate(context, TypeBean.class);
        return dao.queryAll();
    }

}
