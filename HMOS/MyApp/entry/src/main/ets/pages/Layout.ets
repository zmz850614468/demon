import Logger from 'myLibrary/src/main/ets/util/Logger'
import { Index } from './Index'

@Entry
@Component
struct Layout {
  @State message: string = 'Hello World'

  build() {
    // GridRow({
    //   // breakpoints: {
    //   //   value: ['320vp', '600vp', '840vp'],
    //   //   reference: BreakpointsReference.WindowSize
    //   // },
    //   // direction: GridRowDirection.Row
    //   columns: { // 给屏幕分格
    //     sm: 4,
    //     md: 8,
    //     lg: 12
    //   }
    // }) {
    //   GridCol({
    //     span: { //
    //       sm: 4,
    //       md: 6,
    //       lg: 8
    //     },
    //     offset: {
    //       md: 1, // 默认值 0
    //       lg: 2
    //     }
    //   }) {
    //
    //   }
    // }
    GridRow() {
      GridCol({
        span: { sm: 12, md: 6, lg: 4 }
      }) {
        Column() {
          Text("模块一")
          // Index()
        }
      }.borderWidth(0.5)
      .borderRadius(10)
      .borderColor($r("app.color.red"))

      GridCol({
        span: { sm: 12, md: 6, lg: 8 }
      }) {
        Text("模块二")
      }.borderWidth(0.5)
      .borderRadius(10)
      .borderColor($r("app.color.red"))

    }.height('100%')
    .onBreakpointChange((breakPoints: string) => {
      this.showLog('breakPoints:' + breakPoints)
    })

  }

  /**
   * 日志信息
   * @param msg
   */
  showLog(msg: string) {
    Logger.error('Layout', msg)
  }
}