/**
 * 获取网络相关信息
 */
import connection from '@ohos.net.connection'
import Logger from '../util/Logger'

export class NetModel {
  private netConnect: connection.NetConnection

  constructor() {
    this.netConnect = connection.createNetConnection()
  }

  /**
   * 注册网络事件：网络连接实时变化监听
   */
  register() {
    // 网络可用情况
    this.netConnect.on('netAvailable', (netHandle: connection.NetHandle) => {
      if (netHandle.netId >= 100) {
        this.showLog('网络可用：' + netHandle.netId)
      } else {
        this.showLog('没有可用的网络：' + netHandle.netId)
      }
    })

    // 网络阻塞情况
    this.netConnect.on('netBlockStatusChange', (data: {
      netHandle: connection.NetHandle,
      blocked: boolean
    }) => {
      this.showLog('网络阻塞情况:' + data.netHandle.netId + ' - ' + data.blocked)
    })

    // 网络能力变化
    this.netConnect.on('netCapabilitiesChange', (data) => {
      this.showLog('网络能力变化')
    })

    // 网络连接情况变化
    this.netConnect.on('netConnectionPropertiesChange', (data) => {
      this.showLog('网络连接情况变化')
    })

    // 网络丢失情况
    this.netConnect.on('netLost', (netHandle: connection.NetHandle) => {
      this.showLog('网络丢失情况')
    })

    // 网络不可用情况
    this.netConnect.on('netUnavailable', () => {
      this.showLog('网络不可用情况')
    })

    this.netConnect.register((err) => {
      if (err) {
        this.showLog('网络事件注册错误：' + JSON.stringify(err))
      } else {
        this.showLog('网络事件注册成功')
      }
    })
  }

  /**
   * 注销网络事件
   */
  unregister() {
    this.netConnect.unregister((err) => {
    })
  }

  /**
   * 网络ID，取值为0代表没有默认网络，其余取值必须大于等于100。
   * @returns
   */
  private hasDefaultNet(): boolean {
    return connection.getDefaultNetSync().netId >= 100
  }

  /**
   * 获取网络类型
   * @param callback 可以同时存在多个
   * 移动数据
   * wifi
   * 以太网
   */
  getNetType(callback: (err, netType: string) => void) {
    if (!this.hasDefaultNet()) {
      callback('没有可用的网络', null)
      return
    }

    connection.getNetCapabilities(connection.getDefaultNetSync(), (err, capability: connection.NetCapabilities) => {
      if (err) {
        callback(err, null)
      }
      if (capability) {
        // capability.linkUpBandwidthKbps // 上行速度
        // capability.linkDownBandwidthKbps // 下行速度
        // capability.networkCap // 网络能力 - 短信能力、流量是否计费、是否能访问网络、是否是VPN等
        // capability.bearerTypes // 网络类型

        let netType = ''
        capability.bearerTypes.forEach((type) => {
          switch (type) {
            case connection.NetBearType.BEARER_CELLULAR:
              netType += '移动数据,'
              break
            case connection.NetBearType.BEARER_WIFI:
              netType += 'wifi,'
              break
            case connection.NetBearType.BEARER_ETHERNET:
              netType += '以太网,'
              break
          }
        })
        callback(null, netType.substring(0, netType.length - 1))
      }
    })
  }

  /**
   * 获取设备ip地址
   * @param callback
   */
  getDeviceIp(callback: (err, ip: string) => void) {
    if (!this.hasDefaultNet()) {
      callback('没有可用的网络', null)
      return
    }

    connection.getConnectionProperties(connection.getDefaultNetSync(), (err, properties: connection.ConnectionProperties) => {
      if (err) {
        callback(err, null)
      }
      if (properties) {
        // 包含当前地址、路由器地址
        // this.showLog('网卡名称：' + properties.interfaceName)
        // this.showLog('所属域：' + properties.domains) // 默认：""
        // this.showLog('链路信息：' + JSON.stringify(properties.linkAddresses))
        // this.showLog('路由信息：' + JSON.stringify(properties.routes))
        // this.showLog('网络地址：' + JSON.stringify(properties.dnses))
        // this.showLog('最大传输单元：' + properties.mtu)

        // 设备ip信息
        let deviceIp = ''
        properties.linkAddresses.forEach((item) => {
          if (item.address.family == 2) {
            deviceIp = item.address.address
          }
        })
        callback(null, deviceIp)
      }
    })
  }

  /**
   * 获取路由器ip地址
   * @param callback
   */
  getWLanIp(callback: (err, ip: string) => void) {
    if (!this.hasDefaultNet()) {
      callback('没有可用的网络', null)
      return
    }

    connection.getConnectionProperties(connection.getDefaultNetSync(), (err, properties: connection.ConnectionProperties) => {
      if (err) {
        callback(err, null)
      }
      if (properties) {
        let wLanIp = ''
        properties.routes.forEach((item) => {
          if (item.hasGateway && item.isDefaultRoute) {
            wLanIp = item.gateway.address
          }
        })
        callback(null, wLanIp)
      }
    })
  }

  /**
   * 获取外网ip地址
   * @param callback
   */
  getNetIp(callback: (err, ips: Array<string>) => void) {
    if (!this.hasDefaultNet()) {
      callback('没有可用的网络', null)
      return
    }

    connection.getConnectionProperties(connection.getDefaultNetSync(), (err, properties: connection.ConnectionProperties) => {
      if (err) {
        callback(err, null)
      }
      if (properties) {

        // 设备ip信息
        let netIps = []
        properties.dnses.forEach((item) => {
          if (item.family == 2) {
            netIps.push(item.address)
          }
        })
        callback(null, netIps)
      }
    })
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('NetModel', msg)
  }
}