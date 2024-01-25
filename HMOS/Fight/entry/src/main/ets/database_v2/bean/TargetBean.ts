import DBBaseBean from '../DBBaseBean';

/**
 * 目标对象
 */
export class TargetBean implements DBBaseBean {
  static readonly TABLE_NAME = 'TargetTable'
  static readonly SQL = 'CREATE TABLE IF NOT EXISTS ' + TargetBean.TABLE_NAME + '(id INTEGER PRIMARY KEY AUTOINCREMENT,' +
  'year INTEGER,month INTEGER,basic INTEGER,interestRate INTEGER,target INTEGER,result INTEGER)'
  static readonly COLUMNS = ['id', 'year', 'month', 'basic', 'interestRate', 'target', 'result']

  id: number

  year: number

  month: number

  basic: number // 单位：万

  interestRate: number // 利率:百分数

  target: number // 单位：万

  result: number // 结果 单位：元
  // completePercent: number // 完成率：百分数

  monthList: Array<TargetBean> // 年对应的月信息

  getNewBean(): DBBaseBean {
    return new TargetBean()
  }

  calYearResult() {
    if (this.monthList) {
      this.monthList.forEach((item) => {
        this.result += item.result
      })
    }
  }

  percent: number
  /**
   * 计算完成率：百分数
   * @returns
   */
  getPercent(): number {
    if (!this.percent) {
      this.percent = this.result / 100 / (this.target - this.basic)
    }
    return this.percent
  }

  parse(resultSet: any) {
    this.id = resultSet.getDouble(resultSet.getColumnIndex(TargetBean.COLUMNS[0]))
    this.year = resultSet.getDouble(resultSet.getColumnIndex(TargetBean.COLUMNS[1]))
    this.month = resultSet.getDouble(resultSet.getColumnIndex(TargetBean.COLUMNS[2]))
    this.basic = resultSet.getDouble(resultSet.getColumnIndex(TargetBean.COLUMNS[3]))
    this.interestRate = resultSet.getDouble(resultSet.getColumnIndex(TargetBean.COLUMNS[4]))
    this.target = resultSet.getDouble(resultSet.getColumnIndex(TargetBean.COLUMNS[5]))
    this.result = resultSet.getDouble(resultSet.getColumnIndex(TargetBean.COLUMNS[6]))
  }
}


//12 -12253
//11 3609
//10 2901