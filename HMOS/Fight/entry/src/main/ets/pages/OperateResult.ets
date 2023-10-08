// import ResultEach from './ResultEach'
import ResultDay from '../views/ResultDay'
import ResultEach from '../views/ResultEach'
import ResultMonth from '../views/ResultMonth'
import ResultType from '../views/ResultType'

@Entry
@Component
struct OperateResult {
  @State currIndex: number = 0
  @State title: string = '历史详情'

  build() {
    Column() {
      Text(this.title)
        .width('100%')
        .height('5%')
        .fontSize(19)
        .textAlign(TextAlign.Center)

      Tabs({
        barPosition: BarPosition.End,
        index: this.currIndex
      }) {
        TabContent() {
          ResultMonth()
        }
        .tabBar(this.TabBuilder('每月详情', 0, $r('app.media.icon')))

        TabContent() {
          ResultType()
        }
        .tabBar(this.TabBuilder('每种详情', 1, $r('app.media.icon')))

        TabContent() {
          ResultDay()
        }
        .tabBar(this.TabBuilder('每天详情', 2, $r('app.media.icon')))

        TabContent() {
          ResultEach()
        }
        .tabBar(this.TabBuilder('每笔详情', 3, $r('app.media.icon')))
      }
      .height('95%')
      .width('100%')
      .barWidth('100%')
      .barHeight('7.5%')
      .backgroundColor(Color.White)
      .barMode(BarMode.Fixed)
      .scrollable(false)
      .onChange((index: number) => {
        this.currIndex = index;
      })
    }
  }

  @Builder TabBuilder(title: string, index: number, normalImg: Resource) {
    Column() {
      Image(normalImg)
        .width(25)
        .height(25)
      Text(title)
        .margin({ top: 4 })
        .fontSize(10)
        .fontColor(this.currIndex === index ? Color.Blue : Color.Black)
      Blank(this.title = this.currIndex === index ? title : this.title)
        .visibility(Visibility.None)
    }
    .justifyContent(FlexAlign.Center)
    .height('100%')
    .width('100%')
  }
}