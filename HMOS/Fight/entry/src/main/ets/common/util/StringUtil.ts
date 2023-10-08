import systemDateTime from '@ohos.systemDateTime'

export default class StringUtil {
  /**
   * 获取 年-月-日 时间信息
   * @returns
   */
  static async getDayTime(): Promise<string> {
    let date = new Date(await systemDateTime.getCurrentTime())
    return date.getFullYear() + '-' + (date.getMonth() + 1) + "-" + date.getDate()
  }
}