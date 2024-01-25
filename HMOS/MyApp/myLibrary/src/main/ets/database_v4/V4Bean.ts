import { TableInfo } from './TableInfo'

/**
 * 数据库对象:出入金结果
 */
export default class V4Bean {
  static readonly TABLE_NAME = 'V4Table'
  static readonly info: TableInfo = new TableInfo(
    V4Bean.TABLE_NAME,
    'CREATE TABLE IF NOT EXISTS ' + V4Bean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT,' +
    'date TEXT,result INTEGER,memo TEXT)',
    ['id', 'date', 'result', 'memo']
  )

  id: number
  date: string
  result: number
  memo: string
}
