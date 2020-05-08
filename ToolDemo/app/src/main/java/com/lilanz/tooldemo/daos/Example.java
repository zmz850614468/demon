package com.lilanz.tooldemo.daos;

import android.content.Context;

import com.lilanz.tooldemo.beans.FlawBean;

public class Example {

    public void insert(Context context, FlawBean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, FlawBean.class);
        dao.insert(bean);
    }

    public void delete(Context context, FlawBean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, FlawBean.class);
        dao.delete(bean);
    }
}
