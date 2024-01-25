// import ResultEach from './ResultEach'
import BaseTable_v2 from '../database_v2/BaseTable_v2'
import DayOperateBean from '../database_v2/bean/DayOperateBean'
import OperateBean from '../database_v2/bean/OperateBean'
import ResultDay from '../views/ResultDay'
import ResultEach from '../views/ResultEach'
import ResultMonth from '../views/ResultMonth'
import ResultType from '../views/ResultType'
import ResultType_v2 from '../views/ResultType_v2'

@Entry
@Component
struct OperateResult {
  @State currIndex: number = 0
  @State title: string = '操作详情'
  @State operateList: Array<OperateBean> = null
  @State dayOperateList: Array<DayOperateBean> = null
  @State type: string = '月'
  listener: (type: string) => {} = null
  // monthBuild =new ResultMonth({ dayOperateList: this.dayOperateList })

  /**
   * 加载前初始化数据
   */
  aboutToAppear() {
    let table1 = new BaseTable_v2(OperateBean.TABLE_NAME, OperateBean.SQL, OperateBean.COLUMNS, (b: boolean) => {
      table1.queryAll(new OperateBean(), (list: Array<OperateBean>) => {
        this.operateList = list.reverse()
        // this.monthBuild = new ResultMonth({ dayOperateList: this.dayOperateList })
      })
    })

    let table2 = new BaseTable_v2(DayOperateBean.TABLE_NAME, DayOperateBean.SQL, DayOperateBean.COLUMNS, (b: boolean) => {
      table2.queryAll(new DayOperateBean(), (list: Array<DayOperateBean>) => {
        this.dayOperateList = list.reverse()
      })
    })
  }

  build() {
    Column() {
      Stack() {
        Text(this.title)
          .width('100%')
          .height('100%')
          .fontSize(19)
          .textAlign(TextAlign.Center)

        if (this.currIndex == 0) {
          Text(this.type)
            .width('100%')
            .height('100%')
            .fontSize(17)
            .textAlign(TextAlign.End)
            .padding({ left: 10, right: 10, top: 0, bottom: 0 })
            .onClick((e) => {
              switch (this.type) {
                case '月':
                  this.type = '季'
                  break;
                case '季':
                  this.type = '年'
                  break;
                case '年':
                  this.type = '月'
                  break;
              }
              this.listener(this.type)
            })
        }
      }
      .width('100%')
      .height('5%')

      Tabs({
        barPosition: BarPosition.End,
        index: this.currIndex
      }) {
        TabContent() {
          if (this.dayOperateList !== null) {
            ResultMonth({ dayOperateList: this.dayOperateList, operateResult: this, operateList: this.operateList })
          }
        }
        .tabBar(this.TabBuilder('每月详情', 0, $r('app.media.fighting')))

        TabContent() {
          if (this.operateList !== null) {
            // ResultType({ operateList: this.operateList })
            ResultType_v2({ operateList: this.operateList })
          }
        }
        .tabBar(this.TabBuilder('每种详情', 1, $r('app.media.fighting')))

        // TabContent() {
        //   if (this.dayOperateList !== null) {
        //     ResultDay({ dayOperateList: this.dayOperateList })
        //   }
        // }
        // .tabBar(this.TabBuilder('每天详情', 2, $r('app.media.fighting')))

        TabContent() {
          if (this.operateList !== null) {
            ResultEach({ operateList: this.operateList })
          }
        }
        .tabBar(this.TabBuilder('每笔详情', 3, $r('app.media.fighting')))
      }
      .height('95%')
      .width('100%')
      .barWidth('100%')
      .barHeight('7.5%')
      .backgroundColor(Color.White)
      .barMode(BarMode.Fixed)
      .scrollable(true)
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