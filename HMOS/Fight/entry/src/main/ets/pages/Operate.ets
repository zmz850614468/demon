import { getCost } from '../bean/CostBean'
import Toast from '../util/Toast'
import OperateBean from '../database_v2/bean/OperateBean'
import PreferenceModel, { BEST_COMBINATION, HANDS_LIST, OPERATE_LIST } from '../model/PreferenceModel'
import systemDateTime from '@ohos.systemDateTime'
import DayOperateBean from '../database_v2/bean/DayOperateBean'
import BaseTable_v2 from '../database_v2/BaseTable_v2'
import { SingleSelectDialog } from '../dialog/SingleSelectDialog'
import { HandsSelectDialog } from '../dialog/HandsSelectDialog'
import HashMap from '@ohos.util.HashMap'
import { OperateDataModel } from '../model/OperateDataModel'

@Entry
@Component
struct Operate {
  bestCombination: Array<OperateBean> = [] // 最佳组合数据
  @State operateList: Array<OperateBean> = []
  @State isSelectType: boolean = true
  // typeList: Array<string> = ['菜油', '棕榈油', '豆油']
  typeList: Array<string> = []
  @State handsList: Array<number> = []
  inputHands: number = 0
  @State selectBean: OperateBean = null
  @State operateResult: number = 0
  @State posCount: number = 0
  @State negCount: number = 0
  @State poundage: number = 0
  map: HashMap<string, number> = new HashMap() // 实际数量:默认都是10倍数量

  aboutToAppear() {
    this.initData()

    this.map.set('液化气', 20)
    this.map.set('塑料', 5)
  }

  private async initData() {
    this.bestCombination = await PreferenceModel.getObj(BEST_COMBINATION)
    this.bestCombination.forEach((bean) => {
      this.typeList.push(bean.name)
    })

    let value = await PreferenceModel.get(OPERATE_LIST)
    if (value === undefined) {
      let bean = this.getNewBean()
      this.operateList.push(bean)
    } else {
      Object.assign(this.operateList, JSON.parse(value))
      this.calAndSave()
    }

    let list = await PreferenceModel.get(HANDS_LIST)
    if (list !== undefined) {
      this.handsList = JSON.parse(list)
    }
  }

  /**
   * 计算并保存缓存数据
   */
  private calAndSave() {
    this.posCount = 0
    this.negCount = 0
    this.operateResult = 0
    this.poundage = 0
    let lastIndex = 0 // 最后一个买入不为0的数据
    let emptyArray = []
    this.operateList.forEach((bean, index) => {
      if (bean.outPrice > 0) {
        let count = this.map.get(bean.name) ? this.map.get(bean.name) : 10
        bean.result = (bean.outPrice - bean.inPrice) * bean.hands * count * (bean.type == '买入' ? 1 : -1)
        if (bean.result > 0) {
          this.posCount++
        } else {
          this.negCount++
        }
        this.operateResult += bean.result
        this.poundage += bean.hands * getCost(bean.name)
      }
      if (bean.inPrice > 0) {
        lastIndex = index
      }
      if (bean.inPrice == 0) {
        emptyArray.push(index)
      }
    })

    // 空数据全部往后推
    emptyArray.reverse()
    emptyArray.forEach((index) => {
      if (index < lastIndex) {
        this.operateList.push(this.operateList.splice(index, 1)[0])
      }
    })

    // if (emptyArray.length != this.bestCombination.length) {
    // 需要添加空数据
    let length = this.operateList.length
    this.bestCombination.forEach((bean) => {
      if (bean.hands > 0) {
        let has = false
        for (let i = length - emptyArray.length; i < length; i++) {
          if (bean.name === this.operateList[i].name) {
            has = true
            break
          }
        }
        // 数据不存在，则添加
        if (!has) {
          this.operateList.push(this.getNewBean(bean.name, bean.hands))
        }
      }
    })
    // }

    // if (this.operateList.length > 0) {
    //   let bean = this.operateList[this.operateList.length-1]
    //   if (bean.inPrice > 0) {
    //     this.operateList.push(this.getNewBean(bean.name, bean.hands))
    //   }
    // }

    this.operateList.push(this.operateList.pop())
    PreferenceModel.save(OPERATE_LIST, JSON.stringify(this.operateList))
  }

  /**
   * 保存 可选择数量 信息
   */
  // private saveHands() {
  //   PreferenceModel.save(HANDS_LIST, JSON.stringify(this.handsList))
  // }

