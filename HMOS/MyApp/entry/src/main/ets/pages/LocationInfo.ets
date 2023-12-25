import Logger from 'myLibrary/src/main/ets/util/Logger'
import { PermissionUtil } from 'myLibrary/src/main/ets/util/PermissionUtil'
import { Toast } from 'myLibrary/src/main/ets/util/Toast'
import geoLocationManager from '@ohos.geoLocationManager'
import { LocationModel } from 'myLibrary/src/main/ets/model/LocationModel'

@Entry
@Component
struct LocationInfo {
  aboutToAppear() {
    PermissionUtil.requestLocationPermission((results: Array<number>) => {
      if (results[0] === 0 && results[1] === 0) {
        Toast.show('位置权限请求成功')
        // this.listener()
        // this.requestLocation()
        this.getLocationInfo()
      } else {
        Toast.show('位置权限请求失败')
        this.showLog('位置权限请求失败：' + JSON.stringify(results))
      }
    })
  }

  requestLocation() {
    // 获取当前位置信息,
    geoLocationManager.getCurrentLocation({
      priority: geoLocationManager.LocationRequestPriority.ACCURACY,
      scenario: geoLocationManager.LocationRequestScenario.NAVIGATION,
      maxAccuracy: 30,
      timeoutMs: 5000
    }, (err, location) => {
      if (err) {
        this.showLog('获取当前位置信息出错：' + JSON.stringify(err))
      }
      if (location) {
        this.showLog('当前位置信息：' + JSON.stringify(location))
      }
    })

    let location = geoLocationManager.getLastLocation()
    this.showLog('上一次位置信息：' + JSON.stringify(location))

    geoLocationManager.getAddressesFromLocation({
      locale: 'zh',
      latitude: location.latitude,
      longitude: location.longitude,
      maxItems: 5
    }, (err, geoAddressArr) => {
      if (err) {
        this.showLog('获取具体位置信息出错：' + JSON.stringify(err))
      }
      if (geoAddressArr) {
        this.showLog('具体位置信息：' + JSON.stringify(geoAddressArr))
      }
    })

  }

  listener() {
    geoLocationManager.on('locationChange', {
      priority: geoLocationManager.LocationRequestPriority.ACCURACY,
      scenario: geoLocationManager.LocationRequestScenario.NAVIGATION,
      timeInterval: 10,
      distanceInterval: 5,
      maxAccuracy: 5
    }, (location) => {
      this.showLog('实时变化位置信息：' + JSON.stringify(location))
      geoLocationManager.getAddressesFromLocation({
        locale: 'zh',
        latitude: location.latitude,
        longitude: location.longitude,
        maxItems: 5
      }, (err, geoAddressArr) => {
        if (err) {
          this.showLog('获取具体位置信息出错：' + JSON.stringify(err))
        }
        if (geoAddressArr) {
          this.showLog('具体位置信息：' + JSON.stringify(geoAddressArr))
        }
      })
    })
  }

  /**
   * 获取位置相关信息
   */
  getLocationInfo() {

    // 国家信息
    let model = new LocationModel()
    model.getCountryCode((err, countryCode) => {
      if (err) {
        Toast.show('获取国家码出错：' + JSON.stringify(err))
        this.showLog('获取国家码出错：' + JSON.stringify(err))
      }
      if (countryCode) {
        this.country = countryCode.country
      }
    })

    // 上一次经纬度信息
    let location = model.getLastLocation()
    this.lastLocation = '(' + location.latitude.toFixed(2) + ',' + location.longitude.toFixed(2) + ')'

    // 上一次具体位置信息
    model.getAddress((err, address,) => {
      if (err) {
        Toast.show('获取上一次具体位置出错：' + JSON.stringify(err))
        this.showLog('获取上一次具体位置出错：' + JSON.stringify(err))
      }
      if (address) {
        this.lastAddress = address
      }
    }, location)

    // 当前经纬度信息
    model.getCurrentLocation((err, location) => {
      if (err) {
        Toast.show('获取当前位置出错：' + JSON.stringify(err))
        this.showLog('获取当前位置出错：' + JSON.stringify(err))
      }
      if (location) {
        this.currentLocation = '(' + location.latitude.toFixed(2) + ',' + location.longitude.toFixed(2) + ')'
      }
    })

    // 当前具体位置信息
    model.getAddress((err, address) => {
      if (err) {
        Toast.show('获取当前具体位置出错：' + JSON.stringify(err))
        this.showLog('获取当前具体位置出错：' + JSON.stringify(err))
      }
      if (address) {
        this.currentAddress = address
      }
    })
  }

  @State country: string = ''
  @State currentLocation: string = ''
  @State currentAddress: string = ''
  @State lastLocation: string = ''
  @State lastAddress: string = ''

  build() {
    Column() {
      Text('当前国家码：' + this.country).item()
      Text('当前地理位置信息：' + this.currentLocation).item()
      Text('当前具体位置信息：' + this.currentAddress).item()
      Text('上一次地理位置信息：' + this.lastLocation).item()
      Text('上一次具体位置信息：' + this.lastAddress).item()
    }
  }

  // @Builder
  // item(text: string) {
  //   Text(text)
  //     .fontSize(15)
  //     .textAlign(TextAlign.Start)
  //     .width('100%')
  //     .height(40)
  //     .padding({ left: 20, right: 20, top: 0, bottom: 0 })
  // }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('LocationInfo', msg)
  }
}

@Extend(Text) function item() {
  .fontSize(15)
  .textAlign(TextAlign.Start)
  .width('100%')
  .height(40)
  .padding({ left: 20, right: 20, top: 0, bottom: 0 })
}
