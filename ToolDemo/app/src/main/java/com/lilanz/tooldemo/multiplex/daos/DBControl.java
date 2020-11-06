package com.lilanz.tooldemo.multiplex.daos;

import android.content.Context;

import com.lilanz.tooldemo.beans.Bean;

import java.util.List;

public class DBControl {

    public static void createOrUpdate(Context context, Bean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, Bean.class);
        dao.update(bean);
    }

    public static void delete(Context context, Bean bean) {
        BeanDao dao = BeanDao.getDaoOperate(context, Bean.class);
        dao.delete(bean);
    }

    public static List<Bean> quaryAll(Context context) {
        BeanDao dao = BeanDao.getDaoOperate(context, Bean.class);
        return dao.queryAll();
    }
}
