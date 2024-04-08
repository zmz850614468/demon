import BaseTable_v2 from '../database_v2/BaseTable_v2'
import { TargetBean } from '../database_v2/bean/TargetBean'
import { OperateDataModel } from '../model/OperateDataModel'
import Logger from '../util/Logger'
import Toast from '../util/Toast'

@Entry
@Component
struct Target {
  @State updateUiCount: number = 0
  @State targetList: Array<TargetBean> = []
  @State isAddYear: boolean = false
  @State isAddMonth: boolean = false
  targetTable: BaseTable_v2<TargetBean> = null

  aboutToAppear() {
    this.targetTable = new BaseTable_v2(TargetBean.TABLE_NAME, TargetBean.SQL, TargetBean.COLUMNS, (b: boolean) => {
      // this.targetTable.clearData()
      this.refreshData()
    })

    this.year = new Date().getFullYear()
    this.month = new Date().getMonth() + 1
  }

  /**
   * 更新数据
   */
  private refreshData() {
    this.targetList.splice(0) // 清空数据

    this.targetTable.queryAll(new TargetBean(), (list: Array<TargetBean>) => {
      // this.operateList = list.reverse()
      this.showLog('查询结果：' + list.length)
      list.forEach((item) => {
        if (item.month === -1) { // 年数据
          this.targetList.unshift(item)
        } else { // 月数据
          this.targetList.forEach((bean) => {
            if (bean.year === item.year) {
              if (!bean.monthList) {
                bean.monthList = []
              }
              bean.monthList.unshift(item)
            }
          })
        }
      })

      // 计算年结果数据
      this.targetList.forEach((item, index) => {
        if (item.monthList && item.monthList.length > 0) {
          let bean = item.monthList[0]
          bean.result = OperateDataModel.getInstance().getResult(bean.year, bean.month)
          this.targetTable.updateData(bean, () => {
          })
        }
        item.calYearResult()
      })

      //  初始化基数
      if (this.targetList.length > 0) {
        if (this.targetList[0].monthList && this.targetList[0].monthList.length > 0) {
          this.basic = this.targetList[0].monthList[0].basic + Number.parseFloat((this.targetList[0].monthList[0].result / 10000).toFixed(2))
        } else {
          this.basic = this.targetList[0].basic
        }
      }

      this.updateUiCount++
    })
  }

  /**
   * 基数目标结果
   */
  calTarget() {
    if (this.isAddYear) {
      this.target = this.basic * Math.pow(this.interestRate / 100 + 1, this.months)
    } else if (this.isAddMonth) {
      this.target = this.basic * (this.interestRate / 100 + 1)
    }
  }

  build() {
    Stack() {
      Column() {
        Text('目标')
          .fontSize(17)
          .textAlign(TextAlign.Center)
          .width('100%')
          .height(40)
            // .gesture(LongPressGesture().onAction(() => {
            //   this.isAddMonth = false
            //   this.isAddYear = !this.isAddYear
            // }))
          .onClick(() => {
            this.isAddYear = !this.isAddYear
            this.isAddMonth = false
          })

        List({ space: 5 }) {
          ForEach(this.targetList, (item: TargetBean, index) => {
            ListItem() {
              this.yearLayout(item, index)
            }
          }, (item: TargetBean) => this.updateUiCount + '-' + item.year)
        }

        Blank()
          .layoutWeight(1)
      }
      .height('100%')

      Column() {

        if (this.isAddYear) {
          this.yearInputLayout()
        }
        if (this.isAddMonth) {
          this.monthInoutLayout()
        }
      }
    }
    .alignContent(Alignment.Bottom)
    .width('100%')
    .height('100%')

  }

  @State year: number = 2023 // 年份
  @State month: number = 1
  @State basic: number = 10
  @State interestRate: number = 10 // 利率:百分数
  @State months: number = 12 // 月份数
  @State target: number = 0 // 目标

