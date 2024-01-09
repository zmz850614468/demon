/**
 * 分析结果类
 */
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

  fxCount: number = 0 // 反向操作数
  fxResult: number = 0 // 反向操作结果

  private static split: string = ';'

  toString(): string {
    let result = ''
    result += this.month + '/' + this.year + AnalysisBean.split
    result += this.posCount + AnalysisBean.split
    result += this.negCount + AnalysisBean.split
    result += this.count + AnalysisBean.split
    result += this.pay + AnalysisBean.split
    result += this.fxCount + AnalysisBean.split
    result += this.fxResult.toFixed(1) + AnalysisBean.split
    result += (this.posCount * 100 / this.count).toFixed(2) + '%' + AnalysisBean.split
    result += this.result.toFixed(1)

    return result
  }

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
  }

  update(hands: number) {
    this.result *= 2
    this.fxResult *= 2
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