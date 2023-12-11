import { AVPlayerModel, PermissionUtil, Toast } from 'myLibrary'
import Logger from 'myLibrary/src/main/ets/util/Logger'
import common from '@ohos.app.ability.common'
import EntryAbility from '../entryability/EntryAbility'

@Entry
@Component
struct Index {
  aboutToAppear() {

    PermissionUtil.requestFloatPermission((result) => {
      if (result == 0) {
        Toast.show('悬浮框授权成功')
      } else {
        Toast.show('悬浮框授权失败')
      }
    })

    if (this instanceof EntryAbility) {
      this.showLog('this instanceof EntryAbility')
    } else {
      this.showLog('this not instanceof EntryAbility')
    }
  }

  onBackPress() {
    return true
  }

  build() {
    Row() {
      Column() {
        Text('打开悬浮窗')
          .fontSize(15)
          .onClick(() => {
            let want = {
              "deviceId": "", // deviceId为空表示本设备
              "bundleName": "com.demon.myapp",
              "abilityName": "EntryAbility",
              // "abilityName": "FloatAbility",
              "moduleName": "entry", // moduleName非必选
              "parameters": {
                "action": "open"
              }
            }
            let context = getContext(this) as common.UIAbilityContext
            context.startAbility(want)
          })

        Text('关闭悬浮窗')
          .fontSize(15)
          .margin({ left: 0, right: 0, top: 30, bottom: 0 })
          .onClick(() => {
            let want = {
              "deviceId": "", // deviceId为空表示本设备
              "bundleName": "com.demon.myapp",
              "abilityName": "EntryAbility",
              "moduleName": "entry", // moduleName非必选
              "parameters": {
                "action": "close"
              }
            }
            let context = getContext(this) as common.UIAbilityContext
            context.startAbility(want)
          })

        Text('播放音乐')
          .fontSize(15)
          .margin({ left: 0, right: 0, top: 30, bottom: 0 })
          .onClick(() => {
            this.player.play(getContext(this), 'succeed.wav', 3)
          })


      }
      .width('100%')
    }
    .height('100%')
  }

  player: AVPlayerModel = new AVPlayerModel()

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Index', msg)
  }
}


