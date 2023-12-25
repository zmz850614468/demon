import Logger from 'myLibrary/src/main/ets/util/Logger'
import common from '@ohos.app.ability.common'
import window from '@ohos.window'
import { PreferenceModel } from 'myLibrary/src/main/ets/model/PreferenceModel'
import { PreferenceConst } from '../constance/PreferenceConst'
import systemDateTime from '@ohos.systemDateTime'
import { StringUtil } from 'myLibrary'

@Entry
@Component
struct Float {
  @State timeMsg: string = '00:00:00'
  @State gx: number = 0
  @State gy: number = 0
  floatPosition = {
    'x': 300, 'y': 300
  }
  private px = 0
  private py = 0
  currentWindow = null
  timerId: number

  aboutToAppear() {
    this.initData()
    this.currentWindow = window.findWindow('floatWindow')
    this.currentWindow.setBackgroundColor(Color.Transparent)
    this.currentWindow.setWindowFocusable(false)
    this.currentWindow.setWindowSystemBarProperties({ statusBarColor: '#ffffff' })
  }

  aboutToDisappear() {
  }

  async initData() {
    let position = await PreferenceModel.getInstance().getObj(PreferenceConst.FloatPosition)
    if (position) {
      this.floatPosition = position
    }
    this.showLog('position:' + JSON.stringify(this.floatPosition))
    this.currentWindow.moveWindowTo(this.floatPosition.x * 3, this.floatPosition.y * 3)
  }

  @State dragStarted: boolean = false
  count = 0
  @State tempPosition: Position = { x: 0, y: 0 }

  build() {
    // 移动模式一：有bug
    Column() {
      TextClock()
        .format('HH:mm:ss')
        .fontSize(13)
        .fontColor($r('app.color.deepRed'))
    }
    .width(66)
    .height(23)
    .borderWidth(0.5)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
    .backgroundColor('#AA000000')
    .justifyContent(FlexAlign.Center)
    .position(this.tempPosition)
    .onTouch(event => {
      if (event.type === TouchType.Move) {
        if (++this.count > 3) {
          this.tempPosition = { x: event.touches[0].x - this.px, y: event.touches[0].y - this.py }
        }
      }
      else if (event.type === TouchType.Down) {
        this.count = 0
        this.px = event.touches[0].x
        this.py = event.touches[0].y
        this.tempPosition = { x: this.floatPosition.x, y: this.floatPosition.y }
        this.showLog('position:' + JSON.stringify({ x: this.floatPosition.x, y: this.floatPosition.y }))
        this.currentWindow.resize(1500, 3000)
        this.currentWindow.moveWindowTo(0, 0)
      } else if (event.type === TouchType.Up) {
        this.tempPosition = { x: 0, y: 0 }
        this.currentWindow.resize(200, 70)
        this.currentWindow.moveWindowTo((event.touches[0].screenX - this.px) * 3, (event.touches[0].screenY - this.py) * 3)
        this.floatPosition.x = event.touches[0].x - this.px
        this.floatPosition.y = event.touches[0].y - this.py

        PreferenceModel.getInstance().saveObj(PreferenceConst.FloatPosition, this.floatPosition)
      }
    })


    // 移动模式二：有bug
    // Column() {
    //   TextClock()
    //     .format('HH:mm:ss')
    //     .fontSize(13)
    //     .fontColor($r('app.color.deepRed'))
    // }
    // .width('100%')
    // .height('100%')
    // .borderWidth(0.5)
    // .borderRadius(10)
    // .borderColor($r("app.color.gray"))
    // .backgroundColor('#AA000000')
    // .justifyContent(FlexAlign.Center)
    // .onTouch(event => {
    //   if (event.type === TouchType.Move) {
    //     if (++this.count == 5) {
    //       this.count = 0
    //       this.gx = (event.touches[0].screenX - this.px) + this.floatPosition.x
    //       this.gy = (event.touches[0].screenY - this.py) + this.floatPosition.y
    //       if (this.currentWindow) {
    //         this.currentWindow.moveWindowTo(this.gx * 3.3, this.gy * 3.3)
    //       }
    //     }
    //   }
    //   else if (event.type === TouchType.Down) {
    //     this.px = event.touches[0].x
    //     this.py = event.touches[0].y
    //   } else if (event.type === TouchType.Up) {
    //     this.floatPosition.x = this.gx
    //     this.floatPosition.y = this.gy
    //     PreferenceModel.getInstance().saveObj(PreferenceConst.FloatPosition, this.floatPosition)
    //   }
    // })
  }

  @Builder drawItem() {
    Column() {
      TextClock()
        .format('HH:mm:ss')
        .fontSize(13)
        .fontColor($r('app.color.deepRed'))
    }
    .width('100%')
    .height('100%')
    .borderWidth(0.5)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
    .backgroundColor('#AA000000')
    .justifyContent(FlexAlign.Center)

  }

  @Builder drawItem2() {
    Column() {
      TextClock()
        .format('HH:mm:ss')
        .fontSize(13)
        .fontColor($r('app.color.deepRed'))
    }
    .width('100%')
    .height('100%')
    .borderWidth(0.5)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
    .backgroundColor('#AA000000')
    .justifyContent(FlexAlign.Center)

  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Float', msg)
  }
}


