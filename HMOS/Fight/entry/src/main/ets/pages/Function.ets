import router from '@ohos.router'
import Logger from '../util/Logger';
import Notification from '@ohos.notificationManager';
import Toast from '../util/Toast';
import systemDateTime from '@ohos.systemDateTime';
import StringUtil from '../util/StringUtil';
import window from '@ohos.window';
import common from '@ohos.app.ability.common';
import { PermissionUtil } from '../util/PermissionUtil';
import { AVPlayerModel } from '../model/AVPlayerModel';
import { OperateDataModel } from '../model/OperateDataModel';

@Entry
@Component
struct Function {
  @State hasAuth: boolean = false // 用户是否授权
  player: AVPlayerModel = new AVPlayerModel()
  @State isOn: boolean = false
  timerId: number
  @State itemList: Array<any> = []
  hourArr: Array<string> = []
  @Watch('addShowItem')
  @State isShow: boolean = false

  aboutToAppear() {
    OperateDataModel.getInstance()
    this.initData()
    PermissionUtil.requestFloatPermission((result) => {
      if (result == 0) {
        Toast.show('悬浮框授权成功')
      } else {
        Toast.show('悬浮框授权失败')
      }
    })

    // 三个占位用
    this.itemList.push([, '', ''])
    this.itemList.push([, '', ''])
    this.itemList.push([, '', ''])
    this.itemList.push([, '', ''])
    this.itemList.push([, '', ''])
    this.itemList.push([, '', ''])
    this.itemList.push([$r('app.media.crj'), '出入金', 'pages/Money'])
    this.itemList.push([$r('app.media.fl'), '复利', 'pages/FuLi'])
    this.itemList.push([$r('app.media.fy'), '交易费用', 'pages/Cost'])
    // this.itemList.push([$r('app.media.exchange'), '数据接收', 'pages/ReceiverData'])
    // this.itemList.push([$r('app.media.ic_translate_data'), '数据迁移', 'pages/FileManager'])
    this.itemList.push([$r('app.media.ic_record'), '操作详情', 'pages/OperateResult'])
    this.itemList.push([$r('app.media.fxsj'), '数据分析', 'pages/DataAnalysis'])
    this.itemList.push([$r('app.media.ic_compare'), '数据对比', 'pages/DataCompare'])
    this.itemList.push([$r('app.media.ic_memo'), '当前操作', 'pages/Operate'])
    this.itemList.push([$r('app.media.ic_target'), '目标', 'pages/Target'])
    this.itemList.push([$r('app.media.zjzh'), '最佳组合', 'pages/BestCombination'])

    router.pushUrl({ url: 'pages/UserAuth' })
  }

  addShowItem() {
    this.itemList.push([$r('app.media.exchange'), '数据接收', 'pages/ReceiverData'])
    this.itemList.push([$r('app.media.ic_translate_data'), '数据迁移', 'pages/FileManager'])
  }

  initData() {
    this.hourArr.push('09:59')
    this.hourArr.push('11:14')
    this.hourArr.push('14:14')
    this.hourArr.push('14:59')
    this.hourArr.push('21:59')
    this.hourArr.push('22:59')
  }

  onPageShow() {
    let param: any = router.getParams()
    if (param && param.from && param.from === 'UserAuth') {
      if (param.err) {
        Toast.show(param.err)
      }
      if (param.token) {
        // Toast.show('用户授权成功')
        this.hasAuth = true
      }
    }
  }

  build() {
    Stack({ alignContent: Alignment.TopStart }) {
      if (this.hasAuth) {
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
        }.padding({ left: 10, right: 10, top: 20, bottom: 20 })
      }

      // if (!this.isOn) {
      Image($r('app.media.operate'))
        .width('98%')
        .margin({ left: '1%', right: '1%', top: 15, bottom: 0 })
      // }

      Toggle({ type: ToggleType.Checkbox, isOn: this.isOn })
        .onChange((isOn) => {
          this.toggleFloat(isOn)

          // todo 提示声音
          this.isOn = isOn
          if (this.isOn) {
            if (!this.hasAuth) {
              router.pushUrl({ url: 'pages/UserAuth' })
            }
            this.startTime()
          } else {
            clearInterval(this.timerId)
          }
        })
        .height(20)
        .margin({ left: 15, right: 0, top: 15, bottom: 0 })
        .gesture(LongPressGesture().onAction(() => {
          this.isShow = true
        }))
    }
  }

  /**
   * 悬浮窗的 开关
   * @param isOn
   */
  private toggleFloat(isOn: boolean) {
    let want = null
    if (isOn) {
      want = {
        "deviceId": "", // deviceId为空表示本设备
        "bundleName": "com.demon.fight_hm",
        "abilityName": "EntryAbility",
        "moduleName": "entry", // moduleName非必选
        "parameters": {
          "action": "open"
        }
      }
    } else {
      want = {
        "deviceId": "", // deviceId为空表示本设备
        "bundleName": "com.demon.fight_hm",
        "abilityName": "EntryAbility",
        "moduleName": "entry", // moduleName非必选
        "parameters": {
          "action": "close"
        }
      }
    }
    let context = getContext(this) as common.UIAbilityContext
    context.startAbility(want)
  }

  /**
   * 启动计时器
   */
  startTime() {
    let inter = 5 // 间隔时间
    let isTiped = false
    this.timerId = setInterval(() => {
      systemDateTime.getCurrentTime((err, time) => {
        if (err) {
          Logger.error('tag', 'err = ' + JSON.stringify(err))
        } else {
          let temp = StringUtil.getHourTime(time)
          time /= 1000
          time %= inter * 60
          if (!isTiped && time > (inter - 1) * 60) {
            if (!('10:19' === temp || '10:24' === temp)) { // 休息期间不提示
              if (this.hourArr.indexOf(temp) >= 0) { // 整点
                this.player.play(getContext(this), 'succeed_2.wav', 4)
                this.notify("提示", "整点-" + temp)
              } else if (((temp < '10:20' || temp > '20.00') && temp.endsWith('4')) ||
              (temp > '10:20' && temp < '20.00' && temp.endsWith('9'))) { // 五分钟，响一下
                this.player.play(getContext(this), 'succeed.wav', 1)
                this.notify("提示", "5分-" + temp)
              } else { // 十分钟，响
                this.player.play(getContext(this), 'succeed.wav', 4)
                this.notify("提示", "10分-" + temp)
              }
            }
            isTiped = true
          } else if (isTiped && time < 60) {
            isTiped = false
          }
        }
      })
    }, 1000)
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

  onBackPress() {
    return this.isOn
  }

  /**
   * 发送通知消息
   * @param title
   * @param text
   * @param additionalText
   */
  notifyId = 1

  private notify(title: string, text: string, additionalText: string = "") {
    //通知Request对象
    let notificationRequest = {
      id: this.notifyId++,
      content: {
        contentType: Notification.ContentType.NOTIFICATION_CONTENT_BASIC_TEXT,
        normal: {
          title: title,
          text: text,
          additionalText: additionalText,
        }
      },
      tapDismissed: true,
      groupName: 'Fighting',
      // autoDeletedTime: 2,
      // isCountDown: true
    };

    Notification.publish(notificationRequest)
  }
}
