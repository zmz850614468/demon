import systemDateTime from '@ohos.systemDateTime'

export default class StringUtil {
  /**
   * 获取 年-月-日 时间信息
   * @returns
   */
  static getTime(): string {
    let date = new Date()
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() +
    ' ' + date.getHours() + '-' + date.getMinutes() + '-' + date.getSeconds()
  }

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
   * 获取 年-月-日 时间信息
   * @returns
   */
  static getHourTime(time: number): string {
    let date = new Date(time)
    let hours = date.getHours()
    let min = date.getMinutes()
    return (hours < 10 ? '0' + hours : hours) + ':' + (min < 10 ? '0' + min : min)
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