  /**
   * 保存数据库数据
   */
  private async ok() {
    if (this.poundage <= 0) {
      Toast.show('没有需要保存的数据', 500)
      return
    }

    // 天数据
    let time = await systemDateTime.getCurrentTime()
    let bean = new DayOperateBean()
    bean.timeStamp = time
    bean.result = this.operateResult
    bean.posCount = this.posCount
    bean.negCount = this.negCount
    bean.poundage = this.poundage

    let dayOperateTable = new BaseTable_v2(DayOperateBean.TABLE_NAME, DayOperateBean.SQL, DayOperateBean.COLUMNS, (b: boolean) => {
      dayOperateTable.insertData(bean, (id) => {
        // Toast.show('数据添加成功', 300)
        OperateDataModel.getInstance().refreshData()
      })
    })

    // 每笔数据
    let operateTable = new BaseTable_v2(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS, (b: boolean) => {
      this.operateList.forEach((bean) => {
        if (bean.outPrice > 0) {
          bean.timeStamp = time
          operateTable.insertData(bean, (id) => {
          })
        }
      })
      for (let i = this.operateList.length - 1; i >= 0; i--) {
        if (this.operateList[i].outPrice > 0) {
          this.operateList.splice(i, 1)
        }
      }

      OperateDataModel.getInstance().refreshData()

      this.calAndSave()
    })
  }

  /**
   * 初始化默认数据新对象
   * @returns
   */
  private getNewBean(name: string = '菜油', hands: number = 1): OperateBean {
    let bean = new OperateBean()
    bean.name = name
    bean.type = '买入'
    bean.hands = hands
    bean.result = 0
    bean.inPrice = 0
    bean.outPrice = 0

    return bean
  }

  selectTypeDialogController: CustomDialogController = new CustomDialogController({
    builder: SingleSelectDialog({
      title: '请选择品种',
      itemList: this.typeList,
      selected: this.selectBean.name,
      callBack: (selected: string) => {
        this.selectBean.name = selected
        this.operateList.push(this.operateList.pop())
      }
    }),
    autoCancel: true,
    alignment: DialogAlignment.Bottom,
    offset: { dx: 0, dy: -20 }
  })
  selectHandsDialogController: CustomDialogController = new CustomDialogController({
    builder: HandsSelectDialog({
      title: '请选择数量',
      itemList: this.handsList,
      selected: this.selectBean.hands,
      callBack: (selected: number) => {
        this.selectBean.hands = selected
        this.operateList.push(this.operateList.pop())
        this.calAndSave()
      }
    }),
    autoCancel: true,
    alignment: DialogAlignment.Bottom,
    offset: { dx: 0, dy: -20 }
  })

  build() {
    // Stack() {
      Column() {
        Column() {
          Text("当前操作")
            .fontSize(19)
            .width('100%')
            .height(50)
            .textAlign(TextAlign.Center)

          this.resultLayout()

          this.inputTitle()

          List({ space: 1 }) {
            ForEach(this.operateList, (item, index) => {
              ListItem() {
                this.inputLayout(item)
              }.gesture(LongPressGesture().onAction(() => {
                AlertDialog.show({
                  title: '注意',
                  message: '确定要删除当前数据吗？',
                  confirm: {
                    value: '确定',
                    action: () => {
                      this.operateList.splice(index, 1)
                    },
                  }
                })
              }))
            })
          }.layoutWeight(1)
        }.width('100%')
        .layoutWeight(1)

        Row() {
          Text("计算保存")
            .fontSize(17)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)
            .borderWidth(1)
            .borderRadius(15)
            .borderColor($r("app.color.gray"))
            .onClick((e) => {
              this.calAndSave()
              // Toast.show('数据缓存成功')
            })

          Text("确认添加")
            .fontSize(17)
            .width('50%')
            .height('100%')
            .textAlign(TextAlign.Center)
            .borderWidth(1)
            .borderRadius(15)
            .fontColor($r("app.color.white"))
            .backgroundColor($r("app.color.red"))
            .borderColor($r("app.color.gray"))
            .margin({ left: 8, right: 0, top: 0, bottom: 0 })
            .onClick((e) => {
              AlertDialog.show({
                title: "注意",
                message: "确定添加数据吗？",
                confirm: {
                  value: "确定",
                  action: () => {
                    this.ok()
                  },
                }
              })
            })

        }.width('100%')
        .height('6%')
        .padding({ left: 15, right: 15, top: 0, bottom: 5 })
      }.height('100%')

