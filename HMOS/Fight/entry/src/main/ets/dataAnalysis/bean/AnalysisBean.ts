/**
 * 分析结果类
 */
export default class AnalysisBean {
  year: number
  month: string // 月份
  count: number = 0 // 次数
  pay: number = 0 // 费用
  result: number = 0 // 总结果，已经出去费用

  posCount: number = 0 // 正操作数
  negCount: number = 0 // 负操作数
  pos_60_count: number = 0 // 正60以上操作数
  neg_60_count: number = 0 // 负60以上操作数

}