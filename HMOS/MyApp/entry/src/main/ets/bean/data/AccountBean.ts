import { TableInfo } from 'myLibrary/src/main/ets/database_v3/TableInfo'
/**
 * 数据库对象:出入金结果
 */
export class AccountBean {
  static readonly TABLE_NAME = 'AccountTable'

  static tableInfo: TableInfo = {
    tableName: AccountBean.TABLE_NAME,
    sqlCreateTable: 'CREATE TABLE IF NOT EXISTS ' + AccountBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT,' +
    'typeGroup TEXT,name TEXT,account TEXT,pwd TEXT)',
    columns: ['id', 'typeGroup', 'name', 'account', 'pwd']
  }

  id: number
  typeGroup: string // 分组信息
  name: string // 账号名称
  account: string // 账号
  pwd: string // 密码

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(AccountBean.tableInfo.columns[0]))
    this.typeGroup = resultSet.getString(resultSet.getColumnIndex(AccountBean.tableInfo.columns[1]))
    this.name = resultSet.getString(resultSet.getColumnIndex(AccountBean.tableInfo.columns[2]))
    this.account = resultSet.getString(resultSet.getColumnIndex(AccountBean.tableInfo.columns[3]))
    this.pwd = resultSet.getString(resultSet.getColumnIndex(AccountBean.tableInfo.columns[4]))
  }
}