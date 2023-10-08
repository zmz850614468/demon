/*
 * Copyright (c) 2022 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import data_rdb from '@ohos.data.rdb';

export default class Rdb {

  // 数据库配置
  static readonly STORE_CONFIG = {
    name: 'demon.db'
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
      return;
    }
    if (this.rdbStore != null) {
      callback();
      return
    }
    let context = getContext(this);
    data_rdb.getRdbStore(context, Rdb.STORE_CONFIG, 1, (err, rdb) => {
      if (err) {
        return;
      }
      this.rdbStore = rdb;
      this.rdbStore.executeSql(this.sqlCreateTable);
      callback();
    });
  }

  /**
   * 插入数据
   * @param data
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
   * 删除数据
   * @param predicates
   * @param callback
   */
  deleteData(predicates, callback) {
    if (!callback || typeof callback == 'undefined' || callback == undefined) {
      return;
    }
    let resFlag: boolean = false;
    this.rdbStore.delete(predicates, function (err, ret) {
      if (err) {
        callback(resFlag);
        return;
      }
      callback(!resFlag);
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
    let resFlag: boolean = false;
    const valueBucket = data;
    this.rdbStore.update(valueBucket, predicates, function (err, ret) {
      if (err) {
        callback(resFlag);
        return;
      }
      callback(!resFlag);
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