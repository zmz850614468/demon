import window from '@ohos.window'
import systemDateTime from '@ohos.systemDateTime'
import myPreference, { FLOAT_POSITION } from '../model/PreferenceModel'
import Logger from '../util/Logger'

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
  }

  aboutToDisappear() {
  }

  async initData() {
    let position = await myPreference.getObj(FLOAT_POSITION)
    if (position) {
      this.floatPosition = position
    }
    // this.showLog('current:' + await systemDateTime.getCurrentTime())
  }

  count = 0

  build() {
    Column() {
      TextClock()
        .format('HH:mm:ss')
        .fontSize(13)
        .fontColor($r('app.color.deepRed'))
        .onDateChange((value: number) => {
          // this.showLog('time:' + StringUtil.getHourTime())
        })
    }
    .borderWidth(0.5)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
    .backgroundColor('#AA000000')
    .justifyContent(FlexAlign.Center)
    .width('100%')
    .height('100%')
    .onTouch(event => {
      if (event.type === TouchType.Move) {
        if (++this.count == 5) {
          this.count = 0
          this.gx = (event.touches[0].screenX - this.px) * 1 + this.floatPosition.x
          this.gy = (event.touches[0].screenY - this.py) * 1 + this.floatPosition.y
          if (this.currentWindow) {
            this.currentWindow.moveWindowTo(this.gx, this.gy)
            // this.showLog('(x,y)=(' + event.touches[0].x.toFixed(1) + ',' + event.touches[0].y.toFixed(1) + ')')
            // ('(sx,sy)=(' + event.touches[0].screenX + ',' + event.touches[0].screenY + ')')
          }
        }
      }
      else if (event.type === TouchType.Down) {
        this.px = event.touches[0].x
        this.py = event.touches[0].y
        // this.showLog('down position:(' + this.px + ',' + this.py + ')')
      } else if (event.type === TouchType.Up) {
        this.floatPosition.x = this.gx
        this.floatPosition.y = this.gy
        // this.showLog('up position:(' + this.gx + ',' + this.gy + ')')
        myPreference.saveObj(FLOAT_POSITION, this.floatPosition)
      }
    })
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Float', msg)
  }
}


