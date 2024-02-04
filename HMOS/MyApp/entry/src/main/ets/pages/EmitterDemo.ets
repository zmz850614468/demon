import emitter from '@ohos.events.emitter'
import Logger from 'myLibrary/src/main/ets/util/Logger'

@Entry
@Component
struct EmitterDemo {
  @State message: string = 'emitter 类似于广播'
  private count: number = 1

  aboutToAppear() {

  }

  /**
   * 事件回调函数
   * @param eventData
   */
  private callBack(eventData: emitter.EventData) {
    this.message = '接收到数据：' + eventData.data.count
    this.showLog('接收到数据：' + eventData.data.count)
  }

  build() {
    Column() {
      Button('emitter.on()-开始监听').width('80%').margin({ top: 10 }).onClick(() => {
        let event: emitter.InnerEvent = { eventId: 1 }
        emitter.on(event, (eventData: emitter.EventData) => {
          this.callBack(eventData)
        })
        this.message = '触发emitter.on()事件'
      })
      Button('emitter.once()-单次监听').width('80%').margin({ top: 10 }).onClick(() => {
        let event: emitter.InnerEvent = { eventId: 1 }
        emitter.once(event, (eventData: emitter.EventData) => {
          this.callBack(eventData)
        })
        this.message = '触发emitter.once()事件'
      })
      Button('emitter.off()-结束监听').width('80%').margin({ top: 10 }).onClick(() => {
        emitter.off(1)
        this.message = '触发emitter.off()事件'
      })
      Button('emitter.emit()-发送事件').width('80%').margin({ top: 10 }).onClick(() => {
        let data: emitter.EventData = {
          data: {
            'count': this.count++
          }
        }
        let event: emitter.InnerEvent = { eventId: 1, priority: emitter.EventPriority.LOW }
        // this.message = '触发emitter.emit()事件'
        emitter.emit(event, data)
      })

      Text(this.message)
        .fontSize(21).margin({ top: '10vp' })
    }
    .width('100%')
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('EmitterDemo', msg)
  }
}