package com.lilanz.tooldemo.daos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.ArrayMap;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class BeanDao<T> {

    private Dao<T, Integer> dao;

    @SuppressLint("NewApi")
    private static ArrayMap<Class, BeanDao> daoOperateMap = new ArrayMap<>();

    public synchronized static BeanDao getDaoOperate(Context context, Class clazz){
        if (!daoOperateMap.containsKey(clazz)){
            BeanDao beanDao = new BeanDao(context, clazz);
            daoOperateMap.put(clazz, beanDao);
        }
        return daoOperateMap.get(clazz);
    }

    private BeanDao(Context context, Class<T> clazz){
        dao = OrmLiteHelter.getInstance(context).getDao(clazz);
    }

    // 查询所有数据
    public List<T> queryAll(){
        List<T> beanList = null;
        try {
            synchronized (OrmLiteHelter.DATA_LOCK){
                beanList = dao.queryForAll();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return beanList;
    }

    // 插入一个数据对象
    public void insert(T t){
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.createIfNotExists(t);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    // 删除数据对象
    public void delete(T t){
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.delete(t);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void deleteById(int id){
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.deleteById(id);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    // 插入数据集合
    public void insertList(List<T> list){
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.create(list);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    // 更新数据
    public void update(T t){
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.createOrUpdate(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 删除数据集合
    public void deleteList(List<T> list){
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.delete(list);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
