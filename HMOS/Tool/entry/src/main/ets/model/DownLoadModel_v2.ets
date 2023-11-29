import request from '@ohos.request';
import Toast from '../util/Toast';
/**
 * 下载 类
 */
export class DownLoadModel_v2 {
  urls: Array<string>
  pathArray: Array<string> = []
  downLoadCount: number

  constructor(urls: Array<string>) {
    this.urls = urls
    this.urls.forEach((url) => {
      this.pathArray.push(this.getFilePath(url))
    })
    this.downLoadCount = 0
  }

  isComplete() {
    this.downLoadCount++
    if (this.onAllComplete && this.downLoadCount == this.urls.length) {
      this.onAllComplete(this.urls, this.pathArray)
    }
  }

  /**
   * 下载图片/视频到相册
   * {"imageURLs":["http://tm.lilanz.com/qywx/res/img/appicons/p2.jpg"],
   * "videoURLs":["https://oos-fj2.ctyunapi.cn/lilanz/showcase.mp4","https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/LightBusiness.mp4","https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/pad/NewBusiness.mp4"],
   * "isShow":1}
   */
  startDownLoad() {
    this.urls.forEach((url, index) => {
      // let path = this.getFilePath(url)
      let config = this.getConfig(url, this.pathArray[index])
      try {
        request.downloadFile(getContext(this), config).then((data) => {
          // this.downloadTask = data
          this.listener(data, url, this.pathArray[index])
        })
      } catch (e) {
        this.isComplete()
        if (this.onFailure) {
          this.onFailure(-1, "下载异常：" + JSON.stringify(e))
        } else {
          Toast.show("下载异常：" + JSON.stringify(e))
        }
      }
    })
  }

  private listener(downloadTask: request.DownloadTask, url: string, path: string) {
    if (this.onProgress) {
      downloadTask.on('progress', (cur, total) => {
        this.onProgress(cur, total)
      })
    }

    if (this.onComplete) {
      downloadTask.on('complete', () => {
        this.onComplete(url, path)
        this.isComplete()
      })
    }

    if (this.onFailure) {
      downloadTask.on('fail', (code) => {
        this.onFailure(code, '下载任务失败')
        this.isComplete()
      })
    }
  }

  onProgress?: (cur: number, total: number) => void
  onComplete?: (url: string, filePath: string) => void
  onFailure?: (code: number, msg: string) => void
  onAllComplete?: (urls: Array<string>, filePaths: Array<string>) => void

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