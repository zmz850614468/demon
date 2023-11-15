import Toast from '../common/util/Toast';
import BaseTable_v2 from '../database_v2/BaseTable_v2';
import MoneyBean from '../database_v2/bean/MoneyBean';
import Logger from '../common/util/Logger'
import StringUtil from '../common/util/StringUtil';

@Entry
@Component
struct Money {
  @State moneyList: Array<MoneyBean> = null
  table: BaseTable_v2<MoneyBean>
  @State type: string = '出金'
  result: number = 0
  memo: string = ''
  date: string = ''
  private lastClickTime: number = 0

  aboutToAppear() {
    this.table = new BaseTable_v2(MoneyBean.TABLE_NAME, MoneyBean.SQL, MoneyBean.COLUMNS, (b: boolean) => {
      Logger.error("tag", "数据表初始化结果：" + b)
      this.refreshData()
    })
    this.getDate()
  }

  /**
   * 异步获取时间
   */
  private async getDate() {
    this.date = await StringUtil.getDayTimeAsync()
  }

  /**
   * 更新数据
   */
  private refreshData() {
    this.table.queryAll(new MoneyBean(), (list: Array<MoneyBean>) => {
      let count = 0
      list.forEach((bean) => {
        count += bean.result
      })
      let bean = new MoneyBean()
      bean.date = "总计"
      bean.result = count
      list.push(bean)

      this.moneyList = list.reverse()
    })
  }

  build() {
    Column() {
      Text("出入金")
        .fontSize(19)
        .width('100%')
        .height(50)
        .textAlign(TextAlign.Center)

      Row() {
        Text(this.type)
          .fontSize(15)
          .width('20%')
          .height('100%')
          .textAlign(TextAlign.Center)
          .visibility(Visibility.None)
          .onClick(() => {
            if (this.type === '入金') {
              this.type = '出金'
            } else {
              this.type = '入金'
            }
          })

        TextInput({ placeholder: '金额' })
          .fontSize(15)
          .width('30%')
          .height('100%')
          .textAlign(TextAlign.Center)
          .type(InputType.Number)
          .inputFilter('^-?[0-9]*$')
          .padding({ left: 0, right: 0, top: 0, bottom: 0 })
          .borderWidth(1)
          .borderRadius(8)
          .maxLength(9)
          .borderColor($r("app.color.gray"))
          .onChange((val) => {
            try {
              this.result = Number.parseFloat(val)
            } catch (e) {
              this.result = 0
            }
          })

        TextInput({ text: this.memo, placeholder: '备注' })
          .fontSize(15)
          .width('50%')
          .height('100%')
          .textAlign(TextAlign.Center)
          .padding({ left: 0, right: 0, top: 0, bottom: 0 })
          .borderWidth(1)
          .maxLength(6)
          .borderRadius(8)
          .borderColor($r("app.color.gray"))
          .onChange((val) => {
            this.memo = val
          })

        Button('添加')
          .fontSize(15)
          .width('20%')
          .height('100%')
          .onClick(() => {
            let bean = new MoneyBean()
            bean.date = this.date
            bean.result = this.result
            bean.memo = this.memo
            this.table.insertData(bean, (b) => {
              Toast.show('添加数据成功:' + b)
            })
            setTimeout(() => {
              this.refreshData()
            }, 200)
          })
      }
      .width('100%')
      .height(40)

      Row() {
        Text('日期')
          .textItem()
        Text('出/入 金额')
          .textItem()
        Text('备注')
          .textItem()
      }

      List({ space: 1 }) {
        ForEach(this.moneyList, (item: MoneyBean) => {
          ListItem() {
            Row() {
              Text(item.date)
                .textItem()
              Text(item.result.toString())
                .textItem()
              Text(item.memo)
                .textItem()
            }.onClick((e) => {
              Logger.error('tag', 'time:' + e.timestamp) // 返回的是纳秒级 数据
              if (e.timestamp - this.lastClickTime < 300000000) {
                AlertDialog.show({
                  title: "注意",
                  message: "确定删除改数据吗？",
                  confirm: {
                    value: "确定",
                    action: () => {
                      this.table.deleteData(item, (num) => {
                        if (num > 0) {
                          this.refreshData()
                          Toast.show('删除成功')
                        } else {
                          Toast.show('删除失败')
                        }
                      })
                    },
                  }
                })
              }
              this.lastClickTime = e.timestamp
            })
          }
        })
      }
    }
  }
}

@Extend(Text) function textItem() {
  .fontSize(17)
  .width('33.3%')
  .height(40)
  .textAlign(TextAlign.Center)
  .fontColor($r('app.color.black'))
}



