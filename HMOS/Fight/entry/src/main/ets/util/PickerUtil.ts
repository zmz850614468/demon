import picker from '@ohos.file.picker'

/**
 * 文件选择 帮助类
 */
export class PickerUtil {
  /**
   * 选择 图片和视频 文件需要保存的 地址uri
   * @param fileNames 需要保存的文件名
   * @param callback 地址uri
   */
  static photoSavePicker(fileNames: Array<string>, callback: (err, uris: Array<string>) => void) {
    let options = new picker.PhotoSaveOptions()
    options.newFileNames = fileNames

    new picker.PhotoViewPicker().save(options, callback)
  }

  /**
   * 选择 图片、视频 uri地址
   * @param callback
   * @param max 选择最多个数，默认1
   * @param type 选择的类型
   */
  static photoSelectPicker(callback: (err, result: picker.PhotoSelectResult) => void, max: number = 1, type: picker.PhotoViewMIMETypes = picker.PhotoViewMIMETypes.IMAGE_VIDEO_TYPE) {
    let options = new picker.PhotoSelectOptions()
    options.MIMEType = type
    options.maxSelectNumber = max

    new picker.PhotoViewPicker().select(options, callback)
  }

  /**
   * 选择 文档 文件需要保存的 地址uri
   * @param fileNames
   * @param callBack
   */
  static documentSavePicker(fileNames: Array<string>, callBack: (err, uris: Array<string>) => void) {
    let options = new picker.DocumentSaveOptions()
    options.newFileNames = fileNames

    new picker.DocumentViewPicker().save(options, callBack)
  }

  /**
   * 选择 文档 uri地址
   * @param callback
   */
  static documentSelectPicker(callback: (err, uris: Array<string>) => void) {
    let options = new picker.DocumentSelectOptions()

    new picker.DocumentViewPicker().select(options, callback)
  }

  /**
   * 选择 音频 文件需要保存的 地址uri
   * @param fileNames
   * @param callBack
   */
  static audioSavePicker(fileNames: Array<string>, callBack: (err, uris: Array<string>) => void) {
    let options = new picker.AudioSaveOptions()
    options.newFileNames = fileNames

    new picker.AudioViewPicker().save(options, callBack)
  }

  /**
   * 选择 音频 uri地址
   * @param callback
   */
  static audioSelectPicker(callback: (err, uris: Array<string>) => void) {
    let options = new picker.AudioSelectOptions()

    new picker.AudioViewPicker().select(options, callback)
  }
}