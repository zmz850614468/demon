import OperateBean from '../database_v2/bean/OperateBean'
import myPreference, { BEST_COMBINATION, BEST_COMBINATION_TEST } from '../model/PreferenceModel'
import Logger from '../util/Logger'

@Entry
@Component
struct BestCombination {
  @State bestCombination: Array<OperateBean> = []
  @State bestCombinationTest: Array<OperateBean> = []
  @State dragIndex: number = 0
  @State dragStarted: boolean = false
  @State total: number = 0
  @State total_60: number = 0
  @State total_70: number = 0
  @Watch('calResult')
  @State changeTimes: number = 0

  aboutToAppear() {
    this.initData()
  }

  async initData() {
    this.bestCombination = await myPreference.getObj(BEST_COMBINATION)
    this.bestCombinationTest = await myPreference.getObj(BEST_COMBINATION_TEST)
  }

  aboutToDisappear() {
    myPreference.saveObj(BEST_COMBINATION, this.bestCombination)
    myPreference.saveObj(BEST_COMBINATION_TEST, this.bestCombinationTest)
  }

  calResult() {
    this.total = 0
    this.bestCombination.forEach((bean) => {
      if (bean) {
        this.total += bean.result
      }
    })

    this.total /= 10000
    this.total_60 = this.total / 0.6
    this.total_70 = this.total / 0.7
  }
  /**
   * 移动位置
   * @param from
   * @param to
   */
  move(from: number, to: number) {
    let item = this.bestCombinationTest.splice(from, 1)[0]
    this.bestCombinationTest.splice(to, 0, item)
  }

  build() {
    Column() {
      Column() {
        Stack() {
          Text('最佳组合')
            .fontSize(17)
            .width('100%')
            .height(50)
            .textAlign(TextAlign.Center)

          Text('+')
            .fontSize(21)
            .width(50)
            .height(50)
            .textAlign(TextAlign.Center)
            .onClick(() => {
              this.bestCombination.push(new OperateBean())
            })
            .gesture(
              LongPressGesture()
                .onAction(() => {
                  this.bestCombinationTest.push(new OperateBean())
                })
            )
        }
        .alignContent(Alignment.End)
        .width('100%')
        .height(50)

        ResultLayout({ total: $total, total_60: $total_60, total_70: $total_70 })

        List({ space: 1 }) {
          ForEach(this.bestCombination, (item, index) => {
            ListItem() {
              if (item) {
                Item({ bean: item, changeTimes: $changeTimes })
              } else {
                Text('异常项')
                  .textAlign(TextAlign.Center)
                  .width('100%')
                  .height(40)
              }
            }.gesture(
              LongPressGesture()
                .onAction(() => {
                  AlertDialog.show({
                    title: "注意",
                    message: "确定要删除当前数据吗？",
                    confirm: {
                      value: "确定",
                      action: () => {
                        this.bestCombination.splice(index, 1)
                      },
                    }
                  })
                })
            )
          })
        }
      }

      Blank()

      Column() {
        List({ space: 1 }) {
          ForEach(this.bestCombinationTest, (item, index) => {
            ListItem() {
              Item({ bean: item, changeTimes: $changeTimes })
            }
            .onDragStart(() => {
              this.dragStarted = true
              return this.drawItem(item)
            })
            .onTouch((e) => {
              if (e.type === TouchType.Down) {
                this.dragIndex = index // 获取当前拖拽子组件的索引
              } else if (e.type === TouchType.Up) {
                this.dragStarted = false
              }
            })
            .gesture(
              LongPressGesture()
                .onAction(() => {
                  AlertDialog.show({
                    title: "注意",
                    message: "确定要删除当前数据吗？",
                    confirm: {
                      value: "确定",
                      action: () => {
                        this.bestCombinationTest.splice(index, 1)
                      },
                    }
                  })
                })
            )
          })
        }
        .onDrop((e: DragEvent, params: string) => {
          let p = JSON.parse(params)
          this.move(this.dragIndex, p.insertIndex)
        })
      }.padding({ left: 0, right: 0, top: 0, bottom: 10 })
    }
    .height('100%')
  }

  @Builder drawItem(bean: OperateBean) {
    Item({ bean: bean, changeTimes: $changeTimes })
  }
}

@Preview
@Component
struct ResultLayout {
  @Link total: number
  @Link total_60: number
  @Link total_70: number

  build() {
    Column() {
      Row() {
        Text('组合总价')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .width('33%')
          .height('100%')
        Text('70%占比需')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .width('34%')
          .height('100%')
        Text('60%占比需')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .width('34%')
          .height('100%')
      }.width('100%')
      .height(40)

      Row() {
        Text(this.total.toFixed(2) + '万')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .width('33%')
          .height('100%')
        Text(this.total_70.toFixed(2) + '万')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .width('34%')
          .height('100%')
        Text(this.total_60.toFixed(2) + '万')
          .fontSize(15)
          .textAlign(TextAlign.Center)
          .width('34%')
          .height('100%')
      }.width('100%')
      .height(40)
    }.borderWidth(1)
    .borderRadius(10)
    .borderColor($r("app.color.gray"))
  }
}

@Preview
@Component
struct Item {
  @State bean: OperateBean = null
  @Link changeTimes: number

  build() {
    Row() {
      TextInput({ placeholder: '品类', text: this.bean.name })
        .fontSize(15)
        .textAlign(TextAlign.Center)
        .width('25%')
        .height('100%')
        .borderWidth(1)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
        .onChange((value) => {
          this.bean.name = value
        })

      TextInput({ placeholder: '价格', text: (this.bean.inPrice == 0 ? '' : this.bean.inPrice.toString()) })
        .fontSize(15)
        .textAlign(TextAlign.Center)
        .type(InputType.Number)
        .width('25%')
        .height('100%')
        .borderWidth(1)
        .borderRadius(10)
        .borderColor($r("app.color.gray"))
        .onChange((value) => {
          if (value === '') {
            this.bean.inPrice = 0
          } else {
            this.bean.inPrice = Number.parseInt(value)
          }
          this.calResult()
        })
        .margin({ left: 3, right: 3, top: 0, bottom: 0 })

      Counter() {
        Text(this.bean.hands.toString())
          .fontSize(15)
          .textAlign(TextAlign.Center)
      }.onInc(() => {
        this.bean.hands++
        this.calResult()
      }).onDec(() => {
        if (this.bean.hands > 0) {
          this.bean.hands--
          this.calResult()
        }
      })
      .width('30%')
      .height('100%')

      Text((this.bean.result / 10000).toFixed(2) + '万')
        .fontSize(15)
        .textAlign(TextAlign.Center)
        .width('20%')
        .height('100%')

    }.width('100%')
    .height(40)
    .margin({ left: 0, right: 0, top: 5, bottom: 0 })
  }

  calResult() {
    this.bean.result = this.bean.inPrice * this.bean.hands
    this.changeTimes++
  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('BestCombination', msg)
  }
}



