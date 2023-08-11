@Entry
@Component
struct OperateResult {
  build() {
    Column() {
      Text('历史结果')
        .width('100%')
        .height('8%')
        .textAlign(TextAlign.Center)

      Tabs({
        barPosition: BarPosition.End,
        index: 0
      }) {
        TabContent() {
          // HomePage()
          Text('时间')
        }
        .tabBar(this.TabBuilder('时间', $r('app.media.icon')))

        TabContent() {
          // HomePage()
          Text('种类')
        }
        .tabBar(this.TabBuilder('种类', $r('app.media.icon')))

        TabContent() {
          // HomePage()
          Text('详情')
        }
        .tabBar(this.TabBuilder('详情', $r('app.media.icon')))
      }
      .height('92%')
      .width('100%')
      .barWidth('100%')
      .barHeight('7.5%')
      .backgroundColor(Color.White)
      .barMode(BarMode.Fixed)
      .scrollable(false)
      .onChange((index: number) => {
        // this.currentIndex = index;
      })
    }
  }

  @Builder TabBuilder(title: string, normalImg: Resource) {
    Column() {
      Image(normalImg)
        .width(25)
        .height(25)
      Text(title)
        .margin({ top: 4 })
        .fontSize(10)
        .fontColor(Color.Black)
    }
    .justifyContent(FlexAlign.Center)
    .height('100%')
    .width('100%')
  }
}