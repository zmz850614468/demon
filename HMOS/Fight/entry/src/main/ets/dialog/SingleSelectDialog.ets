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

/**
 * 自定义 单选框
 */
@CustomDialog
export struct SingleSelectDialog {
  title: string = null // 标题
  itemList: Array<string> = null // 选择列表
  selected: string = null // 已选择项
  callBack: (selected: string) => void = null // 回调监听

  private controller: CustomDialogController = new CustomDialogController({
    builder: SingleSelectDialog()
  });

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

      List({ space: 1 }) {
        ForEach(this.itemList, (item) => {
          ListItem() {
            Text(item)
              .fontSize(17)
              .fontColor(this.selected == item ? $r('app.color.red') : $r('app.color.black'))
              .height(45)
          }
          .width('100%')
          .onClick((e) => {
            if (this.callBack) {
              this.callBack(item)
            }
            this.controller.close();
          })
        })
      }.padding(10)

    }
    .justifyContent(FlexAlign.Center)
    .padding(12)
  }
}