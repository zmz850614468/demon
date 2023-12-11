import { WifiModel } from '../model/WifiModel'
import abilityAccessCtrl, { Permissions } from '@ohos.abilityAccessCtrl'
import { DownLoadModel } from '../model/DownLoadModel'
import Logger from '../util/Logger'
import picker from '@ohos.file.picker'
import fs from '@ohos.file.fs';
import { CopyFileToModel } from '../model/CopyFileToModel'
import { DownLoadModel_v2 } from '../model/DownLoadModel_v2'

@Entry
@Component
struct Index {
  @State message: string = 'Hello World'
  values: Array<number> = [1132, 950, 894]
  disArr: Array<string> = ['23/7 1132', '23/6 950', '23/4 894']

  aboutToAppear() {
    // this.requestPermission(["ohos.permission.GET_WIFI_INFO", 'ohos.permission.APPROXIMATELY_LOCATION', "ohos.permission.LOCATION"], (data) => {
    //   setTimeout(() => {
    //     new WifiModel()
    //   }, 2000)
    // })


    // 多文件下载
    // let downLoad = new DownLoadModel_v2(['https://oos-fj2.ctyunapi.cn/lilanz/showcase.mp4', 'http://tm.lilanz.com/qywx/res/img/appicons/p2.jpg'])
    // downLoad.onProgress = (cur, total) => {
    //   this.showLog(cur + '/' + total)
    // }
    // downLoad.onComplete = (url, filePath) => {
    //   // this.showLog(url + ' - ' + filePath)
    //   // this.saveFile(filePath)
    //   // SaveFileToModel.saveMediaTo([filePath])
    //   this.showLog('文件下载完成：' + url + ' - ' + filePath)
    // }
    // downLoad.onFailure = (code, msg) => {
    //   this.showLog(code + ' - ' + msg)
    // }
    // downLoad.onAllComplete = (urls, filePaths) => {
    //   CopyFileToModel.copyMediaTo(filePaths)
    //   this.showLog('全部下载完成')
    // }
    // downLoad.startDownLoad()
  }

  startDo() {
    // 单文件下载
    let arr = ['https://oos-fj2.ctyunapi.cn/lilanz/showcase.mp4', 'http://tm.lilanz.com/qywx/res/img/appicons/p2.jpg']
    arr.forEach((tempUrl) => {
      let downLoad = new DownLoadModel(tempUrl)
      downLoad.onProgress = (cur, total) => {
        // this.showLog(downLoad.url + ' - ' + cur + '/' + total)
      }
      downLoad.onComplete = (url, filePath) => {
        this.showLog('完成：' + url + ' - ' + filePath)
        // this.saveFile(filePath)
        // SaveFileToModel.saveMediaTo([filePath])
      }
      downLoad.onFailure = (code, msg) => {
        this.showLog(code + ' - ' + msg)
      }
      downLoad.startDownLoad()
    })
  }

  uri = null
  /**
   * 保存图片文件
   * @param path
   */
  saveFile(path: string) {
    let index = path.lastIndexOf('/')

    let options = new picker.PhotoSaveOptions()
    options.newFileNames = [path.substring(index + 1)]

    new picker.PhotoViewPicker().save(options).then((result) => {
      this.uri = result[0]
      this.showLog("保存文件：" + JSON.stringify(result))
      this.save(path)
    })
  }

  save(path: string) {
    let buffer = new ArrayBuffer(1024)
    let readFile = fs.openSync(path, fs.OpenMode.READ_ONLY)
    let file = fs.openSync(this.uri, fs.OpenMode.READ_WRITE)
    let count = fs.readSync(readFile.fd, buffer,)
    let totalCount = 0
    while (count > 0) {
      fs.writeSync(file.fd, buffer)

      totalCount += count
      count = fs.readSync(readFile.fd, buffer, { offset: totalCount })
    }

    fs.closeSync(file)
    fs.closeSync(readFile)
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Index', msg)
  }

  requestPermission(permission: Array<Permissions>, callBack: (result: Array<number>) => void) {
    let atManager = abilityAccessCtrl.createAtManager();
    try {
      atManager.requestPermissionsFromUser(getContext(this), permission, (err, data) => {
        console.info("tag" + JSON.stringify(data));
        callBack(data.authResults)
      });
    } catch (err) {
      console.log(`catch err->${JSON.stringify(err)}`);
    }
  }

  build() {
    Column() {

      Text('开始下载').onClick(() => {
        this.startDo()
      })
      // CircleView({ values: this.values, disArr: this.disArr })
      //   .width('50%')


      // Stack() {
      //   DataPanel({ values: [400, 300, 200], max: 900, type: DataPanelType.Circle })
      //     .width(300)
      //     .height(300)
      //   Row() {
      //     Text('11/23 666')
      //       .fontSize(15)
      //       .fontColor($r("app.color.circle_1"))
      //       .margin({ left: 8, right: 0, top: 0, bottom: 0 })
      //   }

    }.width('100%')
    .height('100%')
  }
}





