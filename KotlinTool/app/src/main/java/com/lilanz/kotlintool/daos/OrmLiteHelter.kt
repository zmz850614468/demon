package com.lilanz.kotlintool.daos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.ArrayMap
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.lilanz.kotlintool.beans.Bean
import java.sql.SQLException

class OrmLiteHelter : OrmLiteSqliteOpenHelper {
    companion object {
        // 数据库名称
        private val DATABASE_NAME = "orm.db"
        private val VERSION = 1

        private var instance: OrmLiteHelter? = null

        var DATA_LOCK = Any() // 用于处理数据库的异步操作

        /**
         * 构造单例对象
         */
        fun getInstance(context: Context): OrmLiteHelter? {
            if (instance == null) {
                synchronized(DATA_LOCK) {
                    if (instance == null) {
                        instance = OrmLiteHelter(context)
                    }
                }
            }
            return instance
        }
    }

    private val daos = ArrayMap<String, Dao<*, *>>()

    constructor(context: Context) : super(context, DATABASE_NAME, null, VERSION) {
    }

    /**
     * 获取bean类的Dao对象
     */
    fun getDao_(clazz: Class<*>): Dao<*, *>? {
        var dao: Dao<*, *>? = null
        val className = clazz.simpleName
        if (daos.containsKey(className)) {
            dao = daos[className]
        }
        if (dao == null) {
            synchronized(DATA_LOCK) {
                if (dao == null) {
                    try {
                        dao = super.getDao(clazz)
                        daos[className] = dao
                    } catch (ex: SQLException) {
                        ex.printStackTrace()
                    }
                }
            }
        }
        return dao
    }

    /**
     * 创建表
     */
    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable<Bean>(connectionSource, Bean::class.java)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * 更新表
     */
    override fun onUpgrade(
        database: SQLiteDatabase,
        connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int
    ) {
        try {
            TableUtils.dropTable<Bean, Any>(
                connectionSource,
                Bean::class.java, true
            )
            onCreate(database, connectionSource)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}