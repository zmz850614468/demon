@Entry
@Component
struct UI {
  build() {
    Column({ space: 15 }) { // 行间距设置
      Text('Text')
        .width('100%')
        .height(40)
        .fontSize(19)
        .fontColor('#000000')
        .textAlign(TextAlign.Center) // 内容对齐方式
        .fontWeight(FontWeight.Bold)
        .fontStyle(FontStyle.Italic)
        .margin({ left: 10, right: 10 }) // 外间距设置
        .padding({ left: 10, right: 10 }) // 内间距设置
        .backgroundColor($r('app.color.start_window_background'))
        .onClick(() => { // 点击事件
        })
        .style()


    }
    .width('100%')
    .height('100%')
  }
}

/**
 * 属性复用
 */
@Extend(Text) function style() {
  .borderWidth(1) // 边框大小，颜色，角幅度
  .borderColor('#000000')
  .borderRadius(15)
}