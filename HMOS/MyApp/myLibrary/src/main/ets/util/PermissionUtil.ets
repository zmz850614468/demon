import abilityAccessCtrl, { Permissions } from '@ohos.abilityAccessCtrl';
import { Toast } from './Toast';

/**
 * 权限申请列表
 */
export class PermissionUtil {
  /**
   * 请求权限
   * @param permission
   * @param callBack
   */
  static requestPermission(permission: Array<Permissions>, callBack: (result: Array<number>) => void) {
    let atManager = abilityAccessCtrl.createAtManager();
    try {
      atManager.requestPermissionsFromUser(getContext(this), permission, (err, data) => {
        if (err) {
          Toast.show('权限请求出错：' + JSON.stringify(err))
        }
        if (data) {
          callBack(data.authResults)
        }
      })
    } catch (err) {
      Toast.show('权限请求异常：' + JSON.stringify(err))
    }
  }


  /**
   * 请求 位置信息 权限
   * @param callback
   */
  static requestLocationPermission(callback: (results: Array<number>) => void) {
    PermissionUtil.requestPermission(['ohos.permission.APPROXIMATELY_LOCATION', 'ohos.permission.LOCATION'], (arr) => {
      callback(arr)
    })
  }

  /**
   * 请求 悬浮 权限
   * @param callBack
   */
  static requestFloatPermission(callback: (result: number) => void) {
    PermissionUtil.requestPermission(['ohos.permission.SYSTEM_FLOAT_WINDOW'], (arr) => {
      callback(arr[0])
    })
  }

  /**
   * 请求 用户认证 权限
   * @param callback
   */
  static requestAuthPermission(callback: (result: number) => void) {
    PermissionUtil.requestPermission(['ohos.permission.ACCESS_BIOMETRIC'], (arr) => {
      callback(arr[0])
    })
  }
}