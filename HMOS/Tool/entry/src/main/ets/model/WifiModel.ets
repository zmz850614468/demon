/**
 * wifi 类
 */
import wifiManager from '@ohos.wifiManager'
import Logger from '../util/Logger'
import abilityAccessCtrl, { Permissions } from '@ohos.abilityAccessCtrl'

export class WifiModel {
  constructor() {

    Logger.error('tag', 'wifi 打开状态：' + this.getWifiState())
    Logger.error('tag', 'wifi 连接状态：' + this.getWifiConnectState())

    this.listener()
    this.test()
  }

  test() {
    // this.startScan()

    // setTimeout(async () => {
    //   let arr = await wifiManager.getScanResults()
    //   arr.forEach((item: object) => {
    //     Logger.error('tag', 'wifi info:' + JSON.stringify(item))
    //   })
    //   Logger.error('tag', 'wifi info end')
    // }, 5000)

    if (this.getWifiConnectState()) {
      let info = wifiManager.getIpInfo()
      Logger.error('tag', 'ipInfo:' + JSON.stringify(info))
      Logger.error('tag', 'ipInfo:' + this.toIp(info.ipAddress))

      Logger.error('tag', 'LinkedInfo:' + JSON.stringify(wifiManager.getLinkedInfo()))

      Logger.error('tag', 'P2pLocalDevice:' + JSON.stringify(wifiManager.getP2pLocalDevice()))
      Logger.error('tag', 'P2pLinkedInfo:' + JSON.stringify(wifiManager.getP2pLinkedInfo()))

    }
  }

  toIp(id: number): string {
    let ip = (id % 256).toString()
    id = Number.parseInt((id / 256).toString())
    ip = id % 256 + ':' + ip
    id = Number.parseInt((id / 256).toString())
    ip = id % 256 + ':' + ip
    id = Number.parseInt((id / 256).toString())
    ip = id % 256 + ':' + ip

    return ip
  }

  listener() {
    // 是否打开wifi 网络
    wifiManager.on("wifiStateChange", (state: number) => {
      Logger.error('tag', 'wifiStateChange-on: ' + state)
    })
    wifiManager.off("wifiStateChange", (state: number) => {
      Logger.error('tag', 'wifiStateChange-off: ' + state)
    })

    // 是否连接上wifi网络
    wifiManager.on("wifiConnectionChange", (state: number) => {
      Logger.error('tag', 'wifiConnectionChange-on: ' + state)
    })
    wifiManager.off("wifiConnectionChange", (state: number) => {
      Logger.error('tag', 'wifiConnectionChange-off: ' + state)
    })

    // 扫码状态改变
    wifiManager.on("wifiScanStateChange", (state: number) => {
      Logger.error('tag', 'wifiScanStateChange-on: ' + state)
    })
    wifiManager.off("wifiScanStateChange", (state: number) => {
      Logger.error('tag', 'wifiScanStateChange-off: ' + state)
    })

    // 注册P2P开关状态改变事件
    wifiManager.on("p2pStateChange", (state: number) => {
      Logger.error('tag', 'p2pStateChange-on: ' + state)
    })
    wifiManager.off("p2pStateChange", (state: number) => {
      Logger.error('tag', 'p2pStateChange-off: ' + state)
    })

    // 注册P2P连接状态改变事件
    wifiManager.on("p2pConnectionChange", (info: any) => {
      Logger.error('tag', 'p2pConnectionChange-on: ' + JSON.stringify(info))
    })
    wifiManager.off("p2pConnectionChange", (info: any) => {
      Logger.error('tag', 'p2pConnectionChange-off: ' + JSON.stringify(info))
    })

    // this.requestPermission(["ohos.permission.GET_WIFI_INFO", 'ohos.permission.APPROXIMATELY_LOCATION', "ohos.permission.LOCATION"], (data) => {
    //
    //   // 注册P2P设备状态改变事件
    //   wifiManager.on("p2pDeviceChange", (info: any) => {
    //     Logger.error('tag', 'p2pDeviceChange-on: ' + JSON.stringify(info))
    //   })
    //   wifiManager.off("p2pDeviceChange", (info: any) => {
    //     Logger.error('tag', 'p2pDeviceChange-off: ' + JSON.stringify(info))
    //   })
    //
    //   // 注册P2P对端设备状态改变事件
    //   wifiManager.on("p2pPeerDeviceChange", (info: any) => {
    //     Logger.error('tag', 'p2pPeerDeviceChange-on: ' + JSON.stringify(info))
    //   })
    //   wifiManager.off("p2pPeerDeviceChange", (info: any) => {
    //     Logger.error('tag', 'p2pPeerDeviceChange-off: ' + JSON.stringify(info))
    //   })
    //
    //   wifiManager.startDiscoverDevices()
    // })

    // 注册P2P永久组状态改变事件。
    wifiManager.on("p2pPersistentGroupChange", () => {
      Logger.error('tag', 'p2pPersistentGroupChange-on: ')
    })
    wifiManager.off("p2pPersistentGroupChange", () => {
      Logger.error('tag', 'p2pPersistentGroupChange-off: ')
    })

    // 注册发现设备状态改变事件。
    wifiManager.on("p2pDiscoveryChange", (state: number) => {
      Logger.error('tag', 'p2pDiscoveryChange-on: ' + state)
    })
    wifiManager.off("p2pDiscoveryChange", (state: number) => {
      Logger.error('tag', 'p2pDiscoveryChange-off: ' + state)
    })
  }


  requestPermission(permission: Array<Permissions>, callBack: (result: Array<number>) => void) {
    let atManager = abilityAccessCtrl.createAtManager();
    try {
      atManager.requestPermissionsFromUser(getContext(this), permission, (err, data) => {
        console.info("tag" + JSON.stringify(data));
        // console.info("data permissions:" + data.permissions);
        // console.info("data authResults:" + data.authResults);
        callBack(data.authResults)
      });
    } catch (err) {
      console.log(`catch err->${JSON.stringify(err)}`);
    }
  }


  /**
   * 判定 wifi 连接状态
   * @returns
   */
  getWifiState(): boolean {
    return wifiManager.isWifiActive()
  }

  /**
   * 判定 网络 连接状态
   * @returns
   */
  getWifiConnectState(): boolean {
    return wifiManager.isConnected()
  }

  /**
   * 开始扫码
   * @returns
   */
  startScan() {
    wifiManager.scan()
  }
}
