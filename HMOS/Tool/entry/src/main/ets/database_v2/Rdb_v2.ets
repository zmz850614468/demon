import relationalStore from '@ohos.data.relationalStore'

/**
 * api-9 后发布的
 */
export default class Rdb_v2 {
  static readonly STORE_CONFIG = {
    name: "demon.db",
    securityLevel: relationalStore.SecurityLevel.S1
  };
  private rdbStore: any = null;
  private tableName: string;
  private sqlCreateTable: string;
  private columns: Array<string>;

  constructor(tableName: string, sqlCreateTable: string, columns: Array<string>) {
    this.tableName = tableName;
    this.sqlCreateTable = sqlCreateTable;
    this.columns = columns;
  }

  getRdbStore(callback) {
    if (!callback || typeof callback == 'undefined' || callback == undefined) {
      callback(false)
      return;
    }
    if (this.rdbStore != null) {
      callback(true);
      return
    }
    let context = getContext(this);
    relationalStore.getRdbStore(context, Rdb_v2.STORE_CONFIG, (err, rdb) => {
      if (err) {
        callback(false)
        return;
      }
      this.rdbStore = rdb;
      this.rdbStore.executeSql(this.sqlCreateTable);
      callback(true);
    });
  }

  /**
   * 插入数据
   * @param data valueBucket
   * @param callback
   */
  insertData(data, callback) {
    if (!callback || typeof callback == 'undefined' || callback == undefined) {
      return;
    }
    let resFlag: boolean = false;
    const valueBucket = data;
    this.rdbStore.insert(this.tableName, valueBucket, function (err, rowId) {
      if (err) {
        callback(resFlag);
        return;
      }
      callback(rowId);
    });
  }

  /**
   *
   * @param data  [valueBucket]
   * @param callback
   */
  batchInsertData(data: [], callback) {
    if (!callback || typeof callback == 'undefined' || callback == undefined) {
      return;
    }
    let resFlag: boolean = false;
    // const valueBucket = data;
    this.rdbStore.batchInsert(this.tableName, data, function (err, rowIds: []) {
      if (err) {
        callback(resFlag);
        return;
      }
      callback(rowIds);
    });
  }

  /**
   * 删除数据
   * @param predicates
   * @param callback
   */
  deleteData(predicates, callback) {
    if (!callback || typeof callback == 'undefined' || callback == undefined) {
      return;
    }
    // let resFlag: boolean = false;
    this.rdbStore.delete(predicates, function (err, rows) {
      if (err) {
        callback(rows);
        return;
      }
      callback(rows);
    });
  }

  /**
   * 更新数据
   * @param predicates
   * @param data
   * @param callback
   */
  updateData(predicates, data, callback) {
    if (!callback || typeof callback == 'undefined' || callback == undefined) {
      return;
    }
    // let resFlag: boolean = false;
    const valueBucket = data;
    this.rdbStore.update(valueBucket, predicates, function (err, rows) {
      if (err) {
        callback(rows);
        return;
      }
      callback(rows);
    });
  }

  /**
   * 查询数据
   * @param predicates
   * @param callback
   */
  query(predicates, callback) {
    if (!callback || typeof callback == 'undefined' || callback == undefined) {
      return;
    }
    this.rdbStore.query(predicates, this.columns, function (err, resultSet) {
      if (err) {
        return;
      }
      callback(resultSet);
      resultSet.close();
    });
  }
}