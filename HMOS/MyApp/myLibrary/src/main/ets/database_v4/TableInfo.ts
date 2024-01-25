export class TableInfo {
  tableName: string
  sqlCreateTable: string
  columns: Array<string>

  constructor(tableName: string, sqlCreateTable: string, columns: Array<string>) {
    this.tableName = tableName
    this.sqlCreateTable = sqlCreateTable
    this.columns = columns
  }
}