    // }
  }

  /**
   * 结果板块
   */
  @Builder
  resultLayout() {
    Column() {
      Blank()
        .width('100%')
        .height(1)
        .color($r('app.color.gray'))
        .margin({ left: 0, right: 0, top: 1, bottom: 5 })

      Row() {
        Text('操作结果：')
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.End)
          .height('100%')
          .fontColor($r('app.color.black'))

        Text(this.operateResult.toString())
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.Start)
          .height('100%')
          .padding({ left: 10, right: 0, top: 0, bottom: 0 })
          .fontColor($r('app.color.black'))

        Text('正操作次数：')
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.End)
          .height('100%')
          .fontColor($r('app.color.black'))

        Text(this.posCount.toString())
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.Start)
          .height('100%')
          .fontColor($r('app.color.black'))
      }.width('100%')
      .height(40)

      Row() {
        Text('手续费用：')
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.End)
          .height('100%')
          .fontColor($r('app.color.black'))

        Text(this.poundage.toFixed(2))
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.Start)
          .height('100%')
          .padding({ left: 10, right: 0, top: 0, bottom: 0 })
          .fontColor($r('app.color.black'))

        Text('负操作次数：')
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.End)
          .height('100%')
          .fontColor($r('app.color.black'))

        Text(this.negCount.toString())
          .fontSize(17)
          .width('30%')
          .textAlign(TextAlign.Start)
          .height('100%')
          .fontColor($r('app.color.black'))
      }.width('100%')
      .height(40)

      Blank()
        .width('100%')
        .height(1)
        .color($r('app.color.gray'))
        .margin({ left: 0, right: 0, top: 5, bottom: 0 })
    }
  }

  @Builder
  inputTitle() {
    Row() {
      Text('开仓')
        .fontSize(15)
        .width('15%')
        .textAlign(TextAlign.Center)
        .height('100%')
        .fontColor($r('app.color.black'))
      Text('品种')
        .fontSize(15)
        .width('30%')
        .textAlign(TextAlign.Center)
        .height('100%')
        .fontColor($r('app.color.black'))
      Text('手')
        .fontSize(15)
        .width('15%')
        .textAlign(TextAlign.Center)
        .height('100%')
        .fontColor($r('app.color.black'))
      Text('入价')
        .fontSize(15)
        .width('20%')
        .textAlign(TextAlign.Center)
        .height('100%')
        .fontColor($r('app.color.black'))
      Text('出价')
        .fontSize(15)
        .width('20%')
        .textAlign(TextAlign.Center)
        .height('100%')
        .fontColor($r('app.color.black'))
    }.width('100%')
    .height(40)
  }

  @Builder
  inputLayout(bean: OperateBean) {
    Stack() {
      Row() {
        Text(bean.type)
          .fontSize(15)
          .width('15%')
          .textAlign(TextAlign.Center)
          .height('100%')
          .fontColor($r('app.color.black'))
          .onClick((e) => {
            bean.type = bean.type === '买入' ? '卖入' : '买入'
            this.operateList.push(this.operateList.pop())
          })
        Text(bean.name)
          .fontSize(15)
          .width('30%')
          .textAlign(TextAlign.Center)
          .height('100%')
          .fontColor($r('app.color.black'))
          .onClick((e) => {
            this.selectBean = bean
            this.isSelectType = true
            this.selectTypeDialogController.open()
          })
        Text(bean.hands.toString())
          .fontSize(15)
          .width('15%')
          .textAlign(TextAlign.Center)
          .height('100%')
          .fontColor($r('app.color.black'))
          .onClick((e) => {
            this.selectBean = bean
            this.isSelectType = false
            // this.isShow = true
            this.selectHandsDialogController.open()
          })
        TextInput({ text: bean.inPrice == 0 ? '' : bean.inPrice.toString() })
          .fontSize(15)
          .width('20%')
          .textAlign(TextAlign.Center)
          .height('100%')
          .fontColor($r('app.color.black'))
          .padding({ left: 0, right: 0, top: 0, bottom: 0 })
          .type(InputType.Number)
          .inputFilter('[0-9]*.[0-9]*')
          .margin({ left: 0, right: 0, top: 4, bottom: 4 })
          .borderWidth(1)
          .borderRadius(15)
          .borderColor($r("app.color.gray"))
          .onChange((value) => {
            if (value === '') {
              bean.inPrice = 0
            } else {
              bean.inPrice = Number.parseFloat(value)
            }
          })

        Stack() {
          TextInput({ text: bean.outPrice == 0 ? '' : bean.outPrice.toString() })
            .fontSize(15)
            .textAlign(TextAlign.Center)
            .fontColor($r('app.color.black'))
            .padding({ left: 0, right: 0, top: 0, bottom: 0 })
            .type(InputType.Number)
            .inputFilter('[0-9]*.[0-9]*')
            .margin({ left: 0, right: 0, top: 4, bottom: 4 })
            .borderWidth(1)
            .borderRadius(15)
            .borderColor($r("app.color.gray"))
            .width('100%')
            .height('100%')
            .onChange((value) => {
              if (value === '') {
                bean.outPrice = 0
              } else {
                bean.outPrice = Number.parseFloat(value)
              }
            })

          Text(bean.outPrice > 0 ? bean.result.toString() : '')
            .fontSize(13)
            .fontColor(bean.result > 0 ? $r('app.color.red') : $r('app.color.green'))
            .margin({ left: 0, right: 5, top: 0, bottom: 0 })
        }
        .width('20%')
        .height('100%')
        .align(Alignment.BottomEnd)
      }.width('100%')
      .height('100%')

    }.width('100%')
    .height(40)
    .align(Alignment.BottomEnd)
  }
}
