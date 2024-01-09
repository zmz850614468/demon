import cloud, { AuthUser, SignInResult, VerifyCodeResult } from '@hw-agconnect/cloud'
import { Toast } from 'myLibrary/src/main/ets/util/Toast'
import { AGCUtil } from '../serverless/AGCUtil'
import { AuthUtil } from '../serverless/AuthUtil'
import Logger from 'myLibrary/src/main/ets/util/Logger'

// 注意目录，目录可以自定义
import { TestBean } from '../bean_serverless/TestBean';

import { DbManage } from '../serverless/DbManage'


@Entry
@Component
struct HWServerless {
  @State phone: string = ''
  @State code: string = ''
  @State hasSignIn: boolean = false
  searchList: Array<TestBean> = null

  aboutToAppear() {
    AGCUtil.initSDK(getContext(this)) // 初始化sdk - 必须
    // AGCUtil.initApiKey('','') // 初始化秘钥 - 非必须

    AuthUtil.getCurrentUser((err, user: AuthUser | null) => {
      if (err) {
        Toast.show('获取已登录账号出错：' + JSON.stringify(err))
        this.showLog('获取已登录账号出错：' + JSON.stringify(err))
      } else if (user) {
        this.hasSignIn = true
        Toast.show('已登录账号:' + user.getPhone())
        this.showLog('已登录账号:' + JSON.stringify(user))
      }
    })
  }

  /**
   * todo 云函数调用
   */
  async requestTest() {
    Toast.show('开始函数请求')
    let functionResult = await cloud.callFunction({
      name: "request-test",
      timeout: 10 * 1000, //单位为毫秒，默认为70*1000毫秒。
      // params: {
      //   "param1": "val1",
      //   "param2": "val2"
      // }
    });

    let returnValue = functionResult.getValue();
    this.showLog('函数请求结果：' + JSON.stringify(returnValue))
    Toast.show('函数请求结果：' + JSON.stringify(returnValue))
  }

  build() {
    Column() {
      this.loginLayout()

      if (this.hasSignIn) {
        Text('函数测试')
          .item()
          .borderWidth(0.5)
          .borderRadius(10)
          .borderColor($r("app.color.gray"))
          .margin({ left: 0, right: 0, top: 20, bottom: 0 })
          .onClick(() => {
            this.requestTest()
          })

        this.dbLayout()
      }

    }.padding({ left: 0, right: 0, top: 10, bottom: 10 })
  }

  @Builder
  dbLayout() {
    Column() {
      Text('添加数据')
        .item()
        .borderWidth(0.5)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
        .onClick(() => {
          DbManage.getDbManagerIfNotCreate().insertOrUpdate(TestBean, { 'name': 'demon2', 'age': 188 })
          // this.showLog('云数据库插入结果：' + num)
          // Toast.show('云数据库插入结果：' + num)
        })

      Text('删除数据')
        .item()
        .borderWidth(0.5)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
        .margin({ left: 0, right: 0, top: 10, bottom: 10 })
        .onClick(() => {
          if (this.searchList) {
            DbManage.getDbManagerIfNotCreate().delete(TestBean, this.searchList[0])
          }
        })

      Text('查询数据')
        .item()
        .borderWidth(0.5)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
        .onClick(async () => {
          DbManage.getDbManagerIfNotCreate().queryAll(TestBean, (err, list: Array<TestBean>) => {
            if (list) {
              this.searchList = list
              this.showLog('云数据库查询结果：' + JSON.stringify(this.searchList))
              Toast.show('云数据库查询结果：' + JSON.stringify(this.searchList))
            }
          })
        })
    }.margin({ left: 0, right: 0, top: 10, bottom: 0 })
  }

  @Builder
  loginLayout() {
    Column() {
      Text('华为认证模块').item().textAlign(TextAlign.Start).padding({ left: 20, right: 0, top: 0, bottom: 0 })
      if (!this.hasSignIn) {
        Row() {
          TextInput({ placeholder: '手机号' }).item_input()
            .width('70%')
            .onChange((val) => {
              this.phone = val
            })
          TextInput({ placeholder: '验证码' }).item_input()
            .width('30%')
            .onChange((val) => {
              this.code = val
            })
        }.padding({ left: 10, right: 10, top: 0, bottom: 0 })

        Text('获取手机验证码').item()
          .onClick(() => {
            if (this.phone.length < 11) {
              Toast.show('请输入正确的手机号')
            } else {
              Toast.show('开始获取华为验证码')
              AuthUtil.requestCode(this.phone, (err, result: VerifyCodeResult) => {
                if (err) {
                  Toast.show('获取华为验证码失败：' + JSON.stringify(err))
                } else {
                  this.showLog('华为验证码：' + JSON.stringify(result))
                }
              })
            }
          })
        Text('华为账号认证').item()
          .onClick(() => {
            if (this.phone.length < 11 || this.code.length < 4) {
              Toast.show('请输入正确的手机号和验证码')
            } else {
              AuthUtil.signIn(this.phone, this.code, (err, result: SignInResult) => {
                if (err) {
                  Toast.show('登录账号出错：' + JSON.stringify(err))
                  this.showLog('登录账号出错：' + JSON.stringify(err))
                } else {
                  this.hasSignIn = true
                  Toast.show('登录账号成功')
                  this.showLog('登录账号成功：' + JSON.stringify(result))
                }
              })
            }
          })
      } else {
        Text('退出当前账号').item()
          .onClick(() => {
            AuthUtil.signOut((err) => {
              if (err) {
                Toast.show('退出账号出错：' + JSON.stringify(err))
                this.showLog('退出账号出错：' + JSON.stringify(err))
              } else {
                Toast.show('退出当前账号')
                this.hasSignIn = false
              }
            })
          })
      }
    }.borderWidth(0.5)
    .borderRadius(20)
    .borderColor($r("app.color.gray"))
  }

  /** import Logger from 'myLibrary/src/main/ets/util/Logger'
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('HWServerless', msg)
  }
}

@Extend(Text) function item() {
  .fontSize(16)
  .textAlign(TextAlign.Center)
  .width('100%')
  .height(40)
}

@Extend(TextInput) function item_input() {
  .textAlign(TextAlign.Center)
  .width('70%')
  .height(40)
  .borderWidth(0.5)
  .borderRadius(10)
  .borderColor($r("app.color.gray"))
}


