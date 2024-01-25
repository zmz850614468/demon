import relationalStore from '@ohos.data.relationalStore'
import TestBean from '../database_v3/TestBean';
import { TableInfo } from './TableInfo';

/**
 * api-9 后发布的
 */
export class Rdb_v4<T> {
  static readonly STORE_CONFIG = {
    name: "demon.db",
    securityLevel: relationalStore.SecurityLevel.S1
  }
  private rdbStore: relationalStore.RdbStore = null;
  private tableInfo: TableInfo = null // 表信息
  private classType: Object = null // 类型

  // constructor(tableInfo: TableInfo, callback?: (boolean) => void) {
  //   this.tableInfo = tableInfo
  //   this.getRdbStore(callback)
  // }

  constructor(context, classType: object, callback?: (boolean) => void) {
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

  getRdbStore(context, callback?: (boolean) => void) {
    if (this.rdbStore !== null) {
        callback ? callback(true) : null
      return
    }
    relationalStore.getRdbStore(context, Rdb_v4.STORE_CONFIG, (err, rdb: relationalStore.RdbStore) => {
      if (err) {
          callback ? callback(false) : null
      } else {
        this.rdbStore = rdb
        this.rdbStore.executeSql(this.tableInfo.sqlCreateTable)
          callback ? callback(true) : null
      }
    });
  }

  /**
   * 插入数据
   * @param obj 表对象
   * @param callback -1：插入失败 >0:插入的rowId值
   */
  insertData(obj: T, callback?: (number) => void) {
    const valueBucket = this.generateBucket(obj)
    this.rdbStore.insert(this.tableInfo.tableName, valueBucket, function (err, rowId) {
      if (callback) {
          err ? callback(-1) : callback(rowId)
      }
    });
  }

  /**
   * 插入 表对象组
   * @param objs 表对象组
   * @param callback
   */
  batchInsertData(objs: Array<any>, callback?: (Array) => void) {
    let bucketArray = []
    objs.forEach((obj) => {
      bucketArray.push(this.generateBucket(obj))
    })

    this.rdbStore.batchInsert(this.tableInfo.tableName, bucketArray, function (err, rowIds: number) {
      if (callback) {
          err ? callback(null) : callback(rowIds)
      }
    });
  }

  /**
   * 删除数据
   * @param obj 表对象
   * @param callback >=0:删除了多少条数据
   */
  deleteDataById(id: number, callback?: (number) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);
    predicates.equalTo('id', id);

    this.rdbStore.delete(predicates, function (err, rows) {
      if (callback) {
          err ? callback(rows) : callback(rows)
      }
    });
  }

  /**
   * 更新数据
   * @param obj 表对象
   * @param callback >=0:更新了多少条数据
   */
  updateData(obj: T, callback?: (number) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);
    predicates.equalTo('id', obj['id']);

    const valueBucket = this.generateBucket(obj)
    this.rdbStore.update(valueBucket, predicates, function (err, rows) {
      if (callback) {
          err ? callback(rows) : callback(rows)
      }
    });
  }

  /**
   * 查询数据
   * @param clazz 表类
   * @param callback 表对象数组
   */
  queryAll(callback: (Array) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);

    this.rdbStore.query(predicates, this.tableInfo.columns, function (err, resultSet: relationalStore.ResultSet) {
      if (!err) {
        let count = resultSet.rowCount;
        if (count === 0 || typeof count === 'string') {
          callback([]);
        } else {
          resultSet.goToFirstRow();
          const result = [];
          for (let i = 0; i < count; i++) {
            let tmp = new this.classType()
            tmp.parse(resultSet)
            result[i] = tmp;
            resultSet.goToNextRow();
          }
          callback(result);
        }

        resultSet.close();
      }
    });

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