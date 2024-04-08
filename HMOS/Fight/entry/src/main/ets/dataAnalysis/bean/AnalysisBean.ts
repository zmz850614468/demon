/**
 * 分析结果类
 */
import TypeResultBean from './TypeResultBean'
import HashMap from '@ohos.util.HashMap'

export default class AnalysisBean {
  type: string // 品种

  year: number
  month: string // 月份
  count: number = 0 // 次数
  pay: number = 0 // 费用
  result: number = 0 // 总结果，已经除去费用

  posCount: number = 0 // 正操作数
  negCount: number = 0 // 负操作数
  pos_60_count: number = 0 // 正60以上操作数
  neg_60_count: number = 0 // 负60以上操作数

  fxCount: number = 0 // /类型 反向操作数
  fxPosCount: number = 0 // /类型 反向操作数 - 结果>0
  fxResult: number = 0 // /类型 反向操作结果

  fxCount_1: number = 0 // -1类型 反向操作数
  fxPosCount_1: number = 0 // /类型 反向操作数 - 结果>0
  fxResult_1: number = 0 // -1类型 反向操作结果

  typeResultMap: HashMap<String, TypeResultBean> = new HashMap()

  private static split: string = ';'

  /**
   * 获取反向操作胜率
   * @returns
   */
  getFxPercent(): string {
    return (this.fxPosCount * 100 / this.fxCount).toFixed(1) + '%'
  }

  /**
   * 获取反向操作胜率
   * @returns
   */
  getFx_1Percent(): string {
    return (this.fxPosCount_1 * 100 / this.fxCount_1).toFixed(1) + '%'
  }

  hasType(type: string): Boolean {
    return this.typeResultMap.hasKey(type)
  }

  getCountByType(type: string): number {
    return this.typeResultMap.get(type).count
  }

  getResultByType(type: string): number {
    return this.typeResultMap.get(type).result
  }

  getPercentByType(type: string): string {
    return (this.typeResultMap.get(type).posCount * 100 / this.typeResultMap.get(type).count).toFixed(1) + '%'
  }

  // toString(): string {
  //   let result = ''
  //   result += this.month + '/' + this.year + AnalysisBean.split
  //   result += this.posCount + AnalysisBean.split
  //   result += this.negCount + AnalysisBean.split
  //   result += this.count + AnalysisBean.split
  //   result += this.pay + AnalysisBean.split
  //   result += this.fxCount + AnalysisBean.split
  //   result += this.fxResult.toFixed(1) + AnalysisBean.split
  //   result += (this.posCount * 100 / this.count).toFixed(2) + '%' + AnalysisBean.split
  //   result += this.result.toFixed(1)
  //
  //   return result
  // }

  add(bean: AnalysisBean) {
    this.count += bean.count
    this.pay += bean.pay
    this.result += bean.result
    this.posCount += bean.posCount
    this.negCount += bean.negCount
    this.pos_60_count += bean.pos_60_count
    this.neg_60_count += bean.neg_60_count

    this.fxCount += bean.fxCount
    this.fxResult += bean.fxResult

    this.fxCount_1 += bean.fxCount_1
    this.fxResult_1 += bean.fxResult_1

    bean.typeResultMap.forEach((value, key: string) => {
      if (!this.typeResultMap.hasKey(key)) {
        this.typeResultMap.set(key, new TypeResultBean(key))
      }

      let result = this.typeResultMap.get(key);
      result.count += value.count
      result.posCount += value.posCount
      result.result += value.result
    })
  }

  update(hands: number) {
    this.result *= hands
    this.fxResult *= hands
    this.fxResult_1 *= hands

    this.typeResultMap.forEach((val, key) => {
      val.result *= hands
    })
  }

  static getTitle(): string {
    let result = ''
    result += '日期' + AnalysisBean.split
    result += '正操作数' + AnalysisBean.split
    result += '负操作数' + AnalysisBean.split
    result += '总操作数' + AnalysisBean.split
    result += '操作费用' + AnalysisBean.split
    result += '/-操作数' + AnalysisBean.split
    result += '/-操作结果' + AnalysisBean.split
    result += '成功率' + AnalysisBean.split
    result += '收益'

    return result
  }
}