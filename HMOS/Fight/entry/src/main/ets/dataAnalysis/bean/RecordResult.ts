import ArrayList from '@ohos.util.ArrayList'
import AnalysisBean from './AnalysisBean'
import DataBean from './DataBean'
import RecordBean from './RecordBean'
import Logger from "../../common/util/Logger"

/**
 * 记录结果类
 */
export default class RecordResult {
  // 月低点
  monthLowList: Array<RecordBean> = []

  // 高收益月
  profitMonthList: Array<RecordBean> = []

  // 高亏损月
  lossMonthList: Array<RecordBean> = []

  // 高盈利操作
  profitOperateList: Array<RecordBean> = []

  // 高亏损操作
  lossOperateList: Array<RecordBean> = []

  constructor() {

    this.monthLowList.push(new RecordBean())
    this.monthLowList.push(new RecordBean())
    this.monthLowList.push(new RecordBean())

    this.profitMonthList.push(new RecordBean())
    this.profitMonthList.push(new RecordBean())
    this.profitMonthList.push(new RecordBean())

    this.lossMonthList.push(new RecordBean())
    this.lossMonthList.push(new RecordBean())
    this.lossMonthList.push(new RecordBean())

    this.profitOperateList.push(new RecordBean())
    this.profitOperateList.push(new RecordBean())
    this.profitOperateList.push(new RecordBean())

    this.lossOperateList.push(new RecordBean())
    this.lossOperateList.push(new RecordBean())
    this.lossOperateList.push(new RecordBean())
  }

  /**
   * 处理每笔操作数据
   * @param list
   */
  dealOperateData(month: string, list: ArrayList<DataBean>) {
    let total = 0
    let lowBean = new RecordBean()

    list.forEach((bean) => {
      // 月低点
      total += bean.result
      if (total < lowBean.record) {
        lowBean.time = month + '/' + bean.day
        lowBean.record = total
      }

      // 高盈利操作
      if (bean.result > 0 && bean.result > this.profitOperateList[2].record) {
        let temp = null
        if (bean.result > this.profitOperateList[0].record) {
          this.profitOperateList[2] = this.profitOperateList[1]
          this.profitOperateList[1] = this.profitOperateList[0]
          this.profitOperateList[0] = new RecordBean()
          temp = this.profitOperateList[0]
        } else if (bean.result > this.profitOperateList[1].record) {
          this.profitOperateList[2] = this.profitOperateList[1]
          this.profitOperateList[1] = new RecordBean()
          temp = this.profitOperateList[1]
        } else {
          this.profitOperateList[2] = new RecordBean()
          temp = this.profitOperateList[2]
        }
        // temp.name = name
        temp.time = month + '/' + bean.day
        temp.record = bean.result

      } else if (bean.result < 0 && bean.result < this.lossOperateList[2].record) { // 高亏损操作
        let temp = null
        if (bean.result < this.lossOperateList[0].record) {
          this.lossOperateList[2] = this.lossOperateList[1]
          this.lossOperateList[1] = this.lossOperateList[0]
          this.lossOperateList[0] = new RecordBean()
          temp = this.lossOperateList[0]
        } else if (bean.result < this.lossOperateList[1].record) {
          this.lossOperateList[2] = this.lossOperateList[1]
          this.lossOperateList[1] = new RecordBean()
          temp = this.lossOperateList[1]
        } else {
          this.lossOperateList[2] = new RecordBean()
          temp = this.lossOperateList[2]
        }
        // temp.name = name
        temp.time = month + '/' + bean.day
        temp.record = bean.result
      }
    })

    // 月低点
    if (lowBean.record < 0 && lowBean.record < this.monthLowList[2].record) {
      let temp = null
      if (lowBean.record < this.monthLowList[0].record) {
        this.monthLowList[2] = this.monthLowList[1]
        this.monthLowList[1] = this.monthLowList[0]
        this.monthLowList[0] = new RecordBean()
        temp = this.monthLowList[0]
      } else if (lowBean.record < this.monthLowList[1].record) {
        this.monthLowList[2] = this.monthLowList[1]
        this.monthLowList[1] = new RecordBean()
        temp = this.monthLowList[1]
      } else {
        this.monthLowList[2] = new RecordBean()
        temp = this.monthLowList[2]
      }
      // temp.name = name
      temp.time = lowBean.time
      temp.record = lowBean.record
    }
  }

  /**
   * 处理每月操作数据
   * @param list
   */
  dealMonthData(list: ArrayList<AnalysisBean>) {
    list.forEach((bean) => {
      // 高收益月
      if (bean.result > 0 && bean.result > this.profitMonthList[2].record) {
        let temp = null
        if (bean.result > this.profitMonthList[0].record) {
          this.profitMonthList[2] = this.profitMonthList[1]
          this.profitMonthList[1] = this.profitMonthList[0]
          this.profitMonthList[0] = new RecordBean()
          temp = this.profitMonthList[0]
        } else if (bean.result > this.profitMonthList[1].record) {
          this.profitMonthList[2] = this.profitMonthList[1]
          this.profitMonthList[1] = new RecordBean()
          temp = this.profitMonthList[1]
        } else {
          this.profitMonthList[2] = new RecordBean()
          temp = this.profitMonthList[2]
        }
        // temp.name = name
        temp.time = bean.year + '/' + bean.month
        temp.record = bean.result
      } else if (bean.result < 0 && bean.result < this.lossMonthList[2].record) {
        let temp = null
        if (bean.result < this.lossMonthList[0].record) {
          this.lossMonthList[2] = this.lossMonthList[1]
          this.lossMonthList[1] = this.lossMonthList[0]
          this.lossMonthList[0] = new RecordBean()
          temp = this.lossMonthList[0]
        } else if (bean.result < this.lossMonthList[1].record) {
          this.lossMonthList[2] = this.lossMonthList[1]
          this.lossMonthList[1] = new RecordBean()
          temp = this.lossMonthList[1]
        } else {
          this.lossMonthList[2] = new RecordBean()
          temp = this.lossMonthList[2]
        }
        // temp.name = name
        temp.time = bean.year + '/' + bean.month
        temp.record = bean.result
      }
    })
  }
}