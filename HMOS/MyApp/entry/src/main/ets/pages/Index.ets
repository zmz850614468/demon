import { PermissionUtil, Toast } from 'myLibrary'
import Logger from 'myLibrary/src/main/ets/util/Logger'
import EntryAbility from '../entryability/EntryAbility'
import router from '@ohos.router';
import common from '@ohos.app.ability.common';
import window from '@ohos.window';

@Entry
@Component
struct Index {
  @State hasUserAuth: boolean = false // 用户是否授权

  aboutToAppear() {

    window.getLastWindow(getContext(this), (err, window) => {
      if (window) {
        this.showLog('获取窗口成功')
      }
      // window.setWindowSystemBarProperties({ // 设备暂不支持
      //   statusBarColor: '#ffffff',
      //   statusBarContentColor: '#ffffff',
      //   isStatusBarLightIcon: false,
      //   navigationBarColor: '#ffffff',
      //   isNavigationBarLightIcon: false,
      //   navigationBarContentColor: '#ffffff'
      // }, (err) => {
      //   this.showLog('问题：' + JSON.stringify(err))
      // })
    })


    PermissionUtil.requestFloatPermission((result) => {
      if (result == 0) {
        // Toast.show('悬浮框授权成功')
      } else {
        Toast.show('悬浮框授权失败')
      }
    })

    if (this instanceof EntryAbility) {
      this.showLog('this instanceof EntryAbility')
    } else {
      this.showLog('this not instanceof EntryAbility')
    }

    this.test()
  }

  onPageShow() {
    let param: any = router.getParams()
    if (param && param.from && param.from === 'UserAuth') {
      if (param.err) {
        Toast.show(param.err)
      }
      if (param.token) {
        this.hasUserAuth = true
        // Toast.show('用户授权成功')
      }
    }
  }

  // onBackPress() {
  //   return true
  // }

  isFloat = false

  build() {
    Column({ space: 3 }) {
      if (this.hasUserAuth) {
        Text('账号管理').item()
          .onClick(() => {
            router.pushUrl({ url: 'pages/AccountManager' })
          })
      }

      Text('用户认证').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/UserAuth' })
        })

      Text('悬浮窗口').item()
        .onClick(() => {
          this.isFloat = !this.isFloat
          let want = {
            "deviceId": "", // deviceId为空表示本设备
            "bundleName": "com.demon.myapp",
            "abilityName": "EntryAbility",
            "moduleName": "entry", // moduleName非必选
            "parameters": {
              "action": this.isFloat ? "open" : "close"
            }
          }
          let context = getContext(this) as common.UIAbilityContext
          context.startAbility(want)
        })

      Text('拖拽效果').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/DragDemo' })
        })

      Text('网络信息').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/NetInfo' })
        })

      Text('设备信息').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/DeviceInfo' })
        })

      Text('位置信息').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/LocationInfo' })
        })

      Text('显示设备属性').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/DisplayInfo' })
        })

      Text('剪切板').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/PasteBoard' })
        })

      Text('华为-serverless').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/HWServerless' })
        })

      Text('反射机子：Reflect').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/ReflectDemo' })
        })

      Text('数据库存储').item()
        .onClick(() => {
          router.pushUrl({ url: 'pages/DbDemo' })
        })

    }
    .width('100%')
  }

  test() {
    // this.showLog('1.比大小：' + ('9.14' > '9.04'))
    // this.showLog('2.比大小：' + ('9.04' > '8.59'))
    // this.showLog('3.比大小：' + ('10.59' > '09.04'))
    // this.showLog('4.比大小：' + ('14.14' > '12.04'))
    // this.showLog('5.比大小：' + ('22.14' > '09.54'))
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Index', msg)
  }
}

@Extend(Text) function item() {
  .textAlign(TextAlign.Center)
  .fontSize(17)
  .width('100%')
  .height(50)
  .borderWidth(1)
  .borderRadius(10)
  .borderColor($r("app.color.gray"))
}




