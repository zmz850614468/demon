import abilityAccessCtrl, { Permissions } from '@ohos.abilityAccessCtrl';

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
        console.info("tag" + JSON.stringify(data));
        callBack(data.authResults)
      });
    } catch (err) {
      console.log(`catch err->${JSON.stringify(err)}`);
    }
  }

  /**
   * 请求悬浮 权限
   * @param callBack
   */
  static requestFloatPermission(callBack: (result: number) => void) {
    PermissionUtil.requestPermission(['ohos.permission.SYSTEM_FLOAT_WINDOW'], (arr) => {
      callBack(arr[0])
    })
  }
}