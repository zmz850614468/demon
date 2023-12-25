/**
 * 位置信息类
 */
import geoLocationManager from '@ohos.geoLocationManager'

export class LocationModel {

  /**
   * 获取当前位置信息:经纬度信息
   * @param callback
   */
  getCurrentLocation(callback: (err, location: geoLocationManager.Location) => void) {
    // 获取当前位置信息,
    geoLocationManager.getCurrentLocation({
      priority: geoLocationManager.LocationRequestPriority.ACCURACY,
      scenario: geoLocationManager.LocationRequestScenario.NAVIGATION,
      maxAccuracy: 30,
      timeoutMs: 5000
    }, callback)
  }

  /**
   * 获取上次位置信息：经纬度信息
   * @returns
   */
  getLastLocation(): geoLocationManager.Location {
    return geoLocationManager.getLastLocation()
  }

  /**
   * 获取当前位置：具体地址信息
   * @param callback
   * @param location 默认为当前位置信息
   */
  getAddress(callback: (err, address: string) => void, location?: geoLocationManager.Location) {
    if (location) {
      geoLocationManager.getAddressesFromLocation({
        locale: 'zh',
        latitude: location.latitude,
        longitude: location.longitude,
        maxItems: 5
      }, (err, geoAddressArr) => {
        if (err) {
          callback(err, null)
        }
        if (geoAddressArr) {
          callback(null, this.getAddressFromGeoAddress(geoAddressArr[0]))
        }
      })
    } else {
      this.getCurrentLocation((err, location) => {
        if (err) {
          callback(err, null)
        } else {
          this.getAddress(callback, location)
        }
      })
    }
  }

  /**
   * 获取当前国家码
   */
  getCountryCode(callback: (err, countryCode) => void) {
    geoLocationManager.getCountryCode((err, countryCode) => {
      callback(err, countryCode)
    })
  }

  /**
   *
   * @param geoAddress
   * @returns
   */
  private getAddressFromGeoAddress(geoAddress: geoLocationManager.GeoAddress): string {
    let address = ''
    address += geoAddress.administrativeArea
    address += geoAddress.subAdministrativeArea
    address += geoAddress.locality
    address += geoAddress.subLocality
    address += geoAddress.roadName
    address += geoAddress.subRoadName
    if (address.indexOf(geoAddress.placeName) < 0) {
      address += geoAddress.placeName
    }

    return address
  }
}