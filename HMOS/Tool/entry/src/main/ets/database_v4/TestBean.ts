import { Rdb_v4 } from './Rdb_v4'
import { TableInfo } from './TableInfo'

/**
 * 数据库对象:出入金结果
 */
export default class TestBean {

  static readonly TABLE_NAME = 'MoneyTable_test'

  static readonly info: TableInfo = {
    tableName: TestBean.TABLE_NAME,
    sqlCreateTable: 'CREATE TABLE IF NOT EXISTS ' + TestBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT,' +
    'date TEXT,result INTEGER,memo TEXT)',
    columns: ['id', 'date', 'result', 'memo']
  }

  id: number
  date: string
  result: number
  memo: string

  test() {

    // if (typeof this.id == 'number') {
    //   let bean = new TestBean()
    //   if (typeof TestBean["id"] == 'string') {
    //
    //   }
    // }
    //
    // Reflect.has(TestBean, '')
  }

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  static parse(resultSet: any): TestBean {
    let bean = new TestBean();
    bean.id = resultSet.getDouble(resultSet.getColumnIndex(TestBean.info.columns[0]))
    bean.date = resultSet.getString(resultSet.getColumnIndex(TestBean.info.columns[1]))
    bean.result = resultSet.getDouble(resultSet.getColumnIndex(TestBean.info.columns[2]))
    bean.memo = resultSet.getString(resultSet.getColumnIndex(TestBean.info.columns[3]))

    return bean
  }
}
