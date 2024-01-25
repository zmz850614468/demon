export class StringUtil {

  /**
   * 获取 年-月-日 时间信息
   * @param time 没有传入参数，则获取当前时间
   * @returns
   */
  static getDayTime(time?: number): string {
    let date = new Date()
    if (time) {
      date.setDate(time)
    }
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

  /**
   * 获取 年-月-日 时间信息
   * @returns
   */
  static getHourTime(): string {
    let date = new Date()
    let temp = ((date.getHours() < 10 ? '0' : '') + date.getHours())
    temp += ((date.getMinutes() < 10 ? ':0' : ':') + date.getMinutes())
    temp += ((date.getSeconds() < 10 ? ':0' : ':') + date.getSeconds())
    return temp
  }
}