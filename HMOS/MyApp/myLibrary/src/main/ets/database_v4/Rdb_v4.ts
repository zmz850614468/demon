import relationalStore from '@ohos.data.relationalStore'
import { TableInfo } from './TableInfo';

/**
 * api-9 后发布的
 */
export class Rdb_v4<T> {
  static readonly STORE_CONFIG = {
    name: "myapp.db",
    securityLevel: relationalStore.SecurityLevel.S1
  }
  private rdbStore: relationalStore.RdbStore = null;
  private tableInfo: TableInfo = null // 表信息
  private classType: any = null // 类型

  constructor(context, classType: any, callback?: (err) => void) {
    this.classType = classType
    let list = Reflect.ownKeys(this.classType)
    let b = false
    list.forEach((item) => {
      if (this.classType[item] instanceof TableInfo) {
        if (Reflect.has(this.classType, item)) {
          this.tableInfo = Reflect.get(this.classType, item)
          b = true
        }
      }
    })

    if (!b) {
      throw new Error(this.classType + '类没有静态 TableInfo 属性')
    }

    this.getRdbStore(context, callback)
  }

  getRdbStore(context, callback?: (err) => void) {
    if (this.rdbStore !== null) {
        callback ? callback(null) : null
      return
    }
    relationalStore.getRdbStore(context, Rdb_v4.STORE_CONFIG, (err, rdb: relationalStore.RdbStore) => {
      if (rdb) {
        this.rdbStore = rdb
        this.rdbStore.executeSql(this.tableInfo.sqlCreateTable, null, (err) => {
          if (callback) {
            callback(err)
          }
        })
      } else if (callback) {
        callback(err)
      }
    });
  }

  /**
   * 插入数据
   * @param obj 表对象
   * @param callback -1：插入失败 >0:插入的rowId值
   */
  insertBean(obj: T, callback?: (err, rowId: number) => void) {
    const valueBucket = this.generateBucket(obj)
    this.rdbStore.insert(this.tableInfo.tableName, valueBucket, function (err, rowId) {
      if (callback) {
        callback(err, rowId)
      }
    });
  }

  /**
   * 插入 表对象组
   * @param objs 表对象组
   * @param callback
   */
  batchInsertData(objs: Array<any>, callback?: (err, rows: number) => void) {
    let bucketArray = []
    objs.forEach((obj) => {
      bucketArray.push(this.generateBucket(obj))
    })

    this.rdbStore.batchInsert(this.tableInfo.tableName, bucketArray, function (err, rows: number) {
      if (callback) {
        callback(err, rows)
      }
    });
  }

  /**
   * 删除数据
   * @param obj 表对象
   * @param callback >=0:删除了多少条数据
   */
  deleteById(id: number, callback?: (err, rows: number) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);
    predicates.equalTo('id', id);

    this.delete(predicates, callback)
  }

  /**
   * 删除指定条件的数据
   * @param predicates 条件约束
   * @param callback
   */
  delete(predicates: relationalStore.RdbPredicates, callback: (err, rows: number) => void) {
    this.rdbStore.delete(predicates, function (err, rows) {
      if (callback) {
        callback(err, rows)
      }
    });
  }

  /**
   * 更新数据
   * @param obj 表对象
   * @param callback >=0:更新了多少条数据
   */
  updateData(obj: T, callback?: (err, rows: number) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);
    predicates.equalTo('id', obj['id']);

    const valueBucket = this.generateBucket(obj)
    this.update(valueBucket, predicates, callback)
  }

  /**
   * 更新 批量数据
   * @param valueBucket 键值对对象，需要更新的数据
   * @param predicates 条件约束
   * @param callback
   */
  update(valueBucket: any, predicates: relationalStore.RdbPredicates, callback?: (err, rows: number) => void) {
    this.rdbStore.update(valueBucket, predicates, function (err, rows) {
      if (callback) {
        callback(err, rows)
      }
    });
  }

  /**
   * 查询数据
   * @param clazz 表类
   * @param callback 表对象数组
   */
  queryAll(callback: (err, list: Array<T>) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);
    this.query(predicates, callback)
  }

  /**
   * 指定条件查询数据
   * @param clazz 查询的数据类型
   * @param predicates 条件约束
   * @param callback
   */
  query(predicates: relationalStore.RdbPredicates, callback: (err, list: Array<T>) => void) {
    let classType = this.classType
    let columns = this.tableInfo.columns
    this.rdbStore.query(predicates, this.tableInfo.columns, function (err, resultSet: relationalStore.ResultSet) {
      if (!err) {
        let count = resultSet.rowCount;
        if (count === 0 || typeof count === 'string') {
          callback(null, []);
        } else {
          resultSet.goToFirstRow();
          const result = [];
          for (let i = 0; i < count; i++) {
            let tmp = new classType()
            columns.forEach((item) => {
              if (typeof item === 'string') {
                tmp[item] = resultSet.getString(resultSet.getColumnIndex(item))
              } else if (typeof item === 'bigint') {
                tmp[item] = resultSet.getLong(resultSet.getColumnIndex(item))
              } else if (typeof item === 'number') {
                tmp[item] = resultSet.getDouble(resultSet.getColumnIndex(item))
              }
            })
            result[i] = tmp;
            resultSet.goToNextRow();
          }
          callback(null, result);
        }
        resultSet.close();
      } else {
        callback(err, null)
      }
    });
  }

  /**
   * 获取 限制条件对象，就是 where 后添加的约束条件
   * @returns 条件约束
   */
  getPredicates(): relationalStore.RdbPredicates {
    return new relationalStore.RdbPredicates(this.tableInfo.tableName)
  }

  private generateBucket(account: T) {
    let obj = {};
    this.tableInfo.columns.forEach((item) => {
      if (item != 'id') {
        obj[item] = account[item];
      }
    });
    return obj;
  }

  getColumns(): Array<string> {
    return this.tableInfo.columns;
  }
}