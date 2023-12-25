/**
 * 1. 调用用户认证
 */
// router.pushUrl({ url: 'pages/UserAuth' })

/**
 * 2. 认证结果回调
 */
// onPageShow() {
//   let param: any = router.getParams()
//   if (param && param.from && param.from === 'UserAuth') {
//     if (param.err) {
//       Toast.show(param.err)
//     }
//     if (param.token) {
//       Toast.show('用户授权成功')
//     }
//   }
// }

import userIAM_userAuth from '@ohos.userIAM.userAuth';
import router from '@ohos.router';
import { PermissionUtil } from 'myLibrary';
import Logger from 'myLibrary/src/main/ets/util/Logger'

@Entry
@Component
struct UserAuth {
  private challenge = new Uint8Array([3, 8, 5, 6, 2, 3, 5, 6]);
  private authType = userIAM_userAuth.UserAuthType.FINGERPRINT;
  private authTrustLevel = userIAM_userAuth.AuthTrustLevel.ATL1;
  private auth;
  private needCancel = true

  aboutToAppear() {
    PermissionUtil.requestAuthPermission((result) => {
      if (result === 0) {
        this.showLog('用户认证授权成功')
        this.startUserAuth()
      } else {
        this.back('用户认证授权失败:' + result, null)
      }
    })
  }

  aboutToDisappear() {
    if (this.auth) {
      this.auth.off("result")
      if (this.needCancel) {
        try {
          this.auth.cancel()
        } catch (error) {
          this.back('取消用户授权异常:' + JSON.stringify(error), null)
        }
      }
    }
  }

  /**
   *
   * @param err
   * @param data
   */
  back(err: string, token: string) {
    router.back({ url: null, params: { from: 'UserAuth', err: err, token: token } })
  }

  /**
   * 开始用户授权
   */
  startUserAuth() {

    try {
      this.auth = userIAM_userAuth.getAuthInstance(this.challenge, this.authType, this.authTrustLevel);
    } catch (error) {
      this.back('获取授权对象异常:' + JSON.stringify(error), null)
    }

    try {
      // 订阅认证结果
      this.auth.on("result", {
        callback: (result: userIAM_userAuth.AuthResultInfo) => {
          this.needCancel = false
          this.showLog("authV9 result " + result.result);
          this.showLog("authV9 token " + result.token);
          this.showLog("authV9 remainAttempts " + result.remainAttempts);
          this.showLog("authV9 lockoutDuration " + result.lockoutDuration);
          this.dealAuthResult(result)
        }
      });
      this.auth.start()
    } catch (error) {
      this.back('用户授权异常：' + JSON.stringify(error), null)
    }
  }

  /**
   * 处理授权结果
   * @param result
   */
  dealAuthResult(result: userIAM_userAuth.AuthResultInfo) {
    if (result.result == 12500000) { // 认证成功
      this.back(null, JSON.stringify(result.token))
    } else if (result.result === 12500001) { // 认证失败
      if (result.remainAttempts > 0) {
        this.back('剩余认证次数：' + result.remainAttempts, null)
      } else {
        this.back('认证被锁定，时长：' + result.lockoutDuration.toFixed(0), null)
      }
    } else { // 其他错误
      this.back('认证错误码：' + ' - ' + result.result, null)
    }
  }

  build() {
    Column() {
      Image($r('app.media.auth_bg'))
        .width('100%')
        .height('100%')
    }
    .width('100%')
    .height('100%')
    .backgroundColor($r('app.color.black'))
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Index', msg)
  }
}