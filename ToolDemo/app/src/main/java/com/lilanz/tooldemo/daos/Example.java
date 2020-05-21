package com.lilanz.tooldemo.daos;

import android.content.Context;

import com.lilanz.tooldemo.beans.StudentBean;

public class Example {

    public void insert(Context context, StudentBean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, StudentBean.class);
        dao.insert(bean);
    }

    public void delete(Context context, StudentBean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, StudentBean.class);
        dao.delete(bean);
    }
}
