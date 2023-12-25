import fs from '@ohos.file.fs';
import call from '@ohos.telephony.call';

export class FileModel_v2 {
  file = null
  path: string = null

  /**
   * 只能是沙盒路径下才能使用，否则找不到文件
   * 文件是否存在
   * @returns
   */
  isExit(): boolean {
    return fs.accessSync(this.path)
  }

  /**
   * 只能是沙盒路径下才能使用，否则找不到文件
   * @returns 文件属性数据
   */
  getStat(): fs.Stat {
    if (!this.file) {
      return fs.statSync(this.file.fd)
    }
    return null;
  }

  /**
   * 打开文件
   * @param path
   * @param mode fs.OpenMode.CREATE | fs.OpenMode.WRITE_ONLY
   */
  open(path: string, mode: number = fs.OpenMode.READ_ONLY) {
    if (!this.file) {
      this.path = path
      this.file = fs.openSync(this.path, mode)
    }
  }

  /**
   * 写数据
   * @param text
   */
  write(text: string) {
    if (this.file) {
      fs.writeSync(this.file.fd, text)
    }
  }

  /**
   * 读取数据
   * @param callBack
   */
  readText(callBack: (err, data: string) => void) {
    if (this.file) {
      fs.readText(this.path, callBack)
    }
  }

  /**
   * 关闭文件
   */
  close() {
    if (this.file) {
      fs.closeSync(this.file)
      this.file = null
    }
  }

  /**
   * 判定文件打开状态
   * @returns
   */
  isOpened(): boolean {
    return this.file ? true : false
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    console.error('FileModel_v2', msg)
  }
}