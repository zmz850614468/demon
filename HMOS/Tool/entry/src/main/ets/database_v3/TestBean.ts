import { TableInfo } from './TableInfo'

/**
 * 数据库对象:出入金结果
 */
export default class TestBean {
  static readonly TABLE_NAME = 'MoneyTable'

  // todo 1.必须实现
  static info: TableInfo = {
    tableName: TestBean.TABLE_NAME,
    sqlCreateTable: 'CREATE TABLE IF NOT EXISTS ' + TestBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT,' +
    'date TEXT,result INTEGER,memo TEXT)',
    columns: ['id', 'date', 'result', 'memo']
  }

  id: number
  date: string
  result: number
  memo: string

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  // todo 1.必须实现
  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(TestBean.info.columns[0]))
    this.date = resultSet.getString(resultSet.getColumnIndex(TestBean.info.columns[1]))
    this.result = resultSet.getDouble(resultSet.getColumnIndex(TestBean.info.columns[2]))
    this.memo = resultSet.getString(resultSet.getColumnIndex(TestBean.info.columns[3]))
  }
}