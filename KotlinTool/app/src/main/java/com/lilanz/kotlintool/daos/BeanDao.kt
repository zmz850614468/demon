package com.lilanz.kotlintool.daos

import android.annotation.SuppressLint
import android.content.Context
import android.util.ArrayMap
import com.j256.ormlite.dao.Dao
import java.sql.SQLException
import java.util.*
import kotlin.collections.HashMap

class BeanDao<T> {
    companion object {
        @SuppressLint("NewApi")
        private val daoOperateMap: ArrayMap<Class<*>, BeanDao<*>?> = ArrayMap()

        @Synchronized
        fun getDaoOperate(context: Context, clazz: Class<*>): BeanDao<*>? {
            if (!daoOperateMap.containsKey(clazz)) {
                val beanDao = BeanDao(context, clazz)
                daoOperateMap[clazz] = beanDao
            }
            return daoOperateMap[clazz]
        }
    }


    private var dao: Dao<T, Int>? = null

    constructor(context: Context, clazz: Class<T>) {
        dao = OrmLiteHelter.getInstance(context)?.getDao(clazz)
    }

    // 查询所有数据
    fun queryAll(): List<T>? {
        var beanList: List<T>? = null
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) { beanList = dao!!.queryForAll() }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return if (beanList == null) ArrayList() else beanList
    }

    // 插入一个数据对象
    fun insert(t: T) {
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) { dao!!.createIfNotExists(t) }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    // 删除数据对象
    fun delete(t: T) {
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) { dao!!.delete(t) }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    fun deleteById(id: Int) {
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) { dao!!.deleteById(id) }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    // 插入数据集合
    fun insertList(list: List<T>?) {
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) { dao!!.create(list) }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    // 更新数据
    fun update(t: T) {
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) { dao!!.createOrUpdate(t) }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    // 删除数据集合
    fun deleteList(list: List<T>?) {
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) { dao!!.delete(list) }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    // 按条件查询第一条数据
    fun queryWhereForOne(map: HashMap<String, Any>): T? {
        if (map == null || map.size == 0) {
            return null
        }
        var t: T? = null
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) {
                var where = dao!!.queryBuilder().where()
                var index = 0
                for ((key, value) in map) {
                    where = if (index == 0) {
                        where.eq(key, value)
                    } else {
                        where.and().eq(key, value)
                    }
                    index++
                }
                t = where.queryForFirst()
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return t
    }

    // 按条件查询
    fun queryWhereForList(map: Map<String?, Any?>): List<T>? {
        var list: List<T>? = null
        try {
            synchronized(OrmLiteHelter.DATA_LOCK) {
                var where = dao!!.queryBuilder().where()
                var index = 0
                for ((key, value) in map) {
                    where = if (index == 0) {
                        where.eq(key, value)
                    } else {
                        where.and().eq(key, value)
                    }
                    index++
                }
                list = where.query()
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return if (list == null) ArrayList() else list
    }
}