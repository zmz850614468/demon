import Logger from 'myLibrary/src/main/ets/util/Logger'
import { NetModel } from 'myLibrary/src/main/ets/model/NetModel'

@Entry
@Component
struct NetInfo {
  @State netType: string = ''
  @State deviceIp: string = ''
  @State wLanIp: string = ''
  @State netIps: Array<string> = []
  model: NetModel = new NetModel()

  aboutToAppear() {
    this.model.register()
    this.model.getNetType((err, netType: string) => {
      if (err) {
        this.showLog('getNetType：' + JSON.stringify(err))
      }
      if (netType) {
        // this.showLog('当前网络类型：' + netType)
        this.netType = netType
      }
    })

    this.model.getDeviceIp((err, ip) => {
      if (err) {
        this.showLog('getDeviceIp：' + JSON.stringify(err))
      }
      if (ip) {
        this.deviceIp = ip
      }
    })

    this.model.getWLanIp((err, ip) => {
      if (err) {
        this.showLog('getWLanIp：' + JSON.stringify(err))
      }
      if (ip) {
        this.wLanIp = ip
      }
    })

    this.model.getNetIp((err, ips) => {
      if (err) {
        this.showLog('getWLanIp：' + JSON.stringify(err))
      }
      if (ips) {
        this.netIps = ips
      }
    })

  }

  aboutToDisappear() {
    this.model.unregister()
  }

  build() {
    Column({ space: 3 }) {
      Text('当前网络类型：' + this.netType)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(40)
        .borderWidth(1)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))

      Text('当前设备Ip：' + this.deviceIp)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(40)
        .borderWidth(1)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))

      Text('当前路由器Ip：' + this.wLanIp)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(40)
        .borderWidth(1)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))

      Text('当前外网Ips：' + JSON.stringify(this.netIps))
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(40)
        .borderWidth(1)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
    }
    .padding({ left: 20, right: 20, top: 20, bottom: 20 })
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('NetInfo', msg)
  }
}