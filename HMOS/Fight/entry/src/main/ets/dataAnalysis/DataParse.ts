import DataBean from './bean/DataBean'
import ArrayList from '@ohos.util.ArrayList'
import AnalysisBean from './bean/AnalysisBean'

export default class DataParse {

  /**
   * 月份数据分析
   * @returns
   */
  static getMonthAnalysis(year: string, month: string, list: ArrayList<DataBean>): AnalysisBean {
    let bean = new AnalysisBean()
    bean.year = Number.parseInt(year)
    bean.month = month

    list.forEach((item: DataBean) => {
      bean.count++
      bean.result += item.result

      if (item.result > 0) {
        bean.posCount++
      } else {
        bean.negCount++
      }

      if (item.result >= 60) {
        bean.pos_60_count++
      } else if (item.result <= -60) {
        bean.neg_60_count++
      }

      // 反向操作数据
      /**
       * / 旧形式反向操作
       * // 新形式反向操作
       * 0 已经被弃用的形式
       */
      if (item.type === '/' || item.type === '0') {
        bean.fxCount++
        bean.fxResult += item.result
        if (item.result > 0) {
          bean.fxPosCount++
        }
      } else if (item.type === '//') {
        bean.fxCount_1++
        bean.fxResult_1 += item.result
        if (item.result > 0) {
          bean.fxPosCount_1++
        }
      }
    })

    bean.pay = bean.count * 0.5
    bean.result -= bean.pay

    return bean
  }

  /**
   * 字符串转对象
   * 10K数据转换
   * @param data
   * @returns
   */
  static getStringToBean(data: string): ArrayList<DataBean> {
    let list: ArrayList<DataBean> = new ArrayList()

    // 解析对象
    let strArr = data.split('\n')
    strArr.forEach((item: string) => {
      let beanArr = item.split('\t')
      let bean = new DataBean()
      bean.type = beanArr[0]
      bean.day = beanArr[1]
      bean.time = beanArr[2]
      if (beanArr.length == 6) { // 调整了excel的格式后
        bean.op = ""
        bean.dir = Number.parseInt(beanArr[3])
        bean.in_ = Number.parseFloat(beanArr[4])
        bean.out_ = Number.parseFloat(beanArr[5])
      } else {
        bean.op = beanArr[3]
        bean.dir = Number.parseInt(beanArr[4])
        bean.in_ = Number.parseFloat(beanArr[5])
        bean.out_ = Number.parseFloat(beanArr[6])
      }

      // 计算结果
      bean.result = (bean.out_ - bean.in_) * bean.dir
      if (bean.op === "0" || bean.type === "-1") {
        bean.result *= -1
      }

      list.add(bean)
    })

    return list
  }

  /**
   * 月份数据分析
   * @returns
   */
  // static copyMonthAnalysis(type: string, list: ArrayList<AnalysisBean>): string {
  //   let result = type + '\n'
  //   result += AnalysisBean.getTitle() + '\n'
  //   list.forEach((item) => {
  //     result += item.toString() + '\n'
  //   })
  //
  //   return result
  // }

  /**
   * 字符串转对象
   * 5K数据转换
   * @param data
   * @returns
   */
  static getStringToBean_5(data: string): ArrayList<DataBean> {
    let list: ArrayList<DataBean> = new ArrayList()

    // 解析对象
    let strArr = data.split('\n')
    strArr.forEach((item: string) => {
      let beanArr = item.split('\t')
      let bean = new DataBean()
      // bean.type = beanArr[0]
      bean.day = beanArr[0]
      bean.time = beanArr[1]
      // bean.op = beanArr[2]
      bean.dir = Number.parseInt(beanArr[3])
      bean.in_ = Number.parseFloat(beanArr[4])
      bean.out_ = Number.parseFloat(beanArr[5])

      // 计算结果
      bean.result = (bean.out_ - bean.in_) * bean.dir

      if (bean.dir == 1 || bean.dir == -1) {
        list.add(bean)
      }
    })

    return list
  }
}