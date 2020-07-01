package com.lilanz.tooldemo.daos;

import android.content.Context;

import com.lilanz.tooldemo.beans.Bean;

public class Example {

    public void insert(Context context, Bean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, Bean.class);
        dao.insert(bean);
    }

    public void delete(Context context, Bean bean){
        BeanDao dao = BeanDao.getDaoOperate(context, Bean.class);
        dao.delete(bean);
    }
}