  @Builder
  monthInoutLayout() {

    Column() {

      Text('月份：基数*(1+复利)=结果')
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(40)
        .padding({ left: 15, right: 0, top: 0, bottom: 0 })
        .margin({ left: 0, right: 0, top: 0, bottom: 8 })

      Row() {
        Text(this.month.toFixed(0))
          .width('13%')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')
          .borderWidth(0.5)
          .borderRadius(10)
          .borderColor($r("app.color.gray"))
          .onClick(() => {
            this.month++
            if (this.month > 12) {
              this.month = 1
            }
          })

        Text('月:')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')

        TextInput({ placeholder: '基数', text: this.basic.toString() })
          .width('20%')
          .fontSize(15)
          .type(InputType.Number)
          .inputFilter('[0-9]*.[0-9]*')
          .textAlign(TextAlign.Center)
          .height('100%')
          .onChange((val) => {
            if (val !== '') {
              this.basic = Number.parseFloat(val)
              this.calTarget()
            } else {
              this.target = 0
            }
          })

        Text('*(1+')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')

        TextInput({ placeholder: '复率', text: this.interestRate.toString() })
          .width('20%')
          .fontSize(15)
          .type(InputType.Number)
          .textAlign(TextAlign.Center)
          .height('100%')
          .onChange((val) => {
            if (val !== '') {
              this.interestRate = Number.parseFloat(val)
              this.calTarget()
            } else {
              this.target = 0
            }
          })

        Text('%)=')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')

        Text(this.target.toFixed(1))
          .width('20%')
          .fontSize(15)
          .textAlign(TextAlign.Start)
          .height('100%')
          .fontColor($r('app.color.deepRed'))
      }
      .padding({ left: 15, right: 0, top: 0, bottom: 0 })
      .width('100%')
      .height(35)

      Button('确认')
        .width('65%')
        .height(35)
        .margin({ left: 0, right: 0, top: 15, bottom: 0 })
        .onClick(() => {
          if (this.target > 0) {
            let isExit = false
            this.targetList.forEach((item) => {
              if (item.year === this.year) {
                if (!item.monthList) {
                  item.monthList = []
                }
                item.monthList.forEach((bean) => {
                  if (bean.month === this.month) {
                    isExit = true
                    Toast.show(this.year + '年已存在对应的月份信息')
                  }
                })

                if (!isExit) {
                  let bean = new TargetBean()
                  bean.year = this.year
                  bean.month = this.month
                  bean.basic = this.basic
                  bean.interestRate = this.interestRate
                  bean.target = this.target
                  bean.result = OperateDataModel.getInstance().getResult(bean.year, bean.month)
                  this.targetTable.insertData(bean, (rowId) => {
                  })
                  // todo 插入数据库
                  // item.month-- // 为了更新界面用
                  item.monthList.unshift(bean)

                  // this.targetList.push(null)
                  // this.targetList.pop()
                  this.isAddMonth = false
                  this.refreshData()
                  this.updateUiCount++
                }
              }
            })
          } else {
            Toast.show('数据不能为空')
          }
        })

    }.padding({ left: 0, right: 0, top: 10, bottom: 10 })
    .borderWidth(0.5)
    .borderRadius(20)
    .borderColor($r("app.color.gray"))
  }

  @Builder
  yearInputLayout() {
    Column() {
      Text('年份：基数*(1+复利)^月份=结果')
        .textAlign(TextAlign.Start)
        .width('100%')
        .height(40)
        .padding({ left: 15, right: 0, top: 0, bottom: 0 })
        .margin({ left: 0, right: 0, top: 0, bottom: 8 })

      Row() {
        TextInput({ placeholder: '年份', text: this.year.toString() })
          .width('17%')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .type(InputType.Number)
          .height('100%')
          .padding({ left: 0, right: 0, top: 0, bottom: 0 })
          .onChange((val) => {
            if (val !== '') {
              this.year = Number.parseFloat(val)
            }
          })
        Text('年:')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')

        TextInput({ placeholder: '基数', text: this.basic.toString() })
          .width('20%')
          .fontSize(15)
          .type(InputType.Number)
          .inputFilter('[0-9]*.[0-9]*')
          .textAlign(TextAlign.Center)
          .height('100%')
          .padding({ left: 0, right: 0, top: 0, bottom: 0 })
          .onChange((val) => {
            if (val !== '') {
              this.basic = Number.parseFloat(val)
              this.calTarget()
            } else {
              this.target = 0
            }
          })

        Text('*(1+')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')

        TextInput({ placeholder: '复率', text: this.interestRate.toString() })
          .width('13%')
          .fontSize(15)
          .type(InputType.Number)
          .textAlign(TextAlign.Center)
          .height('100%')
          .padding({ left: 0, right: 0, top: 0, bottom: 0 })
          .onChange((val) => {
            if (val !== '') {
              this.interestRate = Number.parseFloat(val)
              this.calTarget()
            } else {
              this.target = 0
            }
          })

        Text('%)^')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')

        Text(this.months.toFixed(0))
          .width('13%')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')
          .borderWidth(0.5)
          .borderRadius(10)
          .borderColor($r("app.color.gray"))
          .onClick(() => {
            this.months++
            if (this.months > 12) {
              this.months = 1
            }
            this.calTarget()
          })
        Text('=')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .height('100%')

        Text(this.target.toFixed(1))
          .width('15%')
          .fontSize(15)
          .textAlign(TextAlign.Start)
          .height('100%')
          .fontColor($r('app.color.deepRed'))
      }
      .width('100%')
      .height(35)

      Button('确认')
        .width('65%')
        .height(35)
        .margin({ left: 0, right: 0, top: 15, bottom: 0 })
        .onClick(() => {
          if (this.target > 0) {
            let isExit = false
            this.targetList.forEach((item) => {
              if (item.year === this.year) {
                isExit = true
                Toast.show('已存在对应的年份信息')
              }
            })
            if (!isExit) {
              let bean = new TargetBean()
              bean.year = this.year
              bean.month = -1
              bean.basic = this.basic
              bean.interestRate = this.interestRate
              bean.target = Number.parseFloat(this.target.toFixed(1))
              this.targetTable.insertData(bean, (rowId) => {
                this.showLog('数据插入结果：' + rowId)
              })
              // todo 插入数据库
              this.targetList.unshift(bean)
              this.isAddYear = false
              this.updateUiCount++
            }
          } else {
            Toast.show('数据不能为空')
          }
        })

    }.padding({ left: 0, right: 0, top: 10, bottom: 10 })
    .borderWidth(0.5)
    .borderRadius(20)
    .borderColor($r("app.color.gray"))
  }

