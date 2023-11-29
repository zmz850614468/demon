import picker from '@ohos.file.picker'
import Toast from '../util/Toast'
import fs from '@ohos.file.fs';

/**
 * 复制沙盒文件到指定目录中去
 */
export class CopyFileToModel {

  /**
   * 复制 图片、视频文件 到指定位置
   * @param array
   */
  static async copyMediaTo(fileArray: Array<string>) {
    let options = new picker.PhotoSaveOptions()
    options.newFileNames = this.getFileNameArray(fileArray)

    new picker.PhotoViewPicker().save(options, (err, result) => {
      if (err) {
        Toast.show('复制媒体文件出错：' + JSON.stringify(err))
      } else {
        this.copyFileTo(fileArray, result)
        Toast.show('复制媒体文件完成')
      }
    })
  }

  /**
   * 复制 文档  到指定位置
   * @param fileArray
   */
  static async copyDocumentTo(fileArray: Array<string>) {
    let options = new picker.DocumentSaveOptions()
    options.newFileNames = this.getFileNameArray(fileArray)

    new picker.DocumentViewPicker().save(options, (err, result) => {
      if (err) {
        Toast.show('复制文档文件出错：' + JSON.stringify(err))
      } else {
        this.copyFileTo(fileArray, result)
        Toast.show('复制文档文件完成')
      }
    })
  }

  /**
   * 复制 音频文件  到指定位置
   * @param fileArray
   */
  static async copyAudioTo(fileArray: Array<string>) {
    let options = new picker.AudioSaveOptions()
    options.newFileNames = this.getFileNameArray(fileArray)

    new picker.AudioViewPicker().save(options, (err, result) => {
      if (err) {
        Toast.show('复制音频文件出错：' + JSON.stringify(err))
      } else {
        this.copyFileTo(fileArray, result)
        Toast.show('复制音频文件完成')
      }
    })
  }

  /**
   * 复制文件 到新地址
   * @param fileArray
   * @param uriArray
   */
  private static copyFileTo(fileArray: Array<string>, uriArray: Array<string>) {
    let buffer = new ArrayBuffer(4096)
    uriArray.forEach((toPath, index) => {
      let fromPath = fileArray[index]
      let fromFile = fs.openSync(fromPath, fs.OpenMode.READ_ONLY)
      let toFile = fs.openSync(toPath, fs.OpenMode.READ_WRITE)

      let count = 0
      let totalCount = 0
      while ((count = fs.readSync(fromFile.fd, buffer, { offset: totalCount })) > 0) {
        fs.writeSync(toFile.fd, buffer)
        totalCount += count
      }
      fs.closeSync(fromFile)
      fs.closeSync(toFile)
    })
  }

  /**
   * 复制文件 到新地址 - 以流的方式复制文件
   * @param fileArray
   * @param uriArray
   */
  private static copyFileToByStream(fileArray: Array<string>, uriArray: Array<string>) {
    let buffer = new ArrayBuffer(4096)
    uriArray.forEach(async (toPath, index) => {
      let fromPath = fileArray[index]
      let fromStream = fs.createStreamSync(fromPath, 'r+')
      let toStream = fs.createStreamSync(toPath, 'w+')

      let count = 0
      let totalCount = 0
      while ((count = await fromStream.read(buffer, { offset: totalCount })) > 0) {
        toStream.write(buffer)
        totalCount += count
      }
      fromStream.closeSync()
      toStream.closeSync()
    })
  }

  /**
   * 获取所有文件名字
   * @param fileArray
   * @returns
   */
  private static getFileNameArray(fileArray: Array<string>): Array<string> {
    let fileNames = []
    fileArray.forEach((path) => {
      fileNames.push(path.substring(path.lastIndexOf('/') + 1))
    })

    return fileNames
  }
}