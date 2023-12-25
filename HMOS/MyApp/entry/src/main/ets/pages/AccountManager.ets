import { Rdb_v3 } from 'myLibrary/src/main/ets/database_v3/Rdb_v3'
import { AccountBean } from '../bean/data/AccountBean'
import Logger from 'myLibrary/src/main/ets/util/Logger'
import { Toast } from 'myLibrary/src/main/ets/util/Toast'
import HashMap from '@ohos.util.HashMap'
import { AddAccountDialog } from '../dialog/AddAccountDialog'

@Entry
@Component
struct AccountManager {
  accountTable: Rdb_v3 = null
  map: HashMap<string, Array<AccountBean>> = new HashMap()
  @State keyList: Array<string> = []
  @State selectedIndex: number = -1

  aboutToAppear() {
    // 获取数据库表
    this.accountTable = new Rdb_v3(AccountBean.tableInfo, (err) => {
      // this.showLog('new Rdb_v4:' + JSON.stringify(err))
      if (err) {
        Toast.show('初始化账号数据表出错：' + JSON.stringify(err))
      } else {
        this.queryData()
      }
    })
  }

  /**
   * 查询数据
   */
  private queryData() {
    this.accountTable.queryAll(AccountBean, (err, list) => {
      this.showLog('queryAll:' + JSON.stringify(err))
      if (err) {
        Toast.show('账号表查询错误：' + JSON.stringify(err))
      } else {
        this.dealResult(list)
      }
    })
  }

  /**
   * 处理所有账号信息
   * @param list
   */
  private dealResult(list: Array<AccountBean>) {
    this.showLog('数据：' + JSON.stringify(list))
    this.map.clear()
    this.keyList.splice(0, this.keyList.length)
    list.forEach((item) => {
      if (!this.map.hasKey(item.typeGroup)) {
        this.map.set(item.typeGroup, [])
        this.keyList.push(item.typeGroup)
      }
      this.map.get(item.typeGroup).push(item)
    })
    if (this.keyList.length > 0) {
      setTimeout(() => {
        this.keyList.push(this.keyList.pop())
      }, 20)
    }
  }

  /**
   * 弹出添加账号对话框
   */
  private showAddDialog() {
    let addAccountDialog: CustomDialogController = new CustomDialogController({
      builder: AddAccountDialog({
        callback: (bean: AccountBean) => {
          this.showLog('bean:' + JSON.stringify(bean))
          this.accountTable.insertBean(bean, (err, rowId) => {
            if (err) {
              Toast.show('插入数据出错：' + JSON.stringify(err))
            } else {
              this.queryData()
            }
          })
          addAccountDialog.close()
        }
      }),
      autoCancel: true,
      alignment: DialogAlignment.Bottom,
      offset: { dx: 0, dy: -40 }
    })
    addAccountDialog.open()
  }

  build() {
    Column() {
      Stack() {
        Text('账号管理')
          .fontSize(17)
          .textAlign(TextAlign.Center)
          .width('100%')
          .height(40)
        Text('+')
          .fontSize(25)
          .textAlign(TextAlign.Center)
          .width(40)
          .height(40)
          .onClick(() => {
            this.showAddDialog()
          })
      }.height(40)
      .alignContent(Alignment.End)

      Text()
        .width('100%')
        .height(0.7)
        .backgroundColor($r('app.color.gray'))

      List({ space: 1 }) {
        ForEach(this.keyList, (key, index) => {
          ListItemGroup({ header: this.headView(key, index), footer: null }) {
            if (index === this.selectedIndex) {
              ForEach(this.map.get(key), (item: AccountBean, index) => {
                ListItem() {
                  this.contentView(item, index)
                }
                .transition({ type: TransitionType.All, scale: { x: 0 } })
                .gesture(LongPressGesture().onAction(() => {
                  AlertDialog.show({
                    title: "注意",
                    message: '确定要删除账号：' + item.name + ' 吗？',
                    confirm: {
                      value: "确定",
                      action: () => {
                        this.accountTable.deleteBean(item, (err, rowId) => {
                          if (err) {
                            Toast.show('删除数据出错：' + JSON.stringify(err))
                          } else {
                            this.queryData()
                          }
                        })
                      },
                    }
                  })
                }))
              })
            }
          }
          // .transition({ type: TransitionType.All, scale: { y: 0 } })
          .divider({ strokeWidth: 0.5, color: $r('app.color.gray'), startMargin: 30, endMargin: 30 })
          .borderWidth(0.5)
          .borderRadius(20)
          .borderColor($r("app.color.gray"))
        }, (key, index) => key + '-' + this.map.get(key).length + '-' + (this.selectedIndex === index))
      }
      .height('100%')
      .padding({ left: 0, right: 0, top: 0, bottom: 40 })
      .sticky(StickyStyle.Header | StickyStyle.Footer)

    }
  }

  @Builder
  private headView(title, index) {
    Column() {
      Text((index + 1) + '-' + title)
        .fontWeight(FontWeight.Bold)
        .fontSize(16)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(40)
        .padding({ left: 20, right: 20, top: 0, bottom: 0 })
      if (index === this.selectedIndex) {
        Text()
          .width('95%')
          .height(0.7)
          .backgroundColor($r('app.color.gray'))
      }
    }
    .onClick(() => {
      // animateTo({ duration: 1000 }, () => {
        if (this.selectedIndex === index) {
          this.selectedIndex = -1
        } else {
          this.selectedIndex = index
        }
      // })
    })
    .backgroundColor($r('app.color.white'))
    .width('100%')
  }

  @Builder
  private contentView(bean: AccountBean, index) {
    Column() {
      Text(++index + '.' + bean.name)
        .fontSize(15)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(35)
        .padding({ left: 30, right: 0, top: 0, bottom: 0 })
      Text('账号：' + bean.account)
        .fontSize(15)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(30)
        .padding({ left: 50, right: 0, top: 0, bottom: 0 })
      Text('密码：' + bean.pwd)
        .fontSize(15)
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(30)
        .padding({ left: 50, right: 0, top: 0, bottom: 0 })
    }
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('AccountManager', msg)
  }
}