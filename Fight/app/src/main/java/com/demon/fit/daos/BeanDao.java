package com.demon.fit.daos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanDao<T> {

    private Dao<T, Integer> dao;

    @SuppressLint("NewApi")
    private static ArrayMap<Class, BeanDao> daoOperateMap = new ArrayMap<>();

    public synchronized static BeanDao getDaoOperate(Context context, Class clazz) {
        if (!daoOperateMap.containsKey(clazz)) {
            BeanDao beanDao = new BeanDao(context, clazz);
            daoOperateMap.put(clazz, beanDao);
        }
        return daoOperateMap.get(clazz);
    }

    private BeanDao(Context context, Class<T> clazz) {
        dao = OrmLiteHelter.getInstance(context).getDao(clazz);
    }

    // 查询所有数据
    public List<T> queryAll() {
        List<T> beanList = null;
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                beanList = dao.queryForAll();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return beanList == null ? new ArrayList<T>() : beanList;
    }

    // 插入一个数据对象
    public void insert(T t) {
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.createIfNotExists(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 删除数据对象
    public void delete(T t) {
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.delete(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.deleteById(id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 插入数据集合
    public void insertList(List<T> list) {
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.create(list);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 更新数据
    public void update(T t) {
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.createOrUpdate(t);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 删除数据集合
    public void deleteList(List<T> list) {
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                dao.delete(list);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 按条件查询第一条数据
    public T queryWhereForOne(@NonNull Map<String, Object> map) {
        T t = null;
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                Where<T, Integer> where = dao.queryBuilder().where();
                int index = 0;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (index == 0) {
                        where = where.eq(entry.getKey(), entry.getValue());
                    } else {
                        where = where.and().eq(entry.getKey(), entry.getValue());
                    }
                    index++;
                }
                t = where.queryForFirst();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return t;
    }

    // 按条件查询
    public List<T> queryWhereForList(@NonNull Map<String, Object> map) {
        List<T> list = null;
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                Where<T, Integer> where = dao.queryBuilder().where();
                int index = 0;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (index == 0) {
                        where = where.eq(entry.getKey(), entry.getValue());
                    } else {
                        where = where.and().eq(entry.getKey(), entry.getValue());
                    }
                    index++;
                }
                list = where.query();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list == null ? new ArrayList<T>() : list;
    }

    /**
     *
     * @param columnName
     * @return
     */
    public List<T> getDistinctList(String columnName){
        List<T> list = null;
        try {
            synchronized (OrmLiteHelter.DATA_LOCK) {
                list = dao.queryBuilder().selectColumns(columnName).distinct().query();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list == null ? new ArrayList<T>() : list;
    }

}
