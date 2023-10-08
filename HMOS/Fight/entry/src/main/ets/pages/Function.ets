import router from '@ohos.router'
import Logger from '../common/util/Logger';
import Notification from '@ohos.notificationManager';

@Entry
@Component
struct Function {
  // onPageShow() {
  //   Logger.error("tag", "Function  onPageShow()")
  // }
  //
  // onPageHide() {
  //   Logger.error("tag", "Function  onPageHide()")
  // }
  //
  aboutToAppear() {
    Logger.error("tag", "Function  aboutToAppear()")
  }

  aboutToDisappear() {
    Logger.error("tag", "Function  aboutToDisappear()")
  }

  build() {
    Column({ space: 15 }) {

      // this.rowItem($r('app.media.exchange'), '数据接收', 'pages/ReceiverData')
      this.rowItem($r('app.media.crj'), '出入金', 'pages/Money')
      this.rowItem($r('app.media.fl'), '复利', 'pages/FuLi')
      this.rowItem($r('app.media.fy'), '交易费用', 'pages/Cost')
      this.rowItem($r('app.media.ic_record'), '历史详情', 'pages/OperateResult')
      // this.rowItem($r('app.media.ic_memo'), '当前操作', 'pages/')
    }
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
        .fontSize(19)
        .margin({ left: 5 })
        .height(40)
        .width('100%')
        .padding({ left: 10 })
        .onClick(() => {
          router.pushUrl({ url: path })
          this.test()
        })
    }
    .height(50)
    .width('100%')
    .padding({ left: 20, top: 5, bottom: 5 })
    .justifyContent(FlexAlign.Start)
  }

  // table: BaseTable_v2<OperateBean>

  // model = new WebSocketModel(new DBWebSocketListener())

  test() {


    // let inter = 5 // 间隔时间
    // let isTiped = false
    // setInterval(() => {
    //   systemDateTime.getCurrentTime((err, time) => {
    //     if (err) {
    //       Logger.error('tag', 'err = ' + JSON.stringify(err))
    //     } else {
    //       time /= 1000
    //       time %= inter * 60
    //       if (!isTiped && time > (inter - 1) * 60) {
    //         Logger.error('tag', 'time = ' + time)
    //         this.notify("title", "tex")
    //         isTiped = true
    //       } else if (isTiped && time < 60) {
    //         isTiped = false
    //       }
    //     }
    //   })
    // }, 1000)


    // new HttpModel().getRequest("http://192.168.35.90:8900/svr-external/fabricReport/report4FKKZ?bjdjh=100981&bjrq=2022-04-12&ph=11&lydjlx=624&lydjid=2707019&userid=26965",
    //   (code: number, err) => {
    //     Logger.error('tag', code + " - " + err)
    //   },
    //   (data) => {
    //     Logger.error('tag', JSON.stringify(data))
    //   })

    // this.table = new BaseTable_v2(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS, (b: boolean) => {
    //   Logger.error("数据表初始化结果：" + b)
    // })

    // this.model.connect("ws://192.168.10.102:16677")
    // setTimeout(() => { // 延迟发送信息
    //   this.model.send("hm-os")
    // }, 500)


    // new DocModel(getContext(this)).saveDoc(null);

    // if (this.table !== undefined) {
    //   this.table.queryAll(new OperateBean(), (result: OperateBean[]) => {
    //
    //     let i = 0
    //     result.forEach((item) => {
    //       this.table.deleteData(item, null)
    //     })
    //   })
    // } else {
    //   this.table = new BaseTable_v2<OperateBean>(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS)
    // }

  }

  /**
   * 发送通知消息
   * @param title
   * @param text
   * @param additionalText
   */
  private notify(title: string, text: string, additionalText: string = "") {
    //通知Request对象
    let notificationRequest = {
      id: Math.random() * 1000,
      content: {
        contentType: Notification.ContentType.NOTIFICATION_CONTENT_BASIC_TEXT,
        normal: {
          title: title,
          text: text,
          additionalText: additionalText
        }
      }
    };

    Notification.publish(notificationRequest)
  }
}
