/**
 * 数据库 基础类
 */
export default abstract class DBBaseBean {
  // static readonly TABLE_NAME
  // static readonly SQL
  // static readonly COLUMNS

  
  id: number

  /**
   * 解析数据库返回的数据信息
   * @param resultSet
   */
  abstract parse(resultSet: any)

  abstract getNewBean(): DBBaseBean

}