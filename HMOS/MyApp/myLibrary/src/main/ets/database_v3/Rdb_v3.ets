import relationalStore from '@ohos.data.relationalStore'
import { TableInfo } from './TableInfo';

/**
 * 数据库操作类
 * api-9 后发布的
 */
export class Rdb_v3 {
  static readonly STORE_CONFIG = {
    name: "demon.db",
    securityLevel: relationalStore.SecurityLevel.S1
  }
  private rdbStore: any = null;
  private tableInfo: TableInfo = null // 表信息

  constructor(tableInfo: TableInfo, callback?: (boolean) => void) {
    this.tableInfo = tableInfo
    this.getRdbStore(callback)
  }

  getRdbStore(callback?: (boolean) => void) {
    if (this.rdbStore != null) {
        callback ? callback(true) : null
      return
    }
    let context = getContext(this);
    relationalStore.getRdbStore(context, Rdb_v3.STORE_CONFIG, (err, rdb) => {
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
   * 插入 单条数据
   * @param obj 表对象
   * @param callback -1：插入失败 >0:插入的rowId值
   */
  insertBean(obj: any, callback?: (number) => void) {
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

    this.rdbStore.batchInsert(this.tableInfo.tableName, bucketArray, function (err, rowIds: []) {
      if (callback) {
          err ? callback(null) : callback(rowIds)
      }
    });
  }

  /**
   * 删除 单条数据
   * @param obj 表对象
   * @param callback >=0:删除了多少条数据
   */
  deleteBean(obj: any, callback?: (number) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);
    predicates.equalTo('id', obj.id);

    this.delete(predicates, callback)
  }

  /**
   * 删除指定条件的数据
   * @param predicates 条件约束
   * @param callback
   */
  delete(predicates: relationalStore.RdbPredicates, callback: (Array) => void) {
    this.rdbStore.delete(predicates, function (err, rows) {
      if (callback) {
          err ? callback(rows) : callback(rows)
      }
    });
  }

  /**
   * 更新 单条数据
   * @param obj 表对象
   * @param callback >=0:更新了多少条数据
   */
  updateBean(obj: any, callback?: (number) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName);
    predicates.equalTo('id', obj.id);

    const valueBucket = this.generateBucket(obj)
    this.update(valueBucket, predicates, callback)
  }

  /**
   * 更新 批量数据
   * @param valueBucket 键值对对象，需要更新的数据
   * @param predicates 条件约束
   * @param callback
   */
  update(valueBucket: any, predicates: relationalStore.RdbPredicates, callback?: (number) => void) {
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
  queryAll(clazz: any, callback: (Array) => void) {
    let predicates = new relationalStore.RdbPredicates(this.tableInfo.tableName)

    this.query(clazz, predicates, callback)
  }

  /**
   * 指定条件查询数据
   * @param clazz 查询的数据类型
   * @param predicates 条件约束
   * @param callback
   */
  query(clazz: any, predicates: relationalStore.RdbPredicates, callback: (Array) => void) {
    this.rdbStore.query(predicates, this.tableInfo.columns, function (err, resultSet) {
      if (!err) {
        let count = resultSet.rowCount;
        if (count === 0 || typeof count === 'string') {
          callback([]);
        } else {
          resultSet.goToFirstRow();
          const result = [];
          for (let i = 0; i < count; i++) {
            let tmp = new clazz()
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

  /**
   * 获取 限制条件对象，就是 where 后添加的约束条件
   * @returns 条件约束
   */
  getPredicates(): relationalStore.RdbPredicates {
    return new relationalStore.RdbPredicates(this.tableInfo.tableName)
  }

  private generateBucket(account: any) {
    let obj = {};
    this.tableInfo.columns.forEach((item) => {
      if (item != 'id') {
        obj[item] = account[item];
      }
    });
    return obj;
  }

  getColumns(): Array<string> {
    return this.tableInfo.columns
  }
}