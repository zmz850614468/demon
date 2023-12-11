import DBBaseBean from '../DBBaseBean'

/**
 * 数据库对象:出入金结果
 */
export default class MoneyBean implements DBBaseBean {
  static readonly TABLE_NAME = 'MoneyTable'
  static readonly SQL = 'CREATE TABLE IF NOT EXISTS ' + MoneyBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT,' +
  'date TEXT,result INTEGER,memo TEXT)'
  static readonly COLUMNS = ['id', 'date', 'result', 'memo']

  id: number
  date: string
  result: number
  memo: string

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(MoneyBean.COLUMNS[0]))
    this.date = resultSet.getString(resultSet.getColumnIndex(MoneyBean.COLUMNS[1]))
    this.result = resultSet.getDouble(resultSet.getColumnIndex(MoneyBean.COLUMNS[2]))
    this.memo = resultSet.getString(resultSet.getColumnIndex(MoneyBean.COLUMNS[3]))
  }

  getNewBean(): MoneyBean {
    return new MoneyBean()
  }

  // getDay(): string {
  //   let date = new Date(this.timeStamp)
  //   return (date.getMonth() + 1) + "-" + date.getDate()
  // }
}