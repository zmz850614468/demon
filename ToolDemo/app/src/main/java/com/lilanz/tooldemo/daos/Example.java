package com.lilanz.tooldemo.daos;

import android.content.Context;

import com.lilanz.tooldemo.beans.BaseBean;

public class Example {

    public void insert(Context context, BaseBean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, BaseBean.class);
        dao.insert(bean);
    }

    public void delete(Context context, BaseBean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, BaseBean.class);
        dao.delete(bean);
    }
}
