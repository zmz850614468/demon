@Component
export default struct ResultType {
  build() {
    Column() {
    }
    .width('100%')
    .height('100%')
  }
}

@Extend(Text) function textItem() {
  .fontSize(17)
  .width('20%')
  .height(40)
  .textAlign(TextAlign.Center)
  .fontColor($r('app.color.black'))
}
