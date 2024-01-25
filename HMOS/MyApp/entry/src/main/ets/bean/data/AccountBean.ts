import { TableInfo } from 'myLibrary/src/main/ets/database_v4/TableInfo'
/**
 * 数据库对象:出入金结果
 */
export class AccountBean {
  static readonly TABLE_NAME = 'AccountTable'

  static readonly tableInfo: TableInfo = new TableInfo(
    AccountBean.TABLE_NAME,
    'CREATE TABLE IF NOT EXISTS ' + AccountBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT,' +
    'typeGroup TEXT,name TEXT,account TEXT,pwd TEXT)',
    ['id', 'typeGroup', 'name', 'account', 'pwd']
  )

  id: number
  typeGroup: string // 分组信息
  name: string // 账号名称
  account: string // 账号
  pwd: string // 密码

}