  @Builder
  yearLayout(item: TargetBean, index) {
    // 年基数 月利率 预计年目标
    Column() {
      Column() {
        Text(item.year + '年')
          .width('100%')
          .fontSize(19)
          .fontColor($r('app.color.deepRed'))
          .padding({ left: 5, right: 0, top: 5, bottom: 0 })
          .gesture(LongPressGesture().onAction(() => {
            // this.year = item.year
            // this.isAddYear = false
            // this.isAddMonth = true
            if (item.monthList && item.monthList.length > 0) {
              Toast.show('当前年份下数据不为空，无法删除')
            } else {
              AlertDialog.show({
                title: '注意',
                message: '确定要删除当前数据吗？',
                confirm: {
                  value: '确定',
                  action: () => {
                    this.targetTable.deleteData(item, () => {
                    })
                    this.targetList.splice(index, 1)
                  },
                }
              })
            }
          }))

        Row() {
          Text('基数:' + item.basic.toFixed(1)).item()
          Text('复率:' + item.interestRate + '%').item()
          Text('年目标:' + item.target.toFixed(1))
            .item()
            .fontColor($r('app.color.white'))
            .backgroundColor($r('app.color.deepRed'))
            .borderRadius(15)
        }

        Row() {
          Progress({ value: (item.getPercent() > 0 ? item.getPercent() : item.getPercent() * -1) })
            .width('66%')
            .backgroundColor($r('app.color.gray'))
            .color(item.getPercent() > 0 ? $r('app.color.deepRed') : $r('app.color.deepGreen'))
            .padding({ left: 30, right: 0, top: 0, bottom: 0 })

          Text('完成率:' + (item.getPercent() ? item.getPercent().toFixed(1) : 0) + '%')
            .item()
            .width('33%')
            .fontColor(item.getPercent() > 0 ? $r('app.color.deepRed') : $r('app.color.deepGreen'))
            .textAlign(TextAlign.Start)
            .padding({ left: 10, right: 0, top: 0, bottom: 0 })
        }.justifyContent(FlexAlign.Center)
      }
      .onClick(() => {
        this.year = item.year
        this.isAddYear = false
        this.isAddMonth = !this.isAddMonth
      })

      List({ space: 1 }) {
        ForEach(item.monthList, (bean, index) => {
          ListItem() {
            this.monthLayout(bean)
          }.gesture(LongPressGesture().onAction(() => {
            AlertDialog.show({
              title: '注意',
              message: '确定要删除当前数据吗？',
              confirm: {
                value: '确定',
                action: () => {
                  this.targetTable.deleteData(bean, () => {
                  })
                  item.monthList.splice(index, 1)
                  this.updateUiCount++
                },
              }
            })
          }))
        })
      }
    }
    .width('100%')
    .borderWidth(0.5)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
    .padding({ left: 0, right: 0, top: 0, bottom: 5 })

  }

  @Builder
  monthLayout(item: TargetBean) {
    // 月基数 月利率 月目标 完成率
    Column() {
      Text()
        .width('95%')
        .height(0.5)
        .backgroundColor($r('app.color.gray'))
        .margin({ left: 0, right: 0, top: 5, bottom: 0 })

      Text(item.month + '月')
        .width('100%')
        .fontSize(17)
        .padding({ left: 20, right: 0, top: 10, bottom: 0 })

      Row() {
        Text('基数:' + item.basic.toFixed(1)).item()
        Text('利率:' + item.interestRate + '%').item()
        Text('月目标:' + item.target.toFixed(1))
          .item()
          .fontWeight(FontWeight.Bold)
      }

      Row() {
        Progress({ value: (item.getPercent() > 0 ? item.getPercent() : item.getPercent() * -1) })
          .width('66%')
          .backgroundColor($r('app.color.gray'))
          .color(item.getPercent() > 0 ? $r('app.color.deepRed') : $r('app.color.deepGreen'))
          .padding({ left: 30, right: 0, top: 0, bottom: 0 })

        Text('完成率:' + (item.getPercent() ? item.getPercent().toFixed(1) : 0) + '%')
          .item()
          .width('33%')
          .fontColor(item.getPercent() > 0 ? $r('app.color.deepRed') : $r('app.color.deepGreen'))
          .textAlign(TextAlign.Start)
          .padding({ left: 10, right: 0, top: 0, bottom: 0 })
      }.justifyContent(FlexAlign.Center)

    }
  }

  /** import Logger from 'myLibrary/src/main/ets/util/Logger'
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Target', msg)
  }
}

@Extend(Text) function item() {
  .fontSize(15)
  .textAlign(TextAlign.Center)
  .height(35)
  .layoutWeight(1)
}