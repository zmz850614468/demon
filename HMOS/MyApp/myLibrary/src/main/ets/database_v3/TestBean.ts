import { TableInfo } from './TableInfo'

/**
 * 数据库对象:出入金结果
 */
export class TestBean {
  static readonly TABLE_NAME = 'MoneyTable'

  static tableInfo: TableInfo = {
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
  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(TestBean.tableInfo.columns[0]))
    this.date = resultSet.getString(resultSet.getColumnIndex(TestBean.tableInfo.columns[1]))
    this.result = resultSet.getDouble(resultSet.getColumnIndex(TestBean.tableInfo.columns[2]))
    this.memo = resultSet.getString(resultSet.getColumnIndex(TestBean.tableInfo.columns[3]))
  }

  // getDay(): string {
  //   let date = new Date(this.timeStamp)
  //   return (date.getMonth() + 1) + "-" + date.getDate()
  // }
}