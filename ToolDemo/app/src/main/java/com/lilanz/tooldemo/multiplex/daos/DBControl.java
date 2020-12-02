package com.lilanz.tooldemo.multiplex.daos;

import android.content.Context;

import java.util.List;
import java.util.Map;

public class DBControl {

    public static List getDistinct(Context context, Class clazz, String columnName) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        return dao.getDistinctList(columnName);
    }

    public static void createOrUpdate(Context context, Class clazz, Object obj) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        dao.update(obj);
    }

    public static List quaryByColumn(Context context, Class clazz, Map<String, Object> map) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        return dao.queryWhereForList(map);
    }

    public static List quaryAll(Context context, Class clazz) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        return dao.queryAll();
    }


    public static void delete(Context context, Class clazz, Object obj) {
        BeanDao dao = BeanDao.getDaoOperate(context, clazz);
        dao.delete(obj);
    }
}
