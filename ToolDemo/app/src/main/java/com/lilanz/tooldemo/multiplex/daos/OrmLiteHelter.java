package com.lilanz.tooldemo.multiplex.daos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;


import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lilanz.tooldemo.beans.Bean;

import java.sql.SQLException;

public class OrmLiteHelter extends OrmLiteSqliteOpenHelper {

    // 数据库名称
    private static final String DATABASE_NAME = "orm.db";
    private static final int VERSION = 1;

    private static OrmLiteHelter instance;

    public static Object DATA_LOCK = new Object();    // 用于处理数据库的异步操作

    @SuppressLint("NewApi")
    private ArrayMap<String, Dao> daos = new ArrayMap<>();

    private OrmLiteHelter(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static OrmLiteHelter getInstance(Context context) {
        if (instance == null) {
            synchronized (DATA_LOCK) {
                if (instance == null) {
                    instance = new OrmLiteHelter(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取bean的Dao对象
     *
     * @param clazz
     * @return
     */
    public Dao getDao(Class clazz) {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            synchronized (DATA_LOCK) {
                if (dao == null) {
                    try {
                        dao = super.getDao(clazz);
                        daos.put(className, dao);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return dao;
    }

    /**
     * 创建表
     *
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Bean.class);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 更新表
     *
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Bean.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
