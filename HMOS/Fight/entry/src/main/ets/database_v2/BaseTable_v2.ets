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

// import data_rdb from '@ohos.data.rdb';
import relationalStore from '@ohos.data.relationalStore'
import DBBaseBean from './DBBaseBean';
// import DBBaseBean from '../database/DBBaseBean';
import Rdb_v2 from './Rdb_v2';

export default class BaseTable_v2<T extends DBBaseBean> {
  private rdbTable: Rdb_v2
  private tableName: string
  private columns: string[]

  constructor(tableName: string, sqlCreateTable: string, columns: string[], callback: Function) {
    this.tableName = tableName
    this.columns = columns
    this.rdbTable = new Rdb_v2(tableName, sqlCreateTable, columns);
    this.rdbTable.getRdbStore(callback);
  }

  getRdbStore(callback?: Function) {
    this.rdbTable.getRdbStore(callback);
  }

  /**
   * 插入数据
   * @param account
   * @param callback
   */
  insertData(account: T, callback?: Function) {
    const valueBucket = this.generateBucket(account);
    this.rdbTable.insertData(valueBucket, callback);
  }

  /**
   * 通过 id 删除数据
   * @param id
   * @param callback
   */
  deleteData(t: T, callback?: Function) {
    let predicates = new relationalStore.RdbPredicates(this.tableName);
    predicates.equalTo('id', t.id);
    this.rdbTable.deleteData(predicates, callback);
  }

  /**
   * 通过 id 更新数据
   * @param t
   * @param callback
   */
  updateData(t: T, callback?: Function) {
    const valueBucket = this.generateBucket(t);
    let predicates = new relationalStore.RdbPredicates(this.tableName);
    predicates.equalTo('id', t.id);
    this.rdbTable.updateData(predicates, valueBucket, callback);
  }

  /**
   * 查询所有数据
   除了 id 字段， 其他全部以字符串形式返回数据
   * @param t 一个新对象
   * @param callback
   */
  queryAll(t: T, callback: Function) {
    let predicates = new relationalStore.RdbPredicates(this.tableName);

    this.rdbTable.query(predicates, function (resultSet) {
      let count = resultSet.rowCount;
      if (count === 0 || typeof count === 'string') {
        callback([]);
      } else {
        resultSet.goToFirstRow();
        const result = [];
        for (let i = 0; i < count; i++) {
          let tmp = t.getNewBean()
          tmp.parse(resultSet)
          result[i] = tmp;

          resultSet.goToNextRow();
        }
        callback(result);
      }
    });
  }

  /**
   * 执行sql语句
   * @param sql
   * @param callback
   */
  exeSql(sql: string) {
    this.rdbTable.exeSql(sql)
  }

  generateBucket(account: T) {
    let obj = {};
    this.columns.forEach((item) => {
      if (item != 'id') {
        obj[item] = account[item];
      }
    });
    return obj;
  }

  getColumns(): Array<string> {
    return this.columns;
  }
}

