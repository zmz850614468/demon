import fs from '@ohos.file.fs';
import Logger from '../util/Logger';
import fileUri from '@ohos.file.fileuri';
import File from '@system.file';

export class FileModel_v2 {
  file: fs.File = null
  path: string = null

  /**
   * 文件是否存在
   * @returns
   */
  isExit(): boolean {
    return fs.accessSync(this.path)
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
   * 同步分段读取数据
   * @param options
   * @returns
   */
  readTextSync(options?: {
    offset?: number;
    length?: number;
    encoding?: string;
  }): string {
    if (this.file) {
      return fs.readTextSync(this.path, options)
    }

    return ''
  }


  /**
   * 读取数据
   * @param callBack
   */
  readSync(callBack: (data: string) => void) {
    if (this.file) {
      let offset = 0

    }
  }

  /**
   * 读取数据 - 同步
   * @param callBack
   * @returns
   */
  // async readTextSync(): Promise<string> {
  //   if (this.file) {
  //     return await fs.readText(this.path)
  //   }
  //
  //   return ''
  // }

  /**
   * 读取数据
   * @param callBack
   */
  // async readText2(callBack: (data: string) => void, length: number = 1024) {
  // if (this.file) {
  //   let offset = 0
  //   while (true) {
  //     let data = await fs.readText(this.path, { offset: offset, length: length })
  //     callBack(data)
  //     if (data.length <= 0) {
  //       break
  //     }
  //     offset += length
  //   }
  // }
  // }

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
    Logger.error('FileModel_v2', msg)
  }
}