import DBBaseBean from '../DBBaseBean'

/**
 * 数据库对象:每天操作结果
 */
export default class DayOperateBean implements DBBaseBean {
  static readonly TABLE_NAME = 'DayOperateTable'
  static readonly SQL = 'CREATE TABLE IF NOT EXISTS ' + DayOperateBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT, timeStamp LONG, ' +
  'result INTEGER,posCount INTEGER, negCount INTEGER, poundage INTEGER)'
  static readonly COLUMNS = ['id', 'timeStamp', 'result', 'posCount', 'negCount', 'poundage']
  id: number
  timeStamp: number
  result: number
  posCount: number
  negCount: number
  poundage: number

  getNewBean(): DBBaseBean {
    return new DayOperateBean()
  }

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(DayOperateBean.COLUMNS[0]))
    this.timeStamp = resultSet.getDouble(resultSet.getColumnIndex(DayOperateBean.COLUMNS[1]))
    this.result = resultSet.getDouble(resultSet.getColumnIndex(DayOperateBean.COLUMNS[2]))
    this.posCount = resultSet.getDouble(resultSet.getColumnIndex(DayOperateBean.COLUMNS[3]))
    this.negCount = resultSet.getDouble(resultSet.getColumnIndex(DayOperateBean.COLUMNS[4]))
    this.poundage = resultSet.getDouble(resultSet.getColumnIndex(DayOperateBean.COLUMNS[5]))
  }

  getDay(): string {
    let date = new Date(this.timeStamp)
    return (date.getMonth() + 1) + "-" + date.getDate()
  }
}