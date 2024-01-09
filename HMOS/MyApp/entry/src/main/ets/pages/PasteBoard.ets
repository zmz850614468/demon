import pasteboard from '@ohos.pasteboard'
import { Toast } from 'myLibrary'
import Logger from 'myLibrary/src/main/ets/util/Logger'

@Entry
@Component
struct PasteBoard {
  getPaste() {
    let board = pasteboard.getSystemPasteboard()
    board.getData((err, data: pasteboard.PasteData) => {
      if (err) {
        Toast.show('获取剪切板数据出错：' + JSON.stringify(err))
        this.showLog('获取剪切板数据出错：' + JSON.stringify(err))
      }
      if (data) {
        let text = data.getPrimaryText()
        this.showLog('剪切板数据：' + text)
      }
    })
  }

  setPaste() {
    let pasteData = pasteboard.createData(pasteboard.MIMETYPE_TEXT_PLAIN, '剪切板测试信息')
    pasteboard.getSystemPasteboard().setData(pasteData)
    this.showLog('设置剪切板数据成功')
  }

  build() {
    Column() {

      Text('获取剪切板内容')
        .fontSize(17)
        .textAlign(TextAlign.Center)
        .width('100%')
        .height(50)
        .borderWidth(0.5)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
        .onClick(() => {
          // hiTraceMeter.startTrace('start', 1)
          // this.getPaste()
          // hiTraceMeter.finishTrace('start', 1)
          // hiTraceMeter.traceByValue('start', 1)
        })

      Text('设置剪切板内容')
        .fontSize(17)
        .textAlign(TextAlign.Center)
        .width('100%')
        .height(50)
        .borderWidth(0.5)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
        .margin({ left: 0, right: 0, top: 30, bottom: 0 })
        .onClick(() => {
          this.setPaste()
        })

    }.height('100%')
    .justifyContent(FlexAlign.Center)
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('PasteBoard', msg)
  }
}