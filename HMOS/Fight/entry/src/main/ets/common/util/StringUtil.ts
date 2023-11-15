import systemDateTime from '@ohos.systemDateTime'

export default class StringUtil {
  /**
   * 获取 年-月-日 时间信息
   * @returns
   */
  static async getDayTimeAsync(): Promise<string> {
    let date = new Date(await systemDateTime.getCurrentTime())
    return date.getFullYear() + '-' + (date.getMonth() + 1) + "-" + date.getDate()
  }

  /**
   * 获取 年-月-日 时间信息
   * @returns
   */
  static getDayTime(time: number): string {
    let date = new Date(time)
    return date.getFullYear() + '-' + (date.getMonth() + 1) + "-" + date.getDate()
  }

  /**
   * 获取 年 时间信息
   * @returns
   */
  static getYear(time: number): number {
    let date = new Date(time)
    return date.getFullYear()
  }

  /**
   * 获取 月 时间信息
   * @returns
   */
  static getMonth(time: number): number {
    let date = new Date(time)
    return date.getMonth() + 1
  }

}