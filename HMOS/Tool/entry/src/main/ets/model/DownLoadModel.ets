import request from '@ohos.request';
import Logger from '../util/Logger';
import Toast from '../util/Toast';
/**
 * 下载 类
 */
export class DownLoadModel {
  downloadTask: request.DownloadTask = null
  url: string
  filePath: string

  constructor(url: string) {
    this.url = url
    this.filePath = this.getFilePath(url)
  }

  /**
   * 下载图片/视频到相册
   * {"imageURLs":["http://tm.lilanz.com/qywx/res/img/appicons/p2.jpg"],
   * "videoURLs":["https://oos-fj2.ctyunapi.cn/lilanz/showcase.mp4","https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/LightBusiness.mp4","https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/pad/NewBusiness.mp4"],
   * "isShow":1}
   */
  startDownLoad() {
    let config = this.getConfig(this.url, this.filePath)
    try {
      Logger.error('tag', this.url + ' - ' + this.filePath)
      request.downloadFile(getContext(this), config).then((data) => {
        this.downloadTask = data
        this.listener()
      })
    } catch (e) {
      if (this.onFailure) {
        this.onFailure(-1, "下载异常：" + JSON.stringify(e))
      } else {
        Toast.show("下载异常：" + JSON.stringify(e))
      }
    }
  }

  private listener() {
    if (this.onProgress) {
      this.downloadTask.on('progress', (cur, total) => {
        Logger.error('tag', this.url + ' - ' + cur + ' - ' + total)
        this.onProgress(cur, total)
      })
    }

    if (this.onComplete) {
      this.downloadTask.on('complete', async () => {
        this.onComplete(this.url, this.filePath)
        // Logger.error('index','完成：' + this.url + ' - ' + this.filePath)
      })
    }

    if (this.onFailure) {
      this.downloadTask.on('fail', (code) => {
        this.onFailure(code, '下载任务失败')
      })
    }
  }

  onProgress?: (cur: number, total: number) => void
  onComplete?: (url: string, filePath: string) => void
  onFailure?: (code: number, msg: string) => void

  /**
   *
   * @param url 网络地址
   * @param filePath 存储地址
   * @param isBackNotify 是否后退通知
   * @returns
   */
  getConfig(url: string, filePath: string): request.DownloadConfig {
    let config = {
      url: url,
      filePath: filePath,
      networkType: request.NETWORK_MOBILE & request.NETWORK_WIFI,
      // background: isBackNotify
    }

    return config
  }

  /**
   * 获取存储地址
   * @param url
   * @returns
   */
  getFilePath(url: string): string {
    let index = url.lastIndexOf('/')
    return getContext(this).tempDir + url.substring(index)
  }
}