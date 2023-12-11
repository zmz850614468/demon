import fs from '@ohos.file.fs'
import Logger from '../util/Logger';
import File from '@system.file';

/**
 * 文件读写
 */
export default class FileModel {

  /**
   * 获取文件详细属性信息
   * @param path
   * @param callback
   */
  stat(path: string, callback) {
    fs.stat(path).then((stat: fs.Stat) => {
      if (callback != null) {
        callback(stat)
      }
    })
  }

  /**
   * 同步方法
   * 判定文件是否存在
   * @param path
   * @returns
   */
  accessSync(path: string): boolean {
    return fs.accessSync(path)
  }

  /**
   * 同步方法
   * 读取文件信息
   * @param path
   */
  readTextSync(path: string): string {
    return fs.readTextSync(path)
  }

  /**
   * 同步方法
   * 打开文件
   * @param path path or uri
   * @returns
   */
  // openSync(path: string): fs.File {
  //   return fs.openSync(path, fs.OpenMode.READ_WRITE)
  // }

  readSync(path: string): string {
    let file = fs.openSync(path, fs.OpenMode.READ_ONLY)
    let buf = new ArrayBuffer(4096);
    let num = fs.readSync(file.fd, buf)
    fs.closeSync(file)

    buf.slice(0,num);
    // Logger.error('tag', 'content = ' + s)
    return buf.toString()
  }
}