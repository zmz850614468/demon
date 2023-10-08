import picker from '@ohos.file.picker';

/**
 * 文件选择器
 */
export class DocModel {
  private documentPicker = new picker.DocumentViewPicker()


  /**
   * 文件选择
   * @param callback
   */
  async selectDoc(callback) {
    this.documentPicker.select().then((resultArr: Array<string>) => {
      if (callback != null) {
        callback(resultArr)
      }
    })
  }

  /**
   * 保存文件
   * @param callback
   */
  async saveDoc(callback) {
    this.documentPicker.save().then((resultAr: Array<string>) => {
      if (callback != null) {
        callback(resultAr)
      }
    })
  }
}