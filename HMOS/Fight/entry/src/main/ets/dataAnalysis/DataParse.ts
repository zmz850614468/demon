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
      bean.op = beanArr[3]
      bean.dir = Number.parseInt(beanArr[4])
      bean.in_ = Number.parseFloat(beanArr[5])
      bean.out_ = Number.parseFloat(beanArr[6])

      // 计算结果
      bean.result = (bean.out_ - bean.in_) * bean.dir
      if (bean.op === "0") {
        bean.result *= -1
      }

      list.add(bean)
    })

    return list
  }

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