import DBBaseBean from '../DBBaseBean'

/**
 * 数据库对象:每笔操作结果
 */
export default class OperateBean implements DBBaseBean {
  static readonly TABLE_NAME = 'OperateTable'
  static readonly SQL = 'CREATE TABLE IF NOT EXISTS ' + OperateBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT, timeStamp LONG, ' +
  'name TEXT,type TEXT, result INTEGER)'
  static readonly COLUMNS = ['id', 'timeStamp', 'name', 'type', 'result']
  id: number
  timeStamp: number // 时间
  name: string // 名称
  type: string // 类型：入手、出手
  // isRight: boolean
  result: number = 0

  hands: number = 0 // 手
  inPrice: number = 0
  outPrice: number = 0

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(OperateBean.COLUMNS[0]))
    this.timeStamp = resultSet.getDouble(resultSet.getColumnIndex(OperateBean.COLUMNS[1]))
    this.name = resultSet.getString(resultSet.getColumnIndex(OperateBean.COLUMNS[2]))
    this.type = resultSet.getString(resultSet.getColumnIndex(OperateBean.COLUMNS[3]))
    this.result = resultSet.getDouble(resultSet.getColumnIndex(OperateBean.COLUMNS[4]))
  }

  getNewBean(): DBBaseBean {
    return new OperateBean()
  }

  getDay(): string {
    let date = new Date(this.timeStamp)
    return (date.getMonth() + 1) + "-" + date.getDate()
  }
}