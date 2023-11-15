/**
 * 数据接收
 */
import Toast from '../common/util/Toast'
import BaseTable_v2 from '../database_v2/BaseTable_v2'
import DayOperateBean from '../database_v2/bean/DayOperateBean'
import OperateBean from '../database_v2/bean/OperateBean'
import DBWebSocketListener from '../model/DBWebSocketListener'
import WebSocketModel from '../model/WebSocketModel'

@Entry
@Component
struct ReceiverData {
  @State timeCount: number = 0
  @State dayCount: number = 0
  model: WebSocketModel = null

  aboutToAppear() {
    this.model = new WebSocketModel(new DBWebSocketListener((msgType: number) => {
      switch (msgType) {
        case 1:
          this.timeCount++
          break
        case 2:
          this.dayCount++
          break
      }
    }))
  }

  aboutToDisappear() {
    // this.model.close()
  }

  build() {
    Column() {
      Text("数据传输")
        .fontSize(19)
        .width('100%')
        .height(50)
        .textAlign(TextAlign.Center)

      Text("次数据量：" + this.timeCount)
        .fontSize(17)
        .width('100%')
        .height(40)
        .margin({ top: 100 })
        .padding({ left: 20, right: 20 })

      Text("天数据量：" + this.dayCount)
        .fontSize(17)
        .width('100%')
        .height(40)
        .margin({ top: 5 })
        .padding({ left: 20, right: 20 })

      Button("开始数据传输")
        .fontSize(17)
        .width('60%')
        .height(40)
        .margin({ top: 80 })
        .onClick(() => {
          if (!this.model.isOpened()) {
            this.model.connect("ws://192.168.10.102:16677")
            setTimeout(() => { // 延迟发送信息
              this.model.send("hm-os")
            }, 500)
          } else {
            Toast.show("webSocket已经开启")
          }
        })

      Button("删除所有数据")
        .fontSize(17)
        .width('45%')
        .height(40)
        .backgroundColor($r("app.color.red"))
        .margin({ top: 200 })
        .onClick(() => {
          AlertDialog.show({
            title: "注意",
            message: "确定要删除所有数据吗？",
            confirm: {
              value: "确定",
              action: () => {
                let dayOperateTable = new BaseTable_v2(DayOperateBean.TABLE_NAME, DayOperateBean.SQL, DayOperateBean.COLUMNS, (b: boolean) => {
                  dayOperateTable.exeSql('delete from ' + DayOperateBean.TABLE_NAME + ' where id>0') // 删除所有数据
                })

                // 每笔数据
                let operateTable = new BaseTable_v2(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS, (b: boolean) => {
                  operateTable.exeSql('delete from ' + OperateBean.TABLE_NAME + ' where id>0') // 删除所有数据
                  Toast.show('数据删除完成')
                })
              },
            }
          })
        })
    }
  }
}


