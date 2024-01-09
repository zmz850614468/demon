// @ts-nocheck
// 注意目录，目录可以自定义
// import { TestBean } from '../bean_serverless/TestBean';
import cloud from '@hw-agconnect/cloud'
import HashMap from '@ohos.util.HashMap';
import { DatabaseCollection, DatabaseZoneQuery } from '@hw-agconnect/cloud/src/main/ets/database/DatabaseCollection';

import schema from '../app-schema.json';

/**
 * serverless 云数据库管理类
 */
export class DbManage {
  static Default_DbName: string = 'TestDB' // todo 默认的存储空间

  // key: 存储空间名
  private static dbManageMap: HashMap<string, DbManage> = new HashMap()

  // key: 对象名
  // value: 对象的 操作类
  private collectionMap: HashMap<string, DatabaseCollection<any>> = new HashMap()
  private dbName: string

  private constructor(dbName: string) {
    this.dbName = dbName
  }

  /**
   * 判定是否有对应 存储空间 的管理类
   * @param dbName
   * @returns
   */
  private static hasDbManager(dbName: string) {
    return DbManage.dbManageMap.hasKey(dbName)
  }

  /**
   * 获取对应 存储空间 的管理类，没有则初始化一个
   * @param dbName 没参数，则是默认 存储空间
   */
  static getDbManagerIfNotCreate(dbName: string = DbManage.Default_DbName): DbManage {
    if (!DbManage.hasDbManager(dbName)) {
      DbManage.dbManageMap.set(dbName, new DbManage(dbName))
    }

    return DbManage.dbManageMap.get(dbName)
  }

  /**
   * 获取对应 对象 数据库操作类
   * @param className
   * @returns
   */
  private getCollectionIfNotCreate(clazz: any): DatabaseCollection<any> {
    if (!this.collectionMap.hasKey(clazz.className)) {
      let collection = cloud.database({ objectTypeInfo: schema, zoneName: this.dbName }).collection(clazz)
      this.collectionMap.set(clazz.className, collection)
      return collection
    }

    return this.collectionMap.get(clazz.className)
  }

  // /**
  //  * 异步 - 插入或更新对象
  //  * @param clazz
  //  * @param objList
  //  * @returns
  //  */
  // async insertOrUpdate(clazz, objList: [] | object): Promise<number> {
  //   return this.getCollectionIfNotCreate(clazz).upsert(objList)
  // }

  /**
   * 异步 - 插入或更新对象
   * @param clazz
   * @param objList
   * @returns
   */
  async insertOrUpdate(clazz, objList: [] | object, callback?: (err, number) => void) {
    try {
      let num = await this.getCollectionIfNotCreate(clazz).upsert(objList)
      if (callback) {
        callback(null, num)
      }
    } catch (err) {
      if (callback) {
        callback(err, null)
      }
    }
  }

  // /**
  //  * 异步 - 删除数据
  //  * @param clazz
  //  * @param objList
  //  * @returns
  //  */
  // async delete(clazz, objList: [] | object): Promise<number> {
  //   return this.getCollectionIfNotCreate(clazz).delete(objList)
  // }

  /**
   * 异步 - 删除数据
   * @param clazz
   * @param objList
   * @returns
   */
  async delete(clazz, objList: [] | object, callback?: (err, number) => void) {
    try {
      let num = await this.getCollectionIfNotCreate(clazz).delete(objList)
      if (callback) {
        callback(null, num)
      }
    } catch (err) {
      if (callback) {
        callback(err, null)
      }
    }
  }

  // /**
  //  * 查询所有数据
  //  * @param clazz
  //  * @param objList
  //  * @returns
  //  */
  // async queryAll(clazz): Promise<any[]> {
  //   return this.getCollectionIfNotCreate(clazz).query().get()
  // }

  /**
   * 查询所有数据
   * @param clazz
   * @param objList
   * @returns
   */
  async queryAll(clazz, callback: (err, []) => void) {
    try {
      let list = await this.getCollectionIfNotCreate(clazz).query().get()
      callback(null, list)
    } catch (err) {
      callback(err, null)
    }
  }

  /**
   * 获取查询条件
   * @param clazz
   * @returns
   */
  getQuery(clazz): DatabaseZoneQuery<any> {
    return this.getCollectionIfNotCreate(clazz).query()
  }

  // /**
  //  * 按照查询条件获取数据
  //  * @param dbQuery
  //  * @returns
  //  */
  // async query(dbQuery: DatabaseZoneQuery<any>) {
  //   return dbQuery.get()
  // }

  /**
   * 按照查询条件获取数据
   * @param dbQuery
   * @returns
   */
  async query(dbQuery: DatabaseZoneQuery<any>, callback: (err, []) => void) {
    try {
      let list = await dbQuery.get()
      callback(null, list)
    } catch (err) {
      callback(err, null)
    }
  }
}