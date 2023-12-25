import Logger from './Logger'
import fs from '@ohos.file.fs';

export class FileUtil {

  /**
   * 复制文件
   * @param from
   * @param to
   */
  static moveFile(from: string, to: string) {
    let buffer = new ArrayBuffer(2048)
    let readFile = fs.openSync(from, fs.OpenMode.READ_ONLY)
    let writeFile = fs.openSync(to, fs.OpenMode.CREATE | fs.OpenMode.WRITE_ONLY)

    let count = 0
    let totalCount = 0
    while ((count = fs.readSync(readFile.fd, buffer, { offset: totalCount })) > 0) {
      fs.writeSync(writeFile.fd, buffer, { length: count })
      totalCount += count
    }

    fs.closeSync(writeFile)
    fs.closeSync(readFile)
  }

  /**
   * 获取 app 的文件地址
   * @param context
   * @returns
   */
  static getAppDirs(context): Array<string> {
    let fileDirs: Array<string> = []
    fileDirs.push(context.cacheDir)
    fileDirs.push(context.tempDir)
    fileDirs.push(context.filesDir)
    fileDirs.push(context.preferencesDir)
    fileDirs.push(context.databaseDir)
    fileDirs.push(context.distributedFilesDir)
    fileDirs.push(context.bundleCodeDir)

    return fileDirs
  }

  /**
   * 列出所有文件信息，测试用
   * @param files
   */
  static async listFiles(files: Array<string>) {
    let size = files.length
    let path = null
    for (let i = 0; i < size; i++) {
      path = files[i]
      let arr = await fs.listFile(path)
      this.showLog(path + ' -- ' + JSON.stringify(arr))
    }
  }

  /**
   * 清空 缓存 和 临时文件夹 下的所有文件
   */
  static async clearCacheAndTempFiles(context) {
    let fileDirs: Array<string> = []
    fileDirs.push(context.cacheDir)
    fileDirs.push(context.tempDir)

    let size = fileDirs.length
    for (let i = 0; i < size; i++) {
      let arr = await fs.listFile(fileDirs[i])
      arr.forEach((name) => {
        let path = fileDirs[i] + '/' + name
        let stat: fs.Stat = fs.statSync(path)
        if (stat.isDirectory()) {
          fs.rmdirSync(path)
        } else if (stat.isFile()) {
          fs.unlink(path)
        }
      })
    }
  }

  /**
   * 获取存储地址
   * @param path
   * @returns
   */
  static getFileName(path: string): string {
    return path.substring(path.lastIndexOf('/') + 1)
  }

  /**
   * 日志信息
   * @param msg
   */
  static showLog(msg: string) {
    Logger.error('FileUtil', msg)
  }
}