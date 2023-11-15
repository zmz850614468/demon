import router from '@ohos.router'
import Logger from '../common/util/Logger';
import Notification from '@ohos.notificationManager';
import Rdb_v3 from '../database_v2/Rdb_v3';
import TestBean from '../database_v2/bean/TestBean';
import Toast from '../common/util/Toast';

@Entry
@Component
struct Function {
  itemList = []

  aboutToAppear() {
    this.itemList.push([$r('app.media.fl'), '复利', 'pages/FuLi'])
    this.itemList.push([$r('app.media.fy'), '交易费用', 'pages/Cost'])
    this.itemList.push([$r('app.media.exchange'), '数据接收', 'pages/ReceiverData'])
    this.itemList.push([$r('app.media.crj'), '出入金', 'pages/Money'])
    this.itemList.push([$r('app.media.fxsj'), '数据分析', 'pages/DataAnalysis'])
    this.itemList.push([$r('app.media.ic_record'), '操作详情', 'pages/OperateResult'])
    this.itemList.push([$r('app.media.ic_memo'), '当前操作', 'pages/Operate'])

    // Logger.error('tag', null ? 'null = true' : 'null = false')
    // Logger.error('tag', undefined ? 'undefined = true' : 'undefined = false')
    // Logger.error('tag', 'undefined' ? 'str = true' : 'str = false')

    let table = new Rdb_v3(TestBean.info, (b) => {
      Toast.show('表初始化：' + b)
      let bean = new TestBean()
      bean.date = "2023-11-15"
      bean.result = -8888
      bean.memo = '测试'
      let bean2 = new TestBean()
      bean2.date = "2023-11-15"
      bean2.result = -6666
      bean2.memo = '测试'

      table.batchInsertData([bean, bean2])
      // table.deleteData(table)
      // table.insertData(bean)


      table.query(TestBean, (list: Array<TestBean>) => {
        list.forEach((item) => {
          Logger.error('tag', JSON.stringify(item))
        })
      })


    })

  }

  build() {
    Column() {
      Grid() {
        ForEach(this.itemList, (item, index) => {
          GridItem() {
            this.rowItem(item[0], item[1], item[2])
          }
        })
      }
      .align(Alignment.Center)
      .layoutDirection(GridDirection.Row)
      .maxCount(3)
      .minCount(3)
      .rowsGap(20)
    }.padding({ left: 20, right: 20, top: 20, bottom: 20 })
  }

  /**
   * 每个选项
   * @param res
   * @param text
   * @param path
   */
  @Builder
  rowItem(res: Resource, text: string, path: string) {
    Column() {
      Image(res)
        .width(50)
        .height(50)

      Text(text)
        .fontSize(17)
        .margin({ left: 0, right: 0, top: 10, bottom: 0 })
        .textAlign(TextAlign.Center)
    }
    .width('33%')
    .height(85)
    .onClick(() => {
      router.pushUrl({ url: path })
    })
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
