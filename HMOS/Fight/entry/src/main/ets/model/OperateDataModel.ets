/**
 * 操作结果数据类
 */
import BaseTable_v2 from '../database_v2/BaseTable_v2'
import DayOperateBean from '../database_v2/bean/DayOperateBean'
import OperateBean from '../database_v2/bean/OperateBean'
import StringUtil from '../util/StringUtil'

export class OperateDataModel {
  static instance: OperateDataModel = null

  static getInstance(): OperateDataModel {
    if (!OperateDataModel.instance) {
      OperateDataModel.instance = new OperateDataModel()
    }

    return OperateDataModel.instance
  }

  dayOperateList: Array<DayOperateBean> = null
  monthList: Array<ResultBean> = []
  dayTable: BaseTable_v2<DayOperateBean> = null

  private constructor() {
    // 数据库查询数据
    this.dayTable = new BaseTable_v2(DayOperateBean.TABLE_NAME, DayOperateBean.SQL, DayOperateBean.COLUMNS, (b: boolean) => {
      this.refreshData()
    })
  }

  refreshData() {
    if (this.dayTable) {
      this.dayTable.queryAll(new DayOperateBean(), (list: Array<DayOperateBean>) => {
        this.dayOperateList = list.reverse()
        this.calculate()
      })
    }
  }

  /**
   * 异步处理数据
   * 处理月份数据
   */
  private async calculate() {
    let lastMonth = -1
    let bean: ResultBean = null
    this.monthList = []
    this.dayOperateList.forEach((item: DayOperateBean) => {
      let month = StringUtil.getMonth(item.timeStamp)
      if (lastMonth != month) {
        lastMonth = month
        bean = new ResultBean()
        bean.year = StringUtil.getYear(item.timeStamp)
        bean.month = month
        this.monthList.push(bean)
      }

      bean.result += item.result
      bean.poundage += item.poundage
      bean.posCount += item.posCount
      bean.negCount += item.negCount
    })
  }

  /**
   * 返回 净收益
   * @returns
   */
  getResult(year: number, month: number): number {
    let size = this.monthList.length
    let item: ResultBean = null
    for (let i = 0; i < size; i++) {
      item = this.monthList[i]
      if (item.year === year && item.month === month) {
        return item.result - item.poundage
      }
    }
    return 0
  }

  hasData(): boolean {
    return this.monthList.length > 0
  }
}

class ResultBean {
  year: number
  month: number = 0
  posCount: number = 0
  negCount: number = 0
  result: number = 0
  poundage: number = 0 // 费用

  toString(): string {
    return this.year + '-' + this.month + '-' + this.result + '-' + this.poundage
  }
}