// selectDialogController: CustomDialogController = new CustomDialogController({
//   builder: SingleSelectDialog({
//     title: '',
//     itemList: ,
//     selected: ,
//     callBack: (selected: string) => {
//
//     }
//   }),
//   autoCancel: true,
//   alignment: DialogAlignment.Bottom,
//   offset: { dx: 0, dy: -20 }
// })

import Toast from '../util/Toast'
import PreferenceModel, { HANDS_LIST } from '../model/PreferenceModel'

/**
 * 自定义 单选框
 */
@CustomDialog
export struct HandsSelectDialog {
  title: string = null // 标题
  itemList: Array<number> = null // 选择列表
  selected: number = null // 已选择项
  callBack: (selected: number) => void = null // 回调监听

  private inputHands: number = 0
  @State handsList: Array<number> = []
  private controller: CustomDialogController = new CustomDialogController({
    builder: HandsSelectDialog()
  });

  aboutToAppear() {
    this.initData()
    this.initData()
  }

  private async initData() {
    let list = await PreferenceModel.get(HANDS_LIST)
    if (list !== undefined) {
      this.handsList = JSON.parse(list)
    }
  }

  /**
   * 保存 可选择数量 信息
   */
  private saveHands() {
    PreferenceModel.save(HANDS_LIST, JSON.stringify(this.handsList))
  }

  build() {
    Column() {
      Column() {
        Text(this.title)
          .fontSize(19)
          .margin({ left: 0, right: 0, top: 10, bottom: 0 })
          .width('100%')
          .textAlign(TextAlign.Center)
      }
      .width('90%')

      Row() {
        TextInput()
          .fontSize(17)
          .width('65%')
          .textAlign(TextAlign.Center)
          .align(Alignment.Center)
          .height('100%')
          .fontColor($r('app.color.black'))
          .padding({ left: 0, right: 0, top: 0, bottom: 0 })
          .type(InputType.Number)
          .inputFilter('[0-9]*.[0-9]*')
          .borderWidth(1)
          .borderRadius(15)
          .borderColor($r("app.color.gray"))
          .onChange((value) => {
            this.inputHands = Number.parseInt(value)
          })

        Text("确认")
          .fontSize(17)
          .width('35%')
          .height('100%')
          .textAlign(TextAlign.Center)
          .borderWidth(1)
          .borderRadius(15)
          .fontColor($r("app.color.white"))
          .backgroundColor($r("app.color.red"))
          .borderColor($r("app.color.gray"))
          .margin({ left: 3, right: 0, top: 0, bottom: 0 })
          .onClick((e) => {
            if (this.inputHands <= 0) {
              Toast.show('请输入有效数值')
            } else {
              this.callBack(this.inputHands)
              this.controller.close()
              this.handsList.unshift(this.inputHands)
              this.selected = this.inputHands
              this.inputHands = 0
              this.saveHands()
            }
          })
      }.width('60%')
      .height(40)

      Grid() {
        ForEach(this.handsList, (item: number, index) => {
          GridItem() {
            Text(item.toString())
              .fontSize(17)
              .width(50)
              .height(50)
              .textAlign(TextAlign.Center)
              .borderWidth(1)
              .borderRadius(10)
              .fontColor((this.selected == item) ? $r("app.color.red") : $r("app.color.black"))
              .onClick((e) => {
                this.selected = item
                this.callBack(item)
                this.controller.close()
                this.handsList.unshift(this.handsList.splice(index, 1)[0])
                this.saveHands()
              })
          }
        })
      }
      .margin({ left: 0, right: 0, top: 8, bottom: 0 })
      .align(Alignment.Center)
      .layoutDirection(GridDirection.Row)
      .maxCount(5)
      .rowsGap(10)
      .columnsGap(10)
    }
    .justifyContent(FlexAlign.Center)
    .padding(12)
  }
}