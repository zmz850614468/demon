import cloud from '@hw-agconnect/cloud';
import { AuthUser, SignInResult, VerifyCodeAction, VerifyCodeResult } from '@hw-agconnect/cloud/src/main/ets/auth/Auth';
/**
 * 认证模块
 */
export class AuthUtil {

  /**
   *  获取当前登录用户
   * @param callback
   */
  static getCurrentUser(callback: (err, user: AuthUser | null) => void) {
    cloud.auth().getCurrentUser().then(user => {
      callback(null, user)
    }).catch((err) => {
      callback(err, null)
    })
  }

  /**
   * 获取手机验证码
   * @param phoneNum
   * @param callback
   */
  static requestCode(phoneNum: string, callback: (err, result: VerifyCodeResult) => void) {
    cloud.auth().requestVerifyCode({
      action: VerifyCodeAction.REGISTER_LOGIN,
      lang: 'zh_CN',
      sendInterval: 60,
      verifyCodeType: {
        phoneNumber: phoneNum,
        countryCode: '86',
        kind: "phone"
      }
    }).then(result => {
      callback(null, result)
    }).catch((err) => {
      callback(err, null)
    })
  }

  /**
   * 手机验证码登录
   * @param phoneNum
   * @param code
   * @param callback
   */
  static signIn(phoneNum: string, code: string, callback: (err, result: SignInResult) => void) {
    cloud.auth().signIn({
      credentialInfo: {
        kind: 'phone',
        phoneNumber: phoneNum,
        countryCode: '86',
        verifyCode: code
      }
    }).then(result => {
      callback(null, result)
    }).catch(err => {
      callback(err, null)
    });
  }

  /**
   * 注册手机用户
   * @param phoneNum
   * @param code
   * @param callback
   */
  static createUser(phoneNum: string, code: string, callback: (err, result: SignInResult) => void) {
    cloud.auth().createUser({
      kind: 'phone',
      countryCode: '86',
      phoneNumber: phoneNum,
      password: '235689', //可以给用户设置初始密码，后续可以用密码来登录
      verifyCode: code
    }).then(result => {
      callback(null, result)
    }).catch(err => {
      callback(err, null)
    })
  }

  /**
   * 登出当前账号
   */
  static signOut(callback: (err) => void) {
    cloud.auth().signOut().then(() => {
      callback(null)
    }).catch(err => {
      callback(err)
    });
  }
}