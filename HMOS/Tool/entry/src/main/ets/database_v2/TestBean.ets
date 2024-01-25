/**
 * 数据库对象:每笔操作结果
 */
import DBBaseBean from './DBBaseBean'

export default class TestBean implements DBBaseBean {
  constructor() {
  }
  static readonly TABLE_NAME = 'OperateTable'
  static readonly SQL = 'CREATE TABLE IF NOT EXISTS ' + TestBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT, timeStamp LONG, ' +
  'name TEXT,type TEXT, result INTEGER)'
  static readonly COLUMNS = ['id', 'timeStamp', 'name', 'type', 'result']
  id: number
  timeStamp: number // 时间
  name: string // 名称
  type: string // 类型：入手、出手
  // isRight: boolean
  result: number

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(TestBean.COLUMNS[0]))
    this.timeStamp = resultSet.getDouble(resultSet.getColumnIndex(TestBean.COLUMNS[1]))
    this.name = resultSet.getString(resultSet.getColumnIndex(TestBean.COLUMNS[2]))
    this.type = resultSet.getString(resultSet.getColumnIndex(TestBean.COLUMNS[3]))
    this.result = resultSet.getDouble(resultSet.getColumnIndex(TestBean.COLUMNS[4]))

    Reflect
  }

  getNewBean(): DBBaseBean {
    return new TestBean()
  }
}