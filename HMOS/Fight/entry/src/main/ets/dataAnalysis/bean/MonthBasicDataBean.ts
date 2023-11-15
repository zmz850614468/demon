/**
 * 月基础数据信息
 */
export default class MonthBasicDataBean {
  year: string
  month: string
  data: string

  constructor(year: string, month: string, data: string) {
    this.year = year
    this.month = month
    this.data = data
  }
}