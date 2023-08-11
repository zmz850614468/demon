import media from '@ohos.multimedia.media'
import StartOptions from '@ohos.app.ability.StartOptions'
import router from '@ohos.router'
import PreferenceModel from '../model/PreferenceModel'
import Logger from '../common/util/Logger';
import webSocket from '@ohos.net.webSocket';

@Entry
@Component
struct Function {
  // @State message: string = 'Hello World'

  build() {

    Column({ space: 15 }) {

      this.rowItem($r('app.media.icon'), '复利', 'pages/FuLi')
      this.rowItem($r('app.media.icon'), '交易费用', 'pages/Cost')
      this.rowItem($r('app.media.ic_record'), '历史结果', 'pages/OperateResult')
      // this.rowItem($r('app.media.icon'), '当前操作', 'pages/')

    }
    .height('85%')

    // Column() {
    //   this.rowItem($r('app.media.icon'), '复利', 'pages/FuLi')
    // }
    // .height('15%')

  }

  /**
   * 每个选项
   * @param res
   * @param text
   * @param path
   */
  @Builder rowItem(res: Resource, text: string, path: string) {
    Row() {
      Image(res)
        .width(40)
        .height(40)
      Text(text)
        .fontSize(21)
        .margin({ left: 5 })
        .height(40)
        .width('100%')
        .padding({ left: 10 })
        .onClick(() => {
          router.pushUrl({ url: path })
        })
    }
    .height(50)
    .width('100%')
    .padding({ left: 20, top: 5, bottom: 5 })
    .justifyContent(FlexAlign.Start)
  }

  test() {
    // let defaultIpAddress = "ws://";
    // let ws = webSocket.createWebSocket();
    // webSocket
  }